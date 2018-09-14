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
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
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
import java.util.concurrent.TimeUnit;

/**
 * Description: 基于4.5.1的httpclient的实现的工具类
 *
 * 若是不需要keepalive，那么需要每次关闭连接。
 *
 * 如果采用客户端关闭链接的方法，在客户端的机器上使用netstat –an命令会看到很多TIME_WAIT的TCP链接。
 * 如果服务器端主动关闭链接这中情况就出现在服务器端。
 * TIME_WAIT的状态会出现在主动关闭链接的这一端。TCP协议中TIME_WAIT状态主要是为了保证数据的完整传输。
 *
 * 强调一下使用上面这些方法关闭链接是在我们的应用中明确知道不需要重用链接时可以主动关闭链接来释放资源。
 * 如果你的应用是需要重用链接的话就没必要这么做，使用原有的链接还可以提供性能。
 *
 * HTTP KeepAlive是就是通常所称的长连接。KeepAlive即服务器端为同一客户端保持连接一段时间（不立即关闭），
 * 以便于更多来自于此客户端的后续请求不断的利用此连接直至连接超时。
 * KeepAlive只是表明了服务器端面对连接的一种优化策略，而客户端也完全可以主动关闭之（不利用）。
 * KeepAlive带来的好处是可以减少HTTP连接的开销，提高性能。比如，同一页面中如有很多内嵌的图片、JS、CSS等请求，则可以利用此特新性，
 * 使用少量的连接数（IE下一般是2个）更快的下载下来，使得网页更快的展示出来。
 * 坏处是：
 * 如果有大量不同的客户端同时（或瞬间）请求服务器端，且每一个客户端的都长期占用连接（比如：不关闭且
 * ConnectionTimeOut设置过长）或服务器端也不快速失效连接（KeepAliveTimeout参数设置过大）的话，
 * 可能会快速占满服务器连接资源，导致更多的请求被排队或被拒绝或服务器down掉。
 *
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
		httpGet.setHeader("Connection", "close");//服务器返回数据后由服务器关闭tcp，
		String responseStr;
		try {
			httpGet.setConfig(getRequestConfig());
			responseStr = httpClient.execute(httpGet, responseHandler);
		} catch (Exception e) {
			throw new IOException("HTTP的Get方式请求异常！url=" + url + " params=" + JSON.toJSONString(params) +
					" charset=" + charset, e);
		} finally {
			if (httpGet != null) {
				//释放到连接池，并没有关闭连接，超时后才关闭。
				httpGet.releaseConnection();
			}
			//todo 如何真正设定从客户端关闭连接呢？需要吗？
//			httpClient.getConnectionManager().releaseConnection();
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
				//这个仅仅是重用，不是断开连接。
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
		BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		httpClientBuilder.setConnectionManager(connManager);
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