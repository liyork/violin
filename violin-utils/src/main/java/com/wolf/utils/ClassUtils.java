/**
 * Description: ClassInfo.java
 * All Rights Reserved.
 */
package com.wolf.utils;


import java.lang.reflect.Field;
import java.util.List;


/**
 * Description: 操作类信息的工具类
 */
public final class ClassUtils {

    private ClassUtils() { }

    /**
	 * 获取当前类及父类的field对象
	 * Description: 
	 */
	  public static void getFields(Class<?> clazz,List<Field> allField) {
		  	
		    Field[] fields = clazz.getDeclaredFields();
		    for (int i = 0; i < fields.length; i++) {
		      Field field = fields[i];
		      allField.add(field);
		      
		    }
		    if (clazz.getSuperclass() != null) {
		    	getFields(clazz.getSuperclass(),allField);
		    }
	  }
	  /**
	   * 获取当前类 及父类的field对象
	   * 
	   */
	  public static Field getAssignField(Class<?> clazz , String field){
		  
		  try {
			Field fil = clazz.getDeclaredField(field);
			return fil ;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			if(clazz.getSuperclass() != null){
				return getAssignField(clazz.getSuperclass(),field);
			}
			
		}
		  return null;
	  }
	
}
