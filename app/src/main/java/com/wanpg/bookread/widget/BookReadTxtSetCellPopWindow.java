package com.wanpg.bookread.widget;

import android.content.Context;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.read.txt.BookReadTxtActivity;
import com.wanpg.bookread.utils.DisplayUtil;

public class BookReadTxtSetCellPopWindow {

    private BookReadTxtActivity activity;

    private PopupWindow pwBottomSetCell;
    private RelativeLayout rl_set_cell_font;
    private RelativeLayout rl_set_cell_light;
    private LinearLayout ll_set_cell_theme;

    private TextView tv_set_cell_cursize;
    private Button b_set_cell_size_big;
    private Button b_set_cell_size_small;
    private Button b_set_cell_type_default;
    private Button b_set_cell_type_custom;

    private TextView tv_set_cell_curlight;
    private SeekBar sb_set_cell_curlight_change;
    private ToggleButton tb_set_cell_syslight_change;

    private LinearLayout ll_set_cell_theme_back1, ll_set_cell_theme_back2, ll_set_cell_theme_back3;
    private TextView tv_set_cell_theme_rowdis, tv_set_cell_theme_pagedis;
    private SeekBar sb_set_cell_theme_rowdis, sb_set_cell_theme_pagedis;

    private boolean isShowFont = false;
    private boolean isShowLight = false;
    private boolean isShowTheme = false;

    private BookReadTxtSetPopWindow txtSetPopWindow;

    public BookReadTxtSetCellPopWindow(BookReadTxtActivity activity2) {
        // TODO Auto-generated constructor stub
        this.activity = activity2;
    }

    public void openPopWindow(View parent, int which, BookReadTxtSetPopWindow txtSetPopWindow) {
        this.txtSetPopWindow = txtSetPopWindow;
        closePopWindow();

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_bookread_setting_bottom_cell, null);

        rl_set_cell_font = (RelativeLayout) view.findViewById(R.id.rl_bookread_bottom_set_cell_font);
        rl_set_cell_light = (RelativeLayout) view.findViewById(R.id.rl_bookread_bottom_set_cell_light);
        ll_set_cell_theme = (LinearLayout) view.findViewById(R.id.ll_bookread_bottom_set_cell_theme);

        tv_set_cell_cursize = (TextView) view.findViewById(R.id.tv_bookread_bottom_set_cell_fontsize_cursize);
        b_set_cell_size_big = (Button) view.findViewById(R.id.b_bookread_bottom_set_cell_fontsize_big);
        b_set_cell_size_small = (Button) view.findViewById(R.id.b_bookread_bottom_set_cell_fontsize_small);
        b_set_cell_type_default = (Button) view.findViewById(R.id.b_bookread_bottom_set_cell_fonttype_default);
        b_set_cell_type_custom = (Button) view.findViewById(R.id.b_bookread_bottom_set_cell_fonttype_custom);

        tv_set_cell_cursize.setText(activity.fontSizeDp + "");
        //字体增大
        b_set_cell_size_big.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.fontSizeDp++;
                tv_set_cell_cursize.setText(activity.fontSizeDp + "");
                activity.changeShowMode();
            }
        });
        b_set_cell_size_small.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.fontSizeDp--;
                tv_set_cell_cursize.setText(activity.fontSizeDp + "");
                activity.changeShowMode();
            }
        });


        tv_set_cell_curlight = (TextView) view.findViewById(R.id.tv_bookread_bottom_set_cell_curlight);
        sb_set_cell_curlight_change = (SeekBar) view.findViewById(R.id.sb_bookread_bottom_set_cell_curlight_change);
        tb_set_cell_syslight_change = (ToggleButton) view.findViewById(R.id.tb_bookread_bottom_set_cell_syslight_change);

        switch (getScreenMode()) {
            case 0://手动调节
                tb_set_cell_syslight_change.setChecked(false);
                sb_set_cell_curlight_change.setEnabled(true);
                int curLight = getScreenBrightness() - 5;
                sb_set_cell_curlight_change.setProgress(curLight);
                tv_set_cell_curlight.setText((curLight / 25) + "");
                break;
            case 1://自动调节
                tb_set_cell_syslight_change.setChecked(true);
                sb_set_cell_curlight_change.setEnabled(false);
                tv_set_cell_curlight.setText("自动");
                break;
            default:
                break;
        }
        tb_set_cell_syslight_change.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    setScreenMode(1);
                    sb_set_cell_curlight_change.setEnabled(false);
                    tv_set_cell_curlight.setText("自动");
                } else {
                    setScreenMode(0);
                    sb_set_cell_curlight_change.setEnabled(true);
                    int curLight = 100;
                    saveScreenBrightness(100 + 5);
                    setScreenBrightness(100 + 5);
                    sb_set_cell_curlight_change.setProgress(curLight);
                    tv_set_cell_curlight.setText((curLight / 25) + "");
                }
                tb_set_cell_syslight_change.setChecked(isChecked);
            }
        });
        sb_set_cell_curlight_change.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                saveScreenBrightness(progress + 5);
                setScreenBrightness(progress + 5);
                tv_set_cell_curlight.setText((progress / 25) + "");
            }
        });


        ll_set_cell_theme_back1 = (LinearLayout) view.findViewById(R.id.ll_theme_back1);
        ll_set_cell_theme_back2 = (LinearLayout) view.findViewById(R.id.ll_theme_back2);
        ll_set_cell_theme_back3 = (LinearLayout) view.findViewById(R.id.ll_theme_back3);

        tv_set_cell_theme_rowdis = (TextView) view.findViewById(R.id.tv_bookread_bottom_set_cell_theme_rowdistitle_num);
        tv_set_cell_theme_pagedis = (TextView) view.findViewById(R.id.tv_bookread_bottom_set_cell_theme_pagedistitle_num);
        sb_set_cell_theme_rowdis = (SeekBar) view.findViewById(R.id.sb_bookread_bottom_set_cell_theme_rowdistitle);
        sb_set_cell_theme_pagedis = (SeekBar) view.findViewById(R.id.sb_bookread_bottom_set_cell_theme_pagedistitle);

        ll_set_cell_theme_back1.setOnClickListener(new MyThemeChangeClickListener());
        ll_set_cell_theme_back2.setOnClickListener(new MyThemeChangeClickListener());
        ll_set_cell_theme_back3.setOnClickListener(new MyThemeChangeClickListener());


        tv_set_cell_theme_rowdis.setText((activity.lineSpace - 1) + "");
        sb_set_cell_theme_rowdis.setProgress(activity.lineSpace);
        tv_set_cell_theme_pagedis.setText((activity.marginWidth) + "");
        sb_set_cell_theme_pagedis.setProgress(activity.marginWidth);
        sb_set_cell_theme_rowdis.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                activity.lineSpace = progress + 1;
                tv_set_cell_theme_rowdis.setText(progress + "");
                activity.changeShowMode();
            }
        });
        sb_set_cell_theme_pagedis.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                activity.marginWidth = progress;
                tv_set_cell_theme_pagedis.setText(progress + "");
                activity.changeShowMode();
            }
        });

        switch (which) {
            case 0://显示字体调节
                isShowFont = true;
                isShowLight = false;
                isShowTheme = false;
                rl_set_cell_font.setVisibility(View.VISIBLE);
                rl_set_cell_light.setVisibility(View.GONE);
                ll_set_cell_theme.setVisibility(View.GONE);
                break;
            case 1://显示亮度调节
                isShowFont = false;
                isShowLight = true;
                isShowTheme = false;
                rl_set_cell_font.setVisibility(View.GONE);
                rl_set_cell_light.setVisibility(View.VISIBLE);
                ll_set_cell_theme.setVisibility(View.GONE);
                break;
            case 2://显示主题调节
                isShowFont = false;
                isShowLight = false;
                isShowTheme = true;
                rl_set_cell_font.setVisibility(View.GONE);
                rl_set_cell_light.setVisibility(View.GONE);
                ll_set_cell_theme.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        pwBottomSetCell = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

        pwBottomSetCell.setBackgroundDrawable(activity.getResources().getDrawable(R.color.black));
        pwBottomSetCell.getBackground().setAlpha(200);
        pwBottomSetCell.setAnimationStyle(R.style.AnimationFade);
        pwBottomSetCell.setFocusable(false);
        pwBottomSetCell.showAtLocation(parent, Gravity.BOTTOM, 0, DisplayUtil.dp2px(55));

    }


    /**
     * 关闭popwindow
     *
     * @return
     */
    public void closePopWindow() {
        isShowFont = false;
        isShowLight = false;
        isShowTheme = false;
        if (pwBottomSetCell != null) {
            if (pwBottomSetCell.isShowing()) {
                pwBottomSetCell.dismiss();
            }
        }
    }

    private class MyThemeChangeClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (activity.isMoonOpen()) {
                txtSetPopWindow.ib_set_moonmode.setImageResource(R.drawable.bookread_bottombar_moon);
            }
            activity.bookOldBG = activity.bookBGPicture;
            if (v.equals(ll_set_cell_theme_back1)) {
                activity.bookBGPicture = R.drawable.bookread_back1;
            } else if (v.equals(ll_set_cell_theme_back2)) {
                activity.bookBGPicture = R.drawable.bookread_back2;
            } else if (v.equals(ll_set_cell_theme_back3)) {
                activity.bookBGPicture = R.drawable.bookread_back3;
            }
            activity.changeTheme();
        }

    }


    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值  0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 设置当前屏幕亮度值  0--255
     */
    private void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private void setScreenBrightness(int paramInt) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

    public boolean isShowFont() {
        return isShowFont;
    }

    public void setShowFont(boolean isShowFont) {
        this.isShowFont = isShowFont;
    }

    public boolean isShowLight() {
        return isShowLight;
    }

    public void setShowLight(boolean isShowLight) {
        this.isShowLight = isShowLight;
    }

    public boolean isShowTheme() {
        return isShowTheme;
    }

    public void setShowTheme(boolean isShowTheme) {
        this.isShowTheme = isShowTheme;
    }

}
