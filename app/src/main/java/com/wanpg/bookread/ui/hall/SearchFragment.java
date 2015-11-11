package com.wanpg.bookread.ui.hall;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.utils.LogUtil;
import com.wanpg.bookread.widget.KeywordsFlowFrameLayout;
import com.wanpg.bookread.widget.Notice;

public class SearchFragment extends BaseFragment {

    private View parent;

    private EditText et_search_content;
    private ImageButton ib_search;
    private RelativeLayout rl_search_cancel;

    private Button bt_change_searchwords;
    private KeywordsFlowFrameLayout key_words_layout;
    private String searchKeysAll = null;
    private String[] searchKeys = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        LogUtil.D("wanpg", this.getClass().getSimpleName()+"_onCreateView");
        if(parent==null){
	        parent = inflater.inflate(R.layout.layout_booksearch, null);
	        initData();
        }else{
        	((ViewGroup)parent.getParent()).removeView(parent);
        }
        return parent;
    }

    private void initData() {
        // TODO Auto-generated method stub
    	searchKeysAll = mActivity.getSharedPreferences(Config.CONFIG_SOFT, Context.MODE_PRIVATE)
        		.getString("search_keys", "海贼之横行天下,疆域秘藏,异世怪医,天地决,金瓶梅,天路");
    	
    	long curDate = System.currentTimeMillis();
    	long searchDate = mActivity.getSharedPreferences(Config.CONFIG_SOFT, Context.MODE_PRIVATE)
        		.getLong("search_key_date", curDate);
    	if(searchDate==curDate || (curDate-searchDate)>5 * 24 * 60 * 60 * 1000){
    		//说明是第一次打开,和五天前获取的数据
            LogUtil.D("wanpg", "BookSearchFragment_initData 获取新数据");
    		initSearchTags();
    	}
    	initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        et_search_content = (EditText) parent.findViewById(R.id.et_search_content);
        ib_search = (ImageButton) parent.findViewById(R.id.ib_search);
        rl_search_cancel = (RelativeLayout) parent.findViewById(R.id.rl_search_cancel);

        et_search_content.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                commitSearchData(tv.getText().toString());
                return false;
            }
        });

        rl_search_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                et_search_content.setText("");
                rl_search_cancel.setVisibility(View.INVISIBLE);
            }
        });
        ib_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commitSearchData(et_search_content.getText().toString());
            }
        });
        key_words_layout = (KeywordsFlowFrameLayout) parent.findViewById(R.id.key_words_layout);
        key_words_layout.setDuration(666);
        key_words_layout.setOnItemClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v instanceof TextView) {
                    String keyword = ((TextView) v).getText().toString();
                    commitSearchData(keyword);
                }
            }
        });
        searchKeys = randomSearchKeys();
        key_words_layout.rubKeywords();
        for (int i = 0; i < searchKeys.length; i++) {
            key_words_layout.feedKeyword(searchKeys[i]);
        }
        key_words_layout.go2Show(KeywordsFlowFrameLayout.ANIMATION_IN);
        
        bt_change_searchwords = (Button) parent.findViewById(R.id.bt_change_searchwords);

        bt_change_searchwords.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	nextSearchTags();
            }
        });

        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hideSoftKeyborad();
            }
        });

    }

    private void commitSearchData(String data) {
        // TODO Auto-generated method stub
        hideSoftKeyborad();
        if (data.equals("")) {
            Notice.showToast("您输入的内容为空！");
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("searchWord", data);
            onFragmentDo(TYPE_TO_BOOK_SEARCH_RESULT, bundle);
        }
    }

    private void nextSearchTags(){
    	searchKeys = randomSearchKeys();
        key_words_layout.rubKeywords();
        for (int i = 0; i < searchKeys.length; i++) {
            key_words_layout.feedKeyword(searchKeys[i]);
        }
        key_words_layout.go2Show(KeywordsFlowFrameLayout.ANIMATION_IN);
    }
    
    private void initSearchTags() {
        // TODO Auto-generated method stub
        new BackTask() {
            @Override
            public void doInThreadWhenCalling(String info) {
                // TODO Auto-generated method stub

            }

            @Override
            public String doInThread() {
                // TODO Auto-generated method stub
                searchKeysAll = ShuPengApi.getHotSearchWords(1, 100);
                mActivity.getSharedPreferences(Config.CONFIG_SOFT, Context.MODE_PRIVATE)
            	.edit().putString("search_keys", searchKeysAll).commit();

                mActivity.getSharedPreferences(Config.CONFIG_SOFT, Context.MODE_PRIVATE)
            	.edit().putLong("search_key_date", System.currentTimeMillis()).commit();
                return null;
            }

            @Override
            public void doBeforeThread() {
                // TODO Auto-generated method stub

            }

            @Override
            public void doAfterThread(String result) {
                // TODO Auto-generated method stub
            }
        }.submit();

    }

    private String[] randomSearchKeys() {
    	String s1[] = null;
    	String s2[] = searchKeysAll.split(",");
    	int length = s2.length;
    	if(length==6){
    		s1 = s2;
    	}else if(length < 6){
    		s1 = "海贼之横行天下,疆域秘藏,异世怪医,天地决,金瓶梅,天路".split(",");
    	}else{
    		int[] a = getRandomPos(6, s2.length);
    		s1 = new String[a.length];
    		for(int i=0;i<a.length;i++){
    			s1[i] = s2[a[i]];
    		}
    	}
    	return s1;
	}
    
    /**
     * 返回rSize个数的数组，随机的，返回在0-fsize中取，包括0，不包括fsize
     * @param rSzie
     * @param fSize
     * @return
     */
    private int[] getRandomPos(int rSzie, int fSize){
    	int[] a = new int[rSzie];
		for(int i=0;i<rSzie;i++){			
			a[i] = (int) (Math.random() * fSize);  
			for (int j=0; j<i;j++){  
				if (a[j] == a[i]){
					i--;
					break;
				}
			}
		}
		return a;
    }
    
    public void hideSoftKeyborad() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        et_search_content.setCursorVisible(false);
        imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
    }
}
