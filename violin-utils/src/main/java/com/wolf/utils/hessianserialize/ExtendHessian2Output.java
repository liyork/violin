package com.wolf.utils.hessianserialize;

import com.caucho.hessian.io.Hessian2Output;

import java.io.IOException;
import java.io.OutputStream;

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
public class ExtendHessian2Output extends Hessian2Output {


	public ExtendHessian2Output(OutputStream os) {
		super(os);

	}

	@Override
	public void writeObject(Object object) throws IOException {

		if(object instanceof CustomJavaSerializer){
			try {
				byte[] res = ObjectUtils.getBytesFromObject(object);
				JavaSerialzerProxy proxy = new JavaSerialzerProxy();
				proxy.setBytes(res);
				super.writeObject(proxy);
				return ;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.writeObject(object);
	}


}
