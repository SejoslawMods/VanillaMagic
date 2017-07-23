package seia.vanillamagic.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * Class which store various methods connected with Class operaions.
 */
public class ClassUtils 
{
	private ClassUtils()
	{
	}
	
	public static Field getField(String className, String fieldName) 
			throws ClassNotFoundException, NoSuchFieldException, SecurityException
	{
		return getField(className, fieldName, true);
	}
	
	public static Field getField(String className, String fieldName, boolean accessible)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException
	{
		Class<?> clazz = Class.forName(className);
		Field field = clazz.getField(fieldName);
		field.setAccessible(accessible);
		return field;
	}
	
	@Nullable
	public static Object getFieldObject(String className, String fieldName, boolean isStatic)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException
	{
		Class<?> clazz = Class.forName(className);
		Object instance = clazz.newInstance();
		Field field = clazz.getField(fieldName);
		
		if (isStatic) return field.get(null);
		else return field.get(instance);
	}
	
	@SuppressWarnings("rawtypes")
	public static Method getMethod(String className, String methodName, Class[] params) 
			throws ClassNotFoundException, NoSuchMethodException, SecurityException
	{
		Class<?> clazz = Class.forName(className);
		return clazz.getMethod(methodName, params);
	}
	
	@SuppressWarnings("rawtypes")
	@Nullable
	public static Object getMethodReturn(String className, String methodName, Class[] params, Object[] args, boolean isStatic)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException
	{
		Class<?> clazz = Class.forName(className);
		Object instance = clazz.newInstance();
		Method method = clazz.getMethod(methodName, params);
		
		if (isStatic) return method.invoke(null, args);
		else return method.invoke(instance, args);
	}
	
	public static boolean isClassImplementingInterface(String className, String interfaceName) 
			throws ClassNotFoundException
	{
		Class<?> clazz = Class.forName(className);
		return clazz.isAssignableFrom(Class.forName(interfaceName));
	}
	
	public static Object getFieldFromStaticInstance(String className, String fieldName) 
			throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Class<?> clazz = Class.forName(className);
		Object INSTANCE = clazz.getField("INSTANCE").get(null);
		return clazz.getField(fieldName).get(INSTANCE);
	}
}