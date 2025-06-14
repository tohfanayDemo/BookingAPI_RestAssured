package com.qa.Booking.utils;

import java.util.Random;

import org.testng.asserts.SoftAssert;

import com.qa.Booking.pojo.Booking;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BookingHelper {
	
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
	
    public void verifyBookingDetails_Get_Put_Response(JsonPath json, Booking expected) {
		softAssert = new SoftAssert();
        softAssert.assertEquals(json.getString("firstname"), expected.getFirstname());
        softAssert.assertEquals(json.getString("lastname"), expected.getLastname());
        softAssert.assertEquals(json.getDouble("totalprice"), expected.getTotalprice());
        softAssert.assertEquals(json.getBoolean("depositpaid"), expected.isDepositpaid());
        softAssert.assertEquals(json.getString("bookingdates.checkin"), expected.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("bookingdates.checkout"), expected.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("additionalneeds"), expected.getAdditionalneeds());
    }
	
    public void verifyBookingDetails_Post_Response(JsonPath json, Booking expected) {
		softAssert = new SoftAssert();
        softAssert.assertEquals(json.getString("booking.firstname"), expected.getFirstname());
        softAssert.assertEquals(json.getString("booking.lastname"), expected.getLastname());
        softAssert.assertEquals(json.getDouble("booking.totalprice"), expected.getTotalprice());
        softAssert.assertEquals(json.getBoolean("booking.depositpaid"), expected.isDepositpaid());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkin"), expected.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkout"), expected.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("booking.additionalneeds"), expected.getAdditionalneeds());
    }
}
