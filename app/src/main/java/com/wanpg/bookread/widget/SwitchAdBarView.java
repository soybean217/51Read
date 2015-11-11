package com.wanpg.bookread.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.wanpg.bookread.R;
import com.wanpg.bookread.data.BookBoard;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.widget.ChildViewPager.OnSingleTouchListener;

public class SwitchAdBarView extends RelativeLayout {

    private List<BookBoard> listItems;
    private List<Bitmap> listBitmaps = new ArrayList<Bitmap>();
    private Context context;
    private View v_main;
    private ChildViewPager vp_switchbar;
    private DotsView mDotsView;
    private List<AsyncImageView> listSwitchBar = new ArrayList<AsyncImageView>();
    private Handler handler;
    private final int SWITCH_CHANGE_PAGE_NOTIFY = 0;
    private final int SWITCH_IMAGE_LOAD_SUCCESS_NOTIFY = 1;
    private int switchBarNowGuideNum = 0;
    private int itemNums = 0;
    private FragmentActivity activity;
    private int imageWidth = 0;
    private int imageHeight = 0;

    public SwitchAdBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public SwitchAdBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public SwitchAdBarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    private void init() {
        // TODO Auto-generated method stub
        imageWidth = DisplayUtil.getDisWidth() - 2 * DisplayUtil.dp2px(1);
        imageHeight = imageWidth * 23 / 64;

        itemNums = listItems.size();
        Log.d("itemNums", itemNums + "");
        v_main = LayoutInflater.from(context).inflate(R.layout.layout_switch_ad_bar, null);
        vp_switchbar = (ChildViewPager) v_main.findViewById(R.id.vp_switchbar);
        mDotsView = (DotsView) v_main.findViewById(R.id.ll_switchbar_tag);

        for (int i = 0; i < itemNums; i++) {
            AsyncImageView ivSwitchBar = new AsyncImageView(context);
            ivSwitchBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            //setImageDrawable(context.getResources().getDrawable(R.color.white));
            ivSwitchBar.setScaleType(ScaleType.FIT_XY);
            listSwitchBar.add(ivSwitchBar);
        }
        
        mDotsView.setDot(itemNums, 5, 3, getResources().getColor(R.color.dot_selected), getResources().getColor(R.color.dot_unselected));

        MyVPAdapter adapter = new MyVPAdapter(listSwitchBar);
        vp_switchbar.setAdapter(adapter);
        vp_switchbar.setCurrentItem(0);
        mDotsView.setSelectIndex(0);
        handler.sendEmptyMessageDelayed(SWITCH_CHANGE_PAGE_NOTIFY, 4000);
        vp_switchbar.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                mDotsView.setSelectIndex(arg0);
                handler.removeMessages(SWITCH_CHANGE_PAGE_NOTIFY);
                handler.sendEmptyMessageDelayed(SWITCH_CHANGE_PAGE_NOTIFY, 4000);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        vp_switchbar.setOnSingleTouchListener(new OnSingleTouchListener() {

            @Override
            public void onSingleTouch() {
                // TODO Auto-generated method stub
                checkedPageListener.onChecked(vp_switchbar.getCurrentItem());
            }
        });
        this.addView(v_main);
    }

    private OnCheckedPageListener checkedPageListener;
    public interface OnCheckedPageListener{
    	void onChecked(int pos);
    }
    public void setOnCheckedPageListener(OnCheckedPageListener checkedPageListener){
    	this.checkedPageListener = checkedPageListener;
    }
    

    public List<BookBoard> getListItems() {
        return listItems;
    }

    public void setListItems(List<BookBoard> listItems) {
        this.listItems = listItems;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case SWITCH_CHANGE_PAGE_NOTIFY:
                        switchBarNowGuideNum++;
                        if (switchBarNowGuideNum >= itemNums) {
                            switchBarNowGuideNum = 0;
                        }
                        vp_switchbar.setCurrentItem(switchBarNowGuideNum, true);
                        mDotsView.setSelectIndex(switchBarNowGuideNum);
                        break;
                    case SWITCH_IMAGE_LOAD_SUCCESS_NOTIFY:
                        for (int i = 0; i < listBitmaps.size(); i++) {
                            listSwitchBar.get(i).setPadding(0, 0, 0, 0);
                            listSwitchBar.get(i).setImageBitmap(listBitmaps.get(i));
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        init();
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }


    private class MyVPAdapter extends PagerAdapter {

        private List<AsyncImageView> viewList;

        public MyVPAdapter(List<AsyncImageView> viewList) {
            this.viewList = viewList;
        }

        //销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) v).removeView(viewList.get(position));

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        //获取当前窗体界面数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList.size();
        }

        //初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            // TODO Auto-generated method stub

            if (position >= listItems.size()) {
                return null;
            }

            AsyncImageView iv = viewList.get(position);
            String href = listItems.get(position).banner;
            iv.setImageUrl(href, R.drawable.image_load, imageWidth, imageHeight);

            ((ViewPager) v).addView(iv);
            return viewList.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            // TODO Auto-generated method stub
            return v == arg1;
        }


        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
