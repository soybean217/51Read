package com.wanpg.bookread.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.ui.activity.BookFileBrowserActivity;
import com.wanpg.bookread.ui.hall.ShelfFragment;
import com.wanpg.bookread.widget.AsyncShelfImageView;
import com.wanpg.bookread.widget.ShelfBookManageDialog;

public class BookShelfGridViewAdapter extends BaseAdapter {

    private List<ShelfBook> listItems = new ArrayList<ShelfBook>();
    private BaseActivity activity;
    private ShelfFragment mFragment;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;

    public BookShelfGridViewAdapter(ShelfFragment fragment, List<ShelfBook> listItems) {
        // TODO Auto-generated constructor stub
        this.listItems = listItems;
        this.mFragment = fragment;
        this.activity = mFragment.mActivity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return 0 + 1;
        } else {
            return listItems.size() + 1;
        }
    }

    @Override
    public ShelfBook getItem(int position) {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return null;
        } else {
            return listItems.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.shelf_list_item_flat, null);
            holder.shelf_main_cell_layout = (RelativeLayout) convertView.findViewById(R.id.shelf_main_cell_layout);
            holder.iv_bookcover = (AsyncShelfImageView) convertView.findViewById(R.id.shelf_book_cover);
            holder.tv_bookname = (TextView) convertView.findViewById(R.id.shelf_book_name);
            holder.tv_bookauthor = (TextView) convertView.findViewById(R.id.shelf_book_author);

            holder.shelf_main_cell_add_layout = (RelativeLayout) convertView.findViewById(R.id.shelf_main_cell_add_layout);
            holder.shelf_main_cell_book_layout = (RelativeLayout) convertView.findViewById(R.id.shelf_main_cell_book_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position < listItems.size()) {
            ShelfBook book = listItems.get(position);
        	holder.shelf_main_cell_book_layout.setVisibility(View.VISIBLE);
            holder.shelf_main_cell_add_layout.setVisibility(View.GONE);

            String bookName = book.bookName;
            String bookAuthor = book.author;


            holder.iv_bookcover.setCover(R.drawable.default_book_cover, book);

            holder.tv_bookname.setText(bookName);
            holder.tv_bookauthor.setText("作者：" + bookAuthor);
        } else {
            holder.shelf_main_cell_book_layout.setVisibility(View.GONE);
            holder.shelf_main_cell_add_layout.setVisibility(View.VISIBLE);
        }
        final int pos = position;
        holder.shelf_main_cell_book_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
            	mFragment.mActivity.openBook(listItems.get(pos));
            }
        });
        holder.shelf_main_cell_add_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(activity, BookFileBrowserActivity.class);
                activity.startActivityForResult(intent, 1);
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.out_none);
            }
        });
        holder.shelf_main_cell_book_layout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                if (pos < listItems.size()) {
                    ShelfBookManageDialog bookManageDialog = new ShelfBookManageDialog(activity, R.style.about_dialog, listItems.get(pos)) {
						
						@Override
						public void onDelete(ShelfBook shelfBookMode) {
							// TODO Auto-generated method stub
							DaoManager.getInstance().getShelfDao().deleteShelfBook(shelfBookMode);
						}
					};
                    Window win = bookManageDialog.getWindow();
                    LayoutParams layoutParams = new LayoutParams();
                    layoutParams.x = 0;
                    layoutParams.y = 0;
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    layoutParams.height = LayoutParams.MATCH_PARENT;
                    win.setAttributes(layoutParams);
                    bookManageDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                    bookManageDialog.show();

                }
                return true;
            }
        });
        return convertView;
    }

    class ViewHolder {
        public RelativeLayout shelf_main_cell_layout;
        public RelativeLayout shelf_main_cell_book_layout;
        public RelativeLayout shelf_main_cell_add_layout;
        public AsyncShelfImageView iv_bookcover;
        public TextView tv_bookname;
        public TextView tv_bookauthor;
    }
    
    public void update(List<ShelfBook> listItems){
    	this.listItems = listItems;
    	this.notifyDataSetChanged();
    }

}
