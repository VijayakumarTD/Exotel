package commonMethods;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.sound.sampled.Port;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.seleniumhq.jetty9.server.Server;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import scripts.Exotel_validation;
import scripts.OutboundCall;
import scripts.Test_file;
import atu.testng.reports.exceptions.ATUReporterException;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class, MethodListener.class })

public class Testcases extends Config {
	{
		System.setProperty("atu.reporter.config", System.getProperty("user.dir") + "/atu.properties");
	}
	public String appURL;
	public String usernameValue;
	public String passwordValue;
	public String project_Name;
	public String version_Name;
	public String environment;
	public String browser;
	public WebDriver driver;

	
	File f = new File(report_folder_create + "\\reports");
	
	Test_file test = new Test_file();
	Exotel_validation server = new Exotel_validation();
	OutboundCall outbound = new OutboundCall();
	

	
	@BeforeClass
	public void getDataFromConfig() throws ATUReporterException, IOException, InterruptedException {
		
		

	}
	
	
	
	
	@Test
	public void outbound_001() throws IOException, JSONException {
		outbound.statusCallback();
	}
	
	
	
	@Test
	public void outbound_011() {
		outbound.EXTC_011();
	}
	
	@Test
	public void outbound_012() {
		outbound.EXTC_012();
	}
	
	@Test
	public void outbound_013() {
		outbound.EXTC_013();
	}
	
	
	
	
	

	@AfterClass
	public void teardown() {
		

		
	}
 
	
	
	

}
