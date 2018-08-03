package topbuzz.model;

import org.json.simple.JSONObject;

public class HtmlElements {
	public static String account;
	public static String password;
	public static String signinMode;
	public static String btnSigninByEmail;
	public static String btnSignin;
	public static String videoPostUrl;
	public static String btnFileUpload;
	public static String errorLogin;
	public static String titleVideo;
	public static String btnPublish;
	
	public static void load() {
		JSONObject config = Config.toJSONObject();
		JSONObject htmlElements = (JSONObject) config.get("htmlElements");
		account = htmlElements.get("account").toString();
		password = htmlElements.get("password").toString();
		signinMode = htmlElements.get("signinMode").toString();
		btnSigninByEmail = htmlElements.get("btnSigninByEmail").toString();
		btnSignin = htmlElements.get("btnSignin").toString();
		videoPostUrl = htmlElements.get("videoPostUrl").toString();
		btnFileUpload = htmlElements.get("btnFileUpload").toString();
		errorLogin = htmlElements.get("errorLogin").toString();
		titleVideo = htmlElements.get("titleVideo").toString();
		btnPublish = htmlElements.get("btnPublish").toString();
	}
}
