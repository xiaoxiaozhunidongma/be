package com.BJ.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class RefreshActivity {
	//删除小组时销毁的界面容器
	public static List<Activity>  activList_1=new ArrayList<Activity>();
	//删除日程时销毁的界面容器
	public static List<Activity>  activList_2=new ArrayList<Activity>();
	//销毁登录后之前的所有界面1
	public static List<Activity>  activList_3=new ArrayList<Activity>();
}
