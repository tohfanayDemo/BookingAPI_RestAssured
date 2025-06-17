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
	public Object[][] provideNegativeBookingData() {
		return new Object[][] {
				{ "All Fields Empty", "", "", 0, false, "", "", "", 400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - firstname", "", "Neigh", 111, true, "2025-06-20", "2025-06-30",
						"Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - lastname", "Toffee", "", 111, true, "2025-06-20", "2025-06-30",
						"Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - totalprice omitted", "Toffee", "Neigh", null, true, "2025-06-20",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - depositpaid omitted", "Toffee", "Neigh", 111, null, "2025-06-20",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - checkin", "Toffee", "Neigh", 111, true, "", "2025-06-30", "Only vegan meal",
						400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - checkout", "Toffee", "Neigh", 111, true, "2025-06-20", "", "Only vegan meal",
						400, "Bad Request", "All Fields are mandatory" },
				{ "Only One Field Empty - additionalneeds", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30",
						"", 400, "Bad Request", "All Fields are mandatory" },
				{ "Mismatched Data Type - firstname contains numbers only", 33, "Neigh", 111, true, "2025-06-20",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "firstname Must contain only letters" },
				{ "Mismatched Data Type - firstname contains alpha-numeric value only", "Toffee33", "Neigh", 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"firstname Must contain only letters" },
				{ "Mismatched Data Type - firstname contains special characters only", "@-&", "Neigh", 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"firstname Must contain only letters" },
				{ "Mismatched Data Type - firstname contains boolean value only", true, "Neigh", 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"firstname Must contain only letters" },
				{ "Mismatched Data Type - lastname contains numbers only", "Toffee", 33, 111, true, "2025-06-20",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "lastname Must contain only letters" },
				{ "Mismatched Data Type - lastname contains alpha-numeric value only", "Toffee", "Neigh33", 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"lastname Must contain only letters" },
				{ "Mismatched Data Type - lastname contains special characters only", "Toffee", "@-&", 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"lastname Must contain only letters" },
				{ "Mismatched Data Type - lastname contains boolean value only", "Toffee", false, 111, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"lastname Must contain only letters" },
				{ "totalprice is zero", "Toffee", "Neigh", 0, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400,
						"Bad Request", "totalprice has to be a positive number" },
				{ "totalprice contains negative number", "Toffee", "Neigh", -111, true, "2025-06-20", "2025-06-30",
						"Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
				{ "totalprice contains null value", "Toffee", "Neigh", null, true, "2025-06-20", "2025-06-30",
						"Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
				{ "Mismatched Data Type - totalprice contains String value only", "Toffee", "Neigh", "111", true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"totalprice has to be a positive number" },
				{ "Mismatched Data Type - totalprice contains Boolean value only", "Toffee", "Neigh", true, true,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"totalprice has to be a positive number" },
				{ "Mismatched Data Type - depositpaid contains String value only", "Toffee", "Neigh", 111, "true",
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"depositpaid has to be true or false" },
				{ "Mismatched Data Type - depositpaid contains Number only", "Toffee", "Neigh", 111, 1, "2025-06-20",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "depositpaid has to be true or false" },
				{ "Mismatched Data Type - depositpaid contains null value", "Toffee", "Neigh", 111, null,
						"2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request",
						"depositpaid has to be true or false" },
				{ "Mismatched Data Type - checkin contains Number only", "Toffee", "Neigh", 111, true, 2020,
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
				{ "Mismatched Data Type - checkin contains String only", "Toffee", "Neigh", 111, true, "Tomorrow",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
				{ "Mismatched Data Type - checkin contains null value", "Toffee", "Neigh", 111, true, null, "2025-06-30",
						"Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
				{ "Mismatched Data Type - checkin contains Aplha-numeric only", "Toffee", "Neigh", 111, true,
						"2020-June-20", "2025-06-30", "Only vegan meal", 400, "Bad Request","checkin only accepts future date" },
				{ "Mismatched Data Type - checkin contains Past date", "Toffee", "Neigh", 111, true, "2025-06-02",
						"2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
				{ "Mismatched Data Type - checkout contains Number only", "Toffee", "Neigh", 111, true, "2025-06-20",
						20250630, "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
				{ "Mismatched Data Type - checkout contains String only", "Toffee", "Neigh", 111, true, "2025-06-20",
						"Tomorrow", "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
				{ "Mismatched Data Type - checkout contains null value", "Toffee", "Neigh", 111, true, "2025-06-20",
						null, "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
				{ "Mismatched Data Type - checkout contains Aplha-numeric only", "Toffee", "Neigh", 111, true,
						"2025-06-20", "2020-Tomorrow", "Only vegan meal", 400, "Bad Request","checkout only accepts future date" },
				{ "Mismatched Data Type - checkout contains Past date", "Toffee", "Neigh", 111, true, "2025-06-20",
						"2025-06-05", "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
				{ "Mismatched Data Type - additionalneeds contains numbers only", "Toffee", "Neigh", 111, true,
						"2025-06-20", "2025-06-30", 33, 400, "Bad Request", "additionalneeds Must contain String" },
				{ "Mismatched Data Type - additionalneeds contains special characters only", "Toffee", "Neigh", 111,
						true, "2025-06-20", "2025-06-30", "@-&", 400, "Bad Request",
						"additionalneeds Must contain String" },
				{ "Mismatched Data Type - additionalneeds contains boolean value only", "Toffee", "Neigh", 111, true,
						"2025-06-20", "2025-06-30", true, 400, "Bad Request", "additionalneeds Must contain String" },
				{ "Mismatched Data Type - additionalneeds contains null value", "Toffee", "Neigh", 111, true,
						"2025-06-20", "2025-06-30", null, 400, "Bad Request","additionalneeds Must contain String" } 
				};
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
