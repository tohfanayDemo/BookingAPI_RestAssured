package com.qa.Booking.tests.Unit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import org.testng.annotations.AfterClass;
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
import com.qa.Booking.schema.SchemaRepo;
import com.qa.Booking.utils.ReusableDataUtil;
import com.qa.Booking.utils.Util;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class PATCH_UpdateBooking extends BaseTest{
	
	
	RequestSpecification request;
	Response response;
	String token;
	int bookingId;
	Booking bookingDetails, patchBooking;
	Bookingdates dates, patchDates;
	
	@BeforeClass
	public void createBooking() {
		
		softAssert = new SoftAssert();
		restClient = new RestClient();
		
		//Create Booking
		dates = new Bookingdates("2025-07-01","2025-07-15");
		bookingDetails = new Booking("Tohfee","Nay",120.99,true, dates,"No vege oil");
		
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
		System.out.println("Token generated = " + token);
	}

	@DataProvider(name = "partialUpdateBookingData")
	public Object[][] partialUpdateBookingData() {
		return new Object[][] {
			{"Update firstname only", "Toofa Only", null, null, null, null, null, null},
	        {"Update lastname only", null, "Naay Only", null, null, null, null, null},
	        {"Update totalprice only", null, null, 900, null, null, null, null},
	        {"Update depositpaid only", null, null, null, false, null, null, null},
	        {"Update checkin only", null, null, null, null, "2025-06-20", null, null},
	        {"Update checkout only", null, null, null, null, null, "2025-06-28", null},
	        {"Update additionalneeds only", null, null, null, null, null, null, "Only vegan meal for Lunch and Dinner"}
		};
	}
	
	@Test(dataProvider = "partialUpdateBookingData")
	public void partialUpdateBooking_positiveScenarios(String scenarioName, Object firstNameData,
			Object lastNameData, Object totalpriceData, Object depositpaidData, Object checkinData, Object checkoutData,
			Object additionalneedsData) {
		
		Util.printSection(scenarioName.toUpperCase());
		//****************************************************
		patchDates = null;	
		
		if(checkinData != null || checkoutData  != null){
			dates = new Bookingdates();
			
			if(checkinData != null) {
				dates.setCheckin(checkinData);
			}
			
			if(checkoutData != null) {
				dates.setCheckout(checkoutData);
			}
		}
		
		Booking patchBooking = new Booking(firstNameData, lastNameData, totalpriceData, depositpaidData,
				dates, additionalneedsData);
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", patchBooking, token);
		response = RestAssured.given(request).pathParam("id", bookingId).when().log().all().patch(PARTIAL_UPDATE_BOOKING);

		softAssert.assertEquals(response.getStatusCode(), APIHTTPStatus.OK_200.getCode());
		softAssert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		
		
		//only softAssert field that has been updated dynamically
		JsonPath json = response.jsonPath();
		if (firstNameData != null) softAssert.assertEquals(json.getString("firstname"), firstNameData);
		if (lastNameData != null) softAssert.assertEquals(json.getString("lastname"), lastNameData);
		if (totalpriceData != null) softAssert.assertEquals(json.getDouble("totalprice"), (Double) totalpriceData);
		if (depositpaidData != null) softAssert.assertEquals(json.getBoolean("depositpaid"), (Boolean) depositpaidData);
		if (checkinData != null) softAssert.assertEquals(json.getString("bookingdates.checkin"), checkinData);
		if (checkoutData != null) softAssert.assertEquals(json.getString("bookingdates.checkout"), checkoutData);
		if (additionalneedsData != null) softAssert.assertEquals(json.getString("additionalneeds"), additionalneedsData);
				
		softAssert.assertAll();

	}	
	
	@DataProvider(name = "partialUpdateInvalidBookingIdData")
	public Object[][] providePartialUpdateInvalidBookingIdData() {
		return ReusableDataUtil.getInvalidBookingIdData();
	}
	
	@Test(dataProvider = "partialUpdateInvalidBookingIdData")
	public void invalidBookingIdForPatchCall(String scenarioName, Object bookingId, int expectedStatusCode, String expectedStatusLine) {
		
		System.out.println(scenarioName);
		
		if (bookingId == null) {
			bookingId =  ""; 
		} 
		
		bookingDetails.setFirstname("UpdatedFirstName");
		dates.setCheckout("2025-07-30");
		bookingDetails.setBookingdates(dates);
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).urlEncodingEnabled(false).pathParam("id", bookingId)
				.when().log().all().patch(PARTIAL_UPDATE_BOOKING);

		softAssert.assertEquals(response.getStatusCode(), expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
		softAssert.assertAll();

	}
	
	@Test
	public void partialUpdateSchemaValidation() {
		
		bookingDetails.setFirstname("UpdatedFirstName");
		dates.setCheckout("2025-07-30");
		bookingDetails.setBookingdates(dates);
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		RestAssured.given(request).pathParam("id", bookingId)
				.when().log().all().patch(PARTIAL_UPDATE_BOOKING)
				.then().assertThat()
				.body(matchesJsonSchema(SchemaRepo.getSchema("partialUpdateBooking")));

	}
	
	@AfterClass()
	public void deleteBooking() {
		
		//Delete Booking
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
		RestAssured.given(request).pathParam("id", bookingId).when().log().all().delete(DELETE_BOOKING).then().log()
				.all().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.NO_CONTENT_204.getCode(), "Text"));


	}

}
