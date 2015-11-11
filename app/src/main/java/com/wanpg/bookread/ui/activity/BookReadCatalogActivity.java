package com.wanpg.bookread.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wanpg.bookread.BaseActivity;
import com.wanpg.bookread.R;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.db.DaoManager;
import com.wanpg.bookread.manager.ShelfManager;
import com.wanpg.bookread.ui.adapter.MyViewPagerAdapter;
import com.wanpg.bookread.ui.read.epub.EpubKernel;
import com.wanpg.bookread.ui.read.epub.EpubKernel.EpubCatalog;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.FileUtil;

public class BookReadCatalogActivity extends BaseActivity {

    private ShelfBook shelfBookMode;
    private boolean isFullScreen;
    private TextView tv_head_catalog;
    private TextView tv_head_maker;
    private ImageView iv_head_catalog;
    private ImageView iv_head_maker;
    private ViewPager vp_main;
    private List<View> viewList = new ArrayList<View>();
    private String tmpPath;

    private ListView lv_catalog_list;

    private String type = "";

    private List<Map<String, String>> listTxtMaker = new ArrayList<Map<String,String>>();
    private List<Map<String, String>> listEpubMaker = new ArrayList<Map<String, String>>();
    private List<EpubCatalog> listEpubCatalog = new ArrayList<EpubCatalog>();
    private List<String> listTextCatalog = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalog);
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        shelfBookMode = (ShelfBook) intent.getSerializableExtra("shelfBookMode");
        isFullScreen = intent.getBooleanExtra("isFullScreen", true);
        if (isFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        tmpPath = intent.getStringExtra("tmpPath");
        type = FileUtil.getFileType(tmpPath);

        if (type.equals("txt")) {
            initTxtData();
        } else if (type.equals("epub")) {
            initEpubData();
        } else {

        }
        initUI();
    }

    private void initTxtData() {
        // TODO Auto-generated method stub
        //获取目录内容
        listTextCatalog = null;
        //获取书签内容
        String configBook = shelfBookMode.bookPath.replace("/", "_");
        listTxtMaker = DaoManager.getInstance().getMarkerDao().queryTxtMakers(configBook);
    }

    private void initEpubData(){
        //获取目录内容
        EpubKernel epubKernel = EpubKernel.getInstance();
		try {
			epubKernel.openEpubFile(tmpPath);
	        listEpubCatalog = epubKernel.getCatalogList();
	        //获取书签内容
	        listEpubMaker = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void initUI() {
        // TODO Auto-generated method stub
        findViewById(R.id.ib_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BookReadCatalogActivity.this.onBackPressed();
            }
        });

        tv_head_catalog = (TextView) findViewById(R.id.tv_head_catalog);
        tv_head_maker = (TextView) findViewById(R.id.tv_head_maker);
        iv_head_catalog = (ImageView) findViewById(R.id.iv_head_catalog);
        iv_head_maker = (ImageView) findViewById(R.id.iv_head_maker);
        lv_catalog_list = (ListView) findViewById(R.id.lv_catalog_list);

        vp_main = (ViewPager) findViewById(R.id.vp_main);

        ViewGroup v = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.download_vp_cell, null);
        ListView lv_01 = (ListView) v.findViewById(R.id.lv_01);
        ListView lv_02 = (ListView) v.findViewById(R.id.lv_02);
        viewList.add(lv_01);
        viewList.add(lv_02);
        //v.removeAllViews();
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(viewList);
        vp_main.setAdapter(adapter);

        if (type.equals("txt")) {
//			MyTxtCatalogAdapter txtCatalogAdapter = new MyTxtCatalogAdapter();
//			lv_01.setAdapter(txtCatalogAdapter);
            ((TextView) findViewById(R.id.tv_title)).setText("书签");
            MyTxtMakerAdapter txtMakerAdapter = new MyTxtMakerAdapter();
            //lv_02.setAdapter(txtMakerAdapter);
            lv_catalog_list.setAdapter(txtMakerAdapter);
        } else if (type.equals("epub")) {
            ((TextView) findViewById(R.id.tv_title)).setText("目录");
            MyEpubCatalogAdapter epubCatalogAdapter = new MyEpubCatalogAdapter();
            //lv_01.setAdapter(epubCatalogAdapter);
            lv_catalog_list.setAdapter(epubCatalogAdapter);
        } else {

        }

        vp_main.setCurrentItem(0);
        iv_head_catalog.setVisibility(View.VISIBLE);
        iv_head_maker.setVisibility(View.GONE);

        tv_head_catalog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                vp_main.setCurrentItem(0);
            }
        });
        tv_head_maker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                vp_main.setCurrentItem(1);
            }
        });

        vp_main.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    case 0:
                        iv_head_catalog.setVisibility(View.VISIBLE);
                        iv_head_maker.setVisibility(View.GONE);
                        break;
                    case 1:
                        iv_head_catalog.setVisibility(View.GONE);
                        iv_head_maker.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
    }

    private class MyEpubCatalogAdapter extends BaseAdapter {

        ViewHolder holder;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (listEpubCatalog == null) {
                return 0;
            } else {
                return listEpubCatalog.size();
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (listEpubCatalog == null) {
                return null;
            } else {
                return listEpubCatalog.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BookReadCatalogActivity.this).inflate(R.layout.book_catalog_cell, null);
                holder.tv = (TextView) convertView.findViewById(R.id.tv_catalog_cell);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int x = position;
            final EpubCatalog ec = listEpubCatalog.get(x); 
            holder.tv.setText(ec.title);

            holder.tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("chapter_id", ec.id);
                    intent.putExtras(bundle);
                    BookReadCatalogActivity.this.setResult(ShelfManager.CATALOG_RESULT_CODE, intent);
                    BookReadCatalogActivity.this.finish();
                    overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView tv;
        }
    }

    private class MyTxtCatalogAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (listTextCatalog == null) {
                return 0;
            } else {
                return listTextCatalog.size();
            }
        }

        @Override
        public Object getItem(int position) {
            // )TODO Auto-generated method stub
            if (listTextCatalog == null) {
                return null;
            } else {
                return listTextCatalog.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            return convertView;
        }
    }

    private class MyEpubMakerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (listEpubMaker == null) {
                return 0;
            } else {
                return listEpubMaker.size();
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (listEpubMaker == null) {
                return null;
            } else {
                return listEpubMaker.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return convertView;
        }
    }

    private class MyTxtMakerAdapter extends BaseAdapter {

        ViewHolder holder;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (listTxtMaker == null) {
                return 0;
            } else {
                return listTxtMaker.size();
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (listTxtMaker == null) {
                return null;
            } else {
                return listTxtMaker.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BookReadCatalogActivity.this).inflate(R.layout.book_maker_cell, null);
                convertView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, DisplayUtil.dp2px(80)));
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_maker_date);
                holder.tv_progress = (TextView) convertView.findViewById(R.id.tv_maker_progress);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_maker_content);
                holder.ib_del = (ImageButton) convertView.findViewById(R.id.ib_maker_del);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final int x = position;
            holder.tv_date.setText(listTxtMaker.get(position).get("date"));
            holder.tv_progress.setText(listTxtMaker.get(position).get("progress"));
            holder.tv_content.setText(listTxtMaker.get(position).get("content").trim());
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("pageBegin", Integer.parseInt(listTxtMaker.get(x).get("pageBegin")));
                    bundle.putInt("pageEnd", Integer.parseInt(listTxtMaker.get(x).get("pageEnd")));
                    intent.putExtras(bundle);
                    BookReadCatalogActivity.this.setResult(ShelfManager.CATALOG_RESULT_CODE, intent);
                    BookReadCatalogActivity.this.finish();
                    overridePendingTransition(R.anim.out_none, R.anim.slide_out_from_right);
                }
            });
            holder.ib_del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    String configBook = shelfBookMode.bookPath.replace("/", "_");
//                    MyDB db = new MyDB(context, configBook + "_" + DataBaseHelper.BOOK_MAKER_TXT_NAME);
//                    db.openDB(MyDB.WRITABLE);
//                    db.delOneTxtMaker(listTxtMaker.get(x));
//                    db.closeDB();
                    DaoManager.getInstance().getMarkerDao().delOneTxtMaker(listTxtMaker.get(x), configBook);
                    listTxtMaker.remove(x);
                    MyTxtMakerAdapter.this.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView tv_date, tv_progress, tv_content;
            public ImageButton ib_del;
        }
    }
}
