package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView;
import com.wanpg.bookread.widget.DropRefreshClickLoadListView.OnLoadListener;
import com.wanpg.bookread.widget.Notice;

public class BookCommentFragment extends BaseFragment {

	private final int LOAD_DATA_SUCCESS = 0;
	private final int LOAD_DATA_FAIL = 1;
	private final int UPDATE_DATA_SUCCESS = 2;
	private final int UPDATE_DATA_FAIL = 3;
	private final int BOTTOM_LOAD_DATA_SUCCESS = 4;
	private final int BOTTOM_LOAD_DATA_FAIL = 5;


	private View v_load;

	private DropRefreshClickLoadListView lv_comment;

	private int bookId;
	private String bookName;

	private int pIndex = 1;
	private final int pSize = 20;

	private List<Map<String, String>> listComment = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listTmp = new ArrayList<Map<String, String>>();

	private MyCommentListAdapter commentListAdapter;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				lv_comment.setVisibility(View.VISIBLE);
				initUI();
				break;
			case LOAD_DATA_FAIL:
				v_load.findViewById(R.id.loading).setVisibility(View.GONE);
				v_load.findViewById(R.id.load_fail).setVisibility(View.VISIBLE);
				v_load.findViewById(R.id.load_fail_button).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						initData();
					}
				});
				break;
			case BOTTOM_LOAD_DATA_SUCCESS:
				listComment.addAll(listTmp);
				commentListAdapter.notifyDataSetChanged();
				if (listTmp.size() < pSize) {
					lv_comment.hideFootMoreView();
				}
				break;
			case BOTTOM_LOAD_DATA_FAIL:
				Notice.showToast("没有新数据！");
				lv_comment.hideFootMoreView();
				break;
			default:
				break;
			}
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		bookId = bundle.getInt("bookId");
		bookName = bundle.getString("bookName");
	}


	private View mMainView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(mMainView)){
			mMainView = inflater.inflate(R.layout.activity_store_book_list, null);

			mMainView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) mMainView.findViewById(R.id.tv_title)).setText("《" + bookName + "》的评论");
			setHeadBackEvent(mMainView.findViewById(R.id.ib_back));

			lv_comment = (DropRefreshClickLoadListView) mMainView.findViewById(R.id.lv_book_list);
			v_load = mMainView.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			lv_comment.setVisibility(View.GONE);
			initData();
		}
		return mMainView;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new BackTask() {

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				listComment = ShuPengApi.getBookComment(pIndex, pSize, bookId);
				if (listComment == null) {
					handler.sendEmptyMessage(LOAD_DATA_FAIL);
				} else {
					handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
				}
				return null;
			}

			@Override
			public void doBeforeThread() {
				// TODO Auto-generated method stub
				v_load.findViewById(R.id.loading).setVisibility(View.VISIBLE);
				v_load.findViewById(R.id.load_fail).setVisibility(View.GONE);
			}

			@Override
			public void doAfterThread(String result) {
				// TODO Auto-generated method stub

			}
		}.submit();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		commentListAdapter = new MyCommentListAdapter();
		lv_comment.setAdapter(commentListAdapter);

		lv_comment.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new BackTask() {

					@Override
					public void doInThreadWhenCalling(String info) {
						// TODO Auto-generated method stub

					}

					@Override
					public String doInThread() {
						// TODO Auto-generated method stub
						pIndex++;
						listTmp = ShuPengApi.getBookComment(pIndex, pSize, bookId);
						if (listTmp == null) {
							handler.sendEmptyMessage(BOTTOM_LOAD_DATA_FAIL);
						} else {
							handler.sendEmptyMessage(BOTTOM_LOAD_DATA_SUCCESS);
						}
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
		});
	}

	private class MyCommentListAdapter extends BaseAdapter {

		ViewHolder holder;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listComment.size();
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return listComment.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public View getView(int pos, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (v == null) {
				v = LayoutInflater.from(mActivity).inflate(R.layout.book_comment_cell_02, null);
				holder = new ViewHolder();
				holder.tv_info = (TextView) v.findViewById(R.id.comment_info);
				holder.tv_content = (TextView) v.findViewById(R.id.comment_content);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			String content = listComment.get(pos).get("content");
			String time = listComment.get(pos).get("time");
			String user = listComment.get(pos).get("user");
			String source = listComment.get(pos).get("source");
			String url = listComment.get(pos).get("url");
			holder.tv_content.setText(content);
			holder.tv_info.setText(user + " 发表于" + time + "|来源：" + source);

			Log.d("holder.tv_info", holder.tv_info.getText().toString());
			return v;
		}

		private class ViewHolder {
			TextView tv_info, tv_content;
		}
	}
}
