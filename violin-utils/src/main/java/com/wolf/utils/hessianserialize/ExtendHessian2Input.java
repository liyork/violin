package com.wolf.utils.hessianserialize;

import com.caucho.hessian.io.Hessian2Input;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/26
 * Time: 9:17
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ExtendHessian2Input extends Hessian2Input {

	public ExtendHessian2Input(InputStream is) {
		super(is);

	}

	@Override
	public Object readObject() throws IOException {

		return javaSerializer(super.readObject());

	}

	@Override
	public Object readReply(@SuppressWarnings("rawtypes") Class expectedClass) throws IOException {

		try {
			return javaSerializer(super.readReply(expectedClass));
		} catch (Throwable e) {

			throw new IOException(e);
		}
	}

	@Override
	public Object readObject(@SuppressWarnings("rawtypes") Class cl) throws IOException {

		return javaSerializer(super.readObject(cl));
	}

	private Object javaSerializer(Object result){

		if(result != null && result instanceof JavaSerialzerProxy){
			byte [] bytes = ((JavaSerialzerProxy)result).getBytes();
			try {
				Object obj = ObjectUtils.getObjectFromBytes(bytes);
				return obj ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}