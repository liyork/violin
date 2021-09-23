package com.wolf.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Created on 2021/9/2 10:25 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class TestA {
    private static final Logger logger = LoggerFactory.getLogger(TestA.class);

    private Map<String, Set<Integer>> getCategoryApiId(Set<Integer> oldAuthSet, Set<Integer> newAuthSet) {
        Map<String, Set<Integer>> categoryApiId = new HashMap();
        if (oldAuthSet != null && newAuthSet != null) {
            //首先判断两个集合是否一致
            if (oldAuthSet.size() == newAuthSet.size()) {
                Set<Integer> tempSet = new HashSet<Integer>();
                tempSet.addAll(oldAuthSet);
                tempSet.addAll(newAuthSet);
                if (tempSet.size() == oldAuthSet.size()) {  // 没变
                    categoryApiId = null;//未增加api个数
                    logger.info("未新增和删除api");
                } else {
                    //1:交集；2:a-b的差集;3:并集
                    Set<Integer> apiSet1 = getApiSet(oldAuthSet, newAuthSet, 1);
                    Set<Integer> deleteAuthSet = getApiSet(oldAuthSet, apiSet1, 2);
                    Set<Integer> insertAuthSet = getApiSet(newAuthSet, oldAuthSet, 2);//获取新增的apiId
                    logger.info("删除的api：" + deleteAuthSet);
                    logger.info("新增的api：" + insertAuthSet);
                    categoryApiId.put("insertApi", insertAuthSet);
                    categoryApiId.put("datele", deleteAuthSet);
                }
            } else {
                //1:交集；2:a-b的差集；3:b-a的差集；4:并集
                Set<Integer> apiSet1 = getApiSet(oldAuthSet, newAuthSet, 1);
                Set<Integer> deleteAuthSet = getApiSet(oldAuthSet, apiSet1, 2);//获取删除的apiId
                Set<Integer> insertAuthSet = getApiSet(newAuthSet, oldAuthSet, 2);//获取新增的apiId
                logger.info("删除的api：" + deleteAuthSet);
                logger.info("新增的api：" + insertAuthSet);
                categoryApiId.put("insertApi", insertAuthSet);
                categoryApiId.put("datele", deleteAuthSet);
            }
        } else {
            if (oldAuthSet == null && newAuthSet != null) {    //全是新增api
                logger.info("首次新增授权api");
                categoryApiId.put("insertApi", newAuthSet);
            } else if (oldAuthSet != null && newAuthSet == null) {  //未修改用户授权api的数据
                logger.info("未增加新授权的api");
                categoryApiId.put("datele", oldAuthSet);
            } else if (oldAuthSet == null && newAuthSet == null) {
                logger.info("已授权api和新增授权api都为空");
                categoryApiId = null;
            }
        }
        return categoryApiId;
    }

    private Set<Integer> getApiSet(Set<Integer> oldAuthSet, Set<Integer> newAuthSet, int flag) {
        Set<Integer> result = new HashSet<Integer>();
        if (flag == 1) { //求交集
            result.clear();
            result.addAll(oldAuthSet);
            result.retainAll(newAuthSet);
        } else if (flag == 2) { //求差集
            result.clear();
            result.addAll(oldAuthSet);
            result.removeAll(newAuthSet);
        } else if (flag == 3) { //求并集
            result.clear();
            result.addAll(oldAuthSet);
            result.addAll(newAuthSet);
        }
        return result;
    }
}
