package com.qa.Booking.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.testng.asserts.SoftAssert;

import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.InvalidBooking;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BookingUtils {
	
	Response response;
	SoftAssert softAssert;
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
    
    public void printSection(String message) {
		System.out.println();
        System.out.println("\n================= " + message + " ==================");
    }
	
    public void verifyBookingDetails_Get_Put_Response(JsonPath json, Booking booking, SoftAssert softAssert) {
        softAssert.assertEquals(json.getString("firstname"), booking.getFirstname());
        softAssert.assertEquals(json.getString("lastname"), booking.getLastname());
        softAssert.assertEquals(json.getDouble("totalprice"), booking.getTotalprice());
        softAssert.assertEquals(json.getBoolean("depositpaid"), booking.isDepositpaid());
        softAssert.assertEquals(json.getString("bookingdates.checkin"), booking.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("bookingdates.checkout"), booking.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("additionalneeds"), booking.getAdditionalneeds());
    }
	
    public void verifyBookingDetails_Post_Response(JsonPath json, Booking expected, SoftAssert softAssert) {
        softAssert.assertEquals(json.getString("booking.firstname"), expected.getFirstname());
        softAssert.assertEquals(json.getString("booking.lastname"), expected.getLastname());
        softAssert.assertEquals(json.getDouble("booking.totalprice"), expected.getTotalprice());
        softAssert.assertEquals(json.getBoolean("booking.depositpaid"), expected.isDepositpaid());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkin"), expected.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkout"), expected.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("booking.additionalneeds"), expected.getAdditionalneeds());
    }
    
    public Map<String, Object> convertToMap(InvalidBooking booking) {
        //Map<String, Object> bookingMap = new LinkedHashMap<>();
    	Map<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("firstname", booking.getFirstname());
        bookingMap.put("lastname", booking.getLastname());
        bookingMap.put("totalprice", booking.getTotalprice());
        bookingMap.put("depositpaid", booking.getDepositpaid());

        // Nested bookingdates
        if (booking.getBookingdates() != null) {
            //Map<String, Object> bookingDatesMap = new LinkedHashMap<>();
        	Map<String, Object> bookingDatesMap = new HashMap<>();
            bookingDatesMap.put("checkin", booking.getBookingdates().getCheckin());
            bookingDatesMap.put("checkout", booking.getBookingdates().getCheckout());
            bookingMap.put("bookingdates", bookingDatesMap);
        }
        bookingMap.put("additionalneeds", booking.getAdditionalneeds());

        return bookingMap;
    }
}
