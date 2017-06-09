
package com.wolf.utils.redis.command;

import com.wolf.utils.SerializationUtils;
import com.wolf.utils.redis.*;

import java.security.acl.Group;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DefaultCommands extends BaseCommands implements Commands {

	protected String groupName;
	 
	public static final int SLEEP_TIME = 500;//单位毫秒
	public static final String ERROR_MESSAGE = "com.xx.redis.TedisDataException: READONLY You can't write against a read only slave.";
    public static final int REPEAT_NUM = 3;//重试次数
    
    public DefaultCommands(String groupName) {
    	this.groupName = groupName;
    }
    
    @Override
    public <K> void delete(final int namespace, final K key) {
    	delete(namespace, key, false);
    }
    
    @Override
    public <K> void delete(final int namespace, final K key, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
				this.deleteForTry(namespace, key, useNewKeySerialize);
			    return;
        	} catch (TedisConnectionException e) {
				if(ERROR_MESSAGE.equals(e.getMessage()) && i < REPEAT_NUM - 1) {
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e1) {
					}
					continue;
				}else {
					throw new TedisException(e);
				}
			}
        }
    }
    
    private <K> void deleteForTry(final int namespace, final K key, final boolean useNewKeySerialize) {
    	doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                commands.del(rawKey(namespace, key, useNewKeySerialize));
                return null;
            }
        });
    }

    @Override
    public <K> void delete(final int namespace, final Collection<K> keys) {
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K> Boolean expire(final int namespace, final K key, long timeout, TimeUnit unit) {
    	return expire(namespace, key, timeout, unit, false);
    }
    
    @Override
    public <K> Boolean expire(final int namespace, final K key, long timeout, TimeUnit unit, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		 return this.expireForTry(namespace, key, timeout, unit, useNewKeySerialize);
			} catch (TedisConnectionException e) {
				if(ERROR_MESSAGE.equals(e.getMessage()) && i < REPEAT_NUM - 1) {
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e1) {
					}
					continue;
				}else {
					throw new TedisException(e);
				}
			}
        }
        throw new TedisException("执行expire失败！");
    }
    
    private <K> Boolean expireForTry(final int namespace, final K key, long timeout, TimeUnit unit, final boolean useNewKeySerialize) {
        final long seconds = unit.toSeconds(timeout);
		return (Boolean) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
	            @Override
	            public Object execute() {
	                return commands.expire(rawKey(namespace, key, useNewKeySerialize), seconds);
	            }
	        });
    }

    @Override
    public <K> Boolean expireAt(final int namespace, final K key, final Date date) {
    	return expireAt(namespace, key, date, false);
    }
    
    @Override
    public <K> Boolean expireAt(final int namespace, final K key, final Date date, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		 return this.expireAtForTry(namespace, key, date, useNewKeySerialize);
			} catch (TedisConnectionException e) {
				if(ERROR_MESSAGE.equals(e.getMessage()) && i < REPEAT_NUM - 1) {
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e1) {
					}
					continue;
				}else {
					throw new TedisException(e);
				}
			}
        }
        throw new TedisException("执行expireAt失败！");
    }

    private <K> Boolean expireAtForTry(final int namespace, final K key, final Date date, final boolean useNewKeySerialize) {
        return (Boolean) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.expireAt(rawKey(namespace, key, useNewKeySerialize), date.getTime());
            }
        });
    }

    @Override
    public <K> Long getExpire(final int namespace, final K key) {
    	return getExpire(namespace, key, false);
    }

    @Override
    public <K> Long getExpire(final int namespace, final K key, final boolean useNewKeySerialize) {
        return (Long) doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName){
            @Override
            public Object execute() {
                return commands.ttl(rawKey(namespace, key, useNewKeySerialize));
            }
        });
    }
   

    @Override
    public <K> Boolean hasKey(final int namespace, final K key) {
    	return hasKey(namespace, key, false);
    }
    
    @Override
    public <K> Boolean hasKey(final int namespace, final K key, final boolean useNewKeySerialize) {
        return (Boolean) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName,READ){
            @Override
            public Object execute() {
                return commands.exists(rawKey(namespace, key, useNewKeySerialize));
            }
        });
    }
    
	@Override
    public <K> Set<K> keys(final int namespace, final String pattern) {
		return keysByNew(namespace, pattern, false);
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public <K> Set<K> keysByNew(final int namespace, final String pattern, boolean useNewKeySerialize) {
    	RedisCluster redisCluster = ClusterManager.clusterMap.get(groupName);
    	if(redisCluster == null){
    		throw new RuntimeException("未找到groupName为"+groupName+"的集群信息");
    	}
    	List<RedisReadWriteConfig> listConf = redisCluster.getAllRedisReadWriteConfig();
    	
    	Set<byte[]> allBytekeys = new HashSet<byte[]>();;
    	for(RedisReadWriteConfig config:listConf){
    		//强制走读库
			RedisPool pool = ClusterManager.getReadRedis(config);
			AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.set(pool);
			Set<byte[]> bytekeys = (Set<byte[]>) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(pattern) ,groupName,READ) {
				@Override
				public Object execute() {
					return commands.keys((namespace + ":" + pattern).getBytes());
				}
			});
			allBytekeys.addAll(bytekeys);
    	}
    	
        Set<byte[]> newbytekeys = new HashSet<byte[]>();
        for (byte[] bytekey : allBytekeys) {
            newbytekeys.add(removeNamespaceFromKey(bytekey));
        }
        if(useNewKeySerialize){
        	return SerializationUtils.deserialize(newbytekeys, getStringSerializer());
        }else{
        	return SerializationUtils.deserialize(newbytekeys, getKeySerializer());
        }
    }

    @Override
    public <K> Boolean persist(final int namespace, final K key) {
        return persist(namespace, key, false);
    }
    
    @Override
    public <K> Boolean persist(final int namespace, final K key, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		 return this.persistForTry(namespace, key, useNewKeySerialize);
			} catch (TedisConnectionException e) {
				if(ERROR_MESSAGE.equals(e.getMessage()) && i < REPEAT_NUM - 1) {
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e1) {
					}
					continue;
				}else {
					throw new TedisException(e);
				}
			}
        }
        throw new TedisException("执行persist失败！");
    }
    
    private <K> Boolean persistForTry(final int namespace, final K key, final boolean useNewKeySerialize) {
        return (Boolean) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.persist(rawKey(namespace, key, useNewKeySerialize));
            }
        });
    }

    @Override
    public <K> void rename(final int namespace, final K oldKey, final K newKey) {
    	rename(namespace, oldKey, newKey, false);
    }
    
    @Override
    public <K> void rename(final int namespace, final K oldKey, final K newKey, final boolean useNewKeySerialize) {
        /*doInTedis(namespace, new TedisBlock(namespace,String.valueOf(oldKey) ,groupName){
            @Override
            public Object execute() {
                commands.rename(rawKey(namespace, oldKey, useNewKeySerialize), rawKey(namespace, newKey, useNewKeySerialize));
                return null;
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K> Boolean renameIfAbsent(final int namespace, final K oldKey, final K newKey) {
        return renameIfAbsent(namespace, oldKey, newKey, false);
    }
    
    @Override
    public <K> Boolean renameIfAbsent(final int namespace, final K oldKey, final K newKey, final boolean useNewKeySerialize) {
        /*return (Boolean) doInTedis(namespace,new TedisBlock(namespace,String.valueOf(oldKey) ,groupName) {
            @Override
            public Object execute() {
                return commands.renameNX(rawKey(namespace, oldKey, useNewKeySerialize), rawKey(namespace, newKey, useNewKeySerialize));
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

	@Override
    public <K, V> List<V> sort(final int namespace, final K key, final SortParams params) {
        return sort(namespace, key, params, false);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public <K, V> List<V> sort(final int namespace, final K key, final SortParams params, final boolean useNewKeySerialize) {
        return deserializeValues((List<byte[]>) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sort(rawKey(namespace, key, useNewKeySerialize), params);
            }
        }));
    }

    @Override
    public <K> Long sort(final int namespace, final K key, final SortParams params, final K storeKey) {
        return sort(namespace, key, params, storeKey, false);
    }
    
    @Override
    public <K> Long sort(final int namespace, final K key, final SortParams params, final K storeKey, final boolean useNewKeySerialize) {
        /*return (Long) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sort(rawKey(namespace, key, useNewKeySerialize), params, rawKey(namespace, storeKey, useNewKeySerialize));
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K> DataType type(final int namespace, final K key) {
        return type(namespace, key, false);
    }
    
    @Override
    public <K> DataType type(final int namespace, final K key, final boolean useNewKeySerialize) {
        return DataType.fromCode((String) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.type(rawKey(namespace, key, useNewKeySerialize));
            }
        }));
    }
    
    protected <K, V> List<V> multiGet(int namespace, Collection<K> keys,
			boolean useNewKeySerialize,CommandsExecutor<K,V> commandsExecutor) {
		Map<String,List<K>> map = groupForKeys(namespace, groupName, keys);
		if(map == null || map.size() == 0){
			return new ArrayList<V>();
		}
		RedisCluster redisCluster = ClusterManager.clusterMap.get(groupName);
		if(redisCluster == null) {
			throw new RuntimeException("未找到groupName为"+groupName+"的集群信息");
		}
    	List<RedisReadWriteConfig> listConf = redisCluster.getAllRedisReadWriteConfig();
        if(listConf.size() == 0) {
        	throw new RuntimeException("groupName为"+groupName+"的集群中未配置实例");
        }
    	
    	int cluSize = listConf.size();
    	List<V> listRes = new ArrayList<V>();
    	if(keys.size() > cluSize){
			
	        Iterator<Map.Entry<String, List<K>>> iterator = map.entrySet().iterator();
    		while(iterator.hasNext()){
    			List<K> partKeys = iterator.next().getValue();
    			final byte[][] rawKeys = new byte[partKeys.size()][];
    	        int counter = 0;
    	        for (K key : partKeys) {
    	            rawKeys[counter++] = rawKey(namespace, key, useNewKeySerialize);
    	        }
			    List<V> list = commandsExecutor.multiExecute(namespace,partKeys,rawKeys);
				for(V v:list){
					 if(v != null){
						 listRes.add(v);
					 }
				}
    		}
    		return listRes ;
    	}
    	
    	 for (K key : keys) {
	           V value =  commandsExecutor.singleExecute(namespace, key, useNewKeySerialize);
	           if(value != null){
	        	   listRes.add(value);
	           }
	     }
    	 
    	 return listRes ;
	}
    
    public Group getProvider() {
        return this.commandsProvider;
    }

}
