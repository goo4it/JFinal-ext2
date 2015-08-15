/**
 * 
 */
package com.jfinal.ext2.plugin.activerecord.tx;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 数据库事务
 * @author BruceZCQ
 */
public abstract class Tx {

	public void execute() {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					Tx.this.sql();
				} catch (Exception e) {
					Tx.this.error(e);
					return false;
				}
				return true;
			}
		});
	}
	
	public abstract void sql();
	
	public abstract void error(Exception e);
}
