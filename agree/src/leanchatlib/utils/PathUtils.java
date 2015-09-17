package leanchatlib.utils;

import android.os.Environment;

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
	 * �� sdcard ��ʱ��С���� /storage/sdcard0/Android/data/com.avoscloud.chat/cache/
	 * �� sdcard ��ʱ��С���� /data/data/com.avoscloud.chat/cache
	 * �����ڰ��������Բ�ͬӦ��ʹ�øÿ�Ҳû���⣬Ҫ�е����롣
	 * 
	 * @return
	 */
	private static File getAvailableCacheDir() {
		if (isExternalStorageWritable()) {
			return new File("/storage/sdcard0/Android/data/com.example.testleabcloud/cache/");
		} else {
			// ֻ�д�Ӧ�ò��ܷ��ʡ����յ�ʱ�������⣬��Ϊ���յ�Ӧ��д�벻�˸��ļ�
			return ChatManager.getContext().getCacheDir();
		}
	}

	/**
	 * �����ļ��ᱻ���������Ҫ����Ƿ����
	 *
	 * @param id
	 * @return
	 */
	public static String getChatFilePath(String id) {
		String path = new File(getAvailableCacheDir(), id).getAbsolutePath();
		// LogUtils.d("path = ", path);
		return path;
	}

	/**
	 * ¼������ĵ�ַ
	 *
	 * @return
	 */
	public static String getRecordPathByCurrentTime() {
		return new File(getAvailableCacheDir(), "record_"
				+ System.currentTimeMillis()).getAbsolutePath();
	}

	/**
	 * ���ձ���ĵ�ַ
	 *
	 * @return
	 */
	public static String getPicturePathByCurrentTime() {
		String path = new File(getAvailableCacheDir(), "picture_"
				+ System.currentTimeMillis()).getAbsolutePath();
		// LogUtils.d("picture path ", path);
		return path;
	}
}
