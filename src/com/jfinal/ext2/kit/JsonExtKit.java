/**
 * Json extension Kit
 */
package com.jfinal.ext2.kit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author BruceZCQ
 *
 */
public class JsonExtKit {

	/**
	 * json string to JSONObject
	 * @param json
	 * @return
	 */
	public static JSONObject jsonToObject(String json){
		return JSON.parseObject(json);
	}
	
	/**
	 * json string to map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> jsonToMap(String json){
		JSONObject obj = jsonToObject(json);
		Map<K, V> map = new HashMap<K, V>();
		Iterator<String> keyIterator = obj.keySet().iterator();
		while (keyIterator.hasNext()) {
			Object key = keyIterator.next();
			Object value = obj.get(key);
			if (value instanceof JSONObject) {
				value = jsonToMap(((JSONObject) value).toJSONString());
			}
			map.put((K)key, (V)value);
		}	
		return map;
	}
	
	/**
	 * json String to Model<T extends Model<T>>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T extends Model<T>> Model<T> jsonToModel(String json, Class<T> clazz){
		Model<T> model = null;
		try {
			model = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Map<String, Object> attrs = jsonToMap(json);
		return model.setAttrs(attrs);
	}

	/**
	 * json to Record
	 * @param json
	 * @return
	 */
	public static Record jsonToRecord(String json){
		Map<String,Object> map = jsonToMap(json);
		return new Record().setColumns(map);
	}
}