package com.wolf.utils.hessianserialize;

import com.caucho.hessian.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/26
 * Time: 8:31
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public final class HessianSerializerUtils {


	private static SerializerFactory serializerFactory = new SerializerFactory();
	private static final Logger LOG = LoggerFactory.getLogger(HessianSerializerUtils.class);

	private HessianSerializerUtils(){

	}
	public static byte[] serialize(Object obj) {

		ByteArrayOutputStream ops = new ByteArrayOutputStream();
		AbstractHessianOutput out = new ExtendHessian2Output(ops);

		out.setSerializerFactory(serializerFactory);
		try {
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			LOG.error("hessian序列化失败", e);
			throw new RuntimeException("hessian序列化失败",e);
		}

		byte[] bytes = ops.toByteArray();
		return bytes;
	}
	public static Object deserialize(byte[] bytes) {
		ByteArrayInputStream ips = new ByteArrayInputStream(bytes);
		AbstractHessianInput in = new ExtendHessian2Input(ips);

		in.setSerializerFactory(serializerFactory);
		Object value = null;
		try {
			value = in.readObject();
			in.close();
		} catch (IOException e) {
			LOG.error("hessian反序列化失败", e);
			throw new RuntimeException("hessian反序列化失败",e);
		}

		return value != null ? value : bytes;
	}

}
