package com.wanpg.bookread.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DBHelper.ShelfBookTable;

public class ShelfDao extends Dao{

	/**
     * 查询书架数据
     * @return
     */
    public List<ShelfBook> getShelfBooks() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	List<ShelfBook> list = new ArrayList<ShelfBook>();
        Cursor cursor = null;
        try{
        	cursor = db.rawQuery("select * from " + ShelfBookTable.TABLE_NAME, new String[]{});
            while (cursor.moveToNext()) {
                ShelfBook book = parseCursor(cursor);
                list.add(book);
            }
        }finally{
        	if (cursor != null) cursor.close();
        }
        return list;
    }
    
    public ShelfBook getShelfBookByBook(ShelfBook book) {
		// TODO Auto-generated method stub
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	String sql = "select * from " + ShelfBookTable.TABLE_NAME + " where "; 
    	String readMode = book.readMode;
        String whereClause = null;
        String[] whereArgs = null;
    	if (readMode.equals(ShelfBook.POS_LOCAL_SDCARD) || readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
            whereClause = "bookPath = ?";
            whereArgs = new String[]{book.bookPath};
        } else if (readMode.equals(ShelfBook.POS_ONLINE)) {
            whereClause = "bookId = ?";
            whereArgs = new String[]{book.bookId + ""};
        }
    	Cursor cursor = db.rawQuery(sql + whereClause, whereArgs);
    	ShelfBook tmpBook = null;
    	if(cursor.moveToNext()){
    		tmpBook = parseCursor(cursor);
    	}
    	cursor.close();
    	cursor = null;
    	return tmpBook;
	}
	
    
    /**
     * 删除书架数据
     *
     * @param shelfBookMode 如果为null，则删除所有数据
     * @return
     */
    public boolean deleteShelfBook(ShelfBook shelfBookMode) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String whereClause = null;
        String[] whereArgs = null;
        if (shelfBookMode != null) {
            String readMode = shelfBookMode.readMode;
            if (readMode.equals(ShelfBook.POS_LOCAL_SDCARD) || readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
                whereClause = "bookPath = ?";
                whereArgs = new String[]{shelfBookMode.bookPath};
            } else if (readMode.equals(ShelfBook.POS_ONLINE)) {
                whereClause = "bookId = ?";
                whereArgs = new String[]{shelfBookMode.bookId + ""};
            }
        }
        int result = db.delete(ShelfBookTable.TABLE_NAME, whereClause, whereArgs);
        Log.d("delete_result", result + "");
        notifyDataChanged(ShelfBookTable.TABLE_NAME, FLAG_DATA_DELETE);
        return false;
    }

    public void saveShelfBooks(List<ShelfBook> listShelfBookModes){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        for(ShelfBook mode : listShelfBookModes){
            saveOrUpdateOneBook(db, mode);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        notifyDataChanged(ShelfBookTable.TABLE_NAME, FLAG_DATA_INSERT);
    }
    
    public void saveOrUpdateOneBook(ShelfBook book) {
		// TODO Auto-generated method stub
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	db.beginTransaction();
    	saveOrUpdateOneBook(db, book);
        db.setTransactionSuccessful();
        db.endTransaction();
        notifyDataChanged(ShelfBookTable.TABLE_NAME, FLAG_DATA_INSERT);
	}
    
    private void saveOrUpdateOneBook(SQLiteDatabase db, ShelfBook book){
    	String sql = "select * from " + ShelfBookTable.TABLE_NAME + " where "; 
    	String readMode = book.readMode;
        String whereClause = null;
        String[] whereArgs = null;
    	if (readMode.equals(ShelfBook.POS_LOCAL_SDCARD) || readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
            whereClause = "bookPath = ?";
            whereArgs = new String[]{book.bookPath};
        } else if (readMode.equals(ShelfBook.POS_ONLINE)) {
            whereClause = "bookId = ?";
            whereArgs = new String[]{book.bookId + ""};
        }
    	Cursor cursor = db.rawQuery(sql + whereClause, whereArgs);
    	boolean isUpdate = false;
    	if(cursor.moveToNext()){
    		ShelfBook tmpBook = parseCursor(cursor);
    		if(tmpBook!=null)
    			isUpdate = true;
    	}
    	cursor.close();
    	cursor = null;
    	ContentValues cv = new ContentValues();
        cv.put("readMode", book.readMode);
        cv.put("bookName", book.bookName);
        cv.put("posInShelf", book.posInShelf);
        cv.put("author", book.author);
        cv.put("bookPath", book.bookPath);
        cv.put("coverPath", book.coverPath);
        cv.put("innerFileType", book.innerFileType);
        cv.put("thumb", book.thumb);
        cv.put("bookId", book.bookId);
        cv.put("url", book.url);
        cv.put("chapterId", book.chapterId);
        cv.put("chapterUrl", book.chapterUrl);
    	if(isUpdate){
    		db.update(ShelfBookTable.TABLE_NAME, cv, whereClause, whereArgs);
    	}else{
    		db.insert(ShelfBookTable.TABLE_NAME, null, cv);
    	}
    }
    
    private ShelfBook parseCursor(Cursor cursor) {
		// TODO Auto-generated method stub
    	ShelfBook shelfBookMode = new ShelfBook();
        //此部分为公共
        shelfBookMode.readMode = cursor.getString(cursor.getColumnIndex(ShelfBookTable.READMODE));
        shelfBookMode.bookName = cursor.getString(cursor.getColumnIndex(ShelfBookTable.BOOKNAME));
        shelfBookMode.posInShelf = cursor.getInt(cursor.getColumnIndex(ShelfBookTable.POSINSHELF));
        shelfBookMode.author = cursor.getString(cursor.getColumnIndex(ShelfBookTable.AUTHOR));

        //此部分为本地书籍信息，
        shelfBookMode.bookPath = cursor.getString(cursor.getColumnIndex(ShelfBookTable.BOOKPATH));
        shelfBookMode.coverPath = cursor.getString(cursor.getColumnIndex(ShelfBookTable.COVERPATH));
        shelfBookMode.innerFileType = cursor.getString(cursor.getColumnIndex(ShelfBookTable.INNERFILETYPE));

        //此部分为网络书籍信息
        shelfBookMode.thumb = cursor.getString(cursor.getColumnIndex(ShelfBookTable.THUMB));
        shelfBookMode.bookId = cursor.getInt(cursor.getColumnIndex(ShelfBookTable.BOOKID));
        shelfBookMode.url = cursor.getString(cursor.getColumnIndex(ShelfBookTable.URL));
        shelfBookMode.chapterId = cursor.getString(cursor.getColumnIndex(ShelfBookTable.CHAPTERID));
        shelfBookMode.chapterUrl = cursor.getString(cursor.getColumnIndex(ShelfBookTable.CHAPTERURL));
		return shelfBookMode;
	}
}
