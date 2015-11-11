package com.wanpg.bookread.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wanpg.bookread.db.DBHelper.DownloadTable;
import com.wanpg.bookread.download.DownItem;

public class DownloadDao extends Dao{

	/**
     * 添加一条下载信息到数据库
     *
     * @param item
     */
    public void addOneDownloadItem(DownItem item) {
    	SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DownloadTable.DOWNLOAD_ID, item.downloadId);
        cv.put(DownloadTable.BOOK_ID, item.bookId);
        cv.put(DownloadTable.URL, item.url);
        cv.put(DownloadTable.COVER_URL, item.coverUrl);
        cv.put(DownloadTable.NAME, item.name);
        cv.put(DownloadTable.CONTENT_TYPE, item.contentType);
        cv.put(DownloadTable.INNERFILE_TYPE, item.innerFileType);
        cv.put(DownloadTable.FILE_LENGTH, item.fileLength + "");
        cv.put(DownloadTable.FINISHED_LENGTH, item.finishedLength + "");
        cv.put(DownloadTable.SAVE_PATH, item.savePath);
        cv.put(DownloadTable.DOWNLOAD_STATUS, item.status);
        db.insert(DownloadTable.TABLE_NAME, null, cv);
    }

    /**
     * 获得下载信息
     *
     * @return
     */
    public List<DownItem> queryDownloadItems() {
    	SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<DownItem> list = new ArrayList<DownItem>();

        Cursor cursor = db.rawQuery("select * from " + DownloadTable.TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            DownItem item = new DownItem();
            item.downloadId = cursor.getInt(cursor.getColumnIndex(DownloadTable.DOWNLOAD_ID));
            item.bookId = cursor.getInt(cursor.getColumnIndex(DownloadTable.BOOK_ID));
            item.url = cursor.getString(cursor.getColumnIndex(DownloadTable.URL));
            item.coverUrl = cursor.getString(cursor.getColumnIndex(DownloadTable.COVER_URL));
            item.name = cursor.getString(cursor.getColumnIndex(DownloadTable.NAME));
            item.contentType = cursor.getString(cursor.getColumnIndex(DownloadTable.CONTENT_TYPE));
            item.innerFileType = cursor.getString(cursor.getColumnIndex(DownloadTable.INNERFILE_TYPE));
            item.fileLength = Long.parseLong(cursor.getString(cursor.getColumnIndex(DownloadTable.FILE_LENGTH)));
            item.finishedLength = Long.parseLong(cursor.getString(cursor.getColumnIndex(DownloadTable.FINISHED_LENGTH)));
            item.savePath = cursor.getString(cursor.getColumnIndex(DownloadTable.SAVE_PATH));
            item.status = cursor.getInt(cursor.getColumnIndex(DownloadTable.DOWNLOAD_STATUS));
            list.add(item);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 修改某一行的某个数据
     *
     * @param item  --数据
     */
    public void updateOneDownloadItem(DownItem item) {
    	SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        String whereClause = null;
        String[] whereArgs = null;

        ContentValues cv = new ContentValues();
        cv.put(DownloadTable.DOWNLOAD_ID, item.downloadId);
        cv.put(DownloadTable.BOOK_ID, item.bookId);
        cv.put(DownloadTable.URL, item.url);
        cv.put(DownloadTable.COVER_URL, item.coverUrl);
        cv.put(DownloadTable.NAME, item.name);
        cv.put(DownloadTable.CONTENT_TYPE, item.contentType);
        cv.put(DownloadTable.INNERFILE_TYPE, item.innerFileType);
        cv.put(DownloadTable.FILE_LENGTH, item.fileLength + "");
        cv.put(DownloadTable.FINISHED_LENGTH, item.finishedLength + "");
        cv.put(DownloadTable.SAVE_PATH, item.savePath);
        cv.put(DownloadTable.DOWNLOAD_STATUS, item.status);
//        db.insert(DownloadTable.TABLE_NAME, null, cv);

        whereClause = DownloadTable.DOWNLOAD_ID + " = ?";
        whereArgs = new String[]{item.downloadId + ""};
        //String sql = "update "+DownloadTable.TABLE_NAME + " set "+cName+"="+value+" where id = "+item.getId();
        //db.execSQL(sql);
        int result = db.update(DownloadTable.TABLE_NAME, cv, whereClause, whereArgs);
        Log.d("update_result", result + "");
    }

    /**
     * 删除下载表中的数据
     *
     * @param item 传入null则删除所有数据
     * @return
     */
    public boolean delDownloadItem(DownItem item) {
    	SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        String whereClause = null;
        String[] whereArgs = null;
        if (item != null) {
            whereClause = DownloadTable.DOWNLOAD_ID + " = ?";
            whereArgs = new String[]{item.downloadId + ""};
        }
        int result = db.delete(DownloadTable.TABLE_NAME, whereClause, whereArgs);
        return false;
    }
	
	
    public void exitDownloadCheck(ConcurrentHashMap<Integer, DownItem> threadMap) {
        Iterator<Integer> iterator = threadMap.keySet().iterator();
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        db.beginTransaction();
        while (iterator.hasNext()) {
            int downLoadId = iterator.next();
            DownItem item = threadMap.get(downLoadId);
            if (item.status == DownItem.STATUS_WAIT || item.status == DownItem.STATUS_DOWNLOADING) {
                item.status = DownItem.STATUS_PAUSE;
                ContentValues cv = new ContentValues();
                cv.put(DownloadTable.DOWNLOAD_ID, item.downloadId);
                cv.put(DownloadTable.BOOK_ID, item.bookId);
                cv.put(DownloadTable.URL, item.url);
                cv.put(DownloadTable.COVER_URL, item.coverUrl);
                cv.put(DownloadTable.NAME, item.name);
                cv.put(DownloadTable.CONTENT_TYPE, item.contentType);
                cv.put(DownloadTable.INNERFILE_TYPE, item.innerFileType);
                cv.put(DownloadTable.FILE_LENGTH, item.fileLength + "");
                cv.put(DownloadTable.FINISHED_LENGTH, item.finishedLength + "");
                cv.put(DownloadTable.SAVE_PATH, item.savePath);
                cv.put(DownloadTable.DOWNLOAD_STATUS, item.status);

                String whereClause = DownloadTable.DOWNLOAD_ID + " = ?";
                String[] whereArgs = new String[]{item.downloadId + ""};
                db.update(DownloadTable.TABLE_NAME, cv, whereClause, whereArgs);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    
}
