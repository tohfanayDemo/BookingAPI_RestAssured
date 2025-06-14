package com.qa.Booking.base;

import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.client.RestClient;
import com.qa.Booking.configuration.ConfigurationManager;
import com.qa.Booking.utils.BookingHelper;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

public class BaseTest {
	
	//Endpoints
	public static final String CREATE_AUTH_TOKEN = "/auth";
	public static final String GET_ALL_BOOKINGS = "/booking";
	public static final String GET_BOOKING_BY_ID = "/booking/{id}";
	public static final String CREATE_BOOKING = "/booking";
	public static final String UPDATE_BOOKING = "/booking/{id}";
	public static final String PARTIAL_UPDATE_BOOKING="/booking/{id}";
	public static final String DELETE_BOOKING="/booking/{id}";
	
	public static final String INCORRECT_BASEURI = "https://restful-bookerrrsss.herokuapp.com";

	
	protected ConfigurationManager config;
	protected Properties prop;
	protected RestClient restClient;
	protected String baseURI, testUsername, testPassword, credentialJson;
	protected SoftAssert softAssert;
	protected BookingHelper bookingHelper;

	@Parameters({"baseURI"})
	@BeforeTest
	public void setUp(@Optional String baseURI) {
		
		RestAssured.filters(new AllureRestAssured());
		
		config = new ConfigurationManager();
		prop = config.initProp();
		softAssert = new SoftAssert();
		
		//Set baseURI
		this.baseURI = (baseURI == null) ? prop.getProperty("baseURI") : baseURI;
		
		//Set username
		String username = System.getProperty("username");
		testUsername = (username == null) ? prop.getProperty("username") : username;
		
		//Set password
		String password = System.getProperty("username");
		testPassword = (password == null) ? prop.getProperty("password") : password;

		credentialJson = "{ \"username\": \""+testUsername+"\", \"password\": \""+testPassword+"\" }";
		bookingHelper = new BookingHelper();
	}
	
	
	
}
