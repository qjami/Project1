package cn.app.config;

public class Constant {
	// APP_ID 替换为你的应用从官方网站申请到的合法appId这是微信的APPId。
	public static final String APP_ID = "wx27d7c793d381d7bf";
	/**URL环境配置*/
	public static final String KEY_CONFIG_CONDITION = "CONDITION_FLAG";
	//月费套餐判断标识
	public static final String MONTHLY_TYPE = "MONTHLY";
	//月费套餐判断标识
	public static final String KEY_HAS_MONTHLY = "HAS_MONTHLY";
	//标识是否要展示第一次进入充电页面展示用户引导
	public static final String KEY_HAS_FIRST_CHARGE = "HAS_FIRST_CHARGE_282";
	public static int first = -1;
	public static String choosed_language = "zh_CN";
	// 当前的版本号
	public static int versionCode = 0;
	public static String versionName = "";
	// 从数据库中获得的时间
	public static String timeStamp;
	public static final String TencentID = "1104832237";
	public static final String ServiceTel = "400-1800910";
	/***************************businessCode***************************/
	public static final int BUSINESS_CODE_USER_INFO = 7;
	/***************************SP KEY ***************************/
	/**用户id key*/
	public static final String STRING_USER_ID = "user_id";
    /**用户user key*/
    public static final String STRING_USER_KEY = "user_key";
/***************************INTENT KEY ***************************/
    /**昵称KEY*/
    public static final String KEY_NICK_NAME = "nick_name";
}
