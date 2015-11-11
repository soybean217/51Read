package com.wanpg.bookread.api;

import android.content.Context;

import com.shupeng.open.Shupeng;
import com.shupeng.open.model.ShupengException;
import com.wanpg.bookread.common.ShuPengConfig;
import com.wanpg.bookread.data.Book;
import com.wanpg.bookread.data.BookBoard;
import com.wanpg.bookread.data.DownloadBook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 此类里包含一些获取书朋网信息的方法
 *
 * @author wanpg
 */
public class ShuPengApi extends Api {

    private static final String ver = null;

    /**
     * 初始化shupeng的sdk
     *
     * @param context
     * @return
     */
    public static boolean initShuPengAppkey(Context context) {
        Shupeng.init(context, ShuPengConfig.SHUPENG_APPKEY);
        return false;
    }

    /**
     * 得到书城首页五个切换也推荐的书籍list
     */
    public static List<BookBoard> getRecommendList() {

        List<BookBoard> list = new ArrayList<BookBoard>();

        try {
            JSONObject jsonObject1 = Shupeng.getBoardList(1, 20, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("boardlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                if (!jsonObject2.getString("banner").equals("")) {
                    BookBoard boardMode = new BookBoard();
                    boardMode.banner = jsonObject2.getString("banner");
                    boardMode.count = jsonObject2.getInt("count");
                    boardMode.id = jsonObject2.getInt("id");
                    boardMode.intro = jsonObject2.getString("intro");
                    boardMode.name = jsonObject2.getString("name");
                    boardMode.thumb = jsonObject2.getString("thumb");
                    list.add(boardMode);
                    if (list.size() >= 5) {
                        break;
                    }
                }
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获得专辑列表
     *
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<BookBoard> getBoardModes(int pIndex, int pSize) {

        List<BookBoard> list = new ArrayList<BookBoard>();

        try {
            JSONObject jsonObject1 = Shupeng.getBoardList(pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("boardlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                if (!jsonObject2.getString("banner").equals("")) {
                    BookBoard boardMode = new BookBoard();
                    boardMode.banner = jsonObject2.getString("banner");
                    boardMode.count = jsonObject2.getInt("count");
                    boardMode.id = jsonObject2.getInt("id");
                    boardMode.intro = jsonObject2.getString("intro");
                    boardMode.name = jsonObject2.getString("name");
                    boardMode.thumb = jsonObject2.getString("thumb");
                    list.add(boardMode);
                }
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获得热门书籍列表
     *
     * @param pIndex
     * @param pSize
     * @return List<Map<String, Object>>
     * -   "author":"柴静",
     * "bookid":4818676,
     * "nick":"看见（柴静个人成长告白书，中国社会十年变迁的备忘录）",
     * "no":0,
     * "name":"看见",
     * "channel":"eb",
     * "thumb":"374a05ddf95ae46eb30facf0d41f7ba4.jpg",
     * "url":"http:\/\/www.shupeng.com\/book\/4818676",
     * "intro":"《看见》是知名记者和主持人柴静讲述央视十年历程的自传性作品，...",
     * "mode":"all"
     */
    public static List<Map<String, Object>> getHotBooks(int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = Shupeng.getHotBooks(pIndex, pSize, ver);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("author", jsonObject.getString("author"));
                map.put("bookId", jsonObject.getInt("bookid"));
                map.put("nick", jsonObject.getString("nick"));
                map.put("no", jsonObject.getString("no"));
                map.put("name", jsonObject.getString("name"));
                map.put("channel", jsonObject.getString("channel"));
                map.put("thumb", jsonObject.getString("thumb"));
                map.put("url", jsonObject.getString("url"));
                map.put("intro", jsonObject.getString("intro"));
                map.put("mode", jsonObject.getString("mode"));
                list.add(map);
            }
            return list;
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 得到网络热门小说列表
     *
     * @param pIndex
     * @param pSize
     * @return --tags 标签
     * --updatetime 最新更新时间
     * --viewcount  点击次数
     * --chapterid  最新章节id
     * --intro  介绍
     * --id  书的id
     * --author  作者
     * --chapter  最新章节名字
     * --name  书名
     * --cover 封面图片url
     */
    public static List<Map<String, Object>> getHotNetNovel(int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = Shupeng.getHotNetnovel(pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("tags", jsonArray2List(jsonObject2.getJSONArray("tags")));
                map.put("updatetime", jsonObject2.getString("updatetime"));
                map.put("viewcount", jsonObject2.getInt("viewcount"));
                map.put("chapterid", jsonObject2.getInt("chapterid"));
                map.put("intro", jsonObject2.getString("intro"));
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("chapter", jsonObject2.getString("chapter"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获得指定页面、数量、id的专辑列表
     *
     * @param pIndex  -- 当前第几页
     * @param pSize   -- 当前页共多少条
     * @param boardId
     * @return
     */
    public static List<Map<String, Object>> getBoardListBookModes(int pIndex, int pSize, int boardId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {

            JSONObject jsonObject1 = Shupeng.getBoardById(pIndex, pSize, boardId, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                //{"tags":["祝无忧","贺老六","吸血鬼","狐狸","韩莫","灵异神怪","悬疑灵异"],
//				"pubtime":"",
//				"id":50952519,
//				"author":"司马洋",
//				"authed":1,
//				"name":"血狐狸",
//				"viewcount":8566,
//				"thumb":"95157e01ede4519a864d70affdff58bf.jpg",
//				"pub":"",
//				"intro":"在一个黑漆漆的夜，四个小孩闯进了吸血狐狸的别墅。惊险刺激之余，一个小男孩偷走了一颗吸血獠王的獠牙，引...",
//				"online":0}
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);

            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }


    /**
     * 得到指定id的书的信息
     *
     * @return bookmode
     */
    public static Book getOneBookMode(int id) {
        Book bookMode = new Book();
        JSONObject jsonObject = null;
        try {
            jsonObject = Shupeng.getBookDetailById(id, ver);

            bookMode.authed = jsonObject.getInt("authed");
            bookMode.author = jsonObject.getString("author");
            bookMode.chapters_url = jsonObject.getString("chapters_url");
            bookMode.thumb = jsonObject.getString("thumb");
            bookMode.downloadList = getDownloadModes(jsonObject.getJSONObject("links"));
            bookMode.id = jsonObject.getInt("id");
            bookMode.intro = jsonObject.getString("intro");
            bookMode.name = jsonObject.getString("name");
            bookMode.online = jsonObject.getInt("online");
            bookMode.pub = jsonObject.getString("pub");
            bookMode.pubTime = jsonObject.getString("pubtime");
            bookMode.read_url = jsonObject.getString("read_url");
            bookMode.tags = jsonArray2List(jsonObject.getJSONArray("tags"));
            bookMode.viewcount = jsonObject.getInt("viewcount");

        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bookMode;
    }

    /**
     * 得到指定id的书的信息
     *
     * @return bookmode
     */
    public static Map<String, Object> getOneBookMap(int id) {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = null;
        try {
            jsonObject = Shupeng.getBookDetailById(id, ver);
            map.put("bookId", jsonObject.getInt("id"));
            map.put("author", jsonObject.getString("author"));
            map.put("name", jsonObject.getString("name"));
            map.put("thumb", jsonObject.getString("thumb"));
            map.put("intro", jsonObject.getString("intro"));

        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return map;
    }

    /**
     * 获取download的信息
     *
     * @param jsonObject
     * @return List<DownloadMode>
     * @throws org.json.JSONException
     */
    public static List<DownloadBook> getDownloadModes(JSONObject jsonObject) throws JSONException {
        List<DownloadBook> list = new ArrayList<DownloadBook>();
        @SuppressWarnings("unchecked")
        Iterator<String> in = jsonObject.keys();
        while (in.hasNext()) {
            JSONArray jsonArray = jsonObject.getJSONArray(in.next());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String format = jsonObject1.getString("format");
                if (format.equalsIgnoreCase("txt") || format.equalsIgnoreCase("epub")) {
                    DownloadBook mode = new DownloadBook();
                    mode.format = format;
                    mode.id = jsonObject1.getInt("id");
                    mode.pwd = jsonObject1.getString("pwd");
                    mode.size = jsonObject1.getString("size");
                    mode.type = jsonObject1.getBoolean("type");
                    mode.url = jsonObject1.getString("url");
                    list.add(mode);
                }
            }
        }
        return list;
    }

    /**
     * 根据bookId获得评论列表
     *
     * @param pIndex
     * @param pSize
     * @param bookId
     * @return
     */
    public static List<Map<String, String>> getBookComment(int pIndex, int pSize, int bookId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = Shupeng.getCommentByBookId(pIndex, pSize, bookId, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("cmntlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", jsonObject2.getString("time"));
                map.put("content", jsonObject2.getString("content"));
                map.put("user", jsonObject2.getString("user"));
                map.put("source", jsonObject2.getString("source"));
                map.put("url", jsonObject2.getString("url"));
                list.add(map);
            }
            return list;
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得书籍分类列表
     *
     * @return --"网络文学"
     * --"畅销书"
     */
    public static Map<String, List<Map<String, Object>>> getCategoryList() {
        Map<String, List<Map<String, Object>>> mapCaregory = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> listWeb = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listBook = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray1 = null;
        try {
            jsonArray1 = Shupeng.getCategoryList(ver);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cid", jsonObject1.getInt("cid"));
                map.put("name", jsonObject1.getString("name"));
                String parent = jsonObject1.getString("parent");
                map.put("parent", parent);
                List<Map<String, Object>> listDesc = new ArrayList<Map<String, Object>>();
                JSONArray jsonArray2 = jsonObject1.getJSONArray("desc");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                    Map<String, Object> mapDesc = new HashMap<String, Object>();
                    mapDesc.put("scid", jsonObject2.getInt("scid"));
                    mapDesc.put("name", jsonObject2.getString("name"));
                    listDesc.add(mapDesc);
                }
                map.put("desc", listDesc);
                if (parent.equals("网络文学")) {
                    listWeb.add(map);
                } else if (parent.equals("畅销书")) {
                    listBook.add(map);
                }
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mapCaregory.put("网络文学", listWeb);
        mapCaregory.put("畅销书", listBook);
        return mapCaregory;
    }

    /**
     * 获得具体分类的书籍列表
     *
     * @param pIndex
     * @param pSize
     * @param cid
     * @param scid
     * @return
     */
    public static List<Map<String, Object>> getSecondCategoryBookList(int pIndex, int pSize, int cid, int scid) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject1 = Shupeng.getBookListOfSecondCategory(pIndex, pSize, cid, scid, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }


    /**
     * 获取最新的书籍列表
     *
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<Map<String, Object>> getNewBookListBookModes(int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = Shupeng.getNewBookList(pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 将简单的JSONArray转换成List<String>
     *
     * @param jsonArray
     * @return List<String>
     * @throws org.json.JSONException
     */
    public static List<String> jsonArray2List(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.get(i).toString());
        }
        return list;
    }

    /**
     * 获得热门搜索词
     *
     * @param pIndex
     * @param pSize
     * @return
     */
    public static String getHotSearchWords(int pIndex, int pSize) {
        String s = "";
        try {
            JSONObject jsonObject1 = Shupeng.getHotWords(pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                if(i==0){
                	s=jsonObject2.getString("name");
                }else{
                	s = s + "," + jsonObject2.getString("name");
                }
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return s;
    }

    /**
     * 获得搜索结果
     *
     * @param word
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<Map<String, Object>> getSearchResult(String word, int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject1 = Shupeng.getSearchResult(word, pIndex, pSize, null, 0, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("matches");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 获得书籍作者推荐的书籍列表
     *
     * @param bookId
     * @return
     */
    public static List<Map<String, Object>> getAuthorRecommendList(int bookId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray array = Shupeng.getRecAuthorBooksById(bookId, ver);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", jsonObject.getInt("id"));
                map.put("name", jsonObject.getString("name"));
                map.put("author", jsonObject.getString("author"));
                map.put("thumb", jsonObject.getString("thumb"));
                list.add(map);
            }
            return list;
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取网络小说列表
     *
     * @param cid
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<Map<String, Object>> getNetNovel(int cid, int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = Shupeng.getNetnovelListByCid(cid, pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }


    /**
     * 获取排行榜
     *
     * @return
     */
    public static List<Map<String, Object>> getRankList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {
            JSONArray jsonArray = Shupeng.getAllTopList(ver);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("topname", jsonObject.getString("name"));

                JSONArray jsonArray1 = jsonObject.getJSONArray("toplist");
                List<Map<String, Object>> listTopList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    Map<String, Object> mapTopList = new HashMap<String, Object>();
                    mapTopList.put("id", jsonObject1.getInt("id"));
                    mapTopList.put("name", jsonObject1.getString("name"));
                    if (!map.containsKey("channel")) {
                        map.put("channel", jsonObject1.getString("channel"));
                    }
                    mapTopList.put("type", jsonObject1.getString("type"));
                    mapTopList.put("mode", jsonObject1.getString("mode"));
                    listTopList.add(mapTopList);
                }
                map.put("toplist", listTopList);
                list.add(map);
            }
        } catch (ShupengException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }


    /**
     * 根据id获取当前排行榜的书籍列表
     *
     * @param topId
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<Map<String, Object>> getTopListInfo(int topId, int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject1 = Shupeng.getTopInfoById(topId, pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject1.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                list.add(map);
            }
        } catch (ShupengException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }


    /**
     * 获得一级分类的具体信息
     *
     * @param cid
     * @return
     */
    public static List<Map<String, Object>> getCategoryFirstList(int cid) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = Shupeng.getSecondCategoryList(cid, ver);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("scid", jsonObject.getInt("scid"));
                map.put("name", jsonObject.getString("name"));
                JSONArray jsonArray1 = jsonObject.getJSONArray("desc");
                List<Map<String, Object>> listBook = new ArrayList<Map<String, Object>>();
                //Log.d("jsonArray1.length", jsonArray1.length()+"");
                for (int j = 0; j < jsonArray1.length(); j++) {
//                    if(j>9){
//                        break;
//                    }
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("bookid", jsonObject1.getInt("id"));
                    map1.put("bookname", jsonObject1.getString("name"));
//                    map1 = getOneBookMap(jsonObject1.getInt("id"));
//                    if(map1!=null){
                    listBook.add(map1);
//                    }
                }
                map.put("booklist", listBook);
                list.add(map);
            }
        } catch (ShupengException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 最新章节更新列表
     *
     * @param pIndex
     * @param pSize
     * @return
     */
    public static List<Map<String, Object>> getUpdateNetnovel(int pIndex, int pSize) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject = Shupeng.getUpdateNetnovel(pIndex, pSize, ver);
            JSONArray jsonArray = jsonObject.getJSONArray("booklist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookId", jsonObject2.getInt("id"));
                map.put("author", jsonObject2.getString("author"));
                map.put("name", jsonObject2.getString("name"));
                map.put("thumb", jsonObject2.getString("thumb"));
                map.put("intro", jsonObject2.getString("intro"));
                map.put("chapterid", jsonObject2.getString("chapterid"));
                map.put("chapter", jsonObject2.getString("chapter"));
                map.put("updatetime", jsonObject2.getString("updatetime"));
                list.add(map);
            }
        } catch (ShupengException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }

}
