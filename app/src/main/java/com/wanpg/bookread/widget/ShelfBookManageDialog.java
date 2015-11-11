package com.wanpg.bookread.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;

public abstract class ShelfBookManageDialog extends AlertDialog {

    private TextView tv_del, tv_info;
    private ShelfBook shelfBookMode;
    private Context context;

    public ShelfBookManageDialog(Context context, boolean cancelable,
                                 OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public ShelfBookManageDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public ShelfBookManageDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public ShelfBookManageDialog(Context context, int theme, ShelfBook shelfBookMode) {
        // TODO Auto-generated constructor stub
        super(context, theme);
        this.shelfBookMode = shelfBookMode;
        this.context = context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_book_mange);
        tv_del = (TextView) findViewById(R.id.tv_pop_book_manage_delete);
        tv_info = (TextView) findViewById(R.id.tv_pop_book_manage_property);
        tv_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	onDelete(shelfBookMode);
                ShelfBookManageDialog.this.dismiss();
            }
        });
        tv_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "属性", Toast.LENGTH_SHORT).show();
                ShelfBookManageDialog.this.dismiss();

                ShelfBookInfoDialog shelfBookInfoDialog = new ShelfBookInfoDialog(context, R.style.about_dialog, shelfBookMode);
                Window win = shelfBookInfoDialog.getWindow();
                LayoutParams layoutParams = new LayoutParams();
                layoutParams.x = 0;
                layoutParams.y = 0;
                layoutParams.width = LayoutParams.FILL_PARENT;
                layoutParams.height = LayoutParams.FILL_PARENT;
                win.setAttributes(layoutParams);
                shelfBookInfoDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                shelfBookInfoDialog.show();

            }
        });
    }
    
    public abstract void onDelete(ShelfBook shelfBookMode);
}
