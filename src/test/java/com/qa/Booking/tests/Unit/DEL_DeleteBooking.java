package com.qa.Booking.tests.Unit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;
import com.qa.Booking.utils.ReusableDataUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DEL_DeleteBooking extends BaseTest{

	RequestSpecification request;
	Response response;
	String token;
	int bookingId;
	Booking bookingDetails;
	Bookingdates dates;
	
	@BeforeClass
	public void createBooking() {
		
		//Create Booking
		dates = new Bookingdates("2025-07-01","2025-07-15");
		bookingDetails = new Booking("Tohfee","Nay",120.99,true, dates,"No vege oil");
		
		restClient = new RestClient();
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON",bookingDetails);
		response = RestAssured.given(request).when().post(CREATE_BOOKING);
		bookingId = response.jsonPath().getInt("bookingid");
		
	}
	
	@BeforeMethod
	public void getAuthToken() {
			
		softAssert = new SoftAssert();
		restClient = new RestClient();
			
		//Create Token
		request = restClient.createRequestSpec_PostPutPatch(baseURI,"JSON",credentialJson);
		token = RestAssured.given(request).when().post(CREATE_AUTH_TOKEN)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().path("token");
		
		//want every test to have same request spec
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
	}
	
	@Test (groups = {"regression","unit"})
	public void deleteBooking_IncorrectHttpMethod() {

		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().patch(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));
		softAssert.assertAll();

	}
	
	@Test (groups = {"regression","unit"})
	public void deleteBooking_IncorrectEndpoint() {

		String incorrectEndpoint = "/baking/{id}";
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().delete(incorrectEndpoint);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();

	}
	
	@Test (groups = {"regression","unit"})
	public void deleteBooking_IncorrectBaseURL() {

		request = restClient.createRequestSpec_GetDelete(INCORRECT_BASEURI, "JSON", token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().delete(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		softAssert.assertAll();

	}
	
	@Test (groups = {"regression","unit"})
	public void deleteBooking_NoAuth() {

		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().delete(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)APIHTTPStatus.FORBIDDEN_403.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.FORBIDDEN_403.getMessage()));
		softAssert.assertAll();

	}
	
	@DataProvider(name = "incorrectBookingIdValues")
	public Object[][] provideIncorrectBookingIdValues() {
		
		return ReusableDataUtil.getInvalidBookingIdData();
	}
	
	
	@Test (dataProvider = "incorrectBookingIdValues", groups = {"regression","unit"})
	public void deleteBooking_IncorrectPathParameter(String scenarioName, Object bookingId, int expectedStatusCode, String expectedStatusLine) {
		
		if (bookingId == null) {
			bookingId =""; 
		} 
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).urlEncodingEnabled(false).pathParam("id", bookingId)
				.when().log().all().delete(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
		softAssert.assertAll();
	}
	
	@Test (groups = {"regression","unit", "smoke"})
	public void deleteBooking_positive() {

		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().delete(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(), (int)APIHTTPStatus.NO_CONTENT_204.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NO_CONTENT_204.getMessage()));
		softAssert.assertAll();

	}
	
	@Test (dependsOnMethods = {"deleteBooking_positive"}, groups = {"regression"})
	public void deleteBooking_AlreadyDeletedBookingId() {

		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().delete(DELETE_BOOKING);
		
		softAssert.assertEquals((int)response.getStatusCode(),(int)APIHTTPStatus.FORBIDDEN_403.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.FORBIDDEN_403.getMessage()));
		softAssert.assertAll();

	}
	
	
}
