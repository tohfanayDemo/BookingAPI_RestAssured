package com.qa.Booking.tests.Unit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;
import com.qa.Booking.utils.JsonDataReader;
import com.qa.Booking.utils.ReusableDataUtil;
import com.qa.Booking.utils.Util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class POST_CreateBooking extends BaseTest {

	RequestSpecification request;
	Response response;
	List<Map<String, Object>> positiveData;
	List<Map<String, Object>> negativeData;
	Map<String, Object> dataMap;
	Booking booking;
	Bookingdates dates;

	@BeforeClass
	public void getCreateBookingData() {
		positiveData = JsonDataReader.getDataByRequestName(testData,"CreateBooking");
		negativeData = JsonDataReader.getDataByRequestName(testData,"Negative - CreateBooking");
	}
	
	@BeforeMethod
	public void getAuthToken() {

		restClient = new RestClient();
		softAssert = new SoftAssert();
		
		dataMap = positiveData.get(0);
		dates = new Bookingdates(dataMap.get("checkin"), dataMap.get("checkout"));
		booking = new Booking(dataMap.get("firstname"), dataMap.get("lastname"), dataMap.get("totalprice"), dataMap.get("depositpaid"),
				dates, dataMap.get("additionalneeds"));
	}

	@Test (groups = {"smoke", "regression", "unit"})
	public void createBooking_positive() {

		Util.printSection(dataMap.get("scenarioName").toString());
		
		Object schemaObject = dataMap.get("expectedSchema");
		ObjectMapper mapper = new ObjectMapper();
		String schemaJson = null;
		
		try {
			schemaJson = mapper.writeValueAsString(schemaObject);
		} catch (JsonProcessingException e) {

		}
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", booking);
		response = RestAssured.given(request).when().log().all().post(CREATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.CREATED_201.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.CREATED_201.getMessage()));
		
		try {
		    response.then().assertThat().body(matchesJsonSchema(schemaJson));
		    softAssert.assertTrue(true, "Schema validation passed.");
		} catch (AssertionError | Exception e) {
		    softAssert.fail("Schema validation failed: " + e.getMessage());
		}
		
		softAssert.assertAll();
	}
	
	@Test (groups = {"unit", "regression"})
	public void createBooking_IncorrectHttpMethod() {

		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", booking);
		response = RestAssured.given(request) .when().log().all().put(CREATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.METHOD_NOT_ALLOWED_405.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.METHOD_NOT_ALLOWED_405.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test (groups = {"unit", "regression"})
	public void createBooking_IncorrectEndpoint() {

		String incorrectEndpoint = "/baking";
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", booking);
		response = RestAssured.given(request) .when().log().all().post(incorrectEndpoint);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test (groups = {"unit", "regression"})
	public void createBooking_IncorrectBaseURL() {

		request = restClient.createRequestSpec_PostPutPatch(INCORRECT_BASEURI, "JSON", booking);
		response = RestAssured.given(request) .when().log().all().post(CREATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.NOT_FOUND_404.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));	
		softAssert.assertAll();
	}
	
	@Test (groups = {"unit", "regression"})
	public void createBooking_IncorrectContentType() {

		request = restClient.createRequestSpec_PostPutPatch(baseURI, "XML", booking);
		response = RestAssured.given(request) .when().log().all().post(CREATE_BOOKING);
		
		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.UNSUPPORTED_MEDIA_TYPE_415.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.UNSUPPORTED_MEDIA_TYPE_415.getMessage()));	
		softAssert.assertAll();
	}
	
	@DataProvider(name = "negativeCreateBookingScenarios")
	public Object[][] provideNegativeBookingPayloadData() {
	    return JsonDataReader.convertMapListTo2DArray(negativeData);
	}

	@Test(dataProvider = "negativeCreateBookingScenarios" ,groups = {"unit", "regression"})
	public void createBooking_negative_invalidBookingdata(Object scenarioName, Object firstNameData,
			Object lastNameData, Object totalpriceData, Object depositpaidData, Object checkinData, Object checkoutData,
			Object additionalneeds, Object expectedStatusCode, Object expectedStatusLine, Object expectedResponseMsg) {

		Bookingdates dates = new Bookingdates(checkinData, checkoutData);
		Booking invalidBooking = new Booking(firstNameData, lastNameData, totalpriceData, depositpaidData,
				dates, additionalneeds);

		if (scenarioName.toString().contains("null value")) {

			Map<String, Object> bookingMap = bookingUtils.convertToMap(invalidBooking);
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingMap);
		} 
		else {
			
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", invalidBooking);
		}

		//*********************************************************
		Util.printSection("API REQ/RES");

		response = RestAssured.given(request).when().log().all().post(CREATE_BOOKING);

		softAssert.assertEquals(Integer.valueOf(response.getStatusCode()), (Integer)expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine.toString()));
		softAssert.assertAll();

	}
	
	@AfterMethod
	public void deleteCreatedBooking() {
		
		if (response.getStatusCode() == 200) {
			int bookingId = response.jsonPath().getInt("bookingid");
			System.out.println("Booking ID received: " + bookingId);

			//*********************************************************
			Util.printSection(" Create token for Delete call ");
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", credentialJson);
			String token = RestAssured.given(request).when().log().all().post(CREATE_AUTH_TOKEN).then().log().all()
					.extract().path("token");

			Util.printSection(" Call DELETE API ");
			request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
			RestAssured.given(request).pathParam("id", bookingId).when().log().all().delete(DELETE_BOOKING).then().log()
					.all();
		}
	}
}
