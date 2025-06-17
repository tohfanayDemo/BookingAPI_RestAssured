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
@JsonInclude(Include.NON_NULL)
public class Booking {

	private Object firstname;
	private Object lastname;
	private Object totalprice;
	private Object depositpaid;
	private Bookingdates bookingdates;
	private Object additionalneeds;

	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	public static class Bookingdates {
		private Object checkin;
		private Object checkout;
	}
}
