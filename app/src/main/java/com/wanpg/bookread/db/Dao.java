package com.wanpg.bookread.db;

public class Dao {
	
	public static final int FLAG_TABLE_CREATE = 0;
	public static final int FLAG_TABLE_DELETE = 1;
	public static final int FLAG_DATA_UPDATE = 2;
	public static final int FLAG_DATA_INSERT = 3;
	public static final int FLAG_DATA_DELETE = 4;
	
	public DBHelper mDbHelper = DBHelper.getInstance();
	
	public interface DataBaseObserver{
		public void onDataChanged(String dbName, int flag);
	}
	
	private DataBaseObserver mDBObserver;
	public void setDBObserver(DataBaseObserver observer){
		mDBObserver = observer;
	}
	
	protected void notifyDataChanged(String dbName, int flag){
		if(mDBObserver!=null)
			mDBObserver.onDataChanged(dbName, flag);
	}
	
}
