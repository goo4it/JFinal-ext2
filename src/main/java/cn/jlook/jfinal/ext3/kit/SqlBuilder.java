package cn.jlook.jfinal.ext3.kit;

import java.util.List;

import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * @author yanglinghui
 */
public class SqlBuilder {
    private static final String _sapce      = " ";
    private static final String _null_filed = "*";
    private static final String _select     = "SELECT";
    private static final String _form       = "FROM";
    private static final String _where      = "WHERE";
    private static final String _group      = "GROUP BY";
    private static final String _orderby    = "ORDER BY";
    private static final String _limit      = "LIMIT";
    private static final String _join       = "JOIN";
    private static final String _union      = "UNION";
    private static final String _union_all  = "UNION ALL";
    private String              table;
    private String              alias;
    private String              where;
    private String[]            fields;
    private String              order;
    private String              limit;
    private String              group;
    private List<_JOIN>         joins;
    private StringBuffer        unionBuffer;

    public SqlBuilder(String table) {
        this.table = table;
    }

    public static SqlBuilder from(String table) {
        return new SqlBuilder(table);
    }

    public static SqlBuilder from(Table table) {
        return SqlBuilder.from(table.getName());
    }

    public static <M extends Model<M>> SqlBuilder from(Class<M> modelClass) {
        return SqlBuilder.from(TableMapping.me().getTable(modelClass));
    }

    /**
     * 别名
     * 
     * @param alias
     * @return
     */
    public SqlBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 查询条件
     * 
     * @param where
     * @return
     */
    public SqlBuilder where(String where) {
        this.where = where;
        return this;
    }

    /**
     * 查询字段
     * 
     * @param fields
     * @return
     */
    public SqlBuilder field(String... fields) {
        this.fields = fields;
        return this;
    }

    /**
     * 排序
     * 
     * @param order
     * @return
     */
    public SqlBuilder order(String order) {
        this.order = order;
        return this;
    }

    /**
     * 指定查询和操作的数量
     * 
     * @param limit
     * @return
     */
    public SqlBuilder limit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    /**
     * 分页查询
     * 
     * @param page
     *            页码
     * @param pageSize
     *            每页大小
     * @return
     */
    public SqlBuilder page(int page, int pageSize) {
        if (page <= 0) page = 1;
        if (pageSize <= 0) pageSize = 10;
        int start = (page - 1) * pageSize;
        this.limit = String.format("%d,%d", start, pageSize);
        return this;
    }

    /**
     * 分组统计
     * 
     * @param group
     * @return
     */
    public SqlBuilder group(String group) {
        this.group = group;
        return this;
    }

    /**
     * 根据两个或多个表中的列之间的关系，从这些表中查询数据。
     * 默认为 INNER JOIN
     * <p>
     * join通常有下面几种类型，不同类型的join操作会影响返回的数据结果。<br/>
     * ○ INNER JOIN: 如果表中有至少一个匹配，则返回行，等同于 JOIN<br/>
     * ○ LEFT JOIN: 即使右表中没有匹配，也从左表返回所有的行<br/>
     * ○ RIGHT JOIN: 即使左表中没有匹配，也从右表返回所有的行<br/>
     * ○ FULL JOIN: 只要其中一个表中存在匹配，就返回行<br/>
     * </p>
     * 
     * @param join
     * @return
     */
    public SqlBuilder join(String join) {
        return join(join, JOIN.INNER);
    }

    /**
     * 根据两个或多个表中的列之间的关系，从这些表中查询数据。
     * 默认为 INNER JOIN
     * <p>
     * join通常有下面几种类型，不同类型的join操作会影响返回的数据结果。<br/>
     * ○ INNER JOIN: 如果表中有至少一个匹配，则返回行，等同于 JOIN<br/>
     * ○ LEFT JOIN: 即使右表中没有匹配，也从左表返回所有的行<br/>
     * ○ RIGHT JOIN: 即使左表中没有匹配，也从右表返回所有的行<br/>
     * ○ FULL JOIN: 只要其中一个表中存在匹配，就返回行<br/>
     * </p>
     * 
     * @param join
     * @param joinType
     * @return
     */
    public SqlBuilder join(String join, JOIN joinType) {
        if (joins == null) joins = Lists.newArrayList();
        joins.add(new _JOIN(join, joinType));
        return this;
    }

    /**
     * 合并两个或多个 SELECT 语句的结果集
     * 
     * @param union
     * @return
     */
    public SqlBuilder union(String union) {
        return union(union, false);
    }

    /**
     * 合并两个或多个 SELECT 语句的结果集,支持UNION ALL 操作
     * 
     * @param union
     * @param all
     *            支持UNION ALL 操作
     * @return
     */
    public SqlBuilder union(String union, boolean all) {
        if (all)
            union = _union_all + _sapce + union;
        else
            union = _union + _sapce + union;
        if (unionBuffer == null)
            unionBuffer = new StringBuffer(union);
        else
            unionBuffer.append(_sapce).append(union);
        return this;
    }

    @Override
    public String toString() {
        boolean start = true;
        StringBuffer sql = new StringBuffer(_select).append(_sapce);
        if (fields == null || fields.length == 0)
            sql.append(_null_filed).append(_sapce);
        else {
            for (String field : fields) {
                if (start)
                    start = false;
                else
                    sql.append(",").append(_sapce);
                sql.append(field);
            }
            sql.append(_sapce);
            start = true;
        }
        sql.append(_form).append(_sapce).append(table).append(_sapce);
        if (StrKit.notBlank(alias)) sql.append(alias).append(_sapce);
        if (joins != null && !joins.isEmpty()) {
            for (_JOIN join : joins) {
                sql.append(join.toString()).append(_sapce);
            }
        }
        if (StrKit.notBlank(where)) sql.append(_where).append(_sapce).append(where).append(_sapce);
        if (StrKit.notBlank(group)) sql.append(_group).append(_sapce).append(group).append(_sapce);
        if (StrKit.notBlank(order)) sql.append(_orderby).append(_sapce).append(order).append(_sapce);
        if (StrKit.notBlank(limit)) sql.append(_limit).append(_sapce).append(limit);
        return sql.toString();
    }

    /**
     * 获取单条记录
     * 
     * @param paras
     * @return
     */
    public Record find(Object... paras) {
        return Db.findFirst(toString(), paras);
    }

    /**
     * 获取类型并转为Model
     * 
     * @param modelClass
     * @param paras
     * @return
     */
    public <M extends Model<M>> M find(Class<M> modelClass, Object... paras) {
        M model = null;
        try {
            model = modelClass.newInstance();
            model.put(find(paras));
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionKit.unchecked(e);
        }
        return model;
    }

    /**
     * 从缓存获取单条记录
     * 
     * @param cacheName
     * @param key
     * @param paras
     * @return
     */
    public Record findCache(String cacheName, Object key, Object... paras) {
        return Db.findFirstByCache(cacheName, key, toString(), paras);
    }

    /**
     * 从缓存获取类型并转为Model
     * 
     * @param modelClass
     * @param paras
     * @return
     */
    public <M extends Model<M>> M findCache(Class<M> modelClass, String cacheName, Object key, Object... paras) {
        M model = null;
        try {
            model = modelClass.newInstance();
            model.put(findCache(cacheName, key, paras));
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionKit.unchecked(e);
        }
        return model;
    }

    /**
     * 获取结果集
     * 
     * @param paras
     * @return
     */
    public List<Record> select(Object... paras) {
        return Db.find(toString(), paras);
    }

    /**
     * 获取结果集并转为Model
     * 
     * @param modelClass
     * @param paras
     * @return
     */
    public <M extends Model<M>> List<M> select(Class<M> modelClass, Object... paras) {
        List<M> result = Lists.newArrayList();
        List<Record> records = select(paras);
        for (Record record : records)
            try {
                result.add(modelClass.newInstance().put(record));
            } catch (InstantiationException | IllegalAccessException e) {
                ExceptionKit.unchecked(e);
            }
        return result;
    }

    /**
     * 从缓存获取结果集
     * 
     * @param cacheName
     * @param key
     * @param paras
     * @return
     */
    public List<Record> selectCache(String cacheName, Object key, Object... paras) {
        return Db.findByCache(cacheName, key, toString(), paras);
    }

    /**
     * 从缓存获取结果集并转为Model
     * 
     * @param cacheName
     * @param key
     * @param paras
     * @return
     */
    public <M extends Model<M>> List<M> selectCache(Class<M> modelClass, String cacheName, Object key,
            Object... paras) {
        List<M> result = Lists.newArrayList();
        List<Record> records = selectCache(cacheName, key, paras);
        for (Record record : records)
            try {
                result.add(modelClass.newInstance().put(record));
            } catch (InstantiationException | IllegalAccessException e) {
                ExceptionKit.unchecked(e);
            }
        return result;
    }

    /**
     * 查询记录条数
     * 
     * @param paras
     * @return
     */
    public long count(Object... paras) {
        fields = new String[] { "count(1)" };
        return Db.queryLong(toString(), paras);
    }

    private static class _JOIN {
        JOIN   joinType;
        String joinString;

        public _JOIN(String join, JOIN joinType) {
            this.joinType = joinType;
            this.joinString = join;
        }

        @Override
        public String toString() {
            return joinType.toString() + _sapce + _join + joinString;
        }
    }

    public static enum JOIN {
        INNER, LEFT, RIGHT, FULL
    }

}
