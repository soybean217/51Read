package com.wanpg.bookread.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.utils.Util;

import java.io.File;

public class ShelfBookInfoDialog extends AlertDialog {

    private Context context;
    private ShelfBook shelfBookMode;

    private TextView tv_book_path, tv_pop_poperity_name;
    private TextView tv_book_length;
    private TextView tv_book_date;
    private LinearLayout ll_book_property_back;

    public ShelfBookInfoDialog(Context context, boolean cancelable,
                               OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public ShelfBookInfoDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public ShelfBookInfoDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ShelfBookInfoDialog(Context context, int theme, ShelfBook shelfBookMode) {
        // TODO Auto-generated constructor stub
        super(context, theme);
        this.context = context;
        this.shelfBookMode = shelfBookMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_book_property);
        //在此处获得子布局的控件
        tv_pop_poperity_name = (TextView) findViewById(R.id.tv_pop_poperity_name);
        tv_book_path = (TextView) findViewById(R.id.tv_pop_poperity_path);
        tv_book_length = (TextView) findViewById(R.id.tv_pop_poperity_length);
        tv_book_date = (TextView) findViewById(R.id.tv_pop_poperity_date);
        ll_book_property_back = (LinearLayout) findViewById(R.id.ll_pop_book_property_back);

        String path = shelfBookMode.bookPath;
        File f = new File(path);
        float length = f.length() / 1024;
        tv_pop_poperity_name.setText(shelfBookMode.bookName);
        tv_book_path.setText(path);
        tv_book_length.setText(length + "k");
        tv_book_date.setText(Util.changeMillisToDate(f.lastModified()));
    }

}
