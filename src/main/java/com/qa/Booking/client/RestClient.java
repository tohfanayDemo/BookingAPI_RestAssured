package com.qa.Booking.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

	private RequestSpecBuilder requestSpec;	

	private static ContentType toContentType(String type) {
	    switch (type.toLowerCase()) {
	        case "json": return ContentType.JSON;
	        case "xml": return ContentType.XML;
	        case "text": return ContentType.TEXT;
	        case "html": return ContentType.HTML;
	        default: throw new IllegalArgumentException("Unsupported content type: " + type);
	    }
	}
	
	/*POST/PUT*/
	public RequestSpecification createRequestSpec_PostPutPatch(String baseURI, String expectedContentType, 
			Object requestBody, String... bearerToken) {

		requestSpec = new RequestSpecBuilder();	
		requestSpec.setBaseUri(baseURI);
		requestSpec.setContentType(toContentType(expectedContentType));
		requestSpec.addHeader("Accept", "*/*");
		requestSpec.addHeader("Connection", "keep-alive");
		requestSpec.setBody(requestBody);
		
		//Set Bearer Token
		if (bearerToken.length > 0 && bearerToken[0].equalsIgnoreCase("invalid")) {
			requestSpec.addHeader("Authorization", "Bearer 12dwsqew845dsInvalid");
		}else if (bearerToken.length > 0) {
			requestSpec.addHeader("Authorization", "Bearer " + bearerToken[0]);
		}

		return requestSpec.build();
	}
	
	
	public RequestSpecification createRequestSpec_GetDelete(String baseURI, String expectedContentType, String... bearerToken) {

		requestSpec = new RequestSpecBuilder();	
		requestSpec.setBaseUri(baseURI);
		requestSpec.setContentType(toContentType(expectedContentType));
		requestSpec.addHeader("Accept", "*/*");
		requestSpec.addHeader("Connection", "keep-alive");

		//Set Bearer Token
		if (bearerToken.length > 0 && bearerToken[0].equalsIgnoreCase("invalid")) {
			requestSpec.addHeader("Authorization", "Bearer 12dwsqew845dsInvalid");
		}else if (bearerToken.length > 0) {
			requestSpec.addHeader("Authorization", "Bearer " + bearerToken[0]);
		}

		return requestSpec.build();
	}
	
	
}
