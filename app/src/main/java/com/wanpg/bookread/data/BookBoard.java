package com.wanpg.bookread.data;

import java.io.Serializable;

/**
 * 书朋网的专辑信息
 *
 * @author wanpg
 */
public class BookBoard implements Serializable {

//	"id":226,
//	"count":21,
//	"thumb":"1361a176050632845d5efbd553b3ec40.jpg",
//	"intro":"书荒补给站：2013年1-3月出版新书",
//	"banner":"http:\/\/a.cdn123.net\/img\/r\/1087ff9d5f9fae835c586c3b0afc1051.jpeg",
//	"name":"书荒补给站：1-3月出版新书"


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 专辑id *
     */
    public int id;
    /**
     * 专辑内的书的数量 *
     */
    public int count;
    /**
     * 专辑的第一本书的封面图片地址 *
     */
    public String thumb;
    /**
     * 专辑简介 *
     */
    public String intro;
    /**
     * 专辑的banner图片地址 *
     */
    public String banner;
    /**
     * 专辑名 *
     */
    public String name;
}
