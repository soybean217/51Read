package com.wanpg.bookread.download;

import java.io.Serializable;

public class DownItem implements Serializable {
    /**
     * 等待状态 *
     */
    public static final int STATUS_WAIT = 0;
    /** 数据准备阶段，此阶段主要用于加载文件信息 **/
    public static final int STATUS_PREPARE = 6;
    /**
     * 正在下载 *
     */
    public static final int STATUS_DOWNLOADING = 1;
    /**
     * 下载完成 *
     */
    public static final int STATUS_FINISHED = 2;
    /**
     * 暂停状态 *
     */
    public static final int STATUS_PAUSE = 3;
    /**
     * 下载出错 *
     */
    public static final int STATUS_ERROR = 4;
    
    public static final int STATUS_DELETE = 5;
    /**
     *
     */
    public static final long serialVersionUID = 1L;

    /**
     * 下载的downloadId *
     */
    public int downloadId;
    /**
     * 下载的url *
     */
    public String url;
    /**
     * 封面的地址 *
     */
    public String coverUrl;
    /**
     * 数的id *
     */
    public int bookId;
    /**
     * 下载的名字 *
     */
    public String name;
    /**
     * 书的author *
     */
    public String author;
    /**
     * 下载的contentType *
     */
    public String contentType;
    /**
     * 内部文件的格式 *
     */
    public String innerFileType;
    /**
     * 下载的fileLength 总长度 *
     */
    public long fileLength;
    /**
     * 下载的已经下载的长度 *
     */
    public long finishedLength;
    /**
     * 下载的保存路径 *
     */
    public String savePath;
    /**
     * 下载的状态 *
     */
    public int status;
}
