
package com.wolf.utils.redis.command;


import com.wolf.utils.redis.CommandsExecutor;
import com.wolf.utils.redis.TedisConnectionException;
import com.wolf.utils.redis.TedisException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:59:08

 * @since 3.3
 */
public class DefaultStringCommands extends DefaultCommands implements StringCommands {

    public DefaultStringCommands(String groupName) {
    	super(groupName);
    }


    @Override
    public Long append(final int namespace, final String key, final String value) {
        return append(namespace, key, value, false);
    }
    
    @Override
    public Long append(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
			    return this.appendWithoutTry(namespace, key, value, useNewKeySerialize);
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
        throw new TedisException("执行append失败！");
    }
    
    private Long appendWithoutTry(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        return (Long) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName) {
            @Override
            public Object execute() {
                return commands.append(rawKey(namespace, key, useNewKeySerialize), rawString(value));
            }
        });
    }

    @Override
    public String get(final int namespace, final Object key) {
    	return get(namespace, key, false);
    }
    
    @Override
    public String get(final int namespace, final Object key, final boolean useNewKeySerialize) {
        return deserializeString((byte[]) doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName,READ)  {
            @Override
            public Object execute() {
                return commands.get(rawKey(namespace, key, useNewKeySerialize));
            }
        }));
    }
    
    @Override
    public String get(final int namespace, final String key, final long start, final long end) {
        return get(namespace, key, start, end, false);
    }

    @Override
    public String get(final int namespace, final String key, final long start, final long end, final boolean useNewKeySerialize) {
        return deserializeString((byte[]) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName,READ)  {
            @Override
            public Object execute() {
                return commands.getRange(rawKey(namespace, key, useNewKeySerialize), (int) start, (int) end);
            }
        }));
    }
    
    @Override
    public String getAndSet(final int namespace, final String key, final String value) {
        return getAndSet(namespace, key, value, false);
    }

    @Override
    public String getAndSet(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
			    return this.getAndSetWithoutTry(namespace, key, value, useNewKeySerialize);
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
        throw new TedisException("执行getAndSet失败！");
    }
    
    private String getAndSetWithoutTry(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
    	return deserializeString((byte[]) doInTedis(namespace,new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
    		@Override
    		public Object execute() {
    			return commands.getSet(rawKey(namespace, key, useNewKeySerialize), rawString(value));
    		}
    	}));
    }
    
    @Override
    public List<String> multiGet(final int namespace, final Collection<String> keys) {
    	return multiGet(namespace, keys, false);
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<String> multiGet(int namespace, Collection<String> keys, boolean useNewKeySerialize) {
        CommandsExecutor<String, String> commandsExecutor = new CommandsExecutor<String, String>() {

        	@Override
			public List<String> multiExecute(int namespace,List<String> partKeys,final byte[][] rawKeys) {
				return deserializeStrings((List<byte[]>)doInTedis(namespace,  new TedisBlock(namespace,String.valueOf(partKeys.get(0)) ,groupName,READ) {
		            @Override
		            public Object execute() {
		                return commands.mGet(rawKeys);
		            }
		        }));
			}

			@Override
			public String singleExecute(int namespace,
					String key, boolean useNewKeySerialize) {
				return get(namespace, key ,useNewKeySerialize);
			}

		};
		
    	 
    	return super.multiGet(namespace, keys, useNewKeySerialize, commandsExecutor) ;
	}

    @Override
    public void multiSet(final int namespace, final Map<String, String> m) {
    	throw new RuntimeException("不支持此方法");
    }

    @Override
    public void multiSetIfAbsent(final int namespace, final Map<String, String> m) {
    	throw new RuntimeException("不支持此方法");

    }

    @Override
    public void set(final int namespace, final String key, final String value) {
    	set(namespace, key, value, false);
    }
    
    @Override
    public void set(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
				this.setWithoutTry(namespace, key, value, useNewKeySerialize);
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
    
    private void setWithoutTry(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                commands.set(rawKey(namespace, key, useNewKeySerialize), rawString(value));
                return null;
            }
        });
    }

    @Override
    public void set(final int namespace, final String key, final String value, final long timeout, final TimeUnit unit) {
    	set(namespace, key, value, timeout, unit, false);
    }
    
    @Override
    public void set(final int namespace, final String key, final String value, final long timeout, final TimeUnit unit, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
				this.setWithoutTry(namespace, key, value, timeout, unit, useNewKeySerialize);
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
    
    private void setWithoutTry(final int namespace, final String key, final String value, final long timeout, final TimeUnit unit, final boolean useNewKeySerialize) {
        doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                commands.setEx(rawKey(namespace, key, useNewKeySerialize), (int) unit.toSeconds(timeout), rawString(value));
                return null;
            }
        });
    }

    @Override
    public void set(final int namespace, final String key, final String value, final long offset) {
    	set(namespace, key, value, offset, false);
    }
    
    @Override
    public void set(final int namespace, final String key, final String value, final long offset, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
				this.setWithoutTry(namespace, key, value, offset, useNewKeySerialize);
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
    
    private void setWithoutTry(final int namespace, final String key, final String value, final long offset, final boolean useNewKeySerialize) {
        doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                commands.setRange(rawKey(namespace, key, useNewKeySerialize), rawString(value), offset);
                return null;
            }
        });
    }

    @Override
    public Boolean setIfAbsent(final int namespace, final String key, final String value) {
        return setIfAbsent(namespace, key, value, false);
    }
    
    @Override
    public Boolean setIfAbsent(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        for(int i = 0 ;i < REPEAT_NUM;i++){
        	try {
        		return this.setIfAbsentWithoutTry(namespace, key, value, useNewKeySerialize);
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
        throw new TedisException("执行setIfAbsent失败！");
    }
    
    private Boolean setIfAbsentWithoutTry(final int namespace, final String key, final String value, final boolean useNewKeySerialize) {
        return (Boolean) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName)  {
            @Override
            public Object execute() {
                return commands.setNX(rawKey(namespace, key, useNewKeySerialize), rawString(value));
            }
        });
    }

    @Override
    public Long size(final int namespace, final String key) {
        return size(namespace, key, false);
    }
    
    @Override
    public Long size(final int namespace, final String key, final boolean useNewKeySerialize) {
        return (Long) doInTedis(namespace, new TedisBlock(namespace,String.valueOf(key) ,groupName,READ)  {
            @Override
            public Object execute() {
                return commands.strLen(rawKey(namespace, key, useNewKeySerialize));
            }
        });
    }

}
