package com.github.volley_examples.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

  public final class GsonUtils {//工具类不继承final

	public static  <T> T parseJson(String json,Class<T> clazz) {
		Gson gson=new Gson();
		 T fromJson = gson.fromJson(json, clazz);
		return fromJson;
	}
/**
 * Type type=new TypeToken<ArrayList<TypeInfo>>(){}.gettype
 * 
 * @param jsonArray
 * @param type
 * @return
 */
	public static  <T> T parseJsonArray(String jsonArray,Type type) {
		Gson gson=new Gson();
		T infos = gson.fromJson(jsonArray, type);
		return infos;
	}
	private GsonUtils(){};//私有化
}
