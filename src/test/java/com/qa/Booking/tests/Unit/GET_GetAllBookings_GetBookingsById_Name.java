package com.qa.Booking.tests.Unit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.utils.BookingHelper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GET_GetAllBookings_GetBookingsById_Name extends BaseTest{

	RequestSpecification request;
	Response response;
	int bookingId;
	String firstName, lastName;
	
	@BeforeMethod
	public void getAuthToken() {

		restClient = new RestClient();
		softAssert = new SoftAssert();
	}
	
	@Test
	public void getAllBookings_positive() {
				
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.body(matchesJsonSchemaInClasspath("schema/GetAllBookings_Schema.json"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		
		bookingId = BookingHelper.generateRandomNumber(response);
	}
	
	@Test
	public void getAllBookings_negative_incorrectBaseURI() {
		
		//incorrect baseURI
		
		request = restClient.createRequestSpec_GetDelete(INCORRECT_BASEURI, "JSON");
		response = RestAssured.given(request).when().log().all().get(GET_ALL_BOOKINGS).then().log().all().extract().response();
		
		softAssert.assertTrue(response.contentType().contains("text/html"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
				
		softAssert.assertAll();
	}
	
	@Test
	public void getAllBookings_negative_incorrectEndpoint() {
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		String incorrect_ENDPOINT = GET_ALL_BOOKINGS + "ggsss";
		response = RestAssured.given(request).when().log().all().get(incorrect_ENDPOINT).then().log().all().extract().response();
		
		softAssert.assertTrue(response.contentType().contains("json"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();
	}
	
	@Test
	public void getAllBookings_negative_incorrectHTTPMethod() {
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().log().all().put(GET_ALL_BOOKINGS);
		
		//Failing: Getting 404 instead of 405
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));
		
		softAssert.assertAll();
	}
	
	@Test
	public void getBookingById_positive() {
		
		bookingId = 1013;
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().get(GET_BOOKING_BY_ID)
				.then().log().all().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.body(matchesJsonSchemaInClasspath("schema/GetBookingByID_Schema.json"))
				.extract().response();
		
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		firstName = response.jsonPath().getString("firstname");
		lastName = response.jsonPath().getString("lastname");
	}
	
	@Test
	public void getBookingById_negative_incorrectBaseURI() {
		
		bookingId = 1013;
		request = restClient.createRequestSpec_GetDelete(INCORRECT_BASEURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().get(GET_BOOKING_BY_ID).then().log().all().extract().response();
				
		softAssert.assertTrue(response.contentType().contains("text/html"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();
	}
	
	@Test
	public void getBookingById_negative_incorrectEndpoint() {
		
		bookingId = 1013;
		// Replace "booking" with "bookingssss" in the endpoint path
	    String incorrect_ENDPOINT = GET_BOOKING_BY_ID.replace("booking", "bookingssss");
	    
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().get(incorrect_ENDPOINT)
				.then().log().all()
				.extract().response();
		
		softAssert.assertTrue(response.contentType().contains("json"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();

	}
	
	@Test
	public void getBookingById_negative_incorrectHTTPMethod() {
		
		bookingId = 1013;
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().post(GET_BOOKING_BY_ID);
		
		//Failing: Getting 404 instead of 405
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));
		
		softAssert.assertAll();

	}
	
	@DataProvider(name = "invalidBookingIdScenarios")
	public Object[][] invalidBookingIdScenarios() {
	    return new Object[][] {
	        {"Incorrect PathParameter - Non-existing ID", 111111111, 404, "Not Found"},
	        {"Incorrect PathParameter - String", "Twelve", 400, "Bad Request"},
	        {"Incorrect PathParameter - Null Value", null, 400, "Bad Request"},
	        {"Incorrect PathParameter - Special Character", "@-$", 400, "Bad Request"}
	        
	    };
	}
	
	@Test(dataProvider = "invalidBookingIdScenarios")
	public void getBookingById_negative_incorrectBookingId(String scenarioName, Object bookingId, int expectedStatusCode, String expectedStatusLine) {
		
		System.out.println(scenarioName);

		System.out.println();
		if (bookingId == null) {
			bookingId = GET_BOOKING_BY_ID.replace("{id}", ""); 
		} 
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).urlEncodingEnabled(false).pathParam("id", bookingId)
				.when().log().all().post(GET_BOOKING_BY_ID);
		
		
		softAssert.assertEquals(response.statusCode(), expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
		
		softAssert.assertAll();
		
	}
}
