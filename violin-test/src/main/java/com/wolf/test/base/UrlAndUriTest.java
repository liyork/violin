package com.wolf.test.base;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p> Description:uri和url区别
 * uri:uniform resource identifier,统一资源标示符，只是标示，不能定位，是url和urn的抽象
 * 可以对路径进行解析，可以对路径进行编码和解码符合RFC 2396.规范，
 * 对于HTML FORM的url encode/decode可以使用java.net.URLEncoder和java.net.URLDecoder来完成，但是对URL对象不适用,
 * 可以表示相对或是绝对路径只要它符合URI的语法规则
 * url:uniform resource locator,统一资源定位，能标示，能定位到某个资源，是具体的，不可编码，
 * 绝对路径,schema必须被指定。
 * urn:uniform resource names,统一资源命名,仅表示统一的资源名称，如:mailto:java-net@java.sun.com
 * URL和URN都是一种URI,这里的绝对(absolute)是指包含scheme，而相对(relative)则不包含scheme。
 * URI抽象结构    [scheme:]scheme-specific-part[#fragment]
 * [scheme:][//authority][path][?query][#fragment]
 * authority为[user-info@]host[:port]
 * <p/>
 * Date: 2015/7/24
 * Time: 11:18
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class UrlAndUriTest {

	public static void main(String[] args) throws Exception {

		URL url = new URL("http://xxx.yyyy.com:9080/cartp;remoteClientMark=true;jsessionid=null/remoteHessian.do_?serviceId=xx&remote_client_projectName=xx&remote_client_grayValue=null");
		URLConnection urlConnection = url.openConnection();
//		try
//		{

//
//			InputStream inputStream = urlConnection.getInputStream();
//
//			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//			Object o = objectInputStream.readObject();
//			System.out.println(o);
//		}
//		catch (Exception urle)
//		{
//			urle.printStackTrace();
//		}

//		HessianConnection hessianConnection = new HessianURLConnection(url,urlConnection);
//		simpleTest();
	}

	@Test
	public void simpleTest() throws MalformedURLException {
		URL url1 = new URL("file:\\E:\\x.txt");
		String file = url1.getFile();
		File f = new File(file);
		System.out.println(f.exists());
		String urlString = "http://xx.xx.xx.xx:8080/swp/mainPage?aa=11&bb%3D22";
		//只负责解析各部分
		URI uri = URI.create(urlString);
		System.out.println("uri.getPath()==>"+uri.getPath());
		System.out.println("uri.getQuery()==>"+uri.getQuery());//解码  aa=11&bb=22
		URL url2 = uri.toURL();
		System.out.println("uri==>"+uri);
		System.out.println("url2==>"+url2);
		URL url = new URL(urlString);
		System.out.println("url.getPath()==>"+url.getPath());
		System.out.println("url.getQuery()==>"+url.getQuery());//不解码  aa=11&bb%3D22
	}
}
