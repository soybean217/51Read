package com.wanpg.bookread.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.adapter.FileBrowserListViewAdapter;
import com.wanpg.bookread.utils.Util;

/**
 * 本地文件浏览界面
 * @author Jinpeng
 */
public class BookFileBrowserActivity extends BaseActivity {

    private TextView tv_head_bar_ok;
    private ListView lv_file_browser;
    private TextView tv_parent_folder_path;
    private List<Map<String, String>> listItems;
    private String strParentPath;
    private Handler handler;
    private FileBrowserListViewAdapter fblva;
    private List<Integer> listLastPos = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_broswer);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 0:

                        break;
                    case 1000:
                        readNext(msg.obj.toString());
                        break;
                    case 1001://进行返回值操作，返回BookMainActivity
                        Intent intent = new Intent();
                        intent.putExtra("choosePath", msg.obj.toString());
                        BookFileBrowserActivity.this.setResult(1, intent);
                        BookFileBrowserActivity.this.finish();
                        overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
                        break;
                    default:
                        break;
                }
            }
        };
        initData();
        initUI();
    }

    private void initData() {
        strParentPath = Environment.getExternalStorageDirectory().toString();

        listItems = getData(strParentPath);
    }

    private void initUI() {
        lv_file_browser = (ListView) findViewById(R.id.lv_file_browser);
        tv_parent_folder_path = (TextView) findViewById(R.id.tv_file_browser_folder_path);

        ((TextView) findViewById(R.id.tv_title)).setVisibility(View.GONE);
        setBackEvent(findViewById(R.id.ib_back));

        tv_parent_folder_path.setText(strParentPath);

        fblva = new FileBrowserListViewAdapter(this, listItems, handler);
        fblva.setParentPath(strParentPath);
        lv_file_browser.setAdapter(fblva);
    }


    private List<Map<String, String>> getData(String path) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
        String[] s1 = new File(path).list();
        s1 = Util.sortArrays(s1);

        for (String s2 : s1) {

            File f = new File(path + "/" + s2);
            if (!f.isHidden()) {

                Map<String, String> map = new HashMap<String, String>();
                map.put("FILENAME", s2);
                String strMsg = "";
                if (f.isDirectory()) {
                    strMsg = f.list().length + "个子文件";
                    map.put("FILEMSG", strMsg);
                    list.add(map);
                } else {
                    strMsg = f.length() / 1024 + "k";
                    map.put("FILEMSG", strMsg);
                    list1.add(map);
                }
            }
        }
        for (int i = 0; i < list1.size(); i++) {
            list.add(list1.get(i));
        }
        return list;
    }

    private void readNext(String path) {
        // TODO Auto-generated method stub
        strParentPath = path;
        tv_parent_folder_path.setText(strParentPath);
        listItems = getData(path);
        fblva.setParentPath(path);
        fblva.setListItems(listItems);
        fblva.notifyDataSetChanged();
        listLastPos.add(lv_file_browser.getCheckedItemPosition());
    }

    private void readBack(String path) {
        // TODO Auto-generated method stub
        strParentPath = path;
        tv_parent_folder_path.setText(strParentPath);
        listItems = getData(path);
        fblva.setParentPath(path);
        fblva.setListItems(listItems);
        fblva.notifyDataSetChanged();
        int x = listLastPos.get(listLastPos.size() - 1);
        //lv_file_browser.setSelection(position)
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (strParentPath.equals(Environment.getExternalStorageDirectory().toString())) {
            super.onBackPressed();
        } else {
            readBack(new File(strParentPath).getParent());
        }
    }
}
