package com.wanpg.bookread.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.MathUtil;
import com.wanpg.bookread.widget.Notice;
import com.wanpg.bookread.widget.SoftAboutDialog;

import java.io.File;

/**
 * 软件设置界面
 *
 * @author Jinpeng
 */
public class BookSettingSoftActivity extends BaseActivity {

    private RelativeLayout rl_soft_skin, rl_custom_download_path, rl_clear_cache, rl_about_us, rl_check_update, rl_set_default;
    private ToggleButton tb_auto_put_to_shelf;
    private TextView tv_custom_download_path;
    private String softSkinMode;
    private String softSkin;
    private boolean isAutoPutDownloadToShelf;
    private String downloadPath;
    private boolean isCustomDownloadPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting_soft);
        ((TextView) findViewById(R.id.tv_title)).setText("软件设置");
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
        rl_soft_skin = (RelativeLayout) findViewById(R.id.rl_soft_skin);
        rl_custom_download_path = (RelativeLayout) findViewById(R.id.rl_custom_download_path);
        rl_clear_cache = (RelativeLayout) findViewById(R.id.rl_clear_cache);
        rl_about_us = (RelativeLayout) findViewById(R.id.rl_about_us);
        rl_check_update = (RelativeLayout) findViewById(R.id.rl_check_update);
        rl_set_default = (RelativeLayout) findViewById(R.id.rl_set_default);

        tv_custom_download_path = (TextView) findViewById(R.id.tv_custom_download_path);

        tb_auto_put_to_shelf = (ToggleButton) findViewById(R.id.tb_auto_put_to_shelf);

        rl_soft_skin.setOnClickListener(mClickListener);
        rl_custom_download_path.setOnClickListener(mClickListener);
        rl_clear_cache.setOnClickListener(mClickListener);
        rl_about_us.setOnClickListener(mClickListener);
        rl_check_update.setOnClickListener(mClickListener);
        rl_set_default.setOnClickListener(mClickListener);
        if (isCustomDownloadPath) {
            tv_custom_download_path.setText(downloadPath);
        } else {
            tv_custom_download_path.setText(Config.BOOK_SD_BOOK);
        }
        tb_auto_put_to_shelf.setChecked(isAutoPutDownloadToShelf);


        tb_auto_put_to_shelf.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                isAutoPutDownloadToShelf = isChecked;
                commitSharedPreferencesData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 0 && resultCode == 1) {
            downloadPath = data.getStringArrayListExtra("LISTCHECKED").get(0);
            isCustomDownloadPath = true;
            tv_custom_download_path.setText(downloadPath);
            commitSharedPreferencesData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnClickListener mClickListener = new OnClickListener() {
		
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(rl_soft_skin)) {

            } else if (v.equals(rl_custom_download_path)) {
                Intent intent = new Intent();
                intent.setClass(BookSettingSoftActivity.this, BookSearchAdvanceActivity.class);
                BookSettingSoftActivity.this.startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.out_none);
            } else if (v.equals(rl_clear_cache)) {
            	final ProgressDialog dialog = new ProgressDialog(BookSettingSoftActivity.this);
                dialog.setTitle(null);
                dialog.setMessage("清理中，请稍后...");
                dialog.show();
            	new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						long cacheLength = 0;
                        File fCache = getCacheDir();
                        Log.d("getCacheFileBaseDir", fCache.getPath());//  /data/data/com.andtest2/cache
                        cacheLength = cacheLength + FileUtil.delFolderChild(fCache);
                        File fExtCache = getExternalCacheDir();
                        cacheLength = cacheLength + FileUtil.delFolderChild(fExtCache);
                        runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
                        Notice.showToast("共清理了" + MathUtil.countFileSize(cacheLength) + "的缓存!");
					}
            	}.start();
            } else if (v.equals(rl_about_us)) {
                //new SoftAboutPopWindow(BookSettingSoftActivity.this).openPopWindow(ll_soft_main);
                SoftAboutDialog aboutDialog = new SoftAboutDialog(BookSettingSoftActivity.this, R.style.about_dialog);
                Window win = aboutDialog.getWindow();
                LayoutParams layoutParams = new LayoutParams();
                layoutParams.x = 0;
                layoutParams.y = 0;
                layoutParams.width = LayoutParams.MATCH_PARENT;
                layoutParams.height = LayoutParams.MATCH_PARENT;
                win.setAttributes(layoutParams);
                aboutDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                aboutDialog.show();

            } else if (v.equals(rl_check_update)) {
                //如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
                com.umeng.common.Log.LOG = true;
                UmengUpdateAgent.setUpdateOnlyWifi(false); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(updateListener);

                UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

					@Override
					public void OnDownloadEnd(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Notice.showToast("下载完成!");
					}

					@Override
					public void OnDownloadStart() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void OnDownloadUpdate(int arg0) {
						// TODO Auto-generated method stub
						
					}

                });
                UmengUpdateAgent.update(BookSettingSoftActivity.this);     
            } else if (v.equals(rl_set_default)) {
                SharedPreferences spc = getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE);
                Editor editor = spc.edit();
                editor.clear();
                editor.commit();

                SharedPreferences spc1 = getSharedPreferences(Config.CONFIG_READ, MODE_PRIVATE);
                Editor editor1 = spc1.edit();
                editor1.clear();
                editor1.commit();

                readSharedPreferencesData();

                tv_custom_download_path.setText(downloadPath);

                tb_auto_put_to_shelf.setChecked(isAutoPutDownloadToShelf);
            }
        }
    };

    private UmengUpdateListener updateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus,
                                     UpdateResponse updateInfo) {
            switch (updateStatus) {
                case 0: // has update
                    UmengUpdateAgent.showUpdateDialog(BookSettingSoftActivity.this, updateInfo);
                    break;
                case 1: // has no update
                    Notice.showToast("没有更新");
                    break;
                case 2: // none wifi
                    Notice.showToast("没有wifi连接， 只在wifi下更新");
                    break;
                case 3: // time out
                    Notice.showToast("超时");
                    break;
                case 4: // is updating
                /*Toast.makeText(mContext, "正在下载更新...", Toast.LENGTH_SHORT)
                        .show();*/
                    break;
            }

        }
    };

    private void clearSoftCache() {
        // TODO Auto-generated method stub
        getCacheDir();
    }


    /**
     * 读取数据从SharedPreferences
     */
    private void readSharedPreferencesData() {
        // TODO Auto-generated method stub
        SharedPreferences spc = getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE);
        isAutoPutDownloadToShelf = spc.getBoolean("isAutoPutDownloadToShelf", true);
        softSkinMode = spc.getString("softSkinMode", Config.SOFT_SKIN_SYSTEM);
        softSkin = spc.getString("softSkin", "");
        downloadPath = spc.getString("downloadPath", Config.BOOK_SD_BOOK);
        isCustomDownloadPath = spc.getBoolean("isCustomDownloadPath", false);
    }

    /**
     * 提交数据到SharedPreferences
     */
    private void commitSharedPreferencesData() {
        // TODO Auto-generated method stub
        SharedPreferences spc = getSharedPreferences(Config.CONFIG_SOFT, MODE_PRIVATE);
        Editor e = spc.edit();
        e.putString("softSkinMode", softSkinMode);
        e.putString("softSkin", softSkin);
        e.putString("downloadPath", downloadPath);
        e.putBoolean("isCustomDownloadPath", isCustomDownloadPath);
        e.putBoolean("isAutoPutDownloadToShelf", isAutoPutDownloadToShelf);
        e.commit();
    }

}
