package com.wolf.test.redis;

import com.wolf.utils.RedisLockUtils;

/**
 * Description:
 * <br/> Created on 2016/8/24 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RedisLockUtilTest {

	public static void main(String[] args) {


		try {
			boolean isLock = RedisLockUtils.lock("xx", 20);
			if (!isLock) {
				System.out.println("获锁失败");
				return;
			}
			
			System.out.println("正常流程");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisLockUtils.unLock("xx");
		}
	}
}
