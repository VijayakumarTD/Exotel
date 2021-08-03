package commonMethods;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import commonMethods.Config;
import commonMethods.Utils;
import atu.testng.reports.ATUReports;
import atu.testng.reports.exceptions.ATUReporterException;
import atu.testng.reports.utils.Directory;
public class Config  extends Keywords {
	public  WebDriver driver;
	ATUReports atuRep=new ATUReports();
	
	public  WebDriver getDriver() throws MalformedURLException {
		return this.driver;
		
	}
public  void setDriver(WebDriver paramDriver) throws MalformedURLException {
		this.driver=paramDriver;
	}
		
public WebDriver getWebDriver(String browserName) throws MalformedURLException {
	if (browserName.equals("Chrome")) {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/driver/chromedriver.exe");
		driver = new ChromeDriver();
		System.out.println("Browser launched...");	
		setDriver(driver);
		driver.manage().window().maximize();
	} else if (browserName.equalsIgnoreCase("Firefox")) {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\driver\\geckodriver.exe");
		driver = new FirefoxDriver();
		setDriver(driver);
		driver.manage().window().maximize();

	} else if (browserName.equalsIgnoreCase("IE")) {
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\driver\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		setDriver(driver);
		driver.manage().window().maximize();
		
	}
	return driver;
}	

}
