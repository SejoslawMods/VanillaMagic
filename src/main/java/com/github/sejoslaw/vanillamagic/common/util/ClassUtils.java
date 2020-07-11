package com.github.sejoslaw.vanillamagic.common.util;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Class which store various methods connected with Class operaions.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ClassUtils {
	private ClassUtils() {
	}

	@Nullable
	public static Object getFieldObject(String className, String fieldName, boolean isStatic)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException {
		Class<?> clazz = Class.forName(className);
		Object instance = clazz.newInstance();
		Field field = clazz.getField(fieldName);

		return isStatic ? field.get(null) : field.get(instance);
	}
}