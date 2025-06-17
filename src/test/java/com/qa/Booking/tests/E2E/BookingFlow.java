package com.qa.Booking.tests.E2E;

import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;
import com.qa.Booking.utils.Util;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingFlow extends BaseTest{
	
	String token;
	RequestSpecification request;
	Response response;
	
	@BeforeMethod
	public void getAuthToken() {
		
		softAssert = new SoftAssert();
		restClient = new RestClient();
		request = restClient.createRequestSpec_PostPutPatch(baseURI,"JSON",credentialJson);
		
		token = RestAssured.given(request).when().post(CREATE_AUTH_TOKEN)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().path("token");
	}
	
	//end-to-end API flow: Auth → GET → POST → GET by ID → PUT → GET by ID → DELETE → GET by ID again to confirm deletion.
	
	@Test
	public void bookingFlow() {
		
		//Step1: Get All Bookings Count
		Util.printSection("Get All Booking and Booking count");
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		
		JsonPath getAll_jsonResponse = response.jsonPath();
		int totalBookingCount = getAll_jsonResponse.getList("$").size(); 
		
		
		
		//Step 2: Create A Booking
		Util.printSection("Create Booking");
		
		Bookingdates dates = new Bookingdates("2025-07-01","2025-07-15");
		Booking bookingDetails = new Booking("Tohfee","Nay",120.99,true, dates,"No vege oil");
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON",bookingDetails);
		response = RestAssured.given(request).when().post(CREATE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath post_jsonResponse = response.jsonPath();
		
		int bookingId = post_jsonResponse.getInt("bookingid");
		bookingUtils.verifyBookingDetails_Post_Response(post_jsonResponse,bookingDetails,softAssert);
				
		Util.printSection("Verify Booking count increased by 1 after POST call");
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then()
				.assertThat()
				.body("$.size()",equalTo(totalBookingCount+1));
		
		
		
		//Step 3: Get Booking By Id
		Util.printSection("Get Booking By Booking Id"); //GET /booking/{id}
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().get(GET_BOOKING_BY_ID)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath getById_jsonResponse = response.jsonPath();
		bookingUtils.verifyBookingDetails_Get_Put_Response(getById_jsonResponse,bookingDetails,softAssert);
		
		
		
		//Step 4: Update Existing Booking By Id
		Util.printSection("Update Booking");
		
		dates.setCheckin("2025-07-07");
		bookingDetails.setFirstname("Updated");
		bookingDetails.setLastname("Updated");
		bookingDetails.setTotalprice(150.99);
		bookingDetails.setDepositpaid(true);
		bookingDetails.setBookingdates(dates);
		bookingDetails.setAdditionalneeds("No vege oil and soy milk");
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId).when().put(UPDATE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath put_jsonResponse = response.jsonPath();
		bookingUtils.verifyBookingDetails_Get_Put_Response(put_jsonResponse,bookingDetails, softAssert);
		
		Util.printSection("Get Booking By Id & Check PUT call changes reflected");
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().log().all().get(GET_BOOKING_BY_ID);
		getById_jsonResponse = response.jsonPath();
		bookingUtils.verifyBookingDetails_Get_Put_Response(getById_jsonResponse,bookingDetails, softAssert);	
		
		
		
		//Step 5: Delete Existing Booking By Id 
		Util.printSection("Delete Booking"); 

		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
		response = RestAssured.given(request).pathParam("id", bookingId).when().log().all().delete(DELETE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.CREATED_201.getCode(), "Text"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.CREATED_201.getMessage()));
		
		Util.printSection("Get Booking By Id After Deletion & Check it is not found anymore");
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().get(GET_BOOKING_BY_ID)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.NOT_FOUND_404.getCode(), "Text"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		
		softAssert.assertAll();
			
	}

}
