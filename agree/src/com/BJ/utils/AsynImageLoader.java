package com.BJ.utils;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fragment.PhotoFragment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsynImageLoader {
	private static final String TAG = "AsynImageLoader";
	public static String CACHE_DIR="AsynImageLoader";
	// 缓存下载过的图片的Map
	private Map<String, SoftReference<Bitmap>> caches;
	// 任务队列
	private List<Task> taskQueue;
	private boolean isRunning = false;
	
	public AsynImageLoader(){
		// 初始化变量
		caches = new HashMap<String, SoftReference<Bitmap>>();
		taskQueue = new ArrayList<AsynImageLoader.Task>();
		// 启动图片下载线程
		isRunning = true;
		new Thread(runnable).start();
	}
	
	   public static Bitmap readBitMap(Context context, int resId){  
	        BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	       opt.inPurgeable = true;  
	       opt.inInputShareable = true;  
	          //获取资源图片  
	       InputStream is = context.getResources().openRawResource(resId);  
	           return BitmapFactory.decodeStream(is,null,opt);  
	    }
	
	/**
	 * 
	 * @param imageView 需要延迟加载图片的对象
	 * @param url 图片的URL地址
	 * @param resId 图片加载过程中显示的图片资源
	 */
	public void showImageAsyn(ImageView imageView, String url, int resId,Context context){
		imageView.setTag(url);
		Bitmap bitmap = loadImageAsyn(url, getImageCallback(imageView, resId, context));
//		Log.e("AsynImageLoader", "这时候的url========="+url);
		if(bitmap == null){
			Bitmap readBitMap = readBitMap(context, resId);
			imageView.setImageBitmap(readBitMap);
//			imageView.setImageResource(resId);
		}else{
			imageView.setImageBitmap(bitmap);
		}
	}
	public void showRoudImageAsyn(ImageView imageView, String url, int resId,Context context){
		imageView.setTag(url);
		Bitmap bitmap = loadImageAsyn(url, getRoudImageCallback(imageView, resId, context));
		if(bitmap == null){
			Bitmap readBitMap = readBitMap(context, resId);
			Bitmap initHead = InitHead.initHead(readBitMap);
			imageView.setImageBitmap(initHead);
			readBitMap.recycle();
			initHead.recycle();
//			imageView.setImageResource(resId);
		}else{
			Bitmap initHead = InitHead.initHead(bitmap);
			imageView.setImageBitmap(initHead);
			initHead.recycle();
		}
	}
	
	public Bitmap loadImageAsyn(String path, ImageCallback callback){
//		Log.e("AsynImageLoader", "这时候的path========="+path);
		// 判断缓存中是否已经存在该图片
		if(caches.containsKey(path)){
//			Log.e("AsynImageLoader", "进入caches包含该路径的地方=========");
			// 取出软引用
			SoftReference<Bitmap> rf = caches.get(path);
			// 通过软引用，获取图片
			Bitmap bitmap = rf.get();
			// 如果该图片已经被释放，则将该path对应的键从Map中移除掉
			if(bitmap == null){
				caches.remove(path);
			}else{
				// 如果图片未被释放，直接返回该图片
				Log.i(TAG, "return image in cache" + path);
//				Log.e("AsynImageLoader", "进入caches不为null的地方=========");
				return bitmap;
			}
		}else{
//			Log.e("AsynImageLoader", "进入caches不包含该路径的地方=========");
			// 如果缓存中不常在该图片，则创建图片下载任务
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			Log.i(TAG, "new Task ," + path);
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				// 唤醒任务下载队列
				synchronized (runnable) {
					runnable.notify();
				}
			}
		}
//		Log.e("AsynImageLoader", "都没进入=========");
		return null;
	}
	/**
	 * 
	 * @param imageView 
	 * @param resId 图片加载完成前显示的图片资源ID
	 * @return
	 */
	private ImageCallback getImageCallback(final ImageView imageView, final int resId,final Context context){
		return new ImageCallback() {
			
			@Override
			public void loadImage(String path, Bitmap bitmap) {
				if(path.equals(imageView.getTag().toString())){
					imageView.setImageBitmap(bitmap);
				}else{
					Bitmap readBitMap = readBitMap(context, resId);
					imageView.setImageBitmap(readBitMap);
					readBitMap.recycle();
//					imageView.setImageResource(resId);
				}
			}
		};
	}
	private ImageCallback getRoudImageCallback(final ImageView imageView, final int resId,final Context context){
		return new ImageCallback() {
			
			@Override
			public void loadImage(String path, Bitmap bitmap) {
				if(path.equals(imageView.getTag().toString())){
					Bitmap initHead = InitHead.initHead(bitmap);
					imageView.setImageBitmap(initHead);
				}else{
					Bitmap readBitMap = readBitMap(context, resId);
					imageView.setImageBitmap(readBitMap);
					readBitMap.recycle();
//					imageView.setImageResource(resId);
				}
			}
		};
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// 子线程中返回的下载完成的任务
			Task task = (Task)msg.obj;
			// 调用callback对象的loadImage方法，并将图片路径和图片回传给adapter
			task.callback.loadImage(task.path, task.bitmap);
		}
		
	};
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			while(isRunning){
				// 当队列中还有未处理的任务时，执行下载任务
				while(taskQueue.size() > 0){
					// 获取第一个任务，并将之从任务队列中删除
					Task task = taskQueue.remove(0);
					// 将下载的图片添加到缓存
					task.bitmap = PicUtil.getbitmap(task.path);
					caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
					if(handler != null){
						// 创建消息对象，并将完成的任务添加到消息对象中
						Message msg = handler.obtainMessage();
						msg.obj = task;
						// 发送消息回主线程
						handler.sendMessage(msg);
					}
				}
				
				//如果队列为空,则令线程等待
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	//回调接口
	public interface ImageCallback{
		void loadImage(String path, Bitmap bitmap);
	}
	
	class Task{
		// 下载任务的下载路径
		String path;
		// 下载的图片
		Bitmap bitmap;
		// 回调对象
		ImageCallback callback;
		
		@Override
		public boolean equals(Object o) {
			Task task = (Task)o;
			return task.path.equals(path);
		}
	}
}
