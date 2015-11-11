package com.wanpg.bookread.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wanpg.bookread.common.Config;
import com.wanpg.bookread.data.ShelfBook;
import com.wanpg.bookread.ui.read.epub.EpubKernel;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.TextUtil;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * 一个用于文件解析的类
 * 目前支持文件类型：txt，epub
 *
 * @author wanpg
 */
public class FileParseHelper {

    /**
     * 解析txt文档，返回MappedByteBuffer
     *
     * @param path
     * @return
     */
    public static MappedByteBuffer parseTXTMapedByte(String path) {
       if(TextUtil.isEmpty(path))
    	   return null;
    	MappedByteBuffer mbBuf = null;
        File f = new File(path);
        long lLen = f.length();
        try {
            mbBuf = new RandomAccessFile(f, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
            return mbBuf;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("resource")
	public static String parseTxtPath(String path) {
        String type = FileUtil.getFileType(path);
        if (type.equals("rar")) {
            Archive a = null;
            FileOutputStream fos = null;
            try {
                a = new Archive(new File(path));
                List<FileHeader> listHeader = a.getFileHeaders();
                for (FileHeader fh : listHeader) {
                    if (FileUtil.checkFileIsTarget(fh.getFileNameW(), "txt")) {
                        File f = new File(Config.BOOK_SD_TEMP + "/" + fh.getFileNameW());
                        if (!f.getParentFile().exists()) {
                            f.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(f);
                        a.extractFile(fh, fos);
                        fos.flush();
                        return f.getPath();
                    }
                }
            } catch (RarException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } finally {
                try {

                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                    if (a != null) {
                        a.close();
                        a = null;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (type.equals("zip")) {
            ZipFile zf = null;
            InputStream ins = null;
            try {
                zf = new ZipFile(path);
                Enumeration<ZipEntry> zes = zf.getEntries();
                while (zes.hasMoreElements()) {
                    ZipEntry ze = zes.nextElement();
                    if (FileUtil.checkFileIsTarget(ze.getName(), "txt")) {
                        ins = zf.getInputStream(ze);
                        File f = new File(Config.BOOK_SD_TEMP + "/" + ze.getName());
                        FileUtil.copyStreamFile(ins, f);
                        return f.getPath();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (ins != null) {
                        ins.close();
                        ins = null;
                    }
                    if (zf != null) {
                        zf.close();
                        zf = null;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (type.equals("txt")) {
            return path;
        } else {
            return null;
        }
        return path;
    }

    /**
     * 解析txt文件，返回byte[]数组
     *
     * @param path
     * @return byte[]
     */
    public static byte[] parseTXT(String path) {
        byte[] bs = null;
        File f = new File(path);

        BufferedInputStream bufIn = null;
        try {
            bufIn = new BufferedInputStream(new FileInputStream(f));
            int len = bufIn.available();
            System.out.println(len);
            bs = new byte[len];
            bufIn.read(bs);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (bufIn != null) {
                    bufIn.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bs;
    }


    public static String parseEpubPath(String path) {
        String type = FileUtil.getFileType(path);
        String newPath = "";
        if (type.equals("rar")) {
            Archive a = null;
            FileOutputStream fos = null;
            try {
                a = new Archive(new File(path));
                List<FileHeader> listHeader = a.getFileHeaders();
                for (FileHeader fh : listHeader) {
                    if (FileUtil.checkFileIsTarget(fh.getFileNameW(), "txt")) {
                        File f = new File(Config.BOOK_SD_TEMP + "/" + fh.getFileNameW());
                        if (!f.getParentFile().exists()) {
                            f.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(f);
                        a.extractFile(fh, fos);
                        fos.flush();
                        newPath = f.getPath();
                        break;
                    }
                }
            } catch (RarException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } finally {
                try {

                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                    if (a != null) {
                        a.close();
                        a = null;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (type.equals("zip")) {
            ZipFile zf = null;
            InputStream ins = null;
            try {
                zf = new ZipFile(path);
                Enumeration<ZipEntry> zes = zf.getEntries();
                while (zes.hasMoreElements()) {
                    ZipEntry ze = zes.nextElement();
                    if (FileUtil.checkFileIsTarget(ze.getName(), "txt")) {
                        ins = zf.getInputStream(ze);
                        File f = new File(Config.BOOK_SD_TEMP + "/" + ze.getName());
                        FileUtil.copyStreamFile(ins, f);
                        newPath = f.getPath();
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            } finally {
                try {
                    if (ins != null) {
                        ins.close();
                        ins = null;
                    }
                    if (zf != null) {
                        zf.close();
                        zf = null;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (type.equals("epub")) {
            newPath = path;
        } else {
            newPath = "";
        }

        return newPath;
    }

//    /**
//     * 解析电子书epub
//     *
//     * @param path
//     * @return map0--"CHAPTERID"--strChapterId <br>
//     * map1--"CHAPTERTITLE"--strChapterTitle <br>
//     * map2--"CHAPTERCONTENT"--listContent <br>
//     * 返回null则表示解析失败
//     * @throws SAXException
//     * @throws IOException
//     * @throws ParserConfigurationException
//     */
//    public static List<Map<String, Object>> parseEPUB(String path) {
//        //定义一个返回的list
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        //定义一本书
//        EpubBook book = new EpubBook();
//
//        ZipFile zf = null;
//        book = parseEpubBook(path);
//        try {
//            /** 阅读顺序 **/
//            List<String> listReadList = book.getListReadList();
//
//            /** 章节内容 **/
//            Map<String, EpubBookItem> mapReadHref = book.getMapReadHref();
//
//            //打开指定路径的文件
//
//            DocumentBuilderFactory mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
//
//            zf = new ZipFile(path);
//
//            for (String hrefId : listReadList) {
//                EpubBookItem item = mapReadHref.get(hrefId);
//                List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
//                ZipEntry ze = zf.getEntry(book.getFullPath() + item.getHref());
//                //对相应的流转换为document
//                System.out.println(item.getHref());
//                Document docChapter = mDocumentBuilder.parse(zf.getInputStream(ze));
//                Element elementBody = (Element) docChapter.getElementsByTagName("body").item(0);
//                NodeList nodeList = elementBody.getChildNodes();
//                list1 = parseContent(nodeList, book.getFullPath());
//
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("CHAPTERID", item.getItemId());
//                map.put("CHAPTERTITLE", item.getItemName());
//                map.put("CHAPTERCONTENT", list1);
//                list.add(map);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            list = null;
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            // TODO Auto-generated catch block
//            list = null;
//            e.printStackTrace();
//        } catch (SAXException e) {
//            // TODO Auto-generated catch block
//            list = null;
//            e.printStackTrace();
//        } finally {
//            try {
//                if (zf != null) {
//                    zf.close();
//                    zf = null;
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }

//    public static EpubBook parseEpubBook(String path) {
//        //定义一本EpubReader
//        EpubReader epubReader = new EpubReader();
//
//        try {
//            epubReader.init();
//            return epubReader.readEpub(path);
//        } catch (ParserConfigurationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (SAXException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 可递归调用的解析函数
     *
     * @param nodeList
     * @param fullPath
     * @return
     */
    public static List<Map<String, String>> parseContent(NodeList nodeList, String fullPath) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("div")) {
                NodeList nodeListDiv = node.getChildNodes();
                //递归调用自己
                list.addAll(parseContent(nodeListDiv, fullPath));
            } else if (node.getNodeName().equals("img")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", "img");
                map.put("img", fullPath + ((Element) node).getAttribute("src"));
                list.add(map);
            } else if (node.getNodeName().equals("p")) {
                if (!node.getTextContent().equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", "text");
                    map.put("text", node.getTextContent());
                    list.add(map);
                }
            } else if (node.getNodeName().equals("h") || node.getNodeName().equals("h1") || node.getNodeName().equals("h2") || node.getNodeName().equals("h3")) {
                if (!node.getTextContent().equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", "text");
                    map.put("text", node.getTextContent());
                    list.add(map);
                }
            } else if (node.getNodeName().equals("ul")) {
                NodeList nodeListLi = ((Element) node).getElementsByTagName("li");
                list.addAll(parseContent(nodeListLi, fullPath));
            } else if (node.getNodeName().equals("li")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", "text");
                map.put("text", node.getTextContent());
                list.add(map);
            }
        }
        return list;
    }
    
    
    /**
     * 根据给定路径获得LocalShelfBookMode
     *
     * @param path
     * @return LocalShelfBookMode
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static ShelfBook getShelfBookByPath(String path) throws IOException, ParserConfigurationException, SAXException {

        File f = new File(path);

        if (f.length() < 5) {
            return null;
        }

        String bookName = f.getName();
        bookName = bookName.replaceAll("[\\t\\n\\r]", "");
        String type = FileUtil.getFileType(bookName);
        String coverPath = "";
        String author = "";
        if (type.equals(".epub")) {
            EpubKernel epubKernel = EpubKernel.getInstance();
            epubKernel.openEpubFile(path);
            bookName = epubKernel.getTitle();
//            coverPath = Config.BOOK_SD_COVER + "/" + bookName + ".cover";
//            coverPath = coverPath.replaceAll("[\\t\\n\\r]", "");
            coverPath = epubKernel.getCoverPath();
        } else {
            bookName = bookName.replace(type, "");
        }

        ShelfBook shelfBookMode = new ShelfBook();
        //此部分为公共
        shelfBookMode.readMode = ShelfBook.POS_LOCAL_SDCARD;
        shelfBookMode.bookName = bookName;
        shelfBookMode.posInShelf = -1;
        shelfBookMode.author = author;

        //此部分为本地书籍信息，
        shelfBookMode.bookPath = path;
        shelfBookMode.coverPath = coverPath;
        shelfBookMode.innerFileType = "";

        //此部分为网络书籍信息
        shelfBookMode.thumb = "";
        shelfBookMode.bookId = 0;
        shelfBookMode.url = "";
        shelfBookMode.chapterId = "";
        shelfBookMode.chapterUrl = "";

        return shelfBookMode;
    }
}
