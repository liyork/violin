
package com.wolf.utils.redis.command;

import com.wolf.utils.SerializationUtils;
import com.wolf.utils.redis.*;
import com.wolf.utils.redis.command.RedisZSetCommands.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.acl.Group;
import java.util.*;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:46:45

 * @since 3.3
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseCommands {
	private static final Logger LOG = LoggerFactory.getLogger(BaseCommands.class);
    public static final byte[] PART = new byte[] { ':' };
    public static final String READ = "READ";
    protected TedisSerializer stringSerializer = new StringTedisSerializer();
    private TedisSerializer keySerializer = new HessianSerializer();
    private TedisSerializer valueSerializer = keySerializer;
    private TedisSerializer hashKeySerializer = keySerializer;
    private TedisSerializer hashValueSerializer = keySerializer;
    
    private RedisCommands defaultRedisCommands ;
    
    protected Group commandsProvider;
    
    private static List<String> exceptionSuffixs = initExceptionSuffixs();
    /**
     * 
     * Description: 初始化需要重试异常信息
     * Created on 2016-1-12 上午10:08:49
     * @author
     * @return
     */
    private static List<String> initExceptionSuffixs() {
    	List<String> messageSuffixs = new ArrayList<String>();
    	messageSuffixs.add("Broken pipe");//通道破裂
    	messageSuffixs.add("Unknown reply:");//返回结果解析失败
    	messageSuffixs.add("It seems like server has closed the connection.");//服务端已主动关闭连接
    	return messageSuffixs;
    }
    
    public void setDefaultRedisCommands(RedisCommands defaultRedisCommands){
    	this.defaultRedisCommands = defaultRedisCommands;
    }

    public void setCommandsProvider(Group commandsProvider) {
        this.commandsProvider = commandsProvider;
    }

    public TedisSerializer getStringSerializer() {
        return stringSerializer;
    }

    public void setStringSerializer(TedisSerializer stringSerializer) {
        this.stringSerializer = stringSerializer;
    }

    public TedisSerializer getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(TedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public TedisSerializer getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(TedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public TedisSerializer getHashKeySerializer() {
        return hashKeySerializer;
    }

    public void setHashKeySerializer(TedisSerializer hashKeySerializer) {
        this.hashKeySerializer = hashKeySerializer;
    }

    public TedisSerializer getHashValueSerializer() {
        return hashValueSerializer;
    }

    public void setHashValueSerializer(TedisSerializer hashValueSerializer) {
        this.hashValueSerializer = hashValueSerializer;
    }
    
    protected boolean isBoolean(Object obj){
    	if(obj instanceof Long){
    		Long result = (Long)obj;
    		if(result >= 1){
    			return true ;
    		}
    		if(result == 0){
    			return false ;
    		}
    	}
    	throw new IllegalArgumentException("参数 obj :"+obj.toString()+"  异常");
    }

    protected byte[] rawKey(int namespace, Object key, boolean useNewKeySerializer) {
        if (key == null) {
            return null;
        }
        byte[] prefix = rawString(String.valueOf(namespace));
        byte[] bytekey = null;
        if(useNewKeySerializer && key instanceof String){
        	bytekey = rawString(String.valueOf(key));
        }else{
        	bytekey = keySerializer.serialize(key);
        }
        
        byte[] result = new byte[prefix.length + bytekey.length + 1];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(PART, 0, result, prefix.length, 1);
        System.arraycopy(bytekey, 0, result, prefix.length + 1, bytekey.length);
        return result;
    }

    protected byte[] removeNamespaceFromKey(byte[] key) {
        int i = 0;
        while (key[i] != PART[0]) {
            i++;
        }
        i++;
        byte[] result = new byte[key.length - i];
        System.arraycopy(key, i, result, 0, key.length - i);
        return result;
    }

    protected byte[] rawString(String key) {
        return stringSerializer.serialize(key);
    }

    protected byte[] rawLong(Long value) {
        return rawString(String.valueOf(value));
    }

    protected byte[] rawValue(Object value) {
        if (value == null) {
            throw new TedisDataException("value can not be null.");
        }
        return valueSerializer.serialize(value);
    }

    protected byte[][] rawValues(Object... value) {
        if (value == null) {
            throw new TedisDataException("value can not be null.");
        }
        byte[][] result = new byte[value.length][];
        int i = 0;
        for(Object v : value) {
            result[i++] = valueSerializer.serialize(v);
        }
        return result;
    }



    protected <HK> byte[] rawHashKey(HK hashKey) {
        if (hashKey == null) {
            throw new TedisDataException("non null hash key required");
        }
        return hashKeySerializer.serialize(hashKey);
    }

    protected <HK> byte[][] rawHashKeys(HK... hashKey) {
        if (hashKey == null) {
            throw new TedisDataException("non null hash key required");
        }
        final byte[][] rawkeys = new byte[hashKey.length][];
        int i = 0;
        for(HK key : hashKey) {
            rawkeys[i++] = hashKeySerializer.serialize(key);
        }
        return rawkeys;
    }



    protected <HV> byte[] rawHashValue(HV value) {
        return hashValueSerializer.serialize(value);
    }

    protected <K> byte[][] rawKeys(int namespace, K key, K otherKey, boolean useNewKeySerializer) {
        final byte[][] rawKeys = new byte[2][];
        rawKeys[0] = rawKey(namespace, key, useNewKeySerializer);
        rawKeys[1] = rawKey(namespace, key, useNewKeySerializer);
        return rawKeys;
    }

    protected <K> byte[][] rawKeys(int namespace, Collection<K> keys, boolean useNewKeySerializer) {
        return rawKeys(namespace, null, keys,useNewKeySerializer);
    }

    protected <K> byte[][] rawKeys(int namespace, K key, Collection<K> keys, boolean useNewKeySerializer) {
        final byte[][] rawKeys = new byte[keys.size() + (key != null ? 1 : 0)][];
        int i = 0;
        if (key != null) {
            rawKeys[i++] = rawKey(namespace, key, useNewKeySerializer);
        }
        for (K k : keys) {
            rawKeys[i++] = rawKey(namespace, k, useNewKeySerializer);
        }
        return rawKeys;
    }

    protected <V> Set<V> deserializeValues(Set<byte[]> rawValues) {
        return SerializationUtils.deserialize(rawValues, valueSerializer);
    }

    protected <V> List<V> deserializeValues(List<byte[]> rawValues) {
        return SerializationUtils.deserialize(rawValues, valueSerializer);
    }

    protected List<String> deserializeStrings(List<byte[]> rawValues) {
        return SerializationUtils.deserialize(rawValues, stringSerializer);
    }

    protected List<Long> deserializeLongs(List<byte[]> rawValues) {
        List<String> list = SerializationUtils.deserialize(rawValues, stringSerializer);
        List<Long> result = new ArrayList<Long>();
        for (String l : list) {
            if (l == null) {
                result.add(0l);
            } else {
                result.add(Long.parseLong(l));
            }
        }
        return result;
    }

    protected <T> Set<T> deserializeHashKeys(Set<byte[]> rawKeys) {
        return SerializationUtils.deserialize(rawKeys, hashKeySerializer);
    }

    protected <T> List<T> deserializeHashValues(List<byte[]> rawValues) {
        return SerializationUtils.deserialize(rawValues, hashValueSerializer);
    }

    protected <HK, HV> Map<HK, HV> deserializeHashMap(Map<byte[], byte[]> entries) {
        if (entries == null) {
            return null;
        }
        Map<HK, HV> map = new LinkedHashMap<HK, HV>(entries.size());
        for (Map.Entry<byte[], byte[]> entry : entries.entrySet()) {
            map.put((HK) deserializeHashKey(entry.getKey()), (HV) deserializeHashValue(entry.getValue()));
        }
        return map;
    }
    
    protected <HK, HV> List<Map<HK, HV>> deserializeHashMaps(List<Map<byte[], byte[]>> entries) {
        if (entries == null) {
            return null;
        }
        List<Map<HK, HV>> list = new ArrayList<Map<HK,HV>>();
        for(Map<byte[], byte[]> t : entries) {
        	list.add((Map<HK, HV>) deserializeHashMap(t));
        }
        return list;
    }

    protected <HK> Map<HK, Double> deserializeTruble(Set<Tuple> tuples) {
        if (tuples == null) {
            return null;
        }
        Map<HK, Double> map = new LinkedHashMap<HK, Double>(tuples.size());
        for (Tuple t : tuples) {
            map.put((HK) deserializeKey(t.getValue()), t.getScore());
        }
        return map;
    }

    protected <K> K deserializeKey(byte[] value) {
        return (K) keySerializer.deserialize(value);
    }

    protected <V> V deserializeValue(byte[] value) {
        return (V) valueSerializer.deserialize(value);
    }

    protected String deserializeString(byte[] value) {
        return (String) stringSerializer.deserialize(value);
    }

    protected Long deserializeLong(byte[] value) {
        String s = deserializeString(value);
        if (s == null) {
            return 0L;
        }
        return Long.parseLong(deserializeString(value));
    }

    protected <HK> HK deserializeHashKey(byte[] value) {
        return (HK) hashKeySerializer.deserialize(value);
    }

    protected <HV> HV deserializeHashValue(byte[] value) {
        return (HV) hashValueSerializer.deserialize(value);
    }

    protected Object doInTedis(int db, TedisBlock block) {
    	
    	if(block.pool == null){
    		throw new NullPointerException("获取redisPool 为 空， 请检查配置");
    	}
    	
    	if(block.pool.isDie && block.pool.isRetry){
    		
    		try {
				for(int i =0;i< 100 ;i++){
					//指定redis 服务器优先
					RedisPool pool = AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.get();
					if(pool == null){
						pool = ClusterManager.clusterModelHash(block.namespaces, block.keys, block.groupName,block.isRead);
						block.pool = pool;
					}else{
						AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.remove();
					}
					if(!pool.isDie){
						try {
							block.commands = pool.getRedis();
							break ;
						} catch (InterruptedException e) {
							LOG.error("get connect fail", e);
						}
						
					}
				}
			}catch (Exception e) {
				block.finish();
				throw new RuntimeException(e);
			}
    		
    	}else if(block.pool.isDie){
    		block.finish();
    		return null ;
    	}
    	
        if (block.commands == null) {
            throw new TedisException("RedisCommands is null.You must set a redisCommands object before using me!");
        }

        long start = 0;
        
        long getConnectEndTime  = System.currentTimeMillis();
        long executeStartTime = getConnectEndTime;

        try{
//        	int loopNum = block.pool.getConfig().getMaximumPoolSize();
        	for(int i = 0;i < 11; i++) {
        		try{
        			start = System.currentTimeMillis();
        			return block.execute();
        		}catch(Exception e) {
        			System.out.println("redis异常记录："+block.pool.getConfig().getHost()+":"+String.valueOf(e.getMessage())+",发生时间："+ new Date());
        			/**
        			 * 如果循环到最后一次，仍获取不到可用的连接，则直接将异常抛出
        			 */
        			if(i == 10) {
        				throw new RuntimeException(e);
        			}
        			//若异常需要重试，则关闭当前连接，重新获取，直至获取到可用连接
        			if(checkException(e.getMessage())) {
        				try {
        					if(block.commands instanceof PoolRedis){
        						((PoolRedis)(block.commands)).close();
        						//回收到池子中，下次使用时重新创建连接
        						block.finish();
        					}
							block.commands = block.pool.getRedis();
							if(block.commands == null) {
								return null;
							}
						} catch (InterruptedException e1) {}
        			}else {
        				throw new RuntimeException(e);
        			}        			
        		}
        	}
        	return null;
        }finally{

            block.finish();

            long end =  System.currentTimeMillis();

            String commandType = ExecuteContext.COMMAND_THREAD_LOCAL.get();
            
            long executeTime = end - start;

//            LogUtil.recordNumberLog(1, executeTime, (block.groupName == null ? "null":block.groupName) +"_"+String.valueOf(block.namespaces)+"_"+commandType+"$"+block.pool.getConfig().getHost());

            ExecuteContext.COMMAND_THREAD_LOCAL.remove();
            
            long diff = getConnectEndTime - block.getConnectStartTime;
            long nExecuteTime = end - executeStartTime;
//			if(diff >= 1 || nExecuteTime >= 2 || SwitchConstant.PRINT_REDIS_TIME_LOG) {
//				LOG.info("groupName:{},get connection of {} from pool time {}, 1 time {}, n time {}",block.groupName, block.pool.getConfig().getHost(), diff, executeTime, nExecuteTime);
//			}
        }
        
    }

    protected abstract class TedisBlock {
        protected RedisCommands commands;
        protected RedisPool pool ;
        protected int namespaces ;
        protected String keys ;
        protected String groupName ;
        protected String isRead ;
        //记录开始获取连接的时间点
        protected long getConnectStartTime;        

        public TedisBlock(int namespace,String key ,String... groupNames) {
        	String groupName = groupNames[0];
        	String isRead = null;
        	if(groupNames.length == 2){
        		isRead = READ;
        	}
        	if(StringUtils.isEmpty(groupName)){
        		commands = defaultRedisCommands ;
        		return ;
        	}
        	this.groupName = groupName;
        	//指定redis 服务器优先
        	pool = AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.get();
        	if(pool == null){
        		this.namespaces = namespace;
        		this.keys = key;
        		this.isRead = isRead ;
        		pool = ClusterManager.clusterModelHash(namespace, key, groupName,isRead);
        	}else{
        		AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.remove();
        	}
        	
        	try {
        		getConnectStartTime = System.currentTimeMillis();
        		
				commands = pool.getRedis();
				
			} catch (InterruptedException e) {
				LOG.error("get connect fail", e);
			}
        }

        public abstract Object execute();
        /**
         * 结束当前对象
         * 
         * <br/> Created on 2014-7-4 下午1:15:25

         * @since 3.3
         */
        public void finish(){
        	if(commands != null && commands instanceof PoolRedis){
        		pool.returnRedis((PoolRedis)commands);
        	}
        	
        }
    }
    
    /**
     * 根据hash取模，对keys集合进行分组，并指定redispool
     * @param namespace
     * @param groupName
     * @param keys
     * @return
     */
    public <K> Map<String,List<K>> groupForKeys(int namespace , String groupName, Collection<K> keys){
    	// key:name
    	Map<String, List<K>> map = new HashMap<String, List<K>>();
		RedisCluster redisCluster = ClusterManager.clusterMap.get(groupName);
		if(redisCluster == null || redisCluster.getRedisProxyList().size() ==0){
			throw new NullPointerException("没有对应的redis实例组   "+groupName);
		}
    	for(K key:keys) {
    		RedisReadWriteConfig config = ClusterManager.getRedisReadWriteConfigByKey(namespace, groupName, String.valueOf(key));
    		List<K> tList = map.get(config.getName());
    		if(tList == null) {
    			tList = new ArrayList<K>();
    			map.put(config.getName(), tList);
    		}
    		tList.add(key);
    	}
    	
    	return map;
    }
    
    /**
     * 
     * Description: 校验该异常是否需要重试
     * Created on 2016-1-12 上午9:23:34
     * @author
     * @param message
     * @return
     */
    private boolean checkException(String message) {
    	if(message == null) {
    		return false;
    	}

    	for(String suffix : exceptionSuffixs) {
    		if(message.indexOf(suffix) >= 0) {
    			return true;
    		}
    	}
    	
    	return false;
    	
    }

}
