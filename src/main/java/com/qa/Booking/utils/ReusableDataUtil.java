package com.qa.Booking.utils;

public class ReusableDataUtil {
	
	public static Object[][] getNegativeBookingPayloadData() {
		return new Object[][] {
			//{ "All Fields Empty", "", "", 0, false, "", "", "", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Only One Field Empty - firstname", "", "Neigh", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Only One Field Empty - lastname", "Toffee", "", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
            //{ "Only One Field Empty - totalprice omitted", "Toffee", "Neigh", null, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
            //{ "Only One Field Empty - depositpaid omitted", "Toffee", "Neigh", 111, null, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Only One Field Empty - checkin", "Toffee", "Neigh", 111, true, "", "2025-06-30", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Only One Field Empty - checkout", "Toffee", "Neigh", 111, true, "2025-06-20", "", "Only vegan meal", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Only One Field Empty - additionalneeds", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30", "", 400, "Bad Request", "All Fields are mandatory" },
			//{ "Mismatched Data Type - firstname contains numbers only", 33, "Neigh", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "firstname Must contain only letters" },
			//{ "Mismatched Data Type - firstname contains alpha-numeric value only", "Toffee33", "Neigh", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "firstname Must contain only letters" },
			//{ "Mismatched Data Type - firstname contains special characters only", "@-&", "Neigh", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "firstname Must contain only letters" },
			//{ "Mismatched Data Type - firstname contains boolean value only", true, "Neigh", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "firstname Must contain only letters" },
			//{ "Mismatched Data Type - lastname contains numbers only", "Toffee", 33, 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "lastname Must contain only letters" },
			//{ "Mismatched Data Type - lastname contains alpha-numeric value only", "Toffee", "Neigh33", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "lastname Must contain only letters" },
			//{ "Mismatched Data Type - lastname contains special characters only", "Toffee", "@-&", 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "lastname Must contain only letters" },
			//{ "Mismatched Data Type - lastname contains boolean value only", "Toffee", false, 111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "lastname Must contain only letters" },
			//{ "totalprice is zero", "Toffee", "Neigh", 0, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
			//{ "totalprice contains negative number", "Toffee", "Neigh", -111, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
            //{ "Mismatched Data Type - totalprice contains null value", "Toffee", "Neigh", null, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
			//{ "Mismatched Data Type - totalprice contains String value only", "Toffee", "Neigh", "111", true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
			//{ "Mismatched Data Type - totalprice contains Boolean value only", "Toffee", "Neigh", true, true, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "totalprice has to be a positive number" },
			//{ "Mismatched Data Type - depositpaid contains String value only", "Toffee", "Neigh", 111, "true", "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "depositpaid has to be true or false" },
			//{ "Mismatched Data Type - depositpaid contains Number only", "Toffee", "Neigh", 111, 1, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "depositpaid has to be true or false" },
            //{ "Mismatched Data Type - depositpaid contains null value", "Toffee", "Neigh", 111, null, "2025-06-20", "2025-06-30", "Only vegan meal", 400, "Bad Request", "depositpaid has to be true or false" },
   			//{ "Mismatched Data Type - checkin contains Number only", "Toffee", "Neigh", 111, true, 2020, "2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
			//{ "Mismatched Data Type - checkin contains String only", "Toffee", "Neigh", 111, true, "Tomorrow", "2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
            //{ "Mismatched Data Type - checkin contains null value", "Toffee", "Neigh", 111, true, null, "2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
			//{ "Mismatched Data Type - checkin contains Aplha-numeric only", "Toffee", "Neigh", 111, true, "2020-June-20", "2025-06-30", "Only vegan meal", 400, "Bad Request","checkin only accepts future date" },
			//{ "Mismatched Data Type - checkin contains Past date", "Toffee", "Neigh", 111, true, "2025-06-02", "2025-06-30", "Only vegan meal", 400, "Bad Request", "checkin only accepts future date" },
			//{ "Mismatched Data Type - checkout contains Number only", "Toffee", "Neigh", 111, true, "2025-06-20", 20250630, "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
			//{ "Mismatched Data Type - checkout contains String only", "Toffee", "Neigh", 111, true, "2025-06-20", "Tomorrow", "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
            //{ "Mismatched Data Type - checkout contains null value", "Toffee", "Neigh", 111, true, "2025-06-20", null, "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
			//{ "Mismatched Data Type - checkout contains Aplha-numeric only", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-Tomorrow", "Only vegan meal", 400, "Bad Request","checkout only accepts future date" },
			//{ "Mismatched Data Type - checkout contains Past date", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-05", "Only vegan meal", 400, "Bad Request", "checkout only accepts future date" },
			//{ "Mismatched Data Type - additionalneeds contains numbers only", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30", 33, 400, "Bad Request", "additionalneeds Must contain String" },
			//{ "Mismatched Data Type - additionalneeds contains special characters only", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30", "@-&", 400, "Bad Request", "additionalneeds Must contain String" },
			//{ "Mismatched Data Type - additionalneeds contains boolean value only", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30", true, 400, "Bad Request", "additionalneeds Must contain String" },
            //{ "Mismatched Data Type - additionalneeds contains null value", "Toffee", "Neigh", 111, true, "2025-06-20", "2025-06-30", null, 400, "Bad Request","additionalneeds Must contain String" } 
			};
    	}
	
	public static Object[][] getInvalidBookingIdData() {
		return new Object[][] {
			{"Incorrect PathParameter - Non-existing ID", 111111111, 404, "Not Found"},
	        {"Incorrect PathParameter - String", "Twelve", 400, "Bad Request"},
	        {"Incorrect PathParameter - Null Value", null, 400, "Bad Request"},
	        {"Incorrect PathParameter - Special Character", "@-$", 400, "Bad Request"}
	        
    	};
	}
	
	public static Object[][] getInvalidBookingNameData() {
		return new Object[][] {
	    	{"Incorrect Path Parameter - Non-existent First Name", "Meh", null, 404, "Not Found"},
	        {"Incorrect Path Parameter - Non-existent Last Name", null, "Meh", 404, "Not Found"},
	        {"Invalid Path Parameter - First Name contains Number", 23, null, 400, "Bad Request"},
	        {"Invalid Path Parameter - Last Name contains Number", null, 23, 400, "Bad Request"},
	        {"Invalid Path Parameter - First Name contains Boolean value", true, null, 400, "Bad Request"},
	        {"Invalid Path Parameter - Last Name contains Boolean value", null, false, 400, "Bad Request"},
	        {"Invalid Path Parameter - First Name contains Special character", "@-!", null, 400, "Bad Request"},
	        {"Invalid Path Parameter - Last Name contains Special character", null, "@-!", 400, "Bad Request"},
	        {"Invalid Path Parameter - First Name contains White space", " ", null, 400, "Bad Request"},
	        {"Invalid Path Parameter - Last Name contains White space", null, " ", 400, "Bad Request"},
	        {"Empty String Values - First Name", "", null, 400, "Bad Request"},
	        {"Empty String Values - Last Name", null, "", 400, "Bad Request"},
	        {"Empty String Values - Both Parameters", "", "", 400, "Bad Request"},
	        {"Missing First Name", null, null, 400, "Bad Request"},
	        {"Missing Last Name", null, null, 400, "Bad Request"},
	        {"Missing Both Names", null, null, 400, "Bad Request"}
	    };
	}
}





