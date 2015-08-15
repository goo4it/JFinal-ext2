/**
 * 
 */
package com.jfinal.ext2.plugin.activerecord.tx;

/**
 * 数据库事务处理
 * @author BruceZCQ
 */
public class DbTx {
	
	/**
	 * 处理数据库操作，包含事务处理
	 * @param tx
	 */
	public static void execute(Tx tx) {
		tx.execute();
	}
}
