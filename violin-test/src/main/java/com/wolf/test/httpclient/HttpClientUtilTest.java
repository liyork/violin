package com.wolf.test.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.wolf.utils.HttpClientUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Description: 模拟客户端打开浏览器输入地址访问服务器
 * <p/>
 * Date: 2015/6/24
 * Time: 16:53
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class HttpClientUtilTest {

	public static void main(String[] args) throws Exception {
//		invokeServerByURI();
//		invokeServerByHttpClient();
	}

	@Test
	public void testGet() throws IOException {
		//null test
//		String result = HttpClientUtil.get("http://localhost:8080/webtest/myFirstServlet");
//		System.out.println("result:"+result);

		//test entity.content
//		Map<String, String> params1 = new HashMap<>();
//		params1.put("username", "abc");
//		params1.put("type", "6");
//		String resultRight = HttpClientUtil.get("http://localhost:8080/webtest/myFirstServlet", params1);
//		System.out.println("resultRight:"+resultRight);

		//test normal
//		Map<String, String> params2 = new HashMap<>();
//		params2.put("username", "abc");
//		String resultRight2 = HttpClientUtil.get("http://localhost:8080/webtest/myFirstServlet", params2);
//		System.out.println("resultRight:"+resultRight2);

		//test timeout
		Map<String, String> params3 = new HashMap<>();
		params3.put("username", "abc");
		params3.put("type", "7");
		String resultRight3 = HttpClientUtil.get("http://localhost:8080/webtest/myFirstServlet", params3);
		System.out.println("resultRight3:"+resultRight3);
	}

	@Test
	public void testPost() throws IOException {
//		Map<String, String> params1 = new HashMap<>();
//		params1.put("appVersion", "800330");
//		String result1 = HttpClientUtil.post("http://localhost:8088/xxapp/resource/d/xx/base/config", params1);
//		System.out.println(result1);

//		Map<String, String> params2 = new HashMap<>();
//		params2.put("appVersion", "800330");
//		Header header = new BasicHeader("headxx", "abc");
//		String result2 = HttpClientUtil.post("http://localhost:8088/xxapp/resource/d/xx/base/config", params2, header);
//		System.out.println(result2);

//		Header header = new BasicHeader("headxx", "abc");
//		String result2 = HttpClientUtil.post("http://localhost:8088/xxapp/resource/d/xx/base/config", "appVersion=800330", header);
//		System.out.println(result2);

//		Map<String, String> params2 = new HashMap<>();
//		params2.put("appVersion", "800330");
//		Header header = new BasicHeader("headxx", "abc");
//		String result2 = HttpClientUtil.post("http://localhost:8088/xxapp/resource/d/xx/base/config", params2, header,"utf-8");
//		System.out.println(result2);

//		Header header = new BasicHeader("headxx", "abc");
//		String result2 = HttpClientUtil.post("http://localhost:8088/xxapp/resource/d/xx/base/config", "appVersion=800330", header, "utf-8");
//		System.out.println(result2);

		Header header = new BasicHeader("headxx", "abc");
		File file = new File("E:\\baidu.jpg");
		FileBody bin = new FileBody(file);
		StringBody comment = new StringBody("9", ContentType.TEXT_PLAIN);
		StringBody comment1 = new StringBody("999100", ContentType.TEXT_PLAIN);
		Map<String, ContentBody> map = new HashMap<>();
		map.put("bin", bin);
		map.put("uploadPictureTye", comment);
		map.put("cid", comment1);
		String result2 = HttpClientUtil.post("http://localhost:8080/xxulapi/resource/d/xx/intrDriver/uploadPicture",
				header, "utf-8", map);
		System.out.println(result2);

	}

	//HttpClient是增强版的HttpURLConnection,包含所有功能，另提供了很多高级功能session,cookie等
	// (应用先调用登录，然后使用同一个HttpClient， HttpClient会自动维护与服务器之间的Session状态)
	private static void invokeServerByHttpClient() throws IOException {
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {

			//对于get和post都可使用传递参数
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter("c", "3");
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000); //设置连接超时为5秒
			HttpConnectionParams.setSoTimeout(httpParams, 5000);

			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost("http://localhost:8088/xxapp/resource/d/xx/base/config");
			httpPost.setParams(httpParams);


			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("a", "1"));
			params.add(new BasicNameValuePair("b", "2"));
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
			httpPost.setEntity(urlEncodedFormEntity);

			HttpResponse response = client.execute(httpPost);
			// 判断网络连接是否成功
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("网络错误异常！!!!");
				return;
			}

			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			reader = new BufferedReader(
					new InputStreamReader(inputStream, "utf-8"));
			String finalStr = "";
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0)
					finalStr = finalStr + line;
			}

			System.out.println(JSONObject.parseObject(finalStr));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放资源
			inputStream.close();
			reader.close();
		}
	}

	private static void invokeServerByURI() throws IOException {
		String requestMethod = "POST";//GET或POST

		URL url;
		if (requestMethod.equals("POST")) {
			//		调用指定url的api获得结果
			url = new URL("http://127.0.0.1:8080/xxxx/resource/m/xx/base/servercitylist");
		} else {
			url = new URL("http://127.0.0.1:8080/xxxx/resource/m/xx/base/servercitylist?a=1&b=2");
		}

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,text/plain,*/*;");
		conn.setRequestProperty("User-Agent", "remote-xx-java");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(60000);

		conn.setRequestMethod(requestMethod);

		//post方式写入参数
		if (requestMethod.equals("POST")) {
			conn.setRequestMethod("POST");
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write("a=1&b=2".getBytes());
			outputStream.close();
		}


		BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "utf-8"));
		String finalStr = "";
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.length() > 0)
				finalStr = finalStr + line;
		}


		System.out.println(JSONObject.parseObject(finalStr));
	}


}
