package com.wanpg.bookread.ui.software;

import android.view.LayoutInflater;
import android.view.View;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.common.YouMengConfig;
import com.wanpg.bookread.ui.MainActivity;
import com.wanpg.bookread.widget.HeightBasedChildGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoftClassifyPage {

    private View parent;
    private BaseFragment mFragment;
    private BaseActivity mActivity;
    private LayoutInflater inflater;

    private List<Map<String, Object>> listGame = new ArrayList<Map<String, Object>>();//6个
    private List<Map<String, Object>> listAPP = new ArrayList<Map<String, Object>>();//11个

    private HeightBasedChildGridView gv_game, gv_app;
    private SoftClassifyGridAdapter adapterGame, adapterApp;

    public SoftClassifyPage(BaseFragment fragment, View parent) {
        // TODO Auto-generated constructor stub
        this.parent = parent;
        this.mFragment = fragment;
        this.mActivity = mFragment.mActivity;
        inflater = LayoutInflater.from(mActivity);
        initData();
    }


    private void initUI() {
        // TODO Auto-generated method stub
        gv_game = (HeightBasedChildGridView) parent.findViewById(R.id.gv_classify_game);
        gv_app = (HeightBasedChildGridView) parent.findViewById(R.id.gv_classify_app);
        adapterGame = new SoftClassifyGridAdapter(mFragment, listGame);
        adapterApp = new SoftClassifyGridAdapter(mFragment, listAPP);
        gv_game.setAdapter(adapterGame);
        gv_app.setAdapter(adapterApp);
    }

    private void initData() {
        // TODO Auto-generated method stub
        Map<String, Object> mapGame0 = new HashMap<String, Object>();
        mapGame0.put("NAME", "动作冒险");
        mapGame0.put("IMAGE", R.drawable.game_00);
        mapGame0.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_ACTAVG);
        listGame.add(mapGame0);

        Map<String, Object> mapGame1 = new HashMap<String, Object>();
        mapGame1.put("NAME", "角色扮演");
        mapGame1.put("IMAGE", R.drawable.game_01);
        mapGame1.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_RPG);
        listGame.add(mapGame1);

        Map<String, Object> mapGame2 = new HashMap<String, Object>();
        mapGame2.put("NAME", "射击飞行");
        mapGame2.put("IMAGE", R.drawable.game_02);
        mapGame2.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_STFYG);
        listGame.add(mapGame2);

        Map<String, Object> mapGame3 = new HashMap<String, Object>();
        mapGame3.put("NAME", "体育竞速");
        mapGame3.put("IMAGE", R.drawable.game_03);
        mapGame3.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_SRG);
        listGame.add(mapGame3);

        Map<String, Object> mapGame4 = new HashMap<String, Object>();
        mapGame4.put("NAME", "策略经营");
        mapGame4.put("IMAGE", R.drawable.game_04);
        mapGame4.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_SMG);
        listGame.add(mapGame4);

        Map<String, Object> mapGame5 = new HashMap<String, Object>();
        mapGame5.put("NAME", "棋牌休闲");
        mapGame5.put("IMAGE", R.drawable.game_05);
        mapGame5.put("APPKEY", YouMengConfig.APPKEY_SOFT_GAME_CRG);
        listGame.add(mapGame5);


        Map<String, Object> mapApp00 = new HashMap<String, Object>();
        mapApp00.put("NAME", "通讯聊天");
        mapApp00.put("IMAGE", R.drawable.app_00);
        mapApp00.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_00);
        listAPP.add(mapApp00);

        Map<String, Object> mapApp01 = new HashMap<String, Object>();
        mapApp01.put("NAME", "网络社区");
        mapApp01.put("IMAGE", R.drawable.app_01);
        mapApp01.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_01);
        listAPP.add(mapApp01);

        Map<String, Object> mapApp02 = new HashMap<String, Object>();
        mapApp02.put("NAME", "影音图像");
        mapApp02.put("IMAGE", R.drawable.app_02);
        mapApp02.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_02);
        listAPP.add(mapApp02);

        Map<String, Object> mapApp03 = new HashMap<String, Object>();
        mapApp03.put("NAME", "学习办公");
        mapApp03.put("IMAGE", R.drawable.app_03);
        mapApp03.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_03);
        listAPP.add(mapApp03);

        Map<String, Object> mapApp04 = new HashMap<String, Object>();
        mapApp04.put("NAME", "旅行生活");
        mapApp04.put("IMAGE", R.drawable.app_04);
        mapApp04.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_04);
        listAPP.add(mapApp04);

        Map<String, Object> mapApp05 = new HashMap<String, Object>();
        mapApp05.put("NAME", "地图导航");
        mapApp05.put("IMAGE", R.drawable.app_05);
        mapApp05.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_05);
        listAPP.add(mapApp05);

        Map<String, Object> mapApp06 = new HashMap<String, Object>();
        mapApp06.put("NAME", "系统安全");
        mapApp06.put("IMAGE", R.drawable.app_06);
        mapApp06.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_06);
        listAPP.add(mapApp06);

        Map<String, Object> mapApp07 = new HashMap<String, Object>();
        mapApp07.put("NAME", "理财购物");
        mapApp07.put("IMAGE", R.drawable.app_07);
        mapApp07.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_07);
        listAPP.add(mapApp07);

        Map<String, Object> mapApp08 = new HashMap<String, Object>();
        mapApp08.put("NAME", "美化壁纸");
        mapApp08.put("IMAGE", R.drawable.app_08);
        mapApp08.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_08);
        listAPP.add(mapApp08);

        Map<String, Object> mapApp09 = new HashMap<String, Object>();
        mapApp09.put("NAME", "应用市场");
        mapApp09.put("IMAGE", R.drawable.app_09);
        mapApp09.put("APPKEY", YouMengConfig.APPKEY_SOFT_APP_09);
        listAPP.add(mapApp09);

        initUI();
    }


}
