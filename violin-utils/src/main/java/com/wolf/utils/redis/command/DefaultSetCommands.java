
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.TedisConnectionException;
import com.wolf.utils.redis.TedisException;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:58:54

 * @since 3.3
 */
public class DefaultSetCommands extends DefaultCommands implements SetCommands {

	    public DefaultSetCommands(String groupName) {
	    	super(groupName);
	    }

    @Override
    public <K, V> Long add(final int namespace, final K key, final V... value) {
        return add(false, namespace, key, value);
    }
    
    @Override
    public <K, V> Long add(final boolean useNewKeySerialize, final int namespace, final K key, final V... value) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.addWithoutTry(useNewKeySerialize, namespace, key, value);
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
        throw new TedisException("执行set的add失败！");
    }
    
    private <K, V> Long addWithoutTry(final boolean useNewKeySerialize, final int namespace, final K key, final V... value) {
        return (Long)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sAdd(rawKey(namespace, key, useNewKeySerialize), rawValues(value));
            }
        });
    }

    @Override
    public <K, V> Set<V> difference(final int namespace, K key, K otherKey) {
        return difference(namespace, key, otherKey, false);
    }
    
    @Override
    public <K, V> Set<V> difference(final int namespace, K key, K otherKey, final boolean useNewKeySerialize) {
        return difference(namespace, key, Collections.singleton(otherKey), useNewKeySerialize);
    }

    @Override
    public <K, V> Set<V> difference(final int namespace, final K key, final Collection<K> otherKeys) {
        return  difference(namespace, key, otherKeys, false);
    }
    
    @Override
    public <K, V> Set<V> difference(final int namespace, final K key, final Collection<K> otherKeys, final boolean useNewKeySerialize) {
        /*return deserializeValues((Set<byte[]>)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sDiff(rawKeys(namespace, key, otherKeys, useNewKeySerialize));
            }
        }));*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> void differenceAndStore(final int namespace, K key, K otherKey, K destKey) {
        differenceAndStore(namespace, key, Collections.singleton(otherKey), destKey);
    }
    
    @Override
    public <K, V> void differenceAndStore(final int namespace, K key, K otherKey, K destKey, final boolean useNewKeySerialize) {
        differenceAndStore(namespace, key, Collections.singleton(otherKey), destKey, useNewKeySerialize);
    }

    @Override
    public <K, V> void differenceAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey) {
    	differenceAndStore(namespace, key, otherKeys, destKey, false);
    }
    
    @Override
    public <K, V> void differenceAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey, final boolean useNewKeySerialize) {
        /*doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                commands.sDiffStore(rawKey(namespace, destKey, useNewKeySerialize), rawKeys(namespace, key, otherKeys, useNewKeySerialize));
                return null;
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> Set<V> intersect(final int namespace, final K key, final K otherKey) {
        return intersect(namespace, key, Collections.singleton(otherKey));
    }
    
    @Override
    public <K, V> Set<V> intersect(final int namespace, final K key, final K otherKey, final boolean useNewKeySerialize) {
        return intersect(namespace, key, Collections.singleton(otherKey), useNewKeySerialize);
    }

    @Override
    public <K, V> Set<V> intersect(final int namespace, final K key, final Collection<K> otherKeys) {
        return intersect(namespace, key, otherKeys, false);
    }
    
    @Override
    public <K, V> Set<V> intersect(final int namespace, final K key, final Collection<K> otherKeys, final boolean useNewKeySerialize) {
    /*   return deserializeValues((Set<byte[]>)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sInter(rawKeys(namespace, key, otherKeys, useNewKeySerialize));
            }
        }));*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> void intersectAndStore(final int namespace, K key, K otherKey, K destKey) {
        intersectAndStore(namespace, key, Collections.singleton(otherKey), destKey);
    }
    
    @Override
    public <K, V> void intersectAndStore(final int namespace, K key, K otherKey, K destKey, final boolean useNewKeySerialize) {
        intersectAndStore(namespace, key, Collections.singleton(otherKey), destKey, useNewKeySerialize);
    }

    @Override
    public <K, V> void intersectAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey) {
        intersectAndStore(namespace, key, otherKeys, destKey, false);
    }
    
    @Override
    public <K, V> void intersectAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey, final boolean useNewKeySerialize) {
        /*doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                commands.sInterStore(rawKey(namespace, destKey, useNewKeySerialize), rawKeys(namespace, key, otherKeys, useNewKeySerialize));
                return null;
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> Boolean isMember(final int namespace, final K key, final Object o) {
        return  isMember(namespace, key, o, false);
    }
    
    @Override
    public <K, V> Boolean isMember(final int namespace, final K key, final Object o, final boolean useNewKeySerialize) {
        return (Boolean)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sIsMember(rawKey(namespace, key, useNewKeySerialize), rawValue(o));
            }
        });
    }

    @Override
    public <K, V> Set<V> members(final int namespace, final K key) {
        return members(namespace, key, false);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Set<V> members(final int namespace, final K key, final boolean useNewKeySerialize) {
        return deserializeValues((Set<byte[]>)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sMembers(rawKey(namespace, key, useNewKeySerialize));
            }
        }));
    }

    @Override
    public <K, V> Boolean move(final int namespace, final K key, final V value, final K destKey) {
        return move(namespace, key, value, destKey, false);
    }
    
    @Override
    public <K, V> Boolean move(final int namespace, final K key, final V value, final K destKey, final boolean useNewKeySerialize) {
        /*return (Boolean)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName){
            @Override
            public Object execute() {
                return commands.sMove(rawKey(namespace, key, useNewKeySerialize), rawKey(namespace, destKey, useNewKeySerialize), rawValue(value));
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> V pop(final int namespace, final K key) {
        return pop(namespace, key, false);
    }
    
    @Override
    public <K, V> V pop(final int namespace, final K key, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.popWithoutTry(namespace, key, useNewKeySerialize);
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
        throw new TedisException("执行set的pop失败！");
    }
    
    private <K, V> V popWithoutTry(final int namespace, final K key, final boolean useNewKeySerialize) {
        return deserializeValue((byte[])doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sPop(rawKey(namespace, key, useNewKeySerialize));
            }
        }));
    }

    @Override
    public <K, V> V randomMember(final int namespace, final K key) {
        return randomMember(namespace, key, false);
    }
    
    @Override
    public <K, V> V randomMember(final int namespace, final K key, final boolean useNewKeySerialize) {
        return deserializeValue((byte[])doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sRandMember(rawKey(namespace, key, useNewKeySerialize));
            }
        }));
    }

    @Override
    public <K, V> Long remove(final int namespace, final K key, final Object... o) {
        return remove(namespace, key, false, o);
    }

    @Override
    public <K, V> Long remove(final int namespace, final K key, final boolean useNewKeySerialize, final Object... o) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.removeWithoutTry(namespace, key, useNewKeySerialize, o);
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
        throw new TedisException("执行set的remove失败！");
    }


    
    private <K, V> Long removeWithoutTry(final int namespace, final K key, final boolean useNewKeySerialize, final Object... o) {
        return (Long)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName){
            @Override
            public Object execute() {
                return commands.sRem(rawKey(namespace, key, useNewKeySerialize), rawValues(o));
            }
        });
    }

    @Override
    public <K, V> Long nremove(final int namespace, final K key, final Object... values) {
        return nremove(false, namespace, key, values);
    }

    @Override
    public <K, V> Long nremove(final boolean useNewKeySerialize,final int namespace, final K key,  final Object... values) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
            try {
                return this.removeWithoutTry(namespace, key, useNewKeySerialize, values);
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
        throw new TedisException("执行set的remove失败！");
    }




    @Override
    public <K, V> Long size(final int namespace, final K key) {
        return size(namespace, key, false); 
    }
    
    @Override
    public <K, V> Long size(final int namespace, final K key, final boolean useNewKeySerialize) {
        return (Long)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName){
            @Override
            public Object execute() {
                return commands.sCard(rawKey(namespace, key, useNewKeySerialize));
            }
        });
    }

    @Override
    public <K, V> Set<V> union(final int namespace, final K key, final K otherKey) {
        return union(namespace, key, Collections.singleton(otherKey));
    }
    
    @Override
    public <K, V> Set<V> union(final int namespace, final K key, final K otherKey, final boolean useNewKeySerialize) {
        return union(namespace, key, Collections.singleton(otherKey), useNewKeySerialize);
    }

    @Override
    public <K, V> Set<V> union(final int namespace, final K key, final Collection<K> otherKeys) {
        return union(namespace, key, otherKeys, false);
    }
    
    @Override
    public <K, V> Set<V> union(final int namespace, final K key, final Collection<K> otherKeys, final boolean useNewKeySerialize) {
        /*return deserializeValues((Set<byte[]>)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.sUnion(rawKeys(namespace, key, otherKeys, useNewKeySerialize));
            }
        }));*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> void unionAndStore(final int namespace, K key, K otherKey, K destKey) {
        unionAndStore(namespace, key, Collections.singleton(otherKey), destKey);
    }
    
    @Override
    public <K, V> void unionAndStore(final int namespace, K key, K otherKey, K destKey, final boolean useNewKeySerialize) {
        unionAndStore(namespace, key, Collections.singleton(otherKey), destKey, useNewKeySerialize);
    }

    @Override
    public <K, V> void unionAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey) {
    	unionAndStore(namespace, key, otherKeys, destKey,false);
    }
    
    @Override
    public <K, V> void unionAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey, final boolean useNewKeySerialize) {
        /*doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                commands.sUnionStore(rawKey(namespace, destKey, useNewKeySerialize), rawKeys(namespace, key, otherKeys, useNewKeySerialize));
                return null;
            }
        });*/
    }

}
