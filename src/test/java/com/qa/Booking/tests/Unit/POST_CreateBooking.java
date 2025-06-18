package com.qa.Booking.tests.Unit;

import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.Booking.base.BaseTest;
import com.qa.Booking.client.RestClient;
import com.qa.Booking.pojo.Booking;
import com.qa.Booking.pojo.Booking.Bookingdates;
import com.qa.Booking.utils.ReusableDataUtil;
import com.qa.Booking.utils.Util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class POST_CreateBooking extends BaseTest {

	RequestSpecification request;
	Response response;

	@BeforeMethod
	public void getAuthToken() {

		restClient = new RestClient();
		softAssert = new SoftAssert();
	}

	@DataProvider(name = "negativeBookingScenarios")
	public Object[][] provideNegativeBookingPayloadData() {
		return ReusableDataUtil.getNegativeBookingPayloadData();
	}

	@Test(dataProvider = "negativeBookingScenarios")
	public void createBooking_negative_invalidBookingdata(String scenarioName, Object firstNameData,
			Object lastNameData, Object totalpriceData, Object depositpaidData, Object checkinData, Object checkoutData,
			Object additionalneeds, int expectedStatusCode, String expectedStatusLine, String expectedResponseMsg) {

		//*********************************************************
		Util.printSection(scenarioName.toUpperCase());
		Bookingdates dates = new Bookingdates(checkinData, checkoutData);
		Booking invalidBooking = new Booking(firstNameData, lastNameData, totalpriceData, depositpaidData,
				dates, additionalneeds);

		if (scenarioName.contains("null value")) {

			Map<String, Object> bookingMap = bookingUtils.convertToMap(invalidBooking);
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", bookingMap);
		} 
		else {
			
			request = restClient.createRequestSpec_PostPutPatch(baseURI, "JSON", invalidBooking);
		}

		//*********************************************************
		Util.printSection("API REQ/RES");

		response = RestAssured.given(request).when().log().all().post(CREATE_BOOKING);

		softAssert.assertEquals(response.getStatusCode(), expectedStatusCode);
		softAssert.assertTrue(response.getStatusLine().contains(expectedStatusLine));
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
