package com.wolf.test.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.xx.baseModules.openAPI.core.manage.ServerNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/26
 * Time: 9:36
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class HessianTest {

	public static void main(String[] args) throws Exception {
		ServerNode serverNode = new ServerNode();
		serverNode.setName("xx");
		byte[] serialize = serialize(serverNode);
		Object deserialize = deserialize(serialize);
		System.out.println(deserialize);

	}

	public static byte[] serialize(Object obj) throws IOException {
		if(obj==null) throw new NullPointerException();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		ho.writeObject(obj);
		return os.toByteArray();
	}

	public static Object deserialize(byte[] by) throws IOException{
		if(by==null) throw new NullPointerException();

		ByteArrayInputStream is = new ByteArrayInputStream(by);
		HessianInput hi = new HessianInput(is);
		return hi.readObject();
	}
}
