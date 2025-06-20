package com.qa.Booking.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonDataReader { //need jackson-databind maven dependency
	
	/**
     * Reads structured request data from a JSON file.
     * Preserves original types (String, Boolean, Integer, Map, etc.)
     */
    public static Map<String, List<Map<String, Object>>> readRequestData(String filePath) {
        Map<String, List<Map<String, Object>>> requestDataMap = new LinkedHashMap<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));

            JsonNode requests = root.get(0).get("requests");
            for (JsonNode requestNode : requests) {
                String requestName = requestNode.get("name").asText();
                JsonNode dataArray = requestNode.get("data");

                List<Map<String, Object>> scenarioList = new ArrayList<>();

                for (JsonNode scenario : dataArray) {
                    // Convert each scenario JSON object to Map<String, Object> with type-preservation
                    @SuppressWarnings("unchecked")
					Map<String, Object> scenarioMap = mapper.convertValue(scenario, Map.class);
                    scenarioList.add(scenarioMap);
                }

                requestDataMap.put(requestName, scenarioList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestDataMap;
    }

    /**
     * Returns list of data maps for a given request name.
     */
    public static List<Map<String, Object>> getDataByRequestName(Map<String, List<Map<String, Object>>> allData, String requestName) {
        if (!allData.containsKey(requestName)) {
            System.err.println("Request name not found: " + requestName);
        }
        return allData.getOrDefault(requestName, Collections.emptyList());
    }
    
    
    
    public static Object[][] convertMapListTo2DArray(List<Map<String, Object>> dataList) {
        // Define keys in the order matching your test method parameters
        List<String> keysInOrder = Arrays.asList(
            "scenarioName",
            "firstname",
            "lastname",
            "totalprice",
            "depositpaid",
            "checkin",
            "checkout",
            "additionalneeds",
            "statusCode",
            "statusLine",
            "message"
        );

        Object[][] data = new Object[dataList.size()][keysInOrder.size()];

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> rowMap = dataList.get(i);
            for (int j = 0; j < keysInOrder.size(); j++) {
                data[i][j] = rowMap.getOrDefault(keysInOrder.get(j), null);
            }
        }

        return data;
    }

    
    
}
