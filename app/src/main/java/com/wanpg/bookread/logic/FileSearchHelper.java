package com.wanpg.bookread.logic;

import android.os.Handler;
import android.os.Message;

import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSearchHelper {
    private Handler handler;

    public FileSearchHelper(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler = handler;
    }

    public void searchAdvance(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            searchAuto(list.get(i));
        }
    }

    public void searchAuto(final String Path) {
        new BackTask() {

            @Override
            public void doInThreadWhenCalling(String info) {
                // TODO Auto-generated method stub

            }

            @Override
            public String doInThread() {
                // TODO Auto-generated method stub
                searchFile(Path);
                return null;
            }

            @Override
            public void doBeforeThread() {
                // TODO Auto-generated method stub

            }

            @Override
            public void doAfterThread(String result) {
                // TODO Auto-generated method stub

            }
        }.submit();
    }

    private void searchFile(String folderPath) {
        File f = new File(folderPath);
        Message msg1 = handler.obtainMessage(1002, folderPath);
        handler.sendMessage(msg1);
        if (f.exists()) {
            if (f.isDirectory()) {
                String[] strFileNames = f.list();
                if (strFileNames != null) {
                    for (String sFileName : strFileNames) {
                        searchFile(f.getPath() + "/" + sFileName);
                    }
                }
            } else {
                if (FileUtil.checkLocalFileIsCouldOpen(f.getName())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("BOOKNAME", f.getName());
                    map.put("BOOKPATH", f.getPath());
                    map.put("BOOKLENGTH", f.length() / 1024 + "k");
                    map.put("BOOKDATE", Util.changeMillisToDate(f.lastModified()));
                    Message msg2 = handler.obtainMessage(1001, map);
                    handler.sendMessage(msg2);//发送搜索结果，并更新界面
                }
            }
        }
    }
}
