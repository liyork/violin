package com.wolf.test.security;

import com.wolf.utils.security.AESCryptor;
import com.wolf.utils.security.Key;
import com.wolf.utils.security.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 公司加密解密测试
 *
 * @author
 * @author
 * @since 2017年01月23日
 */
public class SecurityTest {
	//	md5(cid=xxx;q=xxx;uid=urlencode(xxx))

	public String encodeParam(String cid, String uid, String param) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("?cid=");
		sb.append(cid);
		try {
			sb.append("&uid=");
			sb.append(URLEncoder.encode(uid, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String encryptQ = AESCryptor.encrypt(param, Key.AES_KEY);
		sb.append("&q=").append(encryptQ);
		sb.append("&sign=").append(generateSign(cid, uid, encryptQ));
		return sb.toString();
	}

	public String generateSign(String cid, String uid, String json) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("cid=").append(cid);
		sb.append(";q=").append(json);
		sb.append(";uid=").append(URLEncoder.encode(uid, "utf-8"));
		sb.append(Key.AES_KEY);
		return MD5Utils.encode(sb.toString());
	}

	public static void main(String args[]) throws Exception {
		//前端对参数进行加密
		String cid = "800200";
		String uid = "c170703a-9037-4e41-8035-5d6250510f091457579631660";
		String params = "{\"id\":\"3\",\"taskType\":\"2\",\"takeType\":\"1\",\"status\":\"4\"}";
		SecurityTest securityTest = new SecurityTest();
		String encodedParam = securityTest.encodeParam(cid, uid, params);
		System.out.println("encodedParam：" + encodedParam);


		//服务器对返回值加密
		String encrypt = AESCryptor.encrypt(params, Key.AES_KEY);
		System.out.println(encrypt);
		encrypt = encrypt + ";uid=" + uid;
		//前端解密
		String res = AESCryptor.decrypt(encrypt, Key.AES_KEY);
		System.out.println("result：" + res);

	}
}



