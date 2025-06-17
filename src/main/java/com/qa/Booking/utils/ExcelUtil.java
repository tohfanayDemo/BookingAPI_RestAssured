package com.qa.Booking.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

//always import from  org.apache.poi.ss
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {

	private static final String TEST_DATA_SHEET_PATH = "./src/test/resources/testData/RestAssured_DataDriven.xlsx";

	private static Workbook book; // it reaches up to excel workbook
	private static Sheet sheet; // it reaches up to excel sheet now

	public static Object[][] getTestData(String sheetName) {

		Object data[][] = null;

		try {
			FileInputStream ip = new FileInputStream(TEST_DATA_SHEET_PATH);
			book = WorkbookFactory.create(ip); 
			sheet = book.getSheet(sheetName);

			// get last row number from active sheet
			int row = sheet.getLastRowNum();
			// get column number for a given row
			int column = sheet.getRow(0).getLastCellNum();

			System.out.println("Number of rows = " + row);
			System.out.println("Number of column = " + column);

			data = new Object[row][column];

			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
					// System.out.println(data[i][j]);
				}

				// System.out.println("------------------------------------------");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	
	  public static void main(String[] args) {
	  
	  ExcelUtil.getTestData(TEST_DATA_SHEET_PATH);
	  
	  }
	 

}
