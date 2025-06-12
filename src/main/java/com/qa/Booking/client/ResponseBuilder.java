package com.qa.Booking.client;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class ResponseBuilder {
	
	
	private static ContentType toContentType(String type) {
	    switch (type.toLowerCase()) {
	        case "json": return ContentType.JSON;
	        case "xml": return ContentType.XML;
	        case "text": return ContentType.TEXT;
	        case "html": return ContentType.HTML;
	        default: throw new IllegalArgumentException("Unsupported content type: " + type);
	    }
	}
	
	public static ResponseSpecification expResSpec(int expectedStatusCode, String expectedContentType) {

		ResponseSpecification resSpec = new ResponseSpecBuilder()
				.expectHeader("Server", "Heroku")
		   		.expectStatusCode(expectedStatusCode)
		   		.expectContentType(toContentType(expectedContentType))
		   		.build();
		return resSpec;
	}

}
