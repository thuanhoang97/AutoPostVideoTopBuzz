package topbuzz.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {
	private static JSONObject config;
	
	public static JSONObject toJSONObject() {
		JSONParser parser = new JSONParser();
		try {
			config = (JSONObject) parser.parse(new FileReader("config.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}
	
	public static boolean toJSONFile() {
		try(FileWriter file = new FileWriter("config.json")){
			file.write(config.toJSONString());
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
}
