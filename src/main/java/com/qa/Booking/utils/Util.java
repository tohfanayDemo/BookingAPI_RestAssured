package com.qa.Booking.utils;

import java.util.Random;

import io.restassured.response.Response;

public class Util {

	public static String getRandomEmailId() {
		return "api"+System.currentTimeMillis()+"@api.com";
	}
	
	public static int generateRandomNumber(Response response) {
    	
    	int responseSize = response.jsonPath().getList("$").size();
    	if(responseSize==1) {
    		return 0;
    	}
    	else {
    		Random random = new Random();
    		 return random.nextInt(responseSize - 1) + 1;
    	}
    }
    
    public static void printSection(String message) {
		System.out.println();
        System.out.println("\n================= " + message + " ==================");
    }
	
}
