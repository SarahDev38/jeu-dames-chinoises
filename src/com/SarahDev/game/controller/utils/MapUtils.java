package com.SarahDev.game.controller.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtils {

	public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue(Collections.reverseOrder()));

		Map<String, Integer> result = new LinkedHashMap<>();
		for (Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
