package com.wanpg.bookread.ui.software;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.common.YouMengConfig;
import com.wanpg.bookread.ui.MainActivity;

public class SoftRankPage {
    private Button bt_game, bt_app;
    private ListView lv_game, lv_app;

    private boolean isShowedGame = false;
    private boolean isShowedApp = false;

    private boolean isShowingGame = false;
    private boolean isShowingApp = false;

    private Handler handler;

    private View parent;
    private MainActivity activity;
    private LayoutInflater inflater;

    public SoftRankPage(MainActivity activity, View parent) {
        // TODO Auto-generated constructor stub
        this.parent = parent;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 0:
                        isShowedGame = false;
                        isShowedApp = false;
                        handler.removeMessages(0);
                        handler.sendEmptyMessageDelayed(0, 30000);
                        break;

                    default:
                        break;
                }
            }
        };
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        bt_game = (Button) parent.findViewById(R.id.bt_soft_rank_game);
        bt_app = (Button) parent.findViewById(R.id.bt_soft_rank_app);

        lv_game = (ListView) parent.findViewById(R.id.lv_soft_rank_game);
        lv_app = (ListView) parent.findViewById(R.id.lv_soft_rank_app);

        bt_game.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                initGame();
            }
        });
        bt_app.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                initApp();
            }
        });
        initGame();
        handler.sendEmptyMessageDelayed(0, 30000);
    }

    private void initGame() {
        // TODO Auto-generated method stub
        lv_game.setVisibility(View.VISIBLE);
        lv_app.setVisibility(View.GONE);
        if (!isShowedGame) {
            isShowedGame = true;
            ExchangeDataService service = new ExchangeDataService();
            service.appkey = /*"5153fd13a3107dc5a9e59121";*/YouMengConfig.APPKEY_SOFT_RANK_GAME;
            service.autofill = 0;
            ExchangeViewManager exchangeViewManager1 = new ExchangeViewManager(activity, service);
            exchangeViewManager1.addView((ViewGroup) lv_game.getParent(), lv_game);
        }

        isShowingGame = true;
        isShowingApp = false;
        bt_game.setBackgroundResource(R.drawable.subtab_item_bg_selected_right);
        bt_app.setBackgroundResource(R.drawable.subtab_left_selector);
    }

    private void initApp() {
        // TODO Auto-generated method stub
        lv_game.setVisibility(View.GONE);
        lv_app.setVisibility(View.VISIBLE);
        if (!isShowedApp) {
            isShowedApp = true;
            ExchangeDataService service = new ExchangeDataService();
            service.appkey = /*"5153fd13a3107dc5a9e59121";*/YouMengConfig.APPKEY_SOFT_RANK_APP;
            service.autofill = 0;
            ExchangeViewManager exchangeViewManager1 = new ExchangeViewManager(activity, service);
            exchangeViewManager1.addView((ViewGroup) lv_app.getParent(), lv_app);
        }

        isShowingGame = false;
        isShowingApp = true;
        bt_game.setBackgroundResource(R.drawable.subtab_right_selector);
        bt_app.setBackgroundResource(R.drawable.subtab_item_bg_selected_left);
    }
}
