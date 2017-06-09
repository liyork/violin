
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.command.RedisZSetCommands.Tuple;
import com.wolf.utils.redis.TedisConnectionException;
import com.wolf.utils.redis.TedisException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午2:09:09

 * @since 3.3
 */
public class DefaultZSetCommands extends DefaultCommands implements ZSetCommands {

    public DefaultZSetCommands(String groupName) {
    	super(groupName);
    }


    @Override
    public <K, V> Boolean add(final int namespace, final K key, final V value, final double score) {
        return add(namespace, key, value, score, false);
    }
    
    @Override
    public <K, V> Boolean add(final int namespace, final K key, final V value, final double score, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.addWithoutTry(namespace, key, value, score, useNewKeySerialize);
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
        throw new TedisException("执行zset的add失败！");
    }
    
    private <K, V> Boolean addWithoutTry(final int namespace, final K key, final V value, final double score, final boolean useNewKeySerialize) {
        return (Boolean)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zAdd(rawKey(namespace,key, useNewKeySerialize), score, rawValue(value));
            }
        });
    }

    @Override
    public <K, V> Long add(final int namespace, final K key, final Map<V, Double> maps) {
        return add(namespace, key, maps, false);
    }
    
    @Override
    public <K, V> Long add(final int namespace, final K key, final Map<V, Double> maps, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.addWithoutTry(namespace, key, maps, useNewKeySerialize);
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
        throw new TedisException("执行zset的add失败！");
    }
    
    private <K, V> Long addWithoutTry(final int namespace, final K key, final Map<V, Double> maps, final boolean useNewKeySerialize) {
        final Tuple[] tuples = new Tuple[maps.size()];
        int i = 0;
        for(Entry<V, Double> m : maps.entrySet()) {
            tuples[i++] = new Tuple(rawValue(m.getKey()), m.getValue());
        }
        return (Long)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zAdd(rawKey(namespace,key, useNewKeySerialize), tuples);
            }
        });
    }

    @Override
    public <K, V> Long count(final int namespace, final K key, final double min, final double max) {
        return count(namespace, key, min, max, false);
    }
    
    @Override
    public <K, V> Long count(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
        return (Long)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zCount(rawKey(namespace,key, useNewKeySerialize), min, max);
            }
        });
    }

    @Override
    public <K, V> Double incrementScore(final int namespace, final K key, final V value, final double delta) {
        return incrementScore(namespace, key, value, delta, false);
    }
    
    @Override
    public <K, V> Double incrementScore(final int namespace, final K key, final V value, final double delta, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.incrementScoreWithoutTry(namespace, key, value, delta, useNewKeySerialize);
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
        throw new TedisException("执行zset的incrementScore失败！");
    }
    
    private <K, V> Double incrementScoreWithoutTry(final int namespace, final K key, final V value, final double delta, final boolean useNewKeySerialize) {
        return (Double)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zIncrBy(rawKey(namespace,key, useNewKeySerialize), delta, rawValue(value));
            }
        });
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
        /*doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zInterStore(rawKey(namespace,destKey, useNewKeySerialize), rawKeys(namespace, key, otherKeys, useNewKeySerialize));
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public <K, V> Set<V> range(final int namespace, final K key, final long start, final long end) {
        return range(namespace, key, start, end, false);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Set<V> range(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
        return deserializeValues((Set<byte[]>)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zRange(rawKey(namespace,key, useNewKeySerialize), start, end);
            }
        }));
    }

	@Override
	public <K, V> Map<V, Double> rangeWithScore(final int namespace, final K key, final long start, final long end) {
		return rangeWithScore(namespace, key, start, end, false);
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public <K, V> Map<V, Double> rangeWithScore(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
		return deserializeTruble((Set<Tuple>) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
			@Override
			public Object execute() {
				return commands.zRangeWithScore(rawKey(namespace, key, useNewKeySerialize), start, end);
			}
		}));
	}

    @Override
    public <K, V> Set<V> rangeByScore(final int namespace, final K key, final double min, final double max) {
        return rangeByScore(namespace, key, min, max, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Set<V> rangeByScore(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
        return deserializeValues((Set<byte[]>)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRangeByScore(rawKey(namespace,key, useNewKeySerialize), min, max);
            }
        }));
    }

	@Override
	public <K, V> Set<V> rangeByScore(final int namespace, final K key, final double min, final double max, final int offset, final int count) {
		return rangeByScore(namespace, key, min, max, offset, count, false); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Set<V> rangeByScore(final int namespace, final K key, final double min, final double max, final int offset, final int count, final boolean useNewKeySerialize) {
		return deserializeValues((Set<byte[]>)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zRangeByScore(rawKey(namespace,key, useNewKeySerialize), min, max, offset, count);
            }
        }));
	}

	@Override
	public <K, V> Map<V, Double> rangeByScoreWithScore(final int namespace, final K key, final double min, final double max) {
		return rangeByScoreWithScore(namespace, key, min, max, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Map<V, Double> rangeByScoreWithScore(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
		return deserializeTruble((Set<Tuple>) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
			@Override
			public Object execute() {
				return commands.zRangeByScoreWithScore(rawKey(namespace, key, useNewKeySerialize), min, max);
			}
		}));
	}

    @Override
    public <K, V> Long rank(final int namespace, final K key, final Object o) {
        return rank(namespace, key, o, false);
    }
    
    @Override
    public <K, V> Long rank(final int namespace, final K key, final Object o, final boolean useNewKeySerialize) {
        return (Long)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zRank(rawKey(namespace,key, useNewKeySerialize), rawValue(o));
            }
        });
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
        throw new TedisException("执行zset的remove失败！");
    }

    @Override
    public <K, V> Long nremove(int namespace, K key, Object... values) {
        return nremove(false, namespace, key, values);
    }

    @Override
    public <K, V> Long nremove( boolean useNewKeySerialize,int namespace, K key, Object... values) {
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
        throw new TedisException("执行zset的remove失败！");
    }

    private <K, V> Long removeWithoutTry(final int namespace, final K key, final boolean useNewKeySerialize, final Object... o) {
        return (Long)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zRem(rawKey(namespace,key, useNewKeySerialize), rawValues(o));
            }
        });
    }



    @Override
    public <K, V> void removeRange(final int namespace, final K key, final long start, final long end) {
    	removeRange(namespace, key, start, end, false);
    }

    @Override
    public <K, V> void removeRange(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		this.removeRangeWithoutTry(namespace, key, start, end, useNewKeySerialize);
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
        throw new TedisException("执行zset的removeRange失败！");
    }
    
    private <K, V> void removeRangeWithoutTry(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
        doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRemRange(rawKey(namespace,key, useNewKeySerialize), start, end);
            }
        });
    }

    @Override
    public <K, V> void removeRangeByScore(final int namespace, final K key, final double min, final double max) {
    	removeRangeByScore(namespace, key, min, max, false);
    }

    @Override
    public <K, V> void removeRangeByScore(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		this.removeRangeByScoreWithoutTry(namespace, key, min, max, useNewKeySerialize);
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
        throw new TedisException("执行zset的removeRangeByScore失败！");
    }
    
    private <K, V> void removeRangeByScoreWithoutTry(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
        doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRemRangeByScore(rawKey(namespace,key, useNewKeySerialize), min, max);
            }
        });
    }

    @Override
    public <K, V> Set<V> reverseRange(final int namespace, final K key, final long start, final long end) {
        return reverseRange(namespace, key, start, end, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Set<V> reverseRange(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
        return deserializeValues((Set<byte[]>)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRevRange(rawKey(namespace,key, useNewKeySerialize), start, end);
            }
        }));
    }

	@Override
	public <K, V> Set<V> reverseRangeByScore(final int namespace, final K key, final double min, final double max) {
		return reverseRangeByScore(namespace, key, min, max, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Set<V> reverseRangeByScore(final int namespace, final K key, final double min, final double max, final boolean useNewKeySerialize) {
		return deserializeValues((Set<byte[]>)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRevRangeByScore(rawKey(namespace,key, useNewKeySerialize), min, max);
            }
        }));
	}

	@Override
	public <K, V> Map<V, Double> reverseRangeWithScore(final int namespace, final K key, final long start, final long end) {
    	return reverseRangeWithScore(namespace, key, start, end, false);
	}

    @SuppressWarnings("unchecked")
	@Override
	public <K, V> Map<V, Double> reverseRangeWithScore(final int namespace, final K key, final long start, final long end, final boolean useNewKeySerialize) {
    	return deserializeTruble((Set<Tuple>) doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
			@Override
			public Object execute() {
				return commands.zRevRangeWithScore(rawKey(namespace, key, useNewKeySerialize), start, end);
			}
		}));
	}

    @Override
    public <K, V> Long reverseRank(final int namespace, final K key, final Object o) {
        return reverseRank(namespace, key, o, false);
    }
    

    @Override
    public <K, V> Long reverseRank(final int namespace, final K key, final Object o, final boolean useNewKeySerialize) {
        return (Long)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zRevRank(rawKey(namespace,key, useNewKeySerialize), rawValue(o));
            }
        });
    }

    @Override
    public <K, V> Double score(final int namespace, final K key, final Object o) {
        return score(namespace, key, o, false);
    }

    @Override
    public <K, V> Double score(final int namespace, final K key, final Object o, final boolean useNewKeySerialize) {
        return (Double)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zScore(rawKey(namespace,key, useNewKeySerialize), rawValue(o));
            }
        });
    }

    @Override
    public <K, V> Long size(final int namespace, final K key) {
        return size(namespace, key, false);
    }

    @Override
    public <K, V> Long size(final int namespace, final K key, final boolean useNewKeySerialize) {
        return (Long)doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.zCard(rawKey(namespace,key, useNewKeySerialize));
            }
        });
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
    	unionAndStore(namespace, key, otherKeys, destKey, false);
    }

    @Override
    public <K, V> void unionAndStore(final int namespace, final K key, final Collection<K> otherKeys, final K destKey, final boolean useNewKeySerialize) {
        /*doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zUnionStore(rawKey(namespace,destKey,useNewKeySerialize), rawKeys(namespace, key, otherKeys,useNewKeySerialize));
            }
        });*/
    	/**
    	 * 此方法只适用于同一台redis，由于目前采用redis集群，故暂不支持 此方法
    	 */
    	throw new RuntimeException("不支持此方法");
    }

	@Override
	public <K, V> Set<V> reverseRangeByScore(final int namespace, final K key, final double min, final double max, final int offset, final int count) {
		return reverseRangeByScore(namespace, key, min, max, offset, count, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Set<V> reverseRangeByScore(final int namespace, final K key, final double min, final double max, final int offset, final int count, final boolean useNewKeySerialize) {
		return deserializeValues((Set<byte[]>)doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.zRevRangeByScore(rawKey(namespace,key, useNewKeySerialize), min, max, offset, count);
            }
        }));
	}

}
