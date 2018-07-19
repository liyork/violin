package com.wolf.utils;

import com.alibaba.fastjson.JSON;
import com.wolf.utils.log.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.ConnectionConfig.Builder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Description: 基于4.5.1的httpclient的实现的工具类
 * <br/> Created on 2016-12-23 10:45
 *
 * @author 李超()
 * @since 1.0.0
 */
public class HttpClientUtil {
	private static final String CHARSET_UTF8 = "UTF-8";
	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount > 3) {
				LogUtils.error("HTTP请求重发超过3次，不再重试！");
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				LogUtils.error("HTTP请求第" + executionCount + "次，没有响应数据，将重试！");
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				LogUtils.error("HTTP请求时SSL握手失败，不再是可用的连接，不再重试！");
				return false;
			}
			HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
			//声明为幂等的接口会认为外部调用失败是常态, 并且失败之后必然会有重试.
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {//get，put，delete会重试，post不会重试
				LogUtils.error("HTTP请求失败，将重试！");
				return true;
			} else {
				LogUtils.error("HTTP post（非幂等性）请求失败，不再重试！");
				return false;
			}
		}
	};
	/**
	 * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
	 */
	private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		// 自定义响应处理
		public String handleResponse(HttpResponse response) throws IOException {
//			int statusCode = response.getStatusLine().getStatusCode();//200
			HttpEntity entity = response.getEntity();

			if (entity != null && entity.getContentLength() > 0) {
				ContentType contentType = ContentType.get(entity);

				String charset = CHARSET_UTF8;
				if (null != contentType) {
					charset = contentType.getCharset() == null ? CHARSET_UTF8 : contentType.getCharset().displayName();
				}
				return new String(EntityUtils.toByteArray(entity), charset);
			} else {
				return null;
			}
		}
	};


	//======================================get start============================================================

	/**
	 * Get方式提交,URL中包含查询参数, 格式：http://www.baidu.com?search=p&name=s.....
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		try {
			return get(url, null, null);
		} catch (IOException e) {
			LogUtils.error("HTTP的Get方式请求异常！url=" + url, e);
		}
		return "";
	}

	/**
	 * Get方式提交
	 *
	 * @param url
	 * @param params key value
	 * @return
	 */
	public static String get(String url, Map<String, String> params) {
		try {
			return get(url, params, null);
		} catch (IOException e) {
			LogUtils.error("HTTP的Get方式请求异常！url=" + url + " params=" + JSON.toJSONString(params), e);
		}
		return "";
	}

	/**
	 * Get方式提交
	 *
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> params, String charset) throws IOException {

		if (StringUtils.isEmpty(url)) {
			return "";
		}

		String newCharset = getCharset(charset);

		List<BasicNameValuePair> basicNameValuePairs = convertParams(params);
		if (!CollectionUtils.isEmpty(basicNameValuePairs)) {
			String formatParams = URLEncodedUtils.format(basicNameValuePairs, newCharset);
			url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) :
					(url.substring(0, url.indexOf("?") + 1) + formatParams);
		}
		HttpClient httpClient = getHttpClient(newCharset);
		HttpGet httpGet = new HttpGet(url);
		String responseStr;
		try {
			httpGet.setConfig(getRequestConfig());
			responseStr = httpClient.execute(httpGet, responseHandler);
		} catch (Exception e) {
			throw new IOException("HTTP的Get方式请求异常！url=" + url + " params=" + JSON.toJSONString(params) +
					" charset=" + charset, e);
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return responseStr;
	}

	//======================================get end============================================================

	//======================================post start============================================================

	/**
	 * Post方式提交
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params, null, null);
	}

	/**
	 * Post方式提交
	 *
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String post(String url, Map<String, String> params, Header header) {
		return post(url, params, header, null);
	}

	/**
	 * Post方式提交
	 *
	 * @param url
	 * @param params a=1&b=2
	 * @param header
	 * @return
	 */
	public static String post(String url, String params, Header header) {
		return post(url, params, header, null);
	}

	/**
	 * Post方式提交
	 *
	 * @param url
	 * @param params
	 * @param charset
	 * @param header
	 * @return
	 */
	public static String post(String url, Map<String, String> params, Header header, String charset) {

		UrlEncodedFormEntity formEntity;
		try {
			formEntity = new UrlEncodedFormEntity(convertParams(params), getCharset(charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("HTTP的POST方式请求异常！url=" + url + " params=" + JSON.toJSONString(params) +
					" charset=" + charset + " header=" + JSON.toJSONString(header), e);
		}
		return internalPost(url, header, charset, formEntity);
	}

	/**
	 * Post方式提交
	 *
	 * @param url
	 * @param params  a=1&b=2
	 * @param charset
	 * @param header
	 * @return
	 */
	public static String post(String url, String params, Header header, String charset) {

		ContentType contentType = ContentType.create("application/x-www-form-urlencoded", getCharset(charset));
		//tomcat不能处理下面方式的参数，在服务器中需要使用下面方法getRequestPayload获取IpUtils
//		ContentType contentType = ContentType.create("text/plain", getCharset(charset));
		StringEntity stringEntity = new StringEntity(params, contentType);
		return internalPost(url, header, charset, stringEntity);
	}

	public static String post(String url, Header header, String charset, Map<String, ContentBody> contentBodies) {

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		multipartEntityBuilder.setCharset(Charset.forName(charset));
		for (Entry<String, ContentBody> entry : contentBodies.entrySet()) {
			multipartEntityBuilder.addPart(entry.getKey(), entry.getValue());
		}
		HttpEntity htpEntity = multipartEntityBuilder.build();
		return internalPost(url, header, charset, htpEntity);
	}

	private static String internalPost(String url, Header header, String charset, HttpEntity httpEntity) {

		if (StringUtils.isEmpty(url)) {
			return "";
		}

		HttpClient httpClient = getHttpClient(charset);

		HttpPost httpPost = null;
		String responseStr;
		try {
			httpPost = new HttpPost(url);
			httpPost.setEntity(httpEntity);
			httpPost.addHeader(header);
			httpPost.setConfig(getRequestConfig());
			responseStr = httpClient.execute(httpPost, responseHandler);
		} catch (Exception e) {
			throw new RuntimeException("HTTP的POST方式请求异常！url=" + url +
					" charset=" + charset + " header=" + JSON.toJSONString(header), e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return responseStr;
	}

	//======================================post end============================================================

	private static HttpClient getHttpClient(final String charset) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setRetryHandler(requestRetryHandler);
		Builder connectionConfigBuilder = ConnectionConfig.custom();
		connectionConfigBuilder.setCharset(Charset.forName(getCharset(charset)));
		connectionConfigBuilder.build();
		httpClientBuilder.setDefaultConnectionConfig(ConnectionConfig.custom().build());
		return httpClientBuilder.build();
	}

	/**
	 * connectionRequestTimeout  从连接池获取连接的timeout
	 * connectionTimeout  和服务器建立连接的timeout
	 * socketTimeout  从服务器读取数据的timeout
	 *
	 * @return
	 */
	private static RequestConfig getRequestConfig() {
		return RequestConfig.custom()
				.setConnectionRequestTimeout(1000)
				.setConnectTimeout(5000)
				.setSocketTimeout(5000)
				.build();
	}

	private static List<BasicNameValuePair> convertParams(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			params.add(new BasicNameValuePair(k, v));
		}
		return params;
	}


	private static String getCharset(String charset) {
		charset = (charset == null ? CHARSET_UTF8 : charset);
		return charset;
	}


	//对于使用text/plain提交的数据，只能在服务器这样使用获取参数
	private String getRequestPayload(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}