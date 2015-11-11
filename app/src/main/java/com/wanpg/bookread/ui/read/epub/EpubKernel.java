package com.wanpg.bookread.ui.read.epub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wanpg.bookread.BaseApplication;
import com.wanpg.bookread.utils.DomUtil;
import com.wanpg.bookread.utils.MD5Util;
import com.wanpg.bookread.utils.SparseArray;
import com.wanpg.bookread.utils.TextUtil;
import com.wanpg.bookread.utils.ZipUtil;

public class EpubKernel {

	// xml parser
	private DocumentBuilderFactory mDocumentBuilderFactory;
	private DocumentBuilder mDocumentBuilder;

	// epub基本目录(每个epub解压后的基本目录)
	private String epubBaseDir = "";

	public String getEpubBaseDir() {
		return epubBaseDir;
	}

	// opf
	private List<String> opfList;

	// metadata
	private Map<String,String> metadataMap;
	// manifestitme
	private Map<String,EpubManifestItem> manifestItemMap;
	// spine
	private SparseArray<String> spineMap;

	private String ncx;

	private static final String CONTAINER_FILE = "/META-INF/container.xml";
	private static final String CONTAINER_FILE_MEDIA_TYPE = "application/oebps-package+xml";


	private static EpubKernel THIS = new EpubKernel();

	private EpubKernel() {
		// TODO Auto-generated constructor stub
		try {
			init();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static EpubKernel getInstance(){
		return THIS;
	}

	public static EpubKernel newInstance(){
		THIS = new EpubKernel();
		return THIS;
	}

	// 初始化APP的epub工作目录
	private void init() throws ParserConfigurationException{
		if(mDocumentBuilderFactory==null){
			mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
		}
	}

	public void openEpubFile(String epubFilePath) throws IOException,SAXException,ParserConfigurationException{
		String tmp = getSavePath(epubFilePath);
		if(!epubBaseDir.equals(tmp)){
			epubBaseDir = tmp;
			unzipEpub(epubFilePath, epubBaseDir);
			parserEpubFile(epubBaseDir);
		}
	}

	// 解压epub到workDiectory目录�?workDiectory的命名规则为BOOKID)
	private void unzipEpub(String epubFilePath,String epubWorkDiectory){
		try {
			ZipUtil.unzipEpub(epubFilePath, epubWorkDiectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 解析解压后的epub目录
	private void parserEpubFile(String epubUnzipDiectory) throws IOException,SAXException{
		File file = new File(epubUnzipDiectory);
		if(!file.exists()){
			return;
		}
		parseContainer();
		parseOpfFile();
		parseNcxFile();
	}

	private String coverPath = "";
	public String getCoverPath(){
		if(!TextUtil.isEmpty(coverPath)){
			File f = new File(coverPath);
			if(f.exists()){
				return coverPath;
			}
			coverPath = "";
		}
		EpubManifestItem item = manifestItemMap.get("cover");
		if(item!=null)
			coverPath = epubBaseDir + "/" + item.href;
		return coverPath;
	}

	private void parseContainer() throws IOException,SAXException{
		String containerFile = this.epubBaseDir + CONTAINER_FILE;
		System.out.println("container file: "+containerFile);
		File file = new File(containerFile);
		FileInputStream fis = new FileInputStream(file);
		Document document = mDocumentBuilder.parse(fis);

		String fullPath = "";
		String mediaType = "";
		Element rootNode = (Element)document.getElementsByTagName("rootfiles").item(0);
		NodeList nodes = rootNode.getChildNodes();
		if(nodes.getLength() > 0){
			opfList = new ArrayList<String>();
			for (int i = 0; i < nodes.getLength(); i++) {
				fullPath = DomUtil.getElementAttr(document,"rootfile","full-path",i);
				mediaType = DomUtil.getElementAttr(document,"rootfile","media-type",i);
				if (mediaType.equals(CONTAINER_FILE_MEDIA_TYPE)){
					opfList.add(fullPath);
					break;
				}
			}
		}
	}

	private void parseOpfFile() throws IOException,SAXException{
		if(opfList == null || opfList.size() <= 0){
			return;
		}
		String opfFile = this.epubBaseDir + "/" + opfList.get(0);
		System.out.println("opfFile: "+opfFile);
		File file = new File(opfFile);
		InputStream is = new FileInputStream(file);
		Document document = mDocumentBuilder.parse(is);
		// 获取opf文件中metadata相关数据(图书基础信息)
		metadataMap = new HashMap<String,String>();
		metadataMap.put("title", DomUtil.getElementValue(document, "dc:title", 0));
		metadataMap.put("creator", DomUtil.getElementValue(document, "dc:creator", 0));
		metadataMap.put("description", DomUtil.getElementValue(document, "dc:description", 0));

		// 获取opf文件中manifest相关数据(资源清单)
		Element manifestNode = (Element)document.getElementsByTagName("manifest").item(0);
		NodeList nodes = manifestNode.getChildNodes();
		if(nodes.getLength() > 0){
			manifestItemMap = new HashMap<String,EpubManifestItem>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if ( node.getNodeType() == Node.ELEMENT_NODE ){
					Element elManifestItem = (Element)node;
					EpubManifestItem item = new EpubManifestItem();
					item.itemId = elManifestItem.getAttribute("id");
					item.href = elManifestItem.getAttribute("href");
					item.mediaType = elManifestItem.getAttribute("media_type");
					manifestItemMap.put(item.itemId, item);
				}
			}
		}
		// 获取opf文件中spine相关数据(阅读顺序)
		Element spineNode = (Element)document.getElementsByTagName("spine").item(0);
		// 获取spine中的toc属�?(之后就能根据该属性在manifestItemMap中获取href属�?对应的文件路�?
		ncx = spineNode.getAttribute("toc");

		NodeList spines = document.getElementsByTagName("itemref");
		if(spines.getLength() > 0){
			spineMap = new SparseArray<String>();
			for (int i = 0; i < spines.getLength(); i++) {
				String idref = DomUtil.getElementAttr(document, "itemref", "idref", i);
				spineMap.put(i, idref);
			}
		}
	}

	private ArrayList<EpubCatalog> mCatalogList;
	private void parseNcxFile() throws IOException,SAXException{
		EpubManifestItem item = manifestItemMap.get("ncx");
		String ncxHref = null;
		if(item!=null){
			ncxHref = item.href;
		}
		if(TextUtil.isEmpty(ncxHref)){

		}
		ncxHref = epubBaseDir + "/" + ncxHref;
		File file = new File(ncxHref);
		Document document = mDocumentBuilder.parse(new FileInputStream(file));
		NodeList list = document.getElementsByTagName("navPoint");
		if(mCatalogList==null)
			mCatalogList = new ArrayList<EpubKernel.EpubCatalog>();
		for(int i=0;i<list.getLength();i++){
			EpubCatalog catalog = new EpubCatalog(); 
			Element e = (Element) list.item(i);
			catalog.id = e.getAttribute("id");
			catalog.title = e.getElementsByTagName("text").item(0).getTextContent();
			if(catalog.title!=null && catalog.title.equals("")){
				EpubManifestItem item1 = manifestItemMap.get(catalog.id);
				item1.title = catalog.title;
			}
			Element e1 = (Element) e.getElementsByTagName("content").item(0);
			catalog.href = e1.getAttribute("src");
			mCatalogList.add(catalog);
		}
	}

	public ArrayList<EpubCatalog> getCatalogList(){
		return mCatalogList;
	}

	public SparseArray<String> getSpineMap() {
		return spineMap;
	}

	public String getTitle(){
		return metadataMap.get("title");
	}

	public String getAuthor(){
		return metadataMap.get("creator");
	}

	public String getDesc(){
		return metadataMap.get("description");
	}

	public String getHtmlUrlByIndex(int index){
		String pageIndex = spineMap.get(index);
		EpubManifestItem item = manifestItemMap.get(pageIndex);
		if(item!=null)
			return "file:///"+this.epubBaseDir + "/" + item.href;
		else
			return "";
	}

	public class EpubManifestItem{
		public String itemId;
		public String href;
		public String mediaType;
		public String title;
	}

	public class EpubCatalog{
		public String id;
		public String href;
		public String title;
	}

	private static String EPUB_PATH =  "";
	private static String getSavePath(String path){
		if(TextUtil.isEmpty(EPUB_PATH))
			EPUB_PATH = BaseApplication.getInstance().getExternalCacheDir().getPath() + "/epub/";
		return EPUB_PATH + MD5Util.string2MD5(path);
	}


	HashMap<String, EpubChapter> mChaptersMap = new HashMap<String, EpubKernel.EpubChapter>();
	/**
	 * 解析电子书epub
	 *
	 * @param path
	 * @return map0--"CHAPTERID"--strChapterId <br>
	 * map1--"CHAPTERTITLE"--strChapterTitle <br>
	 * map2--"CHAPTERCONTENT"--listContent <br>
	 * 返回null则表示解析失败
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public EpubChapter parseChapter(String chapterId) {
		//定义一本书
		EpubChapter chapter = mChaptersMap.get(chapterId);
		if(chapter!=null)
			return chapter;
		chapter = new EpubChapter();
		EpubManifestItem item = manifestItemMap.get(chapterId);
		if(item==null){
			return null;
		}
		String chapterPath = epubBaseDir + "/" + item.href;
		try {
			Document document = mDocumentBuilder.parse(new File(chapterPath));
			chapter.title = item.title;
			if(chapter.title==null || chapter.title.equals("")){
				NodeList titleList = document.getElementsByTagName("title");
				if(titleList!=null){
					chapter.title = titleList.item(0).getTextContent();
				}
			}
			Node body = (Element) document.getElementsByTagName("body").item(0);
			NodeList bodyList = body.getChildNodes();
			chapter.content = parseContent(bodyList);
			chapter.chapterId = chapterId;
			if(TextUtil.isEmpty(chapter.title) || chapter.title.equalsIgnoreCase("unknown")){
				chapter.title = getTitleFromNcx(chapterId);
			}
			mChaptersMap.put(chapterId, chapter);
			return chapter;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}

	private String getTitleFromNcx(String id) {
		// TODO Auto-generated method stub
		for(EpubCatalog ec : mCatalogList){
			if(ec.id.equals(id)){
				return ec.title;
			}
		}
		return "unknown";
	}

	/**
	 * 可递归调用的解析函数
	 *
	 * @param nodeList
	 * @param fullPath
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> parseContent(NodeList nodeList) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals("div")) {
				NodeList nodeListDiv = node.getChildNodes();
				//递归调用自己
				list.addAll(parseContent(nodeListDiv));
			} else if (node.getNodeName().equals("img")) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("type", "img");
				map.put("img", ((Element) node).getAttribute("src"));
				list.add(map);
			} else if (node.getNodeName().equals("image")){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("type", "img");
				map.put("img", ((Element) node).getAttribute("xlink:href"));
				list.add(map);
			} else if (node.getNodeName().equals("p")) {
				if (!node.getTextContent().equals("")) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("type", "text");
					map.put("text", node.getTextContent());
					list.add(map);
				}
			} else if (node.getNodeName().equals("h") || node.getNodeName().equals("h1") || node.getNodeName().equals("h2") || node.getNodeName().equals("h3")) {
				if (!node.getTextContent().equals("")) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("type", "text");
					map.put("text", node.getTextContent());
					list.add(map);
				}
			} else if (node.getNodeName().equals("ul")) {
				NodeList nodeListLi = ((Element) node).getElementsByTagName("li");
				list.addAll(parseContent(nodeListLi));
			} else if (node.getNodeName().equals("li")) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("type", "text");
				map.put("text", node.getTextContent());
				list.add(map);
			} else {
				ArrayList<HashMap<String, String>> listTmp = parseContent(node.getChildNodes());
				if(listTmp!=null && listTmp.size()>0){
					list.addAll(listTmp);
				}
			}
		}
		return list;
	}

	public class EpubChapter {
		public String chapterId;
		public String title;
		public ArrayList<HashMap<String, String>> content;
	}
}
