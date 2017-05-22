package com.lhc.shop.utils;
/**
 * 类描述：
 * @author liaoyu
 * 时间：2015年9月7日
 */
public enum DefaultMarkInterface {
	MARK0(0),MARK1(1),MARK2(2)
	,MARK3(3),MARK4(4),MARK5(5)
	,MARK6(6),MARK7(7),MARK8(8),MARK9(9);
	
	private DefaultMarkInterface(int a){
		this.mark = a;
	}
	
	private int mark;
	public Class<?> getMarkInterface(){
		switch (this) {
		case MARK0:
			return Jsonfilter.class;
		case MARK1:
			return Jsonfilter1.class;
		case MARK2:
			return Jsonfilter2.class;
		case MARK3:
			return Jsonfilter3.class;
		case MARK4:
			return Jsonfilter4.class;
		case MARK5:
			return Jsonfilter5.class;
		case MARK6:
			return Jsonfilter6.class;
		case MARK7:
			return Jsonfilter7.class;
		case MARK8:
			return Jsonfilter8.class;
		case MARK9:
			return Jsonfilter9.class;

		default:
			return null;
		}
		
	}
	
	
	public String getFilterName(){
		switch (mark) {
		case 0:
			return "filter0";
		case 1:
			return "filter1";
		case 2:
			return "filter2";
		case 3:
			return "filter3";
		case 4:
			return "filter4";
		case 5:
			return "filter5";
		case 6:
			return "filter6";
		case 7:
			return "filter7";
		case 8:
			return "filter8";
		case 9:
			return "filter9";

		default:
			return null;
		}
	}
}
