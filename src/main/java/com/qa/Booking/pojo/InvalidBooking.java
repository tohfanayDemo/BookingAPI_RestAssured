package com.qa.Booking.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL) // exclude the property only during serialization and not during creation of
								// object
public class InvalidBooking {

	private Object firstname;
	private Object lastname;
	private Object totalprice;
	private Object depositpaid;
	private InvalidBookingdates bookingdates;
	private Object additionalneeds;

	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	public static class InvalidBookingdates {
		private Object checkin;
		private Object checkout;
	}
}
