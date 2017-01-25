package com.wolf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wolf.utils.log.LogUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/29
 * Time: 16:09
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class AutoNavigationUtils {

	public static String requestAmap(String lon, String lat) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("output", "json");
		params.put("location", lon + "," + lat);
		params.put("key", "高德指定key---374649efc8c2ec7d3a757d9bef8de969");
		params.put("radius", "0");//搜索半径
		params.put("extensions", "all");
		if (StringUtils.isEmpty(lon) || StringUtils.isEmpty(lat)) {
			throw new IllegalArgumentException("当前位置无法获取坐标");
		}
		String AMAP_PREFIX = "http://restapi.amap.com/v3";
		//todo 稍后等HttpclientUtil稳定后靠过来
//		String amapStr = HttpclientUtil.get(AMAP_PREFIX + GEOCODE, params);
		return null;
	}


	public static void getAmapRes(String lon, String lat) {

		JSONObject amapJson = JSON.parseObject(requestAmap(lon, lat));
		Integer status = Integer.valueOf(String.valueOf(amapJson.get("status")));
		if (status == 1) {
			JSONObject regeocode = amapJson.getJSONObject("regeocode");
			JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
			//当所在城市为四个直辖市时，该字段返回为空
			String city = addressComponent.getString("city");
			if (StringUtils.isEmpty(city) || city.equals("[]")) {
				city = addressComponent.getString("province");
			}
			//坐标点所在区（县级市（"义乌","昆山","晋江"））
			String district = addressComponent.getString("district");
			if (StringUtils.isEmpty(district) || district.equals("[]")) {
				district = "";
			}

			System.out.println(city + ":" + district);

			if (StringUtils.isEmpty(city)) {
				LogUtils.info("根据经纬度获取城市名称失败！,错误码为" + amapJson.toJSONString() + ",经纬度为" + lon + "," + lat);
			}
		} else {
			LogUtils.info("根据经纬度获取城市名称失败！,错误码为" + amapJson.toJSONString() + ",经纬度为" + lon + "," + lat);
		}

	}
}
