package com.lhc.utils;

/**
 * 
 * @desc 状态码
 * @author xh-lhc
 * @date 2017年4月6日
 *
 */
public interface ActiveStatusConstants {

	/**
	 * 通用：-999到99
	 */
	public enum Common implements ActiveStatusConstants {

		SYSTEM_ERROE("-999", "系统异常"), BREAK_DOWN("0", "失败"), SUCCESS("1", "成功"), NULL_TOKEN(
				"13", "token为空"), NULL_USERNAME("14", "用户名为空"), NOT_LOGGED_IN(
				"15", "用户未登录");

		public String status;
		public String message;

		private Common(String status, String message) {
			this.status = status;
			this.message = message;
		}
	}

	/**
	 * 活动前台返回码：100到199
	 */
	public enum Active implements ActiveStatusConstants {

		ACTIVE_END("100", "活动已结束"),
		ACTIVE_NOT_START("101", "活动暂未开始"),
		USER_NOT_LOTTERY_CHANCE("102", "用户没有抽奖机会，请去投资"),
		USER_LOTTERY_UPPER_LIMIT("103", "今日已达抽奖上限，请明日再来"),
		ACTIVE_START("104", "活动已开始");
		
		public String status;
		public String message;

		private Active(String status, String message) {
			this.status = status;
			this.message = message;
		}
	}

}
