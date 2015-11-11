package com.wanpg.bookread.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.adapter.BookSearchAdvanceListViewAdapter;
import com.wanpg.bookread.utils.Util;
import com.wanpg.bookread.widget.Notice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自动搜索前的选择界面
 *
 * @author Jinpeng
 */
public class BookSearchAdvanceActivity extends BaseActivity {
    private ImageButton ib_head_bar_back;
    private TextView tv_head_bar_ok;
    private ListView lv_search_advance;
    private TextView tv_search_folder_path;
    private String strFolderPath;
    private BookSearchAdvanceListViewAdapter bsalva;
    private List<String> listItems = new ArrayList<String>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search_advance);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 0://接收到往下一层的信息
                        updataList(msg.obj.toString());
                        break;

                    default:
                        break;
                }
            }
        };

        ib_head_bar_back = (ImageButton) findViewById(R.id.ib_back);
        tv_head_bar_ok = (TextView) findViewById(R.id.tv_head_bar_ok);
        lv_search_advance = (ListView) findViewById(R.id.lv_search_advance);
        tv_search_folder_path = (TextView) findViewById(R.id.tv_search_folder_path);


        ib_head_bar_back.setOnClickListener(new BookSearchAdvanceHeadBarClickListener());
        tv_head_bar_ok.setOnClickListener(new BookSearchAdvanceHeadBarClickListener());

        strFolderPath = Environment.getExternalStorageDirectory().toString();
        tv_search_folder_path.setText(strFolderPath);
        listItems = getData(strFolderPath);
        bsalva = new BookSearchAdvanceListViewAdapter(BookSearchAdvanceActivity.this, listItems, handler);
        bsalva.setParentPath(strFolderPath);

        lv_search_advance.setAdapter(bsalva);

    }


    private class BookSearchAdvanceHeadBarClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(ib_head_bar_back)) {
                if (strFolderPath.equals(Environment.getExternalStorageDirectory().toString())) {
                    finish();
                    overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
                } else {
                    updataList(new File(strFolderPath).getParent());
                }
            } else if (v.equals(tv_head_bar_ok)) {
                ArrayList<String> listChecked = getCheckedFolderName();
                if (listChecked.size() > 1) {
                    Notice.showToast("只能选择一个文件夹！");
                } else {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("LISTCHECKED", listChecked);
                    intent.putExtras(bundle);
                    BookSearchAdvanceActivity.this.setResult(1, intent);
                    BookSearchAdvanceActivity.this.finish();
                    overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
                }
            }
        }
    }

    private ArrayList<String> getCheckedFolderName() {
        ArrayList<String> list = new ArrayList<String>();
        Map<Integer, Boolean> mapIsChecked = bsalva.getMapIsChecked();
        for (int i = 0; i < mapIsChecked.size(); i++) {
            if (mapIsChecked.get(i)) {
                list.add(strFolderPath + "/" + listItems.get(i));
            }
        }
        return list;
    }

    private List<String> getData(String path) {
        List<String> list = new ArrayList<String>();
        String[] s1 = new File(path).list();
        s1 = Util.sortArrays(s1);
        for (String s2 : s1) {
            File f = new File(path + "/" + s2);
            if (!f.isHidden()) {
                if (f.isDirectory()) {
                    list.add(s2);
                }
            }
        }
        return list;
    }

    private void updataList(String path) {
        strFolderPath = path;
        tv_search_folder_path.setText(strFolderPath);
        listItems = getData(path);
        bsalva.setParentPath(path);
        bsalva.setListItems(listItems);
        bsalva.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (strFolderPath.equals(Environment.getExternalStorageDirectory().toString())) {
            finish();
            overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
        } else {
            updataList(new File(strFolderPath).getParent());
        }
    }


}
