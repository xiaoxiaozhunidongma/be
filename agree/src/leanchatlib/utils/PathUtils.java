package leanchatlib.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import leanchatlib.controller.ChatManager;

/**
 * Created by lzw on 15/4/26.
 */
public class PathUtils {
	private static File checkAndMkdirs(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * 有 sdcard 的时候，小米是 /storage/sdcard0/Android/data/com.avoscloud.chat/cache/
	 * 无 sdcard 的时候，小米是 /data/data/com.avoscloud.chat/cache
	 * 依赖于包名。所以不同应用使用该库也没问题，要有点理想。
	 * 
	 * @return
	 */
	private static File getAvailableCacheDir() {
		if (isExternalStorageWritable()) {
			//这里路径以后不能写死要修改
			return new File(getSDPath());
		} else {
			// 只有此应用才能访问。拍照的时候有问题，因为拍照的应用写入不了该文件
			return ChatManager.getContext().getCacheDir();
		}
	}
	//获取路径
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	/**
	 * 可能文件会被清除掉，需要检查是否存在
	 *
	 * @param id
	 * @return
	 */
	public static String getChatFilePath(String id) {
		String path = new File(getAvailableCacheDir(), id).getAbsolutePath();
		// LogUtils.d("path = ", path);
//		Log.e("path = ", path);
		return path;
	}

	/**
	 * 录音保存的地址
	 *
	 * @return
	 */
	public static String getRecordPathByCurrentTime() {
		return new File(getAvailableCacheDir(), "record_"
				+ System.currentTimeMillis()).getAbsolutePath();
	}

	/**
	 * 拍照保存的地址
	 *
	 * @return
	 */
	public static String getPicturePathByCurrentTime() {
		String path = new File(getAvailableCacheDir(), "picture_"
				+ System.currentTimeMillis()).getAbsolutePath();
//		 LogUtils.d("picture path ", path);
		 Log.e("picture path ", path);
		return path;
	}
}
