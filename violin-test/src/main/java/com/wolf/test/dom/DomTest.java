package com.wolf.test.dom;

import com.alibaba.fastjson.JSON;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Description:
 * <br/> Created on 2016/9/19 10:51
 *
 * @author 李超()
 * @since 1.0.0
 */
public class DomTest {

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\applicationContext-test.xml");
		Element root = doc.getDocumentElement();    //取根节点
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element ele = (Element) node;
				System.out.println(JSON.toJSONString(ele));
			}
		}
	}
}
