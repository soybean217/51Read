package com.wanpg.bookread.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanpg.bookread.db.DBHelper.TxtMarkerTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MarkerDao extends Dao{

	
	public void addOneNewTxtMaker(Map<String, String> txtMakerMap, String bookPath) {
		SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", txtMakerMap.get("date"));
        cv.put("progress", txtMakerMap.get("progress"));
        cv.put("content", txtMakerMap.get("content"));
        cv.put("pageBegin", txtMakerMap.get("pageBegin"));
        cv.put("pageEnd", txtMakerMap.get("pageEnd"));
        cv.put(TxtMarkerTable.BOOK_PATH, bookPath);
        db.insert(TxtMarkerTable.TABLE_NAME, null, cv);
    }


    public List<Map<String, String>> queryTxtMakers(String bookPath) {
		SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Cursor cursor = db.rawQuery("select * from " + TxtMarkerTable.TABLE_NAME +" where " +TxtMarkerTable.BOOK_PATH+"='"+bookPath+"'", new String[]{});
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("date", cursor.getString(0));
            map.put("progress", cursor.getString(1));
            map.put("content", cursor.getString(2));
            map.put("pageBegin", cursor.getString(3));
            map.put("pageEnd", cursor.getString(4));
            list.add(map);
        }

        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void delOneTxtMaker(Map<String, String> txtMakerMap, String bookPath) {
		SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        String whereClause = null;
        String[] whereArgs = null;
        if (txtMakerMap != null) {
            whereClause = TxtMarkerTable.PROGRESS+" = ? and "+TxtMarkerTable.BOOK_PATH+" = ?";
            whereArgs = new String[]{txtMakerMap.get("progress"), bookPath};
        }
        int result = db.delete(TxtMarkerTable.TABLE_NAME, whereClause, whereArgs);
    }
	
}
