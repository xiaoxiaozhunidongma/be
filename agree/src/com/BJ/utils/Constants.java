package com.BJ.utils;

public class Constants {
	public static final int DIALOG_SINGLE_Size=0;
	public static final int DIALOG_SINGLE_NoWifi=1;
	public static final int DIALOG_SINGLE_Clear=2;
	public static final class Settings {
		public static final String FILE_NAME = "ImageCache";
		public static final String KEY_LIST_MODE = "list_mode_show_summary";
		public static final String KEY_WIFI_MODE = "wifi_mode_load_image";
		public static final int WIFI_MODE_LOAD_SRC_IMG = 0;
		public static final int WIFI_MODE_LOAD_SMALL_IMG = 1;
		public static final int WIFI_MODE_LOAD_NO_IMG = 2;
	}
	public static final class BroadcastKey{
		public static final String ACTION_REFRESHING="www.titlebar.refresh";
		public static final String ACTION_BanToMain="www.Banner.refresh";
	}
	public static final class UrlPath{
		public static final String ROOT="http://7xilef.com1.z0.glb.clouddn.com/";
		public static final String TUIJIAN=ROOT+"tuijian.txt";
		public static final String REDIAN=ROOT+"redian.txt";
		public static final String GUOJI=ROOT+"guoji.txt";
		
		public static final String[] URL_TYPE=new String[]{
			TUIJIAN,REDIAN,GUOJI,
			TUIJIAN,REDIAN,GUOJI,
			TUIJIAN,REDIAN,GUOJI,
			TUIJIAN,REDIAN,GUOJI
		};
	}
}
