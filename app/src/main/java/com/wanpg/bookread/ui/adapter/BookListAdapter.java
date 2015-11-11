package com.wanpg.bookread.ui.adapter;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.ui.book.StoreBookShowFragment;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.AsyncImageView;

public class BookListAdapter extends BaseAdapter {

    private BaseActivity mActivity;
    private BaseFragment mFragment;
    private List<Map<String, Object>> listItems;
    private ViewHolder holder;

    public BookListAdapter(BaseFragment fragment, List<Map<String, Object>> listItems) {
        // TODO Auto-generated constructor stub
    	this.mFragment = fragment;
        this.mActivity = mFragment.mActivity;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return 0;
        } else {
            return listItems.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        if (listItems == null) {
            return null;
        } else {
            return listItems.get(arg0);
        }
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int pos, View v, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (v == null) {
            v = LayoutInflater.from(mActivity).inflate(R.layout.bookstore_vp_recommend_cell, null);
            holder = new ViewHolder();
            holder.rl_main = (RelativeLayout) v.findViewById(R.id.rl_bookstore_cell_main);
            holder.iv_cover = (AsyncImageView) v.findViewById(R.id.iv_bookstore_recommend_cell);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_bookstore_recommend_cell_title);
            holder.tv_intro = (TextView) v.findViewById(R.id.tv_bookstore_recommend_cell_content);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        final int x = pos;
        holder.tv_name.setText(listItems.get(pos).get("name").toString());
        holder.tv_intro.setText(listItems.get(pos).get("intro").toString());
        String imageHref = ShuPengConfig.BOOK_COVER_MAIN_B + listItems.get(pos).get("thumb").toString();

        holder.iv_cover.setImageUrl(imageHref, R.drawable.ic_launcher,DisplayUtil.dp2px(40), DisplayUtil.dp2px(56));

        //		holder.iv_cover.setTag(imageHref);
//		Bitmap bmCache = new AsynImageLoader(imageHref, new ImageCallBack() {
//			
//			public void ImageLoad(String imageHref, Bitmap bitmap) {
//				// TODO Auto-generated method stub
//				ImageView imageViewByTag=(ImageView) listView.findViewWithTag(imageHref);
//				if(imageViewByTag!=null){
//					if(bitmap!=null){
//						imageViewByTag.setImageBitmap(bitmap);
//					}
//				}
//			}
//		}, DisplayUtil.dp2px(context, 40), DisplayUtil.dp2px(context, 56)).showImageAsyn();
//		if(bmCache==null){
//			holder.iv_cover.setImageResource(R.drawable.ic_launcher);
//		}else{
//			holder.iv_cover.setImageBitmap(bmCache);
//		}

        holder.rl_main.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putInt("bookId", Integer.parseInt(listItems.get(x).get("bookId").toString()));
                mFragment.onFragmentDo(BaseFragment.TYPE_TO_BOOK_DETAIL, bundle);
            }
        });

        return v;
    }

    private class ViewHolder {
        public AsyncImageView iv_cover;
        public TextView tv_name;
        public TextView tv_intro;
        public RelativeLayout rl_main;
    }

    public List<Map<String, Object>> getListItems() {
        return listItems;
    }

    public void setListItems(List<Map<String, Object>> listItems) {
        this.listItems = listItems;
    }
}
