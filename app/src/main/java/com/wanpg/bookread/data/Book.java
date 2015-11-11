package com.wanpg.bookread.data;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 书籍的标签 *
     */
    public List<String> tags;
    /**
     * 是否是授权的 *
     */
    public int authed;
    /**
     * 在线阅读章节章节地址 *
     */
    public String chapters_url;
    /**
     * 浏览次数 *
     */
    public int viewcount;
    /**
     * 此书籍的下载信息 *
     */
    public List<DownloadBook> downloadList;
    /**
     * 出版社 *
     */
    public String pub;
    /**
     * 简介 *
     */
    public String intro;
    /**
     * 是否是网络小说 *
     */
    public int online;
    /**
     * 书的id *
     */
    public int id;
    /**
     * 出版时间 *
     */
    public String pubTime;
    /**
     * 作者 *
     */
    public String author;
    /**
     * 书名 *
     */
    public String name;
    /**
     * 在线阅读地址 *
     */
    public String read_url;
    /**
     * 封面地址 *
     */
    public String thumb;

//			"tags":["王小柔","随笔","段子","幽默","杂文","生活"]
//			"authed":0
//			"chapters_url":"http:\/\/m.shupeng.com\/#\/chapter\/1662897"
//			"viewcount":81264,
//			"links":{
//				"txt":[{
//					    "id":4341462,
//					    "pwd":"",
//					    "format":"txt",
//					    "type":true,
//					    "url":"http:\/\/www.shupeng.com\/click?url=8VDMQ+lWMQ515a994c71050078KUOeYiP3zzVgPz9Pjpi4yW9S5KIvkvGfV+8VrcxKhRVYyPyJoFY32+M2LLApgPuUJmej5JoqWTeioNpbjedu4ZAMyf62RIAvPHk\/thtvXY2YOwwv2S2zQnZQWXAxOy9Y27lnNU0wt+V1ZYqBMlktYy&qid=0&bid=1662897&fr=app&x=36v12vx909wz9xy67yxrr13wxv400z4r",
//					    "size":"49.8K"}],
//				"jar":[{
//					    "id":6233528,
//					    "pwd":"",
//					    "format":"jar",
//					    "type":true,
//					    "url":"http:\/\/www.shupeng.com\/click?url=x7zwJ3UPQ5515a994c710500788XIyXn7GO1tcBUpYjZStaNgrtL3b+CzYNgMRuDBJxsCgxuAE0NCL3DHbWZIsGWVKoXZx1tybv4bPqMoGKZw0lV&qid=0&bid=1662897&fr=app&x=36v12vx909wz9xy67yxrr13wxv400z4r",
//					    "size":"57.5K"}]},
//			"pub":"新世界出版社",
//			"intro":"作品简介\n  中国最哏的段子作家王小柔继《都是妖蛾子》、《又是妖蛾子》、《把日子过成段子》又抖《十面包袱》！陈彤、石康、孔庆东联袂推荐。《十面包袱》延续了王小柔的特色文风，将奇思怪想与津味幽默相结合，...",
//			"online":1,
//			"id":1662897,
//			"pubtime":"2008-02-01",
//			"author":"王小柔",
//			"name":"十面包袱",
//			"read_url":"http:\/\/m.shupeng.com\/#\/read\/4330050531",
//			"thumb":"34ff550efd0965e12eb7de3481952b6d.jpg";


//	{"id":4818676,
//	"author":"柴静",
//	"bookid":4818676,
//	"nick":"看见（柴静个人成长告白书，中国社会十年变迁的备忘录）",
//	"no":0,
//	"name":"看见",
//	"channel":"eb",
//	"thumb":"374a05ddf95ae46eb30facf0d41f7ba4.jpg",
//	"url":"http:\/\/www.shupeng.com\/book\/4818676",
//	"intro":"《看见》是知名记者和主持人柴静讲述央视十年历程的自传性作品，...",
//	"mode":"all"}


}
