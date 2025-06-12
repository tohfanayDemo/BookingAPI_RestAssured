package com.qa.Booking.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.qa.Booking.frameworkException.APIFrameworkException;

public class ConfigurationManager {
	
	private Properties prop;
	private FileInputStream ip;

	public Properties initProp() {
		
		prop = new Properties();
		String envName = System.getProperty("env");
		
		try {
			if(envName == null) {
				System.out.println("No environment has been given. Hence, running test on QA environment");		
				ip = new FileInputStream("src\\test\\resources\\config\\qa.config.properties");
			}
			else 
			{
				System.out.println("Running test on "+envName+" environment");
				
					switch (envName.toLowerCase().trim()) {
					case "qa":
						ip = new FileInputStream("src\\test\\resources\\config\\qa.config.properties");
						break;
					case "dev":
						ip = new FileInputStream("src\\test\\resources\\config\\dev.config.properties");
						break;
					case "stage":
						ip = new FileInputStream("src\\test\\resources\\config\\stage.config.properties");
						break;
					case "uat":
						ip = new FileInputStream("src\\test\\resources\\config\\uat.config.properties");
						break;	
					default:
						System.out.println("Please pass the right env name .." + envName);
						throw new APIFrameworkException("Incorrect Env is given");					
					}				
			}
			
			prop.load(ip);
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
		
	}

}
