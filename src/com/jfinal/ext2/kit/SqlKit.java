/**
 * 
 */
package com.jfinal.ext2.kit;

/**
 * @author BruceZCQ
 *
 */
public class SqlKit {

	private  StringBuilder sql = null;
	
	public static final String select = "SELECT";
	public static final String from = "FROM";
	public static final String where = "WHERE";
	public static final String and = "AND";
	public static final String or = "OR";
	public static final String orderby = "ORDER BY";
	public static final String limit = "LIMIT";
	private static final String sapce = " ";
	
	public enum ORDER{
		DESC,
		ASC
	};
	
	public SqlKit(){
		sql = new StringBuilder();
	}
	
	
	public SqlKit select(String... selects){
		int index = 0;
		sql.append(SqlKit.select).append(SqlKit.sapce);
		for (String string : selects) {
			sql.append(string);
			if (index != selects.length-1) {
				sql.append(",");
			}else{
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit from(String... tableNames){
		int index = 0;
		sql.append(SqlKit.from).append(SqlKit.sapce);
		for (String string : tableNames) {
			sql.append(string);
			if (index != tableNames.length - 1) {
				sql.append(",");
			}else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit where(String where){
		sql.append(SqlKit.where).append(SqlKit.sapce).append(where).append(SqlKit.sapce);
		return this;
	}
	
	public SqlKit and(String condition){
		sql.append(SqlKit.and).append(SqlKit.sapce).append(condition).append(SqlKit.sapce);
		return this;
	}
	
	public SqlKit or(String condition){
		sql.append(SqlKit.or).append(SqlKit.sapce).append(condition).append(SqlKit.sapce);
		return this;
	}
	
	private SqlKit orderBy(String condition, ORDER order){
		sql.append(SqlKit.orderby).append(SqlKit.sapce).append(condition).append(SqlKit.sapce).append(order.toString());
		return this;
	}
	
	public SqlKit ascOrderBy(String condition) {
		return this.orderBy(condition, ORDER.ASC);
	}
	
	public SqlKit descOrderBy(String condition) {
		return this.orderBy(condition, ORDER.DESC);
	}

	public SqlKit limit(String arg) {
		sql.append(SqlKit.sapce).append(SqlKit.limit).append(SqlKit.sapce).append(arg);
		return this;
	}
	
	public String sql(){
		String _sql = sql.toString();
		if (!_sql.endsWith(";")) {
			sql.append(";");
			return sql.toString();
		}
		return _sql;
	}
}
