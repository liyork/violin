package com.wolf.utils.redis;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

 * 高可用配置
 */
public class HighAvailConfig {

    private static final String REGISTER_KEY_SUFFIX = ".redis.ha.config";
//    private static final String HA_CONFIG_PREFIX = (GlobalMessage.getProjectName()==null?GlobalMessage.getProjectNameNew():GlobalMessage.getProjectName()) + REGISTER_KEY_SUFFIX;
    private static final String SPEAR = ";";
    private static final String DATA_SPEAR = "=";
    /**
     * 从配置中心加载admin高可用监控
     * @return
     */
    public static Properties loadHaConfigFromCenter(boolean isInit) {
        //todo-my 先注释掉
//        ConfCenterApi confCenterApi = ConfigCenterUtil.getConfCenterApi();
//        if (confCenterApi == null) {
//            return null;
//        }
//        ClientDataSource configDatasource = confCenterApi.getDataSourceByKey(HA_CONFIG_PREFIX);
//        if (configDatasource == null) {
//            return null;
//        }
//        String data = configDatasource.getSourceValue();
//        if(StringUtils.isBlank(data)) {
//            return null;
//        }

        String data = "redis_HA_test_1=127.0.0.1:6380,127.0.0.1:6379;\n" + "redis_HA_test_2=127.0.0.1:6380,127.0.0.1:6379;";
        String[] dataArray = data.split(SPEAR);
        if(dataArray != null && dataArray.length > 0) {
            Properties properties = new Properties();
            for(int i = 0; i < dataArray.length; i++) {
                //去除换行符
                String tempValue = replaceBlank(dataArray[i].trim());
                String[] tempData = null;
                if(tempValue == null || tempValue.equals("")) {
                    tempData = dataArray[i].trim().split(DATA_SPEAR);
                } else {
                    tempData = tempValue.split(DATA_SPEAR);
                }
                if(tempData != null && tempData.length > 1) {
                    properties.setProperty(tempData[0],tempData[1]);
                }
            }
            //todo-my 先注释掉
            //增加回调函数
//            if(isInit && properties != null && properties.size() > 0) {
//                DefaultConfigCenterManager.getInstance().addDataChangeListenerList(new DataChangeListener() {
//                    @Override
//                    public void call(DataSourceTransport dataSourceTransport) {
//                        if(dataSourceTransport == null || dataSourceTransport.getClientDataSource() == null) {
//                            return;
//                        }
//                        //如果当前数据源为修改状态且等于此HA_CONFIG_PREFIX时
//                        if(dataSourceTransport.getClientDataSource().getSourceName() != null
//                                && dataSourceTransport.getClientDataSource().getSourceName().equals(HA_CONFIG_PREFIX)
//                                && dataSourceTransport.getTransferTypeEnum().getType() == TransferTypeEnum.TRANSFER_UPDATE.getType()) {
//                            //只针对之前有节点的，如果之前没有实际数据，那么不管
//                            //如果此处策略需要改变，则需要启动监控线程
//                            if(StartRedisReadWrite.ps == null) {
//                                return;
//                            } else {
//                                //重新加载
//                                Properties nowProperties = loadHaConfigFromCenter(false);
//                                if(nowProperties != null && nowProperties.size() > 0) {
//                                    StartRedisReadWrite.ps = nowProperties;
//                                }
//                            }
//                        }
//
//                    }
//                });
//            }
            return properties;
        }
        return null;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void main(String[] args) {
        //System.out.print(replaceBlank("redis_HA_cdms_5=127.0.0.1,127.0.0.2;"));
        String data ="redis_HA_agent_1=127.0.0.1,127.0.0.2;\n" +
                "redis_HA_agent_2=127.0.0.3,127.0.0.4; \n" ;
        String[] dataArray = data.split(SPEAR);
        if(dataArray != null && dataArray.length > 0) {
            Properties properties = new Properties();
            for (int i = 0; i < dataArray.length; i++) {
                //去除换行符
                String tempValue = replaceBlank(dataArray[i].trim());
                String[] tempData = null;
                if (tempValue == null || tempValue.equals("")) {
                    tempData = dataArray[i].trim().split(DATA_SPEAR);
                } else {
                    tempData = tempValue.split(DATA_SPEAR);
                }
                if (tempData != null && tempData.length > 1) {
                    properties.setProperty(tempData[0], tempData[1]);
                    System.out.println(tempData[0]+"="+tempData[1]);
                }

            }

        }
        System.out.println("debug");
    }


}
