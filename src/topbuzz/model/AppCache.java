package topbuzz.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class AppCache {
	private static String account = "";
	private static String password = "";
	private static String forderPath = "";
	
	
	public static void setAccount(String account) {
		AppCache.account = account;
	}
	
	public static void setPassword(String password) {
		AppCache.password = password;
	}
	
	public static void setForderPath(String forderPath) {
		AppCache.forderPath = forderPath;
	}
	
	public static String getAccount() {
		return account;
	}
	
	public static String getPassWord() {
		return password;
	}
	
	public static String getForderPath() {
		return forderPath;
	}
	
	public static void save() {	
		JSONObject config = Config.toJSONObject();
		JSONObject appCache = (JSONObject) config.get("cache");
		appCache.replace("account", AppCache.account);
		appCache.replace("password", AppCache.password);
		appCache.replace("forderPath", AppCache.forderPath);
		config.replace("cache", appCache);
		
		if(Config.toJSONFile()) {
			System.out.println("Succesfull");
		}else {
			System.out.println("Error");
		}
		
	}
	
	public static void load() {
		JSONObject config = Config.toJSONObject();
		JSONObject appCache = (JSONObject) config.get("cache");
		AppCache.account = appCache.get("account").toString();
		AppCache.password = appCache.get("password").toString();
		AppCache.forderPath = appCache.get("forderPath").toString();
	}
	
}
