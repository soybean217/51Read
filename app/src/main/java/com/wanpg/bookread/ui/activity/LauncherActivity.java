package com.wanpg.bookread.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.ui.MainActivity;
import com.wanpg.bookread.utils.DeviceUtil;
import com.wanpg.bookread.widget.Notice;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        BaseApplication.getInstance().isSdCardOk = DeviceUtil.isSDCardAvailable();
        if (!BaseApplication.getInstance().isSdCardOk) {
            Notice.showToast("储存卡已拔出，本地图书阅读和添加功能暂时不能使用，请选择在线阅读！");
        }

        if (BaseApplication.getInstance().isSoftLiving) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.out_none, R.anim.out_none);
            this.finish();
        } else {
            initUI();
        }
    }

    private void initUI() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_launcher);
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                long time1 = System.currentTimeMillis();
                long time2 = 0;
                while (true) {
                    if (BaseApplication.getInstance().isShelfDataLoadOver) {
                        time2 = System.currentTimeMillis();
                        break;
                    }
                }
                long timeDis = time2 - time1;
                if (timeDis < 3500) {
                    try {
                        Thread.sleep(3500 - timeDis);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                LauncherActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        startBookMain();
                    }
                });

            }
        }.start();
    }

    private void startBookMain() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        this.finish();
    }


    @Override
    public void onBackPressed() {
    }  //空调用，不能在loading中进行返回
}
