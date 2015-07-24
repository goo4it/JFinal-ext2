/**
 * 
 */
package com.jfinal.ext2.kit;

/**
 * @author BruceZCQ
 *
 */
public class SqlKit {
	
	public static final String select = "SELECT";
	public static final String from = "FROM";
	public static final String where = "WHERE";
	public static final String and = "AND";
	public static final String or = "OR";
	public static final String orderby = "ORDER BY";
	public static final String limit = "LIMIT";

	private static final String sapce = " ";
	private StringBuilder sql = null;

	private enum ORDER{
		DESC,
		ASC
	};
	
	private SqlKit orderBy(String condition, ORDER order){
		sql.append(SqlKit.orderby).append(SqlKit.sapce).append(condition).append(SqlKit.sapce).append(order.toString());
		return this;
	}
	
	public static class Column {
		
		private String colName = null;
		private String as = null;
		
		public Column(String colName, String as) {
			this.colName = colName;
			this.as = as;
		}
		
		public String column() {
			return new StringBuilder(this.colName).append(SqlKit.sapce).append(this.as).toString();
		}
	}

	//==========================
	
	public SqlKit(){
		sql = new StringBuilder();
	}
	
	public SqlKit select(String... selects){
		sql.append(SqlKit.select).append(SqlKit.sapce);
		int index = 0;
		for (String string : selects) {
			sql.append(string);
			if (index != selects.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			} else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public Column column(String col, String as) {
		return new Column(col, as);
	}
	
	public SqlKit select(Column... cols) {
		sql.append(SqlKit.select).append(SqlKit.sapce);
		int index = 0;
		for (Column col : cols) {
			sql.append(col.column());
			if (index != cols.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			} else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit from(String... tableNames){
		sql.append(SqlKit.from).append(SqlKit.sapce);
		int index = 0;
		for (String string : tableNames) {
			sql.append(string);
			if (index != tableNames.length - 1) {
				sql.append(",").append(SqlKit.sapce);
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
	
	public SqlKit ascOrderBy(String condition) {
		return this.orderBy(condition, ORDER.ASC);
	}
	
	public SqlKit descOrderBy(String condition) {
		return this.orderBy(condition, ORDER.DESC);
	}

	public SqlKit limit(String... params) {
		if (params.length > 2) {
			throw new IllegalArgumentException("more params");
		}
		
		sql.append(SqlKit.sapce).append(SqlKit.limit).append(SqlKit.sapce);
		
		int index = 0;
		for (String param : params) {
			sql.append(param);
			if (index != params.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			}else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
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
