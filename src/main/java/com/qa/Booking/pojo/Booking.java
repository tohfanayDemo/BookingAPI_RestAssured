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

	private String firstname;
	private String lastname;
	private double totalprice;
	private boolean depositpaid;
	private Bookingdates bookingdates;
	private String additionalneeds;
	
	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	public static class Bookingdates{
		private String checkin;
		private String checkout;
	}
}
