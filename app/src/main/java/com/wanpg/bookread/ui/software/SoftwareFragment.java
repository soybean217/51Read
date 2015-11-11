package com.wanpg.bookread.ui.software;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.newxp.common.ExchangeConstants;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.ui.MainActivity;

public class SoftwareFragment extends BaseFragment {

	private TextView tv_recommend, tv_rank, tv_classify, tv_subject;
	private ImageView iv_recommend_tag, iv_rank_tag, iv_classify_tag, iv_subject_tag;
	private LinearLayout ll_recommend, ll_rank, ll_classify, ll_subject;

	private SoftRecommendPage recommendPage;
	private SoftClassifyPage classifyPage;
	private SoftRankPage rankPage;

	private View parent;
	private MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(parent)){
			parent = inflater.inflate(R.layout.layout_soft_recommend, null);
			activity = (MainActivity) getActivity();
			initData();
		}
		return parent;
	}

	private void initData() {
		// TODO Auto-generated method stub
		ExchangeConstants.DEBUG_MODE = true;
		ExchangeConstants.CONTAINER_AUTOEXPANDED = true;
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		tv_recommend = (TextView) parent.findViewById(R.id.tv_soft_head_recommend);
		tv_rank = (TextView) parent.findViewById(R.id.tv_soft_head_rank);
		tv_classify = (TextView) parent.findViewById(R.id.tv_soft_head_classify);
		tv_subject = (TextView) parent.findViewById(R.id.tv_soft_head_subject);

		iv_recommend_tag = (ImageView) parent.findViewById(R.id.iv_soft_head_recommend);
		iv_rank_tag = (ImageView) parent.findViewById(R.id.iv_soft_head_rank);
		iv_classify_tag = (ImageView) parent.findViewById(R.id.iv_soft_head_classify);
		iv_subject_tag = (ImageView) parent.findViewById(R.id.iv_soft_head_subject);

		ll_recommend = (LinearLayout) parent.findViewById(R.id.ll_soft_recommend);
		ll_rank = (LinearLayout) parent.findViewById(R.id.ll_soft_rank);
		ll_classify = (LinearLayout) parent.findViewById(R.id.ll_soft_classify);
		ll_subject = (LinearLayout) parent.findViewById(R.id.ll_soft_subject);

		tv_recommend.setOnClickListener(new tvHeadMenuClickListener());
		tv_rank.setOnClickListener(new tvHeadMenuClickListener());
		tv_classify.setOnClickListener(new tvHeadMenuClickListener());
		tv_subject.setOnClickListener(new tvHeadMenuClickListener());

		initRecommend();
	}

	private class tvHeadMenuClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.equals(tv_recommend)) {
				initRecommend();
			} else if (v.equals(tv_rank)) {
				initRank();
			} else if (v.equals(tv_classify)) {
				initClassify();
			} else if (v.equals(tv_subject)) {
				initSubject();
			}
		}
	}

	private void hideAll() {
		// TODO Auto-generated method stub
		iv_recommend_tag.setVisibility(View.INVISIBLE);
		iv_rank_tag.setVisibility(View.INVISIBLE);
		iv_classify_tag.setVisibility(View.INVISIBLE);
		iv_subject_tag.setVisibility(View.INVISIBLE);

		ll_recommend.setVisibility(View.GONE);
		ll_rank.setVisibility(View.GONE);
		ll_classify.setVisibility(View.GONE);
		ll_subject.setVisibility(View.GONE);
	}

	public void initClassify() {
		// TODO Auto-generated method stub
		hideAll();
		iv_classify_tag.setVisibility(View.VISIBLE);
		ll_classify.setVisibility(View.VISIBLE);
		if (classifyPage == null) {
			classifyPage = new SoftClassifyPage(this, ll_classify);
		}
	}

	private void initRank() {
		// TODO Auto-generated method stub
		hideAll();
		iv_rank_tag.setVisibility(View.VISIBLE);
		ll_rank.setVisibility(View.VISIBLE);
		if (rankPage == null) {
			rankPage = new SoftRankPage(activity, ll_rank);
		}
	}

	private void initRecommend() {
		// TODO Auto-generated method stub
		hideAll();
		iv_recommend_tag.setVisibility(View.VISIBLE);
		ll_recommend.setVisibility(View.VISIBLE);
		if (recommendPage == null) {
			recommendPage = new SoftRecommendPage(activity, ll_recommend);
		}

	}

	private void initSubject() {
		// TODO Auto-generated method stub
		hideAll();
		iv_subject_tag.setVisibility(View.VISIBLE);
		ll_subject.setVisibility(View.VISIBLE);
	}
}
