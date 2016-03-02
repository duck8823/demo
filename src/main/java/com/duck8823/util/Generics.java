package com.duck8823.util;

import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * ジェネリクスを扱う.
 * Created by maeda on 2016/02/21.
 */
public class Generics {
	private Generics() { /* singleton */ }

	/**
	 * 型パラメータを取得する.
	 *
	 * @param clazz ジェネリクスを含むクラス
	 * @return クラスの型パラメータ
	 */
	public static Class<?> getTypeParameter(Class<?> clazz) {
		return getTypeParameter(clazz, Object.class);
	}

	/**
	 * 型パラメータを取得する.
	 *
	 * @param clazz ジェネリクスを含むクラス
	 * @param bound 境界
	 * @param <T> 境界
	 * @return クラスの型パラメータ
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getTypeParameter(Class<?> clazz, Class<? super T> bound) {
		Assert.notNull(clazz);
		Type t = clazz;
		while (t instanceof Class<?>) {
			t = ((Class<?>) t).getGenericSuperclass();
		}
		if (t instanceof ParameterizedType) {
			for (Type param : ((ParameterizedType) t).getActualTypeArguments()) {
				if (param instanceof Class<?>) {
					final Class<T> cls = determineClass(bound, param);
					if (cls != null) { return cls; }
				}
				else if (param instanceof TypeVariable) {
					for (Type paramBound : ((TypeVariable<?>) param).getBounds()) {
						if (paramBound instanceof Class<?>) {
							final Class<T> cls = determineClass(bound, paramBound);
							if (cls != null) { return cls; }
						}
					}
				}
			}
		}
		throw new IllegalStateException("型パラメータを取得できませんでした: " + clazz.getName());
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> determineClass(Class<? super T> bound, Type candidate) {
		if (candidate instanceof Class<?>) {
			final Class<?> cls = (Class<?>) candidate;
			if (bound.isAssignableFrom(cls)) {
				return (Class<T>) cls;
			}
		}

		return null;
	}
}
