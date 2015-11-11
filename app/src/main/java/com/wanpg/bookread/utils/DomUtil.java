package com.wanpg.bookread.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DomUtil {
	protected Document document;
	protected Element rootElement;

	public DomUtil() {
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			rootElement = document.createElement("root");
			document.appendChild(rootElement);
		} catch (ParserConfigurationException e) {
			System.out.println("Can not create the Document object!");
			e.printStackTrace();
		}
	}

	/**
	 * 在root节点下 创建节点
	 * @param tagName 节点名次
	 * @return
	 */
	public Element element(String tagName){
		return element(rootElement, tagName);
	}
	
	/**
	 * 创建节点
	 * @param node 目标节点
	 * @param tagName 节点名称
	 * @return
	 */
	public Element element(Node node, String tagName) {
		Element el = document.createElement(tagName);
		node.appendChild(el);
		return el;
	}

	/**
	 * 在root节点下 创建节点
	 * 
	 * @param tagName 节点名称
	 * @param value 节点值
	 * @return 当前创建的节点
	 */
	public Element element(String tagName, Object value) {
		return this.element(rootElement, tagName, value);
	}

	/**
	 * 创建节点
	 * 
	 * @param tagName
	 * @param value
	 * @param node
	 * @return 当前创建的节点
	 */
	public Element element(Node node, String tagName, Object value) {
		Element el = document.createElement(tagName);
		node.appendChild(el);
		el.appendChild(document.createTextNode(String.valueOf(value)));
		return el;
	}
	
	/**
	 * 返回文档对象
	 * 
	 * @return
	 */
	public Node getNode() {
		return document;
	}

	@Override
	public String toString() {
		if(document == null)
			return null;
		StringWriter stringWriter = new StringWriter();
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer=factory.newTransformer();
			transformer.transform(new DOMSource(document),new StreamResult(stringWriter));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
	

	public static String getElementValue(Document doc, String element, int index) {
		try {
			return doc.getElementsByTagName(element).item(index).getFirstChild().getNodeValue().trim();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getElementValue(Document doc, String element, int index,String nullDefault) {
		try {
			String rst=doc.getElementsByTagName(element).item(index).getFirstChild().getNodeValue().trim();
			if("".equals(rst) || rst == null){
				rst=nullDefault;
			}
			return rst;
		} catch (Exception e) {
			return "";
		}
	}	
	
	public static String getElementAttr(Document doc, String element,String attr) {
		try {
			Element el=(Element)doc.getElementsByTagName(element).item(0);
			return el.getAttribute(attr);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getElementAttr(Document doc, String element,String attr, int index) {
		try {
			Element el=(Element)doc.getElementsByTagName(element).item(index);
			return el.getAttribute(attr);
		} catch (Exception e) {
			return "";
		}
	}		
	
	public static byte[] deflaterData(byte[] data) throws IOException{
		byte[] buffer = null;
		ByteArrayOutputStream baos = null;
		DeflaterOutputStream dos = null;
		BufferedOutputStream bos = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_COMPRESSION, false));
			bos = new BufferedOutputStream(dos);
			bos.write(data, 0, data.length);
			bos.close();
			dos.finish();
			buffer = baos.toByteArray();
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} finally {
			if (baos !=null) baos.close();
			if (dos != null) dos.close();
			if (bos != null) bos.close();
			
		}
		
		return buffer;
	}
}