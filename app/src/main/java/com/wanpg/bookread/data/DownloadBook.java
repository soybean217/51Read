package com.wanpg.bookread.data;

import java.io.Serializable;

/**
 * 书籍的下载信息
 *
 * @author wanpg
 */
public class DownloadBook implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    /**
     * 文件类型 *
     */
    public String format;
    /**
     * id *
     */
    public int id;
    /**
     * 解压密码 *
     */
    public String pwd;
    /**  **/
    public boolean type;
    /**
     * 下载地址 *
     */
    public String url;
    /**
     * 文件大小 *
     */
    public String size;

//	"txt":[{
//	    "id":4341462,
//	    "pwd":"",
//	    "format":"txt",
//	    "type":true,
//	    "url":"http:\/\/www.shupeng.com\/click?url=8VDMQ+lWMQ515a994c71050078KUOeYiP3zzVgPz9Pjpi4yW9S5KIvkvGfV+8VrcxKhRVYyPyJoFY32+M2LLApgPuUJmej5JoqWTeioNpbjedu4ZAMyf62RIAvPHk\/thtvXY2YOwwv2S2zQnZQWXAxOy9Y27lnNU0wt+V1ZYqBMlktYy&qid=0&bid=1662897&fr=app&x=36v12vx909wz9xy67yxrr13wxv400z4r",
//	    "size":"49.8K"}],

}
