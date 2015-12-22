package com.project.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	/**
	 * Convert jsonString to jsonMap
	 * 
	 * @param jsonText
	 * @return
	 */
	public static Map<String, Object> Json2Map(String jsonText) {
		
		Map<String, Object> objMap = new HashMap<String, Object>();

		try {

			ObjectMapper mapper = new ObjectMapper();
			objMap = mapper.readValue(jsonText, Map.class);
			StringWriter writer = new StringWriter();
			JsonGenerator  gen = new JsonFactory().createJsonGenerator(writer);   
			mapper.writeValue(gen, jsonText); 
			writer.close(); 

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objMap;
	}

	/**
	 * Convert jsonMap to jsonString
	 * 
	 * @param jsonMap
	 * @return
	 */
	public static String Map2Json(Map<String, Object> jsonMap) {

		String jsonText = "";

		try {

			ObjectMapper mapper = new ObjectMapper();

			jsonText = mapper.writeValueAsString(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonText;
	}
	
	public static List<LinkedHashMap<String, Object>> json2List(String jsonText){
		
		List<LinkedHashMap<String, Object>> list;
		
		try {

			ObjectMapper mapper = new ObjectMapper();
			list = mapper.readValue(jsonText, List.class);  
			StringWriter writer = new StringWriter();
			JsonGenerator  gen = new JsonFactory().createJsonGenerator(writer);   
			mapper.writeValue(gen, jsonText); 
			writer.close(); 

			
			return list;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//	public static Map<String , Object> json2keyList(String jsonText){
//		Map<String , Object> map = new HashMap<String, Object>();
//		JSONArray array;
//		try {
//			array = new JSONArray(jsonText);
//			for(int i=0 ; i<array.length() ; i++){
//				JSONObject entityObj = array.getJSONObject(i); 
//			}
//			return map;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
}
