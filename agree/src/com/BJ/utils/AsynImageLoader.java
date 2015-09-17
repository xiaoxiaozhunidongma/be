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
	// �������ع���ͼƬ��Map
	private Map<String, SoftReference<Bitmap>> caches;
	// �������
	private List<Task> taskQueue;
	private boolean isRunning = false;
	
	public AsynImageLoader(){
		// ��ʼ������
		caches = new HashMap<String, SoftReference<Bitmap>>();
		taskQueue = new ArrayList<AsynImageLoader.Task>();
		// ����ͼƬ�����߳�
		isRunning = true;
		new Thread(runnable).start();
	}
	
	   public static Bitmap readBitMap(Context context, int resId){  
	        BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	       opt.inPurgeable = true;  
	       opt.inInputShareable = true;  
	          //��ȡ��ԴͼƬ  
	       InputStream is = context.getResources().openRawResource(resId);  
	           return BitmapFactory.decodeStream(is,null,opt);  
	    }
	
	/**
	 * 
	 * @param imageView ��Ҫ�ӳټ���ͼƬ�Ķ���
	 * @param url ͼƬ��URL��ַ
	 * @param resId ͼƬ���ع�������ʾ��ͼƬ��Դ
	 */
	public void showImageAsyn(ImageView imageView, String url, int resId,Context context){
		imageView.setTag(url);
		Bitmap bitmap = loadImageAsyn(url, getImageCallback(imageView, resId, context));
//		Log.e("AsynImageLoader", "��ʱ���url========="+url);
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
//		Log.e("AsynImageLoader", "��ʱ���path========="+path);
		// �жϻ������Ƿ��Ѿ����ڸ�ͼƬ
		if(caches.containsKey(path)){
//			Log.e("AsynImageLoader", "����caches������·���ĵط�=========");
			// ȡ��������
			SoftReference<Bitmap> rf = caches.get(path);
			// ͨ�������ã���ȡͼƬ
			Bitmap bitmap = rf.get();
			// �����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ���Map���Ƴ���
			if(bitmap == null){
				caches.remove(path);
			}else{
				// ���ͼƬδ���ͷţ�ֱ�ӷ��ظ�ͼƬ
				Log.i(TAG, "return image in cache" + path);
//				Log.e("AsynImageLoader", "����caches��Ϊnull�ĵط�=========");
				return bitmap;
			}
		}else{
//			Log.e("AsynImageLoader", "����caches��������·���ĵط�=========");
			// ��������в����ڸ�ͼƬ���򴴽�ͼƬ��������
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			Log.i(TAG, "new Task ," + path);
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				// �����������ض���
				synchronized (runnable) {
					runnable.notify();
				}
			}
		}
//		Log.e("AsynImageLoader", "��û����=========");
		return null;
	}
	/**
	 * 
	 * @param imageView 
	 * @param resId ͼƬ�������ǰ��ʾ��ͼƬ��ԴID
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
			// ���߳��з��ص�������ɵ�����
			Task task = (Task)msg.obj;
			// ����callback�����loadImage����������ͼƬ·����ͼƬ�ش���adapter
			task.callback.loadImage(task.path, task.bitmap);
		}
		
	};
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			while(isRunning){
				// �������л���δ���������ʱ��ִ����������
				while(taskQueue.size() > 0){
					// ��ȡ��һ�����񣬲���֮�����������ɾ��
					Task task = taskQueue.remove(0);
					// �����ص�ͼƬ��ӵ�����
					task.bitmap = PicUtil.getbitmap(task.path);
					caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
					if(handler != null){
						// ������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
						Message msg = handler.obtainMessage();
						msg.obj = task;
						// ������Ϣ�����߳�
						handler.sendMessage(msg);
					}
				}
				
				//�������Ϊ��,�����̵߳ȴ�
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
	
	//�ص��ӿ�
	public interface ImageCallback{
		void loadImage(String path, Bitmap bitmap);
	}
	
	class Task{
		// �������������·��
		String path;
		// ���ص�ͼƬ
		Bitmap bitmap;
		// �ص�����
		ImageCallback callback;
		
		@Override
		public boolean equals(Object o) {
			Task task = (Task)o;
			return task.path.equals(path);
		}
	}
}
