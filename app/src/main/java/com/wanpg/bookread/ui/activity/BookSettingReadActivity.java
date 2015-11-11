package com.wanpg.bookread.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.utils.DisplayUtil;

/**
 * 阅读设置界面
 *
 * @author Jinpeng
 */
public class BookSettingReadActivity extends BaseActivity {

    private boolean isShowHeadMsg;
    private boolean isShowBottomMsg;
    private boolean isBatteryPercent;
    private boolean isFullScreen;

    /**
     * 锁屏时间，按照分钟计算*
     */
    private int lockScreenTime;
    private boolean isUseVolumeButton;

    private String pageChangeMode;

    private TextView tv_pagechange_none, tv_pagechange_flip, tv_pagechange_slide, tv_pagechange_cover;
    private ToggleButton tb_fullscreen, tb_showheadmsg, tb_showbottommsg, tb_batterypercent, tb_usevolume;
    private TextView tv_lockscreen_5, tv_lockscreen_15, tv_lockscreen_30, tv_lockscreen_none;
    private ImageView iv_pagechange_none, iv_pagechange_flip, iv_pagechange_slide, iv_pagechange_cover,
            iv_lockscreen_5, iv_lockscreen_15, iv_lockscreen_30, iv_lockscreen_none;
    private int disWidth;
    private int marginWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting_read);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        disWidth = wm.getDefaultDisplay().getWidth();
        marginWidth = DisplayUtil.dp2px(15);
        ((TextView) findViewById(R.id.tv_title)).setText("阅读选项");
        setBackEvent(findViewById(R.id.ib_back));
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        readSharedPreferencesData();
        initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        tv_pagechange_none = (TextView) findViewById(R.id.tv_pagechange_none);
        tv_pagechange_flip = (TextView) findViewById(R.id.tv_pagechange_flip);
        tv_pagechange_slide = (TextView) findViewById(R.id.tv_pagechange_slide);
        tv_pagechange_cover = (TextView) findViewById(R.id.tv_pagechange_cover);
        tb_fullscreen = (ToggleButton) findViewById(R.id.tb_fullscreen);
        tb_showheadmsg = (ToggleButton) findViewById(R.id.tb_showheadmsg);
        tb_showbottommsg = (ToggleButton) findViewById(R.id.tb_showbottommsg);
        tb_batterypercent = (ToggleButton) findViewById(R.id.tb_batterypercent);
        tb_usevolume = (ToggleButton) findViewById(R.id.tb_usevolume);
        tv_lockscreen_5 = (TextView) findViewById(R.id.tv_lockscreen_5);
        tv_lockscreen_15 = (TextView) findViewById(R.id.tv_lockscreen_15);
        tv_lockscreen_30 = (TextView) findViewById(R.id.tv_lockscreen_30);
        tv_lockscreen_none = (TextView) findViewById(R.id.tv_lockscreen_none);

        iv_pagechange_none = (ImageView) findViewById(R.id.iv_pagechange_none);
        iv_pagechange_flip = (ImageView) findViewById(R.id.iv_pagechange_flip);
        iv_pagechange_slide = (ImageView) findViewById(R.id.iv_pagechange_slide);
        iv_pagechange_cover = (ImageView) findViewById(R.id.iv_pagechange_cover);
        iv_lockscreen_5 = (ImageView) findViewById(R.id.iv_lockscreen_5);
        iv_lockscreen_15 = (ImageView) findViewById(R.id.iv_lockscreen_15);
        iv_lockscreen_30 = (ImageView) findViewById(R.id.iv_lockscreen_30);
        iv_lockscreen_none = (ImageView) findViewById(R.id.iv_lockscreen_none);

        hideAllPageChangeTag();
        if (pageChangeMode.equals(Config.PAGE_CHANGE_FLIP)) {
            iv_pagechange_flip.setVisibility(View.VISIBLE);
        } else if (pageChangeMode.equals(Config.PAGE_CHANGE_SLIDE)) {
            iv_pagechange_slide.setVisibility(View.VISIBLE);
        } else if (pageChangeMode.equals(Config.PAGE_CHANGE_COVER)) {
            iv_pagechange_cover.setVisibility(View.VISIBLE);
        } else if (pageChangeMode.equals(Config.PAGE_CHANGE_NONE)) {
            iv_pagechange_none.setVisibility(View.VISIBLE);
        }

        tb_fullscreen.setChecked(!isFullScreen);
        tb_showheadmsg.setChecked(isShowHeadMsg);
        tb_showbottommsg.setChecked(isShowBottomMsg);
        tb_batterypercent.setChecked(isBatteryPercent);
        tb_usevolume.setChecked(isUseVolumeButton);

        hideAllLockScreenTag();
        switch (lockScreenTime) {
            case -1://不锁屏
                iv_lockscreen_none.setVisibility(View.VISIBLE);
                break;
            case 5 * 60 * 1000://5分钟
                iv_lockscreen_5.setVisibility(View.VISIBLE);
                break;
            case 15 * 60 * 1000://15分钟
                iv_lockscreen_15.setVisibility(View.VISIBLE);
                break;
            case 30 * 60 * 1000://30分钟
                iv_lockscreen_30.setVisibility(View.VISIBLE);
                break;
        }

        tv_pagechange_none.setOnClickListener(new MyClickListener());
        tv_pagechange_flip.setOnClickListener(new MyClickListener());
        tv_pagechange_slide.setOnClickListener(new MyClickListener());
        tv_pagechange_cover.setOnClickListener(new MyClickListener());
        tv_lockscreen_5.setOnClickListener(new MyClickListener());
        tv_lockscreen_15.setOnClickListener(new MyClickListener());
        tv_lockscreen_30.setOnClickListener(new MyClickListener());
        tv_lockscreen_none.setOnClickListener(new MyClickListener());

        tb_fullscreen.setOnCheckedChangeListener(new MyCheckedChangeListener());
        tb_showheadmsg.setOnCheckedChangeListener(new MyCheckedChangeListener());
        tb_showbottommsg.setOnCheckedChangeListener(new MyCheckedChangeListener());
        tb_batterypercent.setOnCheckedChangeListener(new MyCheckedChangeListener());
        tb_usevolume.setOnCheckedChangeListener(new MyCheckedChangeListener());

    }

    private void hideAllPageChangeTag() {
        // TODO Auto-generated method stub
        iv_pagechange_none.setVisibility(View.INVISIBLE);
        iv_pagechange_flip.setVisibility(View.INVISIBLE);
        iv_pagechange_slide.setVisibility(View.INVISIBLE);
        iv_pagechange_cover.setVisibility(View.INVISIBLE);
    }

    private void hideAllLockScreenTag() {
        // TODO Auto-generated method stub
        iv_lockscreen_5.setVisibility(View.INVISIBLE);
        iv_lockscreen_15.setVisibility(View.INVISIBLE);
        iv_lockscreen_30.setVisibility(View.INVISIBLE);
        iv_lockscreen_none.setVisibility(View.INVISIBLE);
    }

    private class MyClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(tv_pagechange_none)) {
                pageChangeMode = Config.PAGE_CHANGE_NONE;
                hideAllPageChangeTag();
                iv_pagechange_none.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_pagechange_flip)) {
                pageChangeMode = Config.PAGE_CHANGE_FLIP;
                hideAllPageChangeTag();
                iv_pagechange_flip.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_pagechange_slide)) {
                pageChangeMode = Config.PAGE_CHANGE_SLIDE;
                hideAllPageChangeTag();
                iv_pagechange_slide.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_pagechange_cover)) {
                pageChangeMode = Config.PAGE_CHANGE_COVER;
                hideAllPageChangeTag();
                iv_pagechange_cover.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_lockscreen_5)) {
                lockScreenTime = 5 * 60 * 1000;
                hideAllLockScreenTag();
                iv_lockscreen_5.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_lockscreen_15)) {
                lockScreenTime = 15 * 60 * 1000;
                hideAllLockScreenTag();
                iv_lockscreen_15.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_lockscreen_30)) {
                lockScreenTime = 30 * 60 * 1000;
                hideAllLockScreenTag();
                iv_lockscreen_30.setVisibility(View.VISIBLE);
            } else if (v.equals(tv_lockscreen_none)) {
                lockScreenTime = -1;
                hideAllLockScreenTag();
                iv_lockscreen_none.setVisibility(View.VISIBLE);
            }
            commitSharedPreferencesData();
        }
    }


    private class MyCheckedChangeListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (buttonView.equals(tb_fullscreen)) {
                isFullScreen = !isChecked;
            } else if (buttonView.equals(tb_showheadmsg)) {
                isShowHeadMsg = isChecked;
            } else if (buttonView.equals(tb_showbottommsg)) {
                isShowBottomMsg = isChecked;
            } else if (buttonView.equals(tb_batterypercent)) {
                isBatteryPercent = isChecked;
            } else if (buttonView.equals(tb_usevolume)) {
                isUseVolumeButton = isChecked;
            }
            commitSharedPreferencesData();
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        commitSharedPreferencesData();
        super.onPause();
    }

    /**
     * 读取数据从SharedPreferences
     */
    private void readSharedPreferencesData() {
        // TODO Auto-generated method stub
        SharedPreferences spc = getSharedPreferences(Config.CONFIG_READ, MODE_PRIVATE);
        isShowHeadMsg = spc.getBoolean("isShowHeadMsg", true);
        isShowBottomMsg = spc.getBoolean("isShowBottomMsg", true);
        isBatteryPercent = spc.getBoolean("isBatteryPercent", false);
        isFullScreen = spc.getBoolean("isFullScreen", true);
        isUseVolumeButton = spc.getBoolean("isUseVolumeButton", false);
        lockScreenTime = spc.getInt("lockScreenTime", 5 * 60 * 1000);
        pageChangeMode = spc.getString("pageChangeMode", Config.PAGE_CHANGE_FLIP);
    }

    /**
     * 提交数据到SharedPreferences
     */
    private void commitSharedPreferencesData() {
        // TODO Auto-generated method stub
        SharedPreferences spc = getSharedPreferences(Config.CONFIG_READ, MODE_PRIVATE);
        Editor e = spc.edit();
        e.putBoolean("isShowHeadMsg", isShowHeadMsg);
        e.putBoolean("isShowBottomMsg", isShowBottomMsg);
        e.putBoolean("isBatteryPercent", isBatteryPercent);
        e.putBoolean("isFullScreen", isFullScreen);
        e.putBoolean("isUseVolumeButton", isUseVolumeButton);
        e.putInt("lockScreenTime", lockScreenTime);
        e.putString("pageChangeMode", pageChangeMode);
        e.commit();
    }
}
