package com.github.volley_examples.app;

/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Helper class that is used to provide references to initialized
 * RequestQueue(s) and ImageLoader(s)
 * 
 * @author Ognyan Bankov
 * 
 */
public class MyVolley {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private MyVolley() {
		// no instances
	}

	static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
				cacheSize));
	}
	
	public static void post(Context context, String url, final Map<String, String> params, 
			final VolleyListenner listener) {
		StringRequest myReq = new StringRequest(Method.POST, url,
				new Listener<String>() {
			public void onResponse(String response) {
				listener.onResponse(response);
			}
		}, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				listener.onErrorResponse(error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				
				return params;
			}
			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String parsed;
				try {
					// 解决Volley下载中文乱码问题
					parsed = new String(response.data, "utf-8");
				} catch (UnsupportedEncodingException e) {
					parsed = new String(response.data);
				}
				return Response.success(parsed,
						HttpHeaderParser.parseCacheHeaders(response));
			}
		};
		if (mRequestQueue == null) {
			init(context);
		}
		mRequestQueue.add(myReq);
	}

	public static void get(Context context, String url,		//封装的方法
			final VolleyListenner listenner) {
		StringRequest myReq = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						listenner.onResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						listenner.onErrorResponse(error);
					}
				}) {
					@Override
					protected Response<String> parseNetworkResponse(
							NetworkResponse response) {
				        String parsed;
				        try {
				            parsed = new String(response.data,"utf-8");//乱码问题，改为utf-8
//				            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
				        } catch (UnsupportedEncodingException e) {
				            parsed = new String(response.data);
				        }
				        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
				    }
		};

		if (mRequestQueue == null) {//初始化mRequestQueue
			init(context);
		}
		mRequestQueue.add(myReq);// 添加对象!
	}

	public static RequestQueue getRequestQueue(Context context) {
		// if(mRequestQueue==null){
		// init(context);
		// }
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/**
	 * Returns instance of ImageLoader initialized with {@see FakeImageCache}
	 * which effectively means that no memory caching is used. This is useful
	 * for images that you know that will be show only once.
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
