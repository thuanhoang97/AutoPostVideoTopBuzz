package topbuzz.model;

import java.io.File;
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
		File appCacheFile = new File("AppCache.json");
		if(!appCacheFile.exists()) {
			try {
				appCacheFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONObject appCache = new JSONObject();
		appCache.put("account", AppCache.account);
		appCache.put("password", AppCache.password);
		appCache.put("forderPath", AppCache.forderPath);
		
		try(FileWriter file = new FileWriter("AppCache.json")){
			file.write(appCache.toJSONString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void load() {
		JSONParser parser = new JSONParser();
		JSONObject appCache;
		try {
			appCache = (JSONObject) parser.parse(new FileReader("AppCache.json"));
			AppCache.account = appCache.get("account").toString();
			AppCache.password = appCache.get("password").toString();
			AppCache.forderPath = appCache.get("forderPath").toString();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
