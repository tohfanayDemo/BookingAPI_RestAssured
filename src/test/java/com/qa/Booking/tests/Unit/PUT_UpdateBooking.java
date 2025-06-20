package com.qa.Booking.tests.Unit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;
import com.qa.Booking.utils.JsonDataReader;
import com.qa.Booking.utils.ReusableDataUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PUT_UpdateBooking extends BaseTest{
	
	RequestSpecification request;
	Response response;
	String token;
	int bookingId;
	Booking bookingDetails;
	Bookingdates dates;
	List<Map<String, Object>> positiveData;
	List<Map<String, Object>> negativeData;
	Map<String, Object> dataMap;

	@BeforeClass
	public void createBooking() {
		
		positiveData = JsonDataReader.getDataByRequestName(testData,"UpdateBooking");
		negativeData = JsonDataReader.getDataByRequestName(testData,"Negative - UpdateBooking");

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
		
		//want every test to have same Booking update
		dataMap = positiveData.get(0);
		dates.setCheckin(dataMap.get("checkin"));
		dates.setCheckout(dataMap.get("checkout"));
		bookingDetails.setFirstname(dataMap.get("firstname"));
		bookingDetails.setLastname(dataMap.get("lastname"));
		bookingDetails.setTotalprice(dataMap.get("totalprice"));
		bookingDetails.setDepositpaid(dataMap.get("depositpaid"));
		bookingDetails.setBookingdates(dates);
		bookingDetails.setAdditionalneeds(dataMap.get("additionalneeds"));

	}
	
	@Test
	public void updateBooking_positive() {

		Object schemaObject = dataMap.get("expectedSchema");

		ObjectMapper mapper = new ObjectMapper();
		String schemaJson = null;
		try {
			schemaJson = mapper.writeValueAsString(schemaObject);
		} catch (JsonProcessingException e) {

		}
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.OK_200.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		
		try {
		    response.then().assertThat().body(matchesJsonSchema(schemaJson));
		    softAssert.assertTrue(true, "Schema validation passed.");
		} catch (AssertionError | Exception e) {
		    softAssert.fail("Schema validation failed: " + e.getMessage());
		}
		
		softAssert.assertAll();
	}
	
	@Test
	public void updateBooking_IncorrectHttpMethod() {

		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().post(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test
	public void updateBooking_IncorrectEndpoint() {

		String incorrectEndpoint = "/baking/{id}";
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(incorrectEndpoint);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test
	public void updateBooking_IncorrectBaseURL() {

		request = restClient.createRequestSpec_PostPutPatch(INCORRECT_BASEURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test
	public void updateBooking_IncorrectContentType() {

		request = restClient.createRequestSpec_PostPutPatch(baseURI, "XML", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.UNSUPPORTED_MEDIA_TYPE_415.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.UNSUPPORTED_MEDIA_TYPE_415.getMessage()));	
		softAssert.assertAll();
	}
	
	
	@Test
	public void updateBooking_NoToken() {

		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails);
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.FORBIDDEN_403.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.FORBIDDEN_403.getMessage()));	
		softAssert.assertAll();
	}
	
	
	@DataProvider(name = "negativeUpdateData")
	public Object[][] provideNegativeUpdateData() {

	    return JsonDataReader.convertMapListTo2DArray(negativeData);
	}
	
	@Test(dataProvider = "negativeUpdateData")
	public void testNegativeUpdateBooking(Object scenarioName, Object firstname, Object lastname, Object totalprice,
	        Object depositpaid, Object checkin, Object checkout, Object additionalneeds,
	        Object expectedStatusCode, Object expectedStatusLine, Object expectedMessage) {

		dates.setCheckin(checkin);
		dates.setCheckout(checkout);
		bookingDetails.setFirstname(firstname);
		bookingDetails.setLastname(lastname);
		bookingDetails.setTotalprice(totalprice);
		bookingDetails.setDepositpaid(depositpaid);
		bookingDetails.setBookingdates(dates);
		bookingDetails.setAdditionalneeds(additionalneeds);
				
		if (scenarioName.toString().contains("null value")) {

			Map<String, Object> bookingMap = bookingUtils.convertToMap(bookingDetails);
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingMap, token);
		} 
		else {
			
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		}
		
		
		response = RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
			
		softAssert.assertEquals(Integer.valueOf(response.getStatusCode()),(Integer)expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine.toString()));
		//softAssert.assertTrue(response.jsonPath().getString("message").contains(expectedMessage.toString()));
		
		softAssert.assertAll();
		
	}
	
	
	@DataProvider(name = "incorrectBookingIdValues")
	public Object[][] provideIncorrectBookingIdValues() {
		
		return ReusableDataUtil.getInvalidBookingIdData();
	}
	
	
	@Test (dataProvider = "incorrectBookingIdValues")
	public void updateBooking_IncorrectPathParameter(String scenarioName, Object bookingId, int expectedStatusCode, String expectedStatusLine) {
		
		if (bookingId == null) {
			bookingId =""; 
		} 
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).urlEncodingEnabled(false).pathParam("id", bookingId)
				.when().log().all().put(UPDATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
		softAssert.assertAll();
	}
	
	
	  @AfterClass public void deleteBooking() {
	  
	  request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
	  RestAssured.given(request).pathParam("id", bookingId)
		  .when().log().all().delete(DELETE_BOOKING)
		  .then().assertThat().statusCode(APIHTTPStatus.NO_CONTENT_204.getCode());
	  
	  }
	 
	
}
