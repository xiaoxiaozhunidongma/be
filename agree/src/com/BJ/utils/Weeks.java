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
			    	Log.e("OkPartyActivity", "����1");
			    	week = "����һ";
			    	break;
			    case 1: 
			    	Log.e("OkPartyActivity", "����2");
			    	week = "���ڶ�";
			    	break;
			    case 2: 
			    	Log.e("OkPartyActivity", "����3");
			    	week = "������";
			    	break;
			    case 3: 
			    	Log.e("OkPartyActivity", "����4");
			    	week = "������";
			    	break;
			    case 4:
			    	Log.e("OkPartyActivity", "����5");
			    	week = "������";
			    	break;
			    case 5:
			    	Log.e("OkPartyActivity", "����6");
			    	week = "������";
			    	break;
			    case 6: 
			    	Log.e("OkPartyActivity", "����7");
			    	week = "������";
			    	break;
			    }
		}
	
	public static String getweek()
	{
		return week;
	}
}

