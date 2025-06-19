package com.qa.Booking.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonDataReader { //need jackson-databind maven dependency

	public static Map<String, List<Map<String, String>>> readRequestData(String filePath) {
        Map<String, List<Map<String, String>>> requestDataMap = new LinkedHashMap<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));

            JsonNode requests = root.get(0).get("requests");
            for (JsonNode requestNode : requests) {
                String requestName = requestNode.get("name").asText();
                JsonNode dataArray = requestNode.get("data");

                List<Map<String, String>> scenarioList = new ArrayList<>();

                for (JsonNode scenario : dataArray) {
                    Map<String, String> scenarioMap = new LinkedHashMap<>();
                    scenario.fields().forEachRemaining(entry -> {
                        scenarioMap.put(entry.getKey(), entry.getValue().asText(""));
                    });
                    scenarioList.add(scenarioMap);
                }

                requestDataMap.put(requestName, scenarioList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestDataMap;
    }
	
	public static List<Map<String, String>> getDataByRequestName(Map<String, List<Map<String, String>>> allData, String requestName) {
	    
		 if (!allData.containsKey(requestName)) {
	            System.err.println("Request name not found: " + requestName);
	        }
	
		return allData.getOrDefault(requestName, Collections.emptyList());
	}
}
