package com.wolf.utils;

import com.wolf.utils.log.LogUtils;
import redis.clients.jedis.Jedis;

/**
 * Description:
 * <br/> Created on 2016/8/24 9:12
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RedisLockUtils {

	static Jedis jedis = new Jedis("127.0.0.1");

	public static boolean lock(String key,  long timeout) {
		boolean flag = false;
		try {

			long nowTime = System.currentTimeMillis();
			String expire = nowTime + timeout+"";
			//这里稍后需要加锁，不是原子的
			flag = jedis.setnx( key, expire) == 1;
			if (!flag) {//没成功
				String oldTime = jedis.get(key);
				//刚巧在redis准备清除过期数据的临界点，值为空,或者超期时获取值未被清除
				if (oldTime == null || Long.parseLong(oldTime) < nowTime) {
					String temp = jedis.getSet(key, expire);
					//都为null，或者一直未被清除,上锁成功，
					//否则上锁失败，但是redis内数据一直有直到下次使用同样key上锁
					//todo 还需再考虑
					if (isEquals(temp, oldTime)) {
						flag = true;
					}
				}
			}
			if (flag) {
				jedis.expire( key, (int)timeout);
			}
		} catch (Exception e) {
			LogUtils.info("lock error key = " + key + ", timeout = " + timeout);
		}
		return flag;
	}


	/**
	 * 判断数值o1 和数值o2是否相等
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean isEquals(String o1, String o2) {
		if (o1 == o2) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}

	public static void unLock(String key) {
		jedis.del(key);
	}
}
