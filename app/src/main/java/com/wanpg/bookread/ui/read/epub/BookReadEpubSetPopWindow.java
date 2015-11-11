package com.wanpg.bookread.ui.read.epub;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.wanpg.bookread.R;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.ui.activity.BookReadCatalogActivity;
import com.wanpg.bookread.ui.activity.BookSettingReadActivity;
import com.wanpg.bookread.utils.DisplayUtil;

public class BookReadEpubSetPopWindow {
    private BookReadEpubActivity activity;
    private ImageButton ib_set_fontsize;
    private ImageButton ib_set_light;
    public ImageButton ib_set_moonmode;
    private ImageButton ib_set_theme;
    private ImageButton ib_set_more;
    private PopupWindow pwBottomSet;
    private PopupWindow pwHeadSet;
    private View viewParent;

    private ImageButton ib_set_back;
    private ImageButton ib_set_maker;
    private ImageButton ib_set_catalog;
    private BookReadEpubSetCellPopWindow brscpw;

    public BookReadEpubSetPopWindow(BookReadEpubActivity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
    }

    public void openPopWindow(View parent, final boolean isFullScreen) {
        viewParent = parent;
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_bookread_setting_bottom, null);
        ib_set_fontsize = (ImageButton) view.findViewById(R.id.ib_bookread_set_fontsize);
        ib_set_light = (ImageButton) view.findViewById(R.id.ib_bookread_set_light);
        ib_set_moonmode = (ImageButton) view.findViewById(R.id.ib_bookread_set_moonmode);
        ib_set_more = (ImageButton) view.findViewById(R.id.ib_bookread_set_more);
        ib_set_theme = (ImageButton) view.findViewById(R.id.ib_bookread_set_theme);

        View view1 = layoutInflater.inflate(R.layout.pop_bookread_setting_head, null);
        ib_set_back = (ImageButton) view1.findViewById(R.id.ib_bookread_set_head_back);
        ib_set_maker = (ImageButton) view1.findViewById(R.id.ib_bookread_set_head_marker);
        ib_set_catalog = (ImageButton) view1.findViewById(R.id.ib_bookread_set_head_catalog);

        ib_set_maker.setVisibility(View.GONE);


        pwBottomSet = new PopupWindow(view, LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(50), true);


        pwBottomSet.setBackgroundDrawable(activity.getResources().getDrawable(R.color.black));
        pwBottomSet.getBackground().setAlpha(200);
        pwBottomSet.setAnimationStyle(R.style.AnimationFade);
        pwBottomSet.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        if (activity.isMoonOpen()) {
            ib_set_moonmode.setImageResource(R.drawable.bookread_bottombar_sun);
        } else {
            ib_set_moonmode.setImageResource(R.drawable.bookread_bottombar_moon);
        }

        pwHeadSet = new PopupWindow(view1, LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(45), true);
        pwHeadSet.setBackgroundDrawable(activity.getResources().getDrawable(R.color.black));
        pwHeadSet.getBackground().setAlpha(200);
        pwHeadSet.setAnimationStyle(R.style.AnimationFade);
        pwHeadSet.setFocusable(false);
        if (isFullScreen) {
            if(android.os.Build.VERSION.SDK_INT >= 14){
        		pwHeadSet.showAtLocation(parent, Gravity.TOP, 0, DisplayUtil.getStatusHeight(activity));
        	}else{
        		pwHeadSet.showAtLocation(parent, Gravity.TOP, 0, 0);
        	}
        } else {
            pwHeadSet.showAtLocation(parent, Gravity.TOP, 0, DisplayUtil.getStatusHeight(activity));
        }


        brscpw = new BookReadEpubSetCellPopWindow(activity);

        ib_set_fontsize.setOnClickListener(new PopClickListener());
        ib_set_light.setOnClickListener(new PopClickListener());
        ib_set_moonmode.setOnClickListener(new PopClickListener());
        ib_set_more.setOnClickListener(new PopClickListener());
        ib_set_theme.setOnClickListener(new PopClickListener());

        ib_set_back.setOnClickListener(new PopClickListener());
        ib_set_maker.setOnClickListener(new PopClickListener());
        ib_set_catalog.setOnClickListener(new PopClickListener());


        pwBottomSet.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                closePopWindow();
                brscpw.closePopWindow();
                activity.setScreenMode(isFullScreen);
            }
        });
    }

    private class PopClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(ib_set_fontsize)) {
                if (brscpw.isShowFont()) {
                    brscpw.closePopWindow();
                } else {
                    brscpw.openPopWindow(viewParent, 0, BookReadEpubSetPopWindow.this);
                }
            } else if (v.equals(ib_set_light)) {
                if (brscpw.isShowLight()) {
                    brscpw.closePopWindow();
                } else {
                    brscpw.openPopWindow(viewParent, 1, BookReadEpubSetPopWindow.this);
                }
            } else if (v.equals(ib_set_moonmode)) {
                brscpw.closePopWindow();
                if (activity.isMoonOpen()) {
                    ib_set_moonmode.setImageResource(R.drawable.bookread_bottombar_moon);
                } else {
                    ib_set_moonmode.setImageResource(R.drawable.bookread_bottombar_sun);
                }
                activity.changeMoonMode();

            } else if (v.equals(ib_set_theme)) {
                if (brscpw.isShowTheme()) {
                    brscpw.closePopWindow();
                } else {
                    brscpw.openPopWindow(viewParent, 2, BookReadEpubSetPopWindow.this);
                }
            } else if (v.equals(ib_set_more)) {
                brscpw.closePopWindow();
                Intent intent = new Intent(activity, BookSettingReadActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.out_none);
                activity.epubPaint.prePage();
                closePopWindow();
            } else if (v.equals(ib_set_back)) {
                brscpw.closePopWindow();
                closePopWindow();
                activity.finish();
            } else if (v.equals(ib_set_maker)) {
                Toast.makeText(activity, "ib_set_maker", Toast.LENGTH_LONG).show();
            } else if (v.equals(ib_set_catalog)) {
                Intent intent = new Intent();
                intent.putExtra("shelfBookMode", activity.bookMode);
                intent.putExtra("tmpPath", activity.mTmpPath);
                intent.putExtra("isFullScreen", activity.isFullScreen);
                intent.setClass(activity, BookReadCatalogActivity.class);
                activity.startActivityForResult(intent, ShelfManager.CATALOG_REQUEST_CODE);
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.out_none);
                activity.epubPaint.prePage();
                closePopWindow();
            }
        }

    }

    /**
     * 关闭popwindow
     *
     * @return
     */
    public void closePopWindow() {
        if (pwBottomSet != null) {
            if (pwBottomSet.isShowing()) {
                pwBottomSet.dismiss();
            }
        }
        if (pwHeadSet != null) {
            if (pwHeadSet.isShowing()) {
                pwHeadSet.dismiss();
            }
        }
    }
}
