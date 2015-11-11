package com.wanpg.bookread.data;

import java.io.Serializable;

/**
 * 书架书籍的model
 *
 * @author Jinpeng
 */
public class ShelfBook implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String POS_ONLINE = "online";
    public static final String POS_LOCAL_SDCARD = "local_sdcard";
    public static final String POS_LOCAL_SHUPENG = "local_shupeng";

    /**
     * 书的位置,online,local *
     */
    public String readMode;
    /**
     * 本地封面的路径 *
     */
    public String coverPath;
    /**
     * 本地书的路径 *
     */
    public String bookPath;
    /**
     * 作者 *
     */
    public String author;
    /**
     * 书名 *
     */
    public String bookName;
    /**
     * 网络书籍的封面后缀 *
     */
    public String thumb;
    /**
     * 网络书籍id *
     */
    public int bookId;
    /**
     * 网络书籍url *
     */
    public String url;
    /**
     * 当前章节id *
     */
    public String chapterId;
    /**
     * 当前章节url *
     */
    public String chapterUrl;
    /**
     * 在书架中的位置 *
     */
    public int posInShelf;
    /**
     * 内部文件类型，主要是针对从书朋下载的书籍是压缩格式的 *
     */
    public String innerFileType;

}
