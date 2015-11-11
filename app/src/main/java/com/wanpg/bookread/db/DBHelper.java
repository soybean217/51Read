package com.wanpg.bookread.db;

import com.wanpg.bookread.BaseApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public static int VERSION = 1;
		
	private static final String dbName = "read_database";

	private static DBHelper dbHelper = new DBHelper(BaseApplication.getInstance(), dbName);
	
	public final static int WRITABLE = 0;
    public final static int READABLE = 1;
	
	public static DBHelper getInstance(){
		if(dbHelper==null){
			
		}
		return dbHelper;
	}
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DBHelper(Context context, String name) {
        this(context, name, VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		ShelfBookTable.createTable(db);
		DownloadTable.createTable(db);
		TxtMarkerTable.createTable(db);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		ShelfBookTable.createTable(db);
		DownloadTable.createTable(db);
		TxtMarkerTable.createTable(db);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void closeDBHelper(){
		this.close();
	}
	
	public SQLiteDatabase openDBHelper(int type){
		SQLiteDatabase db = openDB(type); 
		this.onOpen(db);
		return db;
	}

	private SQLiteDatabase openDB(int type){
		if(type==READABLE){
			return this.getReadableDatabase();
		}
		if(type==WRITABLE){
			return this.getWritableDatabase();
		}
		return null;
	}
	
	/**
	 * 本地书架的数据库
	 * @author Jinpeng
	 */
	public static class ShelfBookTable{
		public static final String TABLE_NAME = "localshelf";
		
		public static final String READMODE = "readMode";
		public static final String BOOKNAME = "bookName";
		public static final String POSINSHELF = "posInShelf";
		public static final String AUTHOR = "author";
		public static final String BOOKPATH = "bookPath";
		public static final String COVERPATH = "coverPath";
		public static final String INNERFILETYPE = "innerFileType";
		public static final String THUMB = "thumb";
		public static final String BOOKID = "bookId";
		public static final String URL = "url";
		public static final String CHAPTERID = "chapterId";
		public static final String CHAPTERURL = "chapterUrl";
		
		public static final String CREATE_TABLE_SQL = 
				"create table if not exists " + TABLE_NAME + " (" +
				READMODE + " text," +
				BOOKNAME + " text," +
	            POSINSHELF + " integer," +
	            AUTHOR + " text," +
	            BOOKPATH + "  text," +
	            COVERPATH + " text," +
	            INNERFILETYPE + " text," +
	            THUMB + " text," +
	            BOOKID + " integer," +
	            URL + " text," +
	            CHAPTERID + " text," +
	            CHAPTERURL + " text)";	
		
		public static void createTable(SQLiteDatabase db){
			db.execSQL(CREATE_TABLE_SQL);
		}
	}
    
	/**
	 * 下载的表
	 * @author Jinpeng
	 */
    public static class DownloadTable{
		public static final String TABLE_NAME = "download";
		
		public static final String DOWNLOAD_ID = "downloadId";
		public static final String BOOK_ID = "bookId";
		public static final String URL = "url";
		public static final String COVER_URL = "coverUrl";
		public static final String NAME = "name";
		public static final String CONTENT_TYPE = "contentType";
		public static final String INNERFILE_TYPE = "innerFileType";
		public static final String FILE_LENGTH = "fileLength";
		public static final String FINISHED_LENGTH = "finishedLength";
		public static final String SAVE_PATH = "savePath";
		public static final String DOWNLOAD_STATUS = "downloadStatus";
		
		private static final String CREATE_TABLE_SQL = 
				"create table if not exists " + TABLE_NAME + " (" +
	            DOWNLOAD_ID + " integer," +
	            BOOK_ID + " integer," +
	            URL + " text," +
	            COVER_URL + " text," +
	            NAME + " integer," +
	            CONTENT_TYPE + " text," +
	            INNERFILE_TYPE + "  text," +
	            FILE_LENGTH + " text," +
	            FINISHED_LENGTH + " text," +
	            SAVE_PATH + " text," +
	            DOWNLOAD_STATUS + " integer" +
	            ")";
		public static void createTable(SQLiteDatabase db){
			db.execSQL(CREATE_TABLE_SQL);
		}
	}
    
    /**
     * txt书籍的书签表
     * @author Jinpeng
     */
    public static class TxtMarkerTable{
		public static final String TABLE_NAME = "txt_marker";
		
		public static final String DATE = "date";
		public static final String PROGRESS = "progress";
		public static final String CONTENT = "content";
		public static final String PAGE_BEGIN = "pageBegin";
		public static final String PAGE_END = "pageEnd";
		public static final String BOOK_PATH = "bookPath";
		private static final String CREATE_TABLE_SQL = 
				"create table if not exists " + TABLE_NAME + " (" +
				DATE + " text," +
				PROGRESS + " text," +
				CONTENT + " text," +
				PAGE_BEGIN + " text," +
				PAGE_END + " text," + 
				BOOK_PATH + " text" + 
	            ")";
		public static void createTable(SQLiteDatabase db){
			db.execSQL(CREATE_TABLE_SQL);
		}
	}
}
