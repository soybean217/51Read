package com.wanpg.bookread.ui.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.BaseFragment;
import com.wanpg.bookread.R;
import com.wanpg.bookread.api.ShuPengApi;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.Book;
import com.wanpg.bookread.data.DownloadBook;
import com.wanpg.bookread.download.DownItem;
import com.wanpg.bookread.logic.BackTask;
import com.wanpg.bookread.ui.activity.WebStoreActivity;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.LogUtil;
import com.wanpg.bookread.widget.AsyncImageView;
import com.wanpg.bookread.widget.HeightBasedChildGridView;
import com.wanpg.bookread.widget.HeightBasedChildListView;

public class StoreBookShowFragment extends BaseFragment {

	private Book bookMode = null;
	private int bookId;

	private final int LOAD_DATA_SUCCESS = 0;
	private final int LOAD_DATA_FAIL = 1;
	private final int LOAD_RECOMMEND_FAIL = 2;

	private boolean isShowRecommend = true;

	private AsyncImageView iv_cover;
	private TextView tv_name, tv_author, tv_pub, tv_tags, tv_intro;
	private TextView tv_button_online, tv_button_download;
	private HeightBasedChildListView lv_comment;
	private TextView book_show_no_comment;
	private HeightBasedChildGridView cgv_download_list;
	private LinearLayout book_show_author_recommend;
	private LinearLayout book_show_author_recommend_layout;
	private ScrollView sv_book_show_main;
	private List<DownloadBook> listDownloadModes = new ArrayList<DownloadBook>();
	private List<Map<String, Object>> listAuthorRecommend = new ArrayList<Map<String, Object>>();
	private List<Map<String, String>> listComment = new ArrayList<Map<String, String>>();
	private View v1, v2, v3;
	private Bundle bundle;
	private View v_main = null;
	private LinearLayout ll_book_show_main;
	private View v_load;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case LOAD_DATA_SUCCESS:
				v_load.setVisibility(View.GONE);
				ll_book_show_main.setVisibility(View.VISIBLE);
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
			case LOAD_RECOMMEND_FAIL:
				isShowRecommend = false;
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
		bundle = getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(!resetView(v_main)){
			v_main = inflater.inflate(R.layout.activity_store_book_show, null);
			v_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			((TextView) v_main.findViewById(R.id.tv_title)).setText("书籍详情");
			setHeadBackEvent(v_main.findViewById(R.id.ib_back));
			ll_book_show_main = (LinearLayout) v_main.findViewById(R.id.ll_book_show_main);
			iv_cover = (AsyncImageView) v_main.findViewById(R.id.book_show_cover);
			tv_name = (TextView) v_main.findViewById(R.id.book_show_name);
			tv_author = (TextView) v_main.findViewById(R.id.book_show_author);
			tv_pub = (TextView) v_main.findViewById(R.id.book_show_pub);
			tv_tags = (TextView) v_main.findViewById(R.id.book_show_tag);
			tv_intro = (TextView) v_main.findViewById(R.id.book_show_intro);
			tv_button_online = (TextView) v_main.findViewById(R.id.book_show_read_online);
			tv_button_download = (TextView) v_main.findViewById(R.id.book_show_download);
			lv_comment = (HeightBasedChildListView) v_main.findViewById(R.id.book_show_comment);
			book_show_no_comment = (TextView) v_main.findViewById(R.id.book_show_no_comment);
			cgv_download_list = (HeightBasedChildGridView) v_main.findViewById(R.id.book_show_download_list);
			book_show_author_recommend = (LinearLayout) v_main.findViewById(R.id.book_show_author_recommend);
			book_show_author_recommend_layout = (LinearLayout) v_main.findViewById(R.id.book_show_author_recommend_layout);
			sv_book_show_main = (ScrollView) v_main.findViewById(R.id.sv_book_show_main);
			v1 = v_main.findViewById(R.id.count_layout_01);
			v2 = v_main.findViewById(R.id.count_layout_02);
			v3 = v_main.findViewById(R.id.count_layout_03);
			v_load = v_main.findViewById(R.id.in_loading);
			v_load.setVisibility(View.VISIBLE);
			ll_book_show_main.setVisibility(View.GONE);

			initData();
		}
		return v_main;
	}

	private void initData() {
		// TODO Auto-generated method stub
		new BackTask() {

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

			@Override
			public void doInThreadWhenCalling(String info) {
				// TODO Auto-generated method stub

			}

			@Override
			public String doInThread() {
				// TODO Auto-generated method stub
				bookMode = (Book) bundle.getSerializable("data");

				if (bookMode == null) {
					bookId = bundle.getInt("bookId", 0);
					bookMode = ShuPengApi.getOneBookMode(bookId);
				} else {
					bookId = bookMode.id;
				}

				if (bookMode == null) {
					handler.sendEmptyMessage(LOAD_DATA_FAIL);
					return "FAIL";
				} else {
					listAuthorRecommend = ShuPengApi.getAuthorRecommendList(bookId);
					if (listAuthorRecommend == null || listAuthorRecommend.size() == 0) {
						handler.sendEmptyMessage(LOAD_RECOMMEND_FAIL);
					}

					listComment = ShuPengApi.getBookComment(1, 3, bookId);
					listDownloadModes = bookMode.downloadList;

					handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
					return "SUCCESS";
				}
			}

		}.submit();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		iv_cover.setImageUrl(ShuPengConfig.BOOK_COVER_MAIN_L + bookMode.thumb, R.drawable.ic_launcher, 0, 0);
		tv_name.setText(bookMode.name);
		tv_author.setText(bookMode.author);
		tv_pub.setText(bookMode.pub);
		if (bookMode.tags != null) {
			tv_tags.setText(bookMode.tags.toString());
		}
		tv_intro.setText(bookMode.intro);


		BookShowDownloadGridAdapter gridAdapter = new BookShowDownloadGridAdapter(getActivity(), listDownloadModes);
		cgv_download_list.setAdapter(gridAdapter);

		tv_button_online.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), WebStoreActivity.class);
				intent.putExtra("HREF", bookMode.read_url);
				getActivity().startActivity(intent);
			}
		});

		tv_button_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int height = v1.getHeight() + v2.getHeight() + v3.getHeight();
				LogUtil.D("height", height + "");
				sv_book_show_main.scrollTo(0, height);
			}
		});


		//加载authorRecommend
		if (isShowRecommend) {
			book_show_author_recommend.setVisibility(View.VISIBLE);
			int size = DisplayUtil.getDisWidthDp() / (40 + 10);
			size = (size > listAuthorRecommend.size()) ? listAuthorRecommend.size() : size;
			for (int i = 0; i < size; i++) {
				final Map<String, Object> mapRecommendCell = listAuthorRecommend.get(i);
				View v_cell = LayoutInflater.from(mActivity).inflate(R.layout.book_show_author_recommend_cell, null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(DisplayUtil.dp2px(10), 0, 0, 0);
				book_show_author_recommend_layout.addView(v_cell, params);
				AsyncImageView iv_cover = (AsyncImageView) v_cell.findViewById(R.id.iv_book_show_author_recommend_cell);
				TextView tv_name = (TextView) v_cell.findViewById(R.id.tv_book_show_author_recommend_cell);
				LinearLayout ll_layout = (LinearLayout) v_cell.findViewById(R.id.ll_book_show_author_recommend_cell);
				tv_name.setText(mapRecommendCell.get("name").toString());
				String imageHref = ShuPengConfig.BOOK_COVER_MAIN_B + mapRecommendCell.get("thumb");
				iv_cover.setImageUrl(imageHref, R.drawable.ic_launcher);

				ll_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putInt("bookId", Integer.parseInt(mapRecommendCell.get("id").toString()));
						onFragmentDo(TYPE_TO_BOOK_DETAIL, bundle);
					}
				});
			}
		} else {
			book_show_author_recommend.setVisibility(View.GONE);
		}

		//加载评论列表
		if (listComment == null || listComment.size() == 0) {
			//没有评论
			book_show_no_comment.setVisibility(View.VISIBLE);
			lv_comment.setVisibility(View.GONE);
		} else {
			//有评论
			book_show_no_comment.setVisibility(View.GONE);
			lv_comment.setVisibility(View.VISIBLE);
			View v = LayoutInflater.from(mActivity).inflate(R.layout.book_comment_more_cell, null);
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putString("bookName", bookMode.name);
					bundle.putInt("bookId", bookId);		
					onFragmentDo(TYPE_TO_BOOK_COMMENT, bundle);
				}
			});
			lv_comment.addFooterView(v);
			MyCommentListAdapter commentListAdapter = new MyCommentListAdapter();
			lv_comment.setAdapter(commentListAdapter);
		}

	}

	/**
	 * 下载地址按钮gridview的adapter
	 *
	 * @author wanpg
	 */
	private class BookShowDownloadGridAdapter extends BaseAdapter {

		private Context context;
		private List<DownloadBook> listItems;
		private ViewHolder holder;

		public BookShowDownloadGridAdapter(Context context, List<DownloadBook> listItems) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (listItems == null) {
				return 0;
			} else {
				return listItems.size();
			}
		}

		@Override
		public DownloadBook getItem(int arg0) {
			// TODO Auto-generated method stub
			if (listItems == null) {
				return null;
			} else {
				return listItems.get(arg0);
			}
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (v == null) {
				v = LayoutInflater.from(context).inflate(R.layout.book_show_download_button, null);
				holder = new ViewHolder();
				holder.tv_download = (TextView) v.findViewById(R.id.book_show_download_url);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			final int x = pos;
			holder.tv_download.setText("地址" + (x + 1) + "(" + listItems.get(x).format + "," + listItems.get(x).size + ")");
			holder.tv_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DownItem item = new DownItem();
					item.name = bookMode.name;
					item.url = listItems.get(x).url;
					item.downloadId = listItems.get(x).id;
					item.bookId = bookMode.id;
					item.author = bookMode.author;
					item.coverUrl = bookMode.thumb;
					item.innerFileType = listItems.get(x).format;
					BaseApplication baseApplication = (BaseApplication) getActivity().getApplication();
					baseApplication.downloadService.addTask(item);
				}
			});
			return v;
		}

		private class ViewHolder {
			public TextView tv_download;
		}
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
				v = LayoutInflater.from(getActivity()).inflate(R.layout.book_comment_cell, null);
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
			if (content.length() > 80) {
				content = content.substring(0, 79) + "...";
			}
			holder.tv_content.setText(content);
			holder.tv_info.setText(user + " 发表于" + time + "|来源：" + source);

			LogUtil.D("holder.tv_info", holder.tv_info.getText().toString());
			return v;
		}

		private class ViewHolder {
			TextView tv_info, tv_content;
		}
	}
}
