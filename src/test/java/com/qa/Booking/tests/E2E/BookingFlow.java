package com.qa.Booking.tests.E2E;

import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.ResponseBuilder;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.constants.APIHTTPStatus;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;

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
		printSection("Get All Booking and Booking count");
		//restClient = new RestClient();
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then().assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath getAll_jsonResponse = response.jsonPath();
		int totalBookingCount = getAll_jsonResponse.getList("$").size(); 
		
		
		
		//Step 2: Create A Booking
		printSection("Create Booking");
		
		Bookingdates dates = new Bookingdates("2025-07-01","2025-07-15");
		Booking bookingDetails = new Booking("Tohfee","Nay",120.99,true, dates,"No vege oil");
		
		System.out.println("**************************************");
		System.out.println("Booking details created:");
		System.out.println(bookingDetails.toString());
		System.out.println("**************************************");
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON",bookingDetails);
		response = RestAssured.given(request).when().post(CREATE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath post_jsonResponse = response.jsonPath();
		
		int bookingId = post_jsonResponse.getInt("bookingid");
		verifyBookingDetails_Post_Response(post_jsonResponse,bookingDetails);
				
		printSection("Verify Booking count increased by 1 after POST call");
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		RestAssured.given(request).when().get(GET_ALL_BOOKINGS)
				.then()
				.assertThat()
				.body("$.size()",equalTo(totalBookingCount+1));
		
		
		
		//Step 3: Get Booking By Id
		printSection("Get Booking By Booking Id"); //GET /booking/{id}
		
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().get(GET_BOOKING_BY_ID)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath getById_jsonResponse = response.jsonPath();
		verifyBookingDetails_Get_Put_Response(getById_jsonResponse,bookingDetails);
		
		
		
		//Step 4: Update Existing Booking By Id
		printSection("Update Booking");
		dates.setCheckin("2025-07-07");
		bookingDetails.setFirstname("Updated");
		bookingDetails.setLastname("Updated");
		bookingDetails.setTotalprice(150.99);
		bookingDetails.setDepositpaid(true);
		bookingDetails.setBookingdates(dates);
		bookingDetails.setAdditionalneeds("No vege oil and soy milk");
		
		System.out.println("**************************************");
		System.out.println("Booking details -- UPDATED:");
		System.out.println(bookingDetails.toString());
		System.out.println("**************************************");
		
		request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingDetails, token);
		response = RestAssured.given(request).pathParam("id", bookingId).when().put(UPDATE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.OK_200.getCode(), "JSON"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.OK_200.getMessage()));
		JsonPath put_jsonResponse = response.jsonPath();
		verifyBookingDetails_Get_Put_Response(put_jsonResponse,bookingDetails);
		
		printSection("Get Booking By Id & Check PUT call changes reflected");
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().log().all().get(GET_BOOKING_BY_ID);
		getById_jsonResponse = response.jsonPath();
		verifyBookingDetails_Get_Put_Response(getById_jsonResponse,bookingDetails);	
		
		
		
		//Step 5: Delete Existing Booking By Id 
		printSection("Delete Booking"); 

		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON", token);
		response = RestAssured.given(request).pathParam("id", bookingId).when().log().all().delete(DELETE_BOOKING)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.CREATED_201.getCode(), "Text"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.CREATED_201.getMessage()));
		
		printSection("Get Booking By Id After Deletion & Check it is not found anymore");
		request = restClient.createRequestSpec_GetDelete(baseURI, "JSON");
		response = RestAssured.given(request).pathParam("id", bookingId).when().get(GET_BOOKING_BY_ID)
				.then().log().all()
				.assertThat().spec(ResponseBuilder.expResSpec(APIHTTPStatus.NOT_FOUND_404.getCode(), "Text"))
				.extract().response();
		Assert.assertTrue(response.getStatusLine().contains(APIHTTPStatus.NOT_FOUND_404.getMessage()));
		
		softAssert.assertAll();
			
	}
	
	private void printSection(String message) {
		System.out.println();
        System.out.println("\n================= " + message + " ==================");
    }
	
	private void verifyBookingDetails_Get_Put_Response(JsonPath json, Booking expected) {
        softAssert.assertEquals(json.getString("firstname"), expected.getFirstname());
        softAssert.assertEquals(json.getString("lastname"), expected.getLastname());
        softAssert.assertEquals(json.getDouble("totalprice"), expected.getTotalprice());
        softAssert.assertEquals(json.getBoolean("depositpaid"), expected.isDepositpaid());
        softAssert.assertEquals(json.getString("bookingdates.checkin"), expected.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("bookingdates.checkout"), expected.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("additionalneeds"), expected.getAdditionalneeds());
    }
	
	private void verifyBookingDetails_Post_Response(JsonPath json, Booking expected) {
        softAssert.assertEquals(json.getString("booking.firstname"), expected.getFirstname());
        softAssert.assertEquals(json.getString("booking.lastname"), expected.getLastname());
        softAssert.assertEquals(json.getDouble("booking.totalprice"), expected.getTotalprice());
        softAssert.assertEquals(json.getBoolean("booking.depositpaid"), expected.isDepositpaid());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkin"), expected.getBookingdates().getCheckin());
        softAssert.assertEquals(json.getString("booking.bookingdates.checkout"), expected.getBookingdates().getCheckout());
        softAssert.assertEquals(json.getString("booking.additionalneeds"), expected.getAdditionalneeds());
    }
}
