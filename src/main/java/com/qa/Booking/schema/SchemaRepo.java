package com.qa.Booking.schema;

import java.util.HashMap;
import java.util.Map;

public class SchemaRepo {
	
	private static final Map<String, String> schemas = new HashMap<>();

        
	public static final String GETALL_GETBYNAME_BOOKING_SCHEMA = "{\r\n"
			+ "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\r\n"
			+ "  \"title\": \"Generated schema for Root\",\r\n"
			+ "  \"type\": \"array\",\r\n"
			+ "  \"items\": {\r\n"
			+ "    \"type\": \"object\",\r\n"
			+ "    \"properties\": {\r\n"
			+ "      \"bookingid\": {\r\n"
			+ "        \"type\": \"number\"\r\n"
			+ "      }\r\n"
			+ "    },\r\n"
			+ "    \"required\": [\r\n"
			+ "      \"bookingid\"\r\n"
			+ "    ]\r\n"
			+ "  }\r\n"
			+ "}";
	
	public static final String GET_PUT_PATCH_BOOKING_BYID_SCHEMA = "{\r\n"
			+ "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\r\n"
			+ "  \"title\": \"Generated schema for Root\",\r\n"
			+ "  \"type\": \"object\",\r\n"
			+ "  \"properties\": {\r\n"
			+ "    \"firstname\": {\r\n"
			+ "      \"type\": \"string\"\r\n"
			+ "    },\r\n"
			+ "    \"lastname\": {\r\n"
			+ "      \"type\": \"string\"\r\n"
			+ "    },\r\n"
			+ "    \"totalprice\": {\r\n"
			+ "      \"type\": \"number\"\r\n"
			+ "    },\r\n"
			+ "    \"depositpaid\": {\r\n"
			+ "      \"type\": \"boolean\"\r\n"
			+ "    },\r\n"
			+ "    \"bookingdates\": {\r\n"
			+ "      \"type\": \"object\",\r\n"
			+ "      \"properties\": {\r\n"
			+ "        \"checkin\": {\r\n"
			+ "          \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"checkout\": {\r\n"
			+ "          \"type\": \"string\"\r\n"
			+ "        }\r\n"
			+ "      },\r\n"
			+ "      \"required\": [\r\n"
			+ "        \"checkin\",\r\n"
			+ "        \"checkout\"\r\n"
			+ "      ]\r\n"
			+ "    },\r\n"
			+ "    \"additionalneeds\": {\r\n"
			+ "      \"type\": \"string\"\r\n"
			+ "    }\r\n"
			+ "  },\r\n"
			+ "  \"required\": [\r\n"
			+ "    \"firstname\",\r\n"
			+ "    \"lastname\",\r\n"
			+ "    \"totalprice\",\r\n"
			+ "    \"depositpaid\",\r\n"
			+ "    \"bookingdates\",\r\n"
			+ "    \"additionalneeds\"\r\n"
			+ "  ]\r\n"
			+ "}";
	
	public static final String POST_BOOKING_SCHEMA = "{\r\n"
			+ "    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\r\n"
			+ "    \"title\": \"Generated schema for Root\",\r\n"
			+ "    \"type\": \"object\",\r\n"
			+ "    \"properties\": {\r\n"
			+ "        \"bookingid\": {\r\n"
			+ "        \"type\": \"number\"\r\n"
			+ "        },\r\n"
			+ "        \"booking\": {\r\n"
			+ "        \"type\": \"object\",\r\n"
			+ "        \"properties\": {\r\n"
			+ "            \"firstname\": {\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "            },\r\n"
			+ "            \"lastname\": {\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "            },\r\n"
			+ "            \"totalprice\": {\r\n"
			+ "            \"type\": \"number\"\r\n"
			+ "            },\r\n"
			+ "            \"depositpaid\": {\r\n"
			+ "            \"type\": \"boolean\"\r\n"
			+ "            },\r\n"
			+ "            \"bookingdates\": {\r\n"
			+ "            \"type\": \"object\",\r\n"
			+ "            \"properties\": {\r\n"
			+ "                \"checkin\": {\r\n"
			+ "                \"type\": \"string\"\r\n"
			+ "                },\r\n"
			+ "                \"checkout\": {\r\n"
			+ "                \"type\": \"string\"\r\n"
			+ "                }\r\n"
			+ "            },\r\n"
			+ "            \"required\": [\r\n"
			+ "                \"checkin\",\r\n"
			+ "                \"checkout\"\r\n"
			+ "            ]\r\n"
			+ "            },\r\n"
			+ "            \"additionalneeds\": {\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "            }\r\n"
			+ "        },\r\n"
			+ "        \"required\": [\r\n"
			+ "            \"firstname\",\r\n"
			+ "            \"lastname\",\r\n"
			+ "            \"totalprice\",\r\n"
			+ "            \"depositpaid\",\r\n"
			+ "            \"bookingdates\",\r\n"
			+ "            \"additionalneeds\"\r\n"
			+ "        ]\r\n"
			+ "        }\r\n"
			+ "    },\r\n"
			+ "    \"required\": [\r\n"
			+ "        \"bookingid\",\r\n"
			+ "        \"booking\"\r\n"
			+ "    ]\r\n"
			+ "    }";
	
	static {
		schemas.put("getAllBooking", GETALL_GETBYNAME_BOOKING_SCHEMA);
        schemas.put("createBooking", POST_BOOKING_SCHEMA);
        schemas.put("updateBooking", GET_PUT_PATCH_BOOKING_BYID_SCHEMA);
        schemas.put("getBookingById", GET_PUT_PATCH_BOOKING_BYID_SCHEMA);
        schemas.put("getBookingByName", GETALL_GETBYNAME_BOOKING_SCHEMA);
        schemas.put("partialUpdateBooking", GET_PUT_PATCH_BOOKING_BYID_SCHEMA);

    }
	
	/*
	 * getAllBookings=GetBookingByName done
	 * getBookingById = UpdateBooking = PartialUpdateBooking
	 * createBooking done
	 * 
	 * */

    public static String getSchema(String key) {
        return schemas.get(key);
    }
}
