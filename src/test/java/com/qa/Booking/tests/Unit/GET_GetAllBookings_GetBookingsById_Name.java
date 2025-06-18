package com.qa.Booking.tests.Unit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.utils.BookingUtils;
import com.qa.Booking.utils.ReusableDataUtil;
import com.qa.Booking.utils.Util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.Method;

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
	
	@Test (groups = {"regression","unit", "smoke"})
	public void getAllBookings_positive() {
				
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.body(matchesJsonSchemaInClasspath("schema/GetAllBookings_GeyByName_Schema.json")) //matchesJsonSchemaInClasspath(...) expects a file name located in your src/test/resources directory, not a raw JSON string
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		
		bookingId = Util.generateRandomNumber(response);
	}
	
	@Test (groups = {"regression","unit"})
	public void getAllBookings_negative_incorrectBaseURI() {
		
		//incorrect baseURI
		
		request = restClient.createRequestSpec_GetDelete(INCORRECT_BASEURI, "JSON");
		response = RestAssured.given(request).when().log().all().get(GET_ALL_BOOKINGS).then().log().all().extract().response();
		
		softAssert.assertTrue(response.contentType().contains("text/html"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
				
		softAssert.assertAll();
	}
	
	@Test (groups = {"regression","unit"})
	public void getAllBookings_negative_incorrectEndpoint() {
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		String incorrect_ENDPOINT = GET_ALL_BOOKINGS + "ggsss";
		response = RestAssured.given(request).when().log().all().get(incorrect_ENDPOINT).then().log().all().extract().response();
		
		softAssert.assertTrue(response.contentType().contains("json"));
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();
	}
	
	@Test (groups = {"regression","unit"})
	public void getAllBookings_negative_incorrectHTTPMethod() {
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().log().all().put(GET_ALL_BOOKINGS);
		
		//Failing: Getting 404 instead of 405
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));
		
		softAssert.assertAll();
	}
	
	@Test (groups = {"regression","unit", "smoke"})
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
	
	@Test (groups = {"regression","unit"})
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
	
	@Test (groups = {"regression","unit"})
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
	
	@Test (groups = {"regression","unit"})
	public void getBookingById_negative_incorrectHTTPMethod() {
		
		bookingId = 1013;
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().get(GET_BOOKING_BY_ID);
		
		//Failing: Getting 404 instead of 405
		softAssert.assertEquals(response.statusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));
		
		softAssert.assertAll();

	}
	
	@DataProvider(name = "invalidBookingIdScenarios")
	public Object[][] provideInvalidBookingIdData() {
	    return ReusableDataUtil.getInvalidBookingIdData();
	}
	
	@Test(dataProvider = "invalidBookingIdScenarios", groups = {"regression","unit"})
	public void getBookingById_negative_incorrectBookingId(String scenarioName, Object bookingId, int expectedStatusCode, String expectedStatusLine) {
		
		System.out.println(scenarioName);

		System.out.println();
		if (bookingId == null) {
			bookingId = GET_BOOKING_BY_ID.replace("{id}", ""); 
		} 
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).urlEncodingEnabled(false).pathParam("id", bookingId)
				.when().log().all().get(GET_BOOKING_BY_ID);
		
		
		softAssert.assertEquals(response.statusCode(), expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
		
		softAssert.assertAll();
		
	}
	
	@Test (groups = {"regression","unit", "smoke"})
	public void getBookingByName_positive() {
		
		firstName ="Eric";
		lastName = "Jackson";
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("firstName", firstName).pathParam("lastName", lastName)
				.when().log().all().get(GET_BOOKING_BY_NAME)
				.then().log().all().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.body(matchesJsonSchemaInClasspath("schema/GetAllBookings_GeyByName_Schema.json"))
				.extract().response();
		
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		
		List<Integer> bookingIds = response.jsonPath().getList("bookingid");

		Assert.assertTrue(bookingIds.contains(bookingId), "Expected bookingid "+bookingId+" was not found.");
		
	}
	
	@DataProvider(name = "invalidBookingNameScenarios")
	public Object[][] provideInvalidBookingNameData() {
		return ReusableDataUtil.getInvalidBookingNameData();
	}
	
	@Test(dataProvider = "invalidBookingNameScenarios", groups = {"regression","unit"})
	public void getBookingByName_negative_incorrectName(String scenarioName, Object firstNameData, Object lastNameData, 
			int expectedStatusCode, String expectedStatusLine) {
		
		System.out.println("Scenario Name = " + scenarioName);
		
		Object firstName,lastName; 
		
		//default name values
		//firstName = (firstNameData == null) ? this.firstName : firstNameData;
		firstName = (firstNameData == null) ? "Tohfa" : firstNameData;

		//lastName = (lastNameData == null) ? this.lastName : lastNameData;
		lastName = (lastNameData == null) ? "Nay" : lastNameData;

		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		String endpoint = "/booking";
		
		try {
			if(scenarioName.contains("Missing")) { //"where" clause omitted - full/partial
					
					// Build path request
				    String queryString = null, path = null;
				    
				    // Conditionally add query parameters based on scenario
				    if ("Missing First Name".equals(scenarioName)) {
				    	queryString = "?lastname={lastName}";
				    	queryString = queryString.replace("{lastName}", lastName.toString());
				    } 
				    else if ("Missing Last Name".equals(scenarioName)) {
				    	queryString = "?firstname={firstName}";
				    	queryString = queryString.replace("{firstName}", firstName.toString());
				    } 
				    else if ("Missing Both Names".equals(scenarioName)) {
				    	queryString = ""; 
				    } 
				    
				    path = endpoint + queryString;
				    response = RestAssured.given(request).when().log().all().get(path);
				  
			}
			else {
					
				if(scenarioName.contains("White space")) { //params with white space
					
					String rawUrl = null, malformedQuery = "";
					
					if(scenarioName.contains("First Name contains White space")) {
						malformedQuery = "?firstname= " + "&lastname="+lastName.toString()+"";
						rawUrl = baseURI + endpoint + malformedQuery;
			
						
					}else if (scenarioName.equals("Last Name contains White space")) {
					    malformedQuery = "?firstname="+firstName.toString()+"&lastname= "; //this one throwing exception
						rawUrl = baseURI + endpoint + malformedQuery;
	
					}
	
					response = RestAssured.given().spec(request).when().log().all().request(Method.GET, rawUrl);  // <== This bypasses URI encoding checks
				}
				
				Map<String, Object> queryParams = new HashMap<String, Object>();
				queryParams.put("firstname", firstName);
				queryParams.put("lastname", lastName);
	
				response = RestAssured.given(request).queryParams(queryParams).urlEncodingEnabled(false)
						.when().log().all().get(endpoint).then().log().headers().extract().response();
			
			}
		} catch (IllegalArgumentException e) {
		    System.out.println("Caught expected IllegalArgumentException for malformed URI:");
		    System.out.println(e.getMessage());
		    softAssert.fail("Malformed URI triggered exception as expected: " + e.getMessage());
		}	
		
	    softAssert.assertEquals(response.statusCode(), expectedStatusCode);
	    softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
	    softAssert.assertAll(); 

	}
}
