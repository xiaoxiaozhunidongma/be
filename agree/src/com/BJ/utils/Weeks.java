package com.BJ.utils;

import android.util.Log;

public class Weeks {
	private static String week;
	public static void CaculateWeekDay(int y, int m, int d) {
			 if(m==1||m==2) {
			        m+=12;
			        y--;
			    }
			    int iWeek=(d+2*m+3*(m+1)/5+y+y/4-y/100+y/400)%7;
			    switch(iWeek)
			    {
			    case 0: 
			    	Log.e("OkPartyActivity", "星期1");
			    	week = "星期一";
			    	break;
			    case 1: 
			    	Log.e("OkPartyActivity", "星期2");
			    	week = "星期二";
			    	break;
			    case 2: 
			    	Log.e("OkPartyActivity", "星期3");
			    	week = "星期三";
			    	break;
			    case 3: 
			    	Log.e("OkPartyActivity", "星期4");
			    	week = "星期四";
			    	break;
			    case 4:
			    	Log.e("OkPartyActivity", "星期5");
			    	week = "星期五";
			    	break;
			    case 5:
			    	Log.e("OkPartyActivity", "星期6");
			    	week = "星期六";
			    	break;
			    case 6: 
			    	Log.e("OkPartyActivity", "星期7");
			    	week = "星期日";
			    	break;
			    }
		}
	
	public static String getweek()
	{
		return week;
	}
}

