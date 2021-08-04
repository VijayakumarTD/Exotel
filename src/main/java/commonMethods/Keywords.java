package commonMethods;


import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.reports.utils.Directory;
import okhttp3.Credentials;

public class Keywords extends ATUReports implements OR {
	private static final String HMAC_SHA1_ALGORITHM = "HMACSHA1";

	// Testcases test1=new Testcases();

	public String ElementWait = Utils.getDataFromTestConfig("Wait Time");
	public int WaitElementSeconds = new Integer(ElementWait);
	public String Main_Window = "";
	public ArrayList<String> tabs;
	public WebElement fromElement;
	public ITestResult result;
	public String report_Filepath = Utils.getDataFromTestConfig("Reports  path");
	public String date = getCurrentDate();
	public String folder_name = report_Filepath.concat(date);
	public String folder_name_subfolder = folder_name.concat("/");
	public String report_folder_create = folder_name_subfolder;
	public String report_name = "PocReport";
	public String filepath_date_concat = folder_name_subfolder.concat(report_name).concat(".html");
	public String screenshot_folder_name = folder_name_subfolder.concat("Screenshot");
	public String screenshot_folder_path = screenshot_folder_name.concat("/");
	public String screenshot_folder_create = screenshot_folder_path;
	public String firstValue;
	public String secondValue;
	public boolean failureScreenshot = Directory.TestPassScreenshot;
	

	public String getCurrentDate() {
		Format formatter = new SimpleDateFormat("dd-MM-YYYY HH-mm-ss");
		Date date = new Date();
		String value = formatter.format(date);
		return value;
	}

	public String[] splitXpath(String path) {
		String[] a = path.split(">");
		return a;
	}
	
	 public static Timestamp dateTime()
     {
          //Date object
 	 Date date= new Date();
          //getTime() returns current time in milliseconds
 	 long time = date.getTime();
          //Passed the milliseconds to constructor of Timestamp class 
 	 Timestamp ts = new Timestamp(time);
 	 
 	 
 	 return ts;
 	 
 	 //System.out.println("Current Time Stamp: "+ts);
 	 
     }

	public String screenshot(WebDriver driver, String screenshotName) {
		String image_dest = null;
		try {

			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String currenttime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			image_dest = System.getProperty("user.dir").concat("/toastmessages/" + currenttime + screenshotName)
					.concat(".png");
			File destination = new File(image_dest);
			FileUtils.copyFile(source, destination);
			return image_dest;
		} catch (Exception e) {
			System.out.println("Exception while taking Screenshot" + e.getMessage());
			return e.getMessage();
		}
	}

	public void wait(String inputData) {
		try {
			int time = Integer.parseInt(inputData);
			int seconds = time * 1000;
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			add1("Unable to wait ", LogAs.FAILED, true, "Wait");
			Assert.fail();
		}
	}

	public void waitForElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			int WaitElementSeconds1 = new Integer(ElementWait);
			WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(values[1])));
			add(driver, "Wait for the Element " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Element Not Found - " + values[0] + e, LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void waitForElementWithLessWait(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(values[1])));

	}

	public void click(WebDriver driver, String path) {
		String[] values = splitXpath(path);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.click();
			add(driver, "Click on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to click on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void clickByClassName(WebDriver driver, String className) {
		String[] values = splitXpath(className);
		try {
			WebElement webElement = driver.findElement(By.className(values[1]));
			webElement.click();
			add(driver, "Click on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to click on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void clickWithoutFail(WebDriver driver, String path) {
		String[] values = splitXpath(path);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		webElement.click();
		add(driver, "Click on " + values[0], LogAs.PASSED, true, values[0]);

	}

	public void jsClickByXPath(WebDriver driver, String Xpath) {
		String[] values = splitXpath(Xpath);
		try {
			// waitForElement(driver,Xpath);
			WebElement element = driver.findElement(By.xpath(values[1]));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			add(driver, "Click on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to click on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public String clearAndType(WebDriver driver, String xpaths, String keysToSend) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.clear();
			webElement.sendKeys(keysToSend);
			add(driver, "Clear and Type on " + values[0], keysToSend, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to type on " + values[0], keysToSend, true, values[0]);
			Assert.fail();
		}
		return keysToSend;
	}

	public String actionType(WebDriver driver, String xpath, String keysToSend) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Actions action = new Actions(driver);
			action.sendKeys(webElement, keysToSend).build().perform();
			add(driver, "Type on " + values[0], keysToSend, true, values[0]);
		} catch (StaleElementReferenceException e) {
			add1(driver, "Unable to type on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
		return keysToSend;
	}

	public void actionClick(WebDriver driver, String Xpath) {
		String[] values = splitXpath(Xpath);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		try {
			Actions action = new Actions(driver);
			action.click(webElement).build().perform();
			add(driver, "Click on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to click on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void doubleClick(WebDriver driver, String element) {
		String[] values = splitXpath(element);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Actions action = new Actions(driver).doubleClick(webElement);
			action.build().perform();
			add(driver, "Double click on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to click on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public String sendKeys(WebDriver driver, String xpaths, String keysToSend) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.sendKeys(keysToSend);
			add(driver, "Type on " + values[0], keysToSend, true, values[0]);

		} catch (Exception e) {
			add1(driver, "Unable to type on " + values[0], keysToSend, true, values[0]);
			Assert.fail();
		}
		return keysToSend;
	}

	public void clear(WebDriver driver, String xpaths) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.clear();
			add(driver, "Clear on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to clear on " + values[0], LogAs.PASSED, true, values[0]);
			Assert.fail();
		}
	}

	public void selectCheckBox(WebDriver driver, String xpaths) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			if (element.isSelected()) {
			} else {
				element.click();
			}
			add(driver, "Select the checkbox on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to select the checkbox on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void deSelectCheckBox(WebDriver driver, String xpaths) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			if (element.isSelected()) {
				element.click();
			} else {
			}
			add(driver, "Deselect the checkbox on " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to deselect the checkbox on " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void selectByIndex(WebDriver driver, String xpaths, String inputData) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Integer index = new Integer(inputData);
			Select selectBox = new Select(webElement);
			selectBox.selectByIndex(index);
			add(driver, "Select the Dropdown by Index " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to select the Dropdown by Index " + values[0], inputData, true, values[0]);
			Assert.fail();
		}
	}

	public void selectByText(WebDriver driver, String xpaths, String inputData) {
		String[] values = splitXpath(xpaths);
		try {
			Select selectBox = new Select(driver.findElement(By.xpath(values[1])));
			selectBox.selectByVisibleText(inputData);
			add(driver, "Select the Dropdown by text " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to select the Dropdown by text " + values[0], inputData, true, values[0]);
			Assert.fail();
		}
	}

	public void selectByValue(WebDriver driver, String xpaths, String inputData) {
		String[] values = splitXpath(xpaths);
		try {

			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Select selectBox = new Select(webElement);
			selectBox.selectByValue(inputData);
			add(driver, "Select the Dropdown by Value " + values[0], inputData, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to select the Dropdown by Value " + values[0], inputData, true, values[0]);
			Assert.fail();
		}
	}

	public void checkTwoString(String GetText1, String GetText2) {
		try {
			if (GetText1.equalsIgnoreCase(GetText2)) {
				add2( "Expected Value " + GetText1 + " and Actual Value " + GetText2 + " are matched", GetText1, true, GetText2,GetText2);
				
			} else if (GetText1.equals(null)) {
				add3("Expected Value " + GetText1 + " and Actual Value " + GetText2 + " are not matched", GetText1, true, GetText2,GetText2);
				
				Assert.fail();
			} else if (GetText2.equals(null)) {
				add3("Expected Value " + GetText1 + " and Actual Value " + GetText2 + " are not matched", GetText1, true, GetText2,GetText2);
				
				Assert.fail();
			} else {
				add3("Expected Value " + GetText1 + " and Actual Value " + GetText2 + " are not matched", GetText1, true, GetText2,GetText2);
				
				Assert.fail();
			}
		} catch (NoSuchElementException e) {
			add3("Expected Value " + GetText1 + " and Actual Value " + GetText2 + " are not matched", GetText1, true, GetText2,GetText2);
			Assert.fail();
		}
	}

	public void checkPartialText(WebDriver driver, String GetText1, String GetText2) {
		try {
			if ((GetText1.contains(GetText2)) || (GetText2.contains(GetText1))) {
				add(driver, "The Value1 " + GetText1 + " and Value2 " + GetText2 + " are matched", LogAs.PASSED, true,
						GetText1);
			} else {
				add(driver, "The Value1 " + GetText1 + " and Value2 " + GetText2 + " are not matched", LogAs.FAILED,
						true, GetText1);
				Assert.fail();
			}
		} catch (NoSuchElementException e) {
			add1(driver, "The Value1 " + GetText1 + " and Value2 " + GetText2 + " are not matched", LogAs.FAILED, true,
					GetText1);
			Assert.fail();
		}
	}

	public void close(WebDriver driver) {
		try {
			driver.close();
			add(driver, "Application is closed", LogAs.PASSED, true, "Not Req");
		} catch (Exception e) {
			add1(driver, "Unable to close the application ", LogAs.FAILED, true, "Not Req");
			Assert.fail();
		}
	}

	public String getText(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			String text = webElement.getText();
			add(driver, "The value ' " + text + " ' is retrieved from the element ' " + values[0] + "'", LogAs.PASSED,
					true, values[0]);
			return text;

		} catch (Exception e) {
			add1(driver, "Unable to retrieve the text " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
			return null;
		}
	}

	public String getTextWithoutFail(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		String text = webElement.getText();
		add(driver, "The value ' " + text + " ' is retrieved for the element ' " + values[0] + "'", LogAs.PASSED, true,
				values[0]);
		return text;

	}

	public void waitTime(WebDriver driver, String waitSeconds) {
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(waitSeconds), TimeUnit.SECONDS);
	}

	public void scrollBottom(WebDriver driver) {
		try {

			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,250)", "");
			waitTime(driver, "5");
			add(driver, "Scrolled to the bottom ", LogAs.PASSED, true, "Not");
		} catch (Exception e) {
			add1(driver, "Unable to scroll to the bottom", LogAs.FAILED, true, "Not");
			Assert.fail();
		}
	}

	public void scrollTop(WebDriver driver) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,-250)", "");
			add(driver, "Scrolled to the Top ", LogAs.PASSED, true, "Not");

		} catch (Exception e) {
			add1(driver, "Unable to scroll to the Top", LogAs.FAILED, true, "Not");
			Assert.fail();
		}
	}

	public boolean verifyElementIsPresent(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			element.isDisplayed();
			add(driver, "Element '" + values[0] + "' is verified ", LogAs.PASSED, true, values[0]);
			return true;
		} catch (NoSuchElementException e) {
			add1(driver, "Element is Not Present " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
			return true;
		}
	}

	public void verifyElementHasText(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			String text = driver.findElement(By.xpath(values[1])).getText();
			if (!text.equals("")) {
				add(driver, "Element '" + values[0] + "' has text " + text, LogAs.PASSED, true, values[0]);
			} else {
				add1(driver, "No text on the element " + values[0], LogAs.FAILED, true, values[0]);
				Assert.fail();
			}
		} catch (NoSuchElementException e) {
			add1(driver, "Element is Not Present " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public String getAttribute(WebDriver driver, String xpath, String attribute) {
		String[] values = splitXpath(xpath);
		try {
			WebElement inputBox = driver.findElement(By.xpath(values[1]));
			String textInsideInputBox = inputBox.getAttribute(attribute);
			add(driver, "Retrieved the text of " + values[0], textInsideInputBox, true, values[0]);
			return textInsideInputBox;
		} catch (NoSuchElementException e) {
			add1(driver, "Unable to retrieve the value " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
			return null;
		}

	}

	public void verifyElementIsNotPresent(WebDriver driver, String xpaths) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			element.isDisplayed();
			add1(driver, "Element is Present" + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();

		} catch (NoSuchElementException e) {
			add(driver, "Verified Element is not Present" + values[0], values[0], true, values[0]);
		}
	}

	public void scrollUsingElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			add(driver, "Scrolled to " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to scroll " + values[0], LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public void goBack(WebDriver driver) {

		try {
			driver.navigate().back();
			add(driver, "Go Back", LogAs.PASSED, true, "goback");

		} catch (Exception e) {
			add(driver, "Unable to Go Back", LogAs.FAILED, true, "goback");
			Assert.fail();
		}
	}

	public void keyBoardEvent(int eventNumber) {
		try {

			Thread.sleep(1000);

			Runtime.getRuntime().exec(

					"cmd /C adb shell input keyevent " + eventNumber);

			Thread.sleep(3000);

		} catch (Throwable t) {

			t.printStackTrace();

		}
	}

	public void waitTillVisibilityElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);

		try {

			WebElement webElement = driver.findElement(By.xpath(values[1]));
			WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds);
			wait.until(ExpectedConditions.visibilityOf(webElement));
			add(driver, "Waited till the element is visible", LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add(driver, "Unable to wait till an element is visible", LogAs.FAILED, true, values[0]);
			Assert.fail();

		}
	}

	public void waitTillElementIsClickable(WebDriver driver, String xpath) {
		try {
			String[] values = splitXpath(xpath);
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds);
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			add(driver, "Waited till the element is clickable", LogAs.PASSED, true, "Scroll down");
		} catch (Exception e) {
			add(driver, "Unable to wait till an element is clickable", LogAs.FAILED, true, "Scroll down");
			Assert.fail();

		}
	}

	public void IsElementEnabled(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.isEnabled();
			add(driver, "Element is enabled" + values[0], LogAs.PASSED, true, values[0]);

		} catch (NoSuchElementException e) {
			add(driver, "Element is not enabled", LogAs.FAILED, true, values[0]);
			Assert.fail();
		}
	}

	public int getRandomNum(WebDriver driver, int upperlimit) {
		List<Integer> randomZeroToSeven = new ArrayList<>();
		for (int i = 1; i <= upperlimit; i++) {
			randomZeroToSeven.add(i);
		}
		Collections.shuffle(randomZeroToSeven);

		return randomZeroToSeven.get(0);

	}

	public void deSelectByIndex(WebDriver driver, String xpath, String inputData) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Integer index = new Integer(inputData);
			Select selectBox = new Select(webElement);
			selectBox.deselectByIndex(index);
			add(driver, "Deselect the dropdown by index " + values[0], LogAs.PASSED, true, values[1]);
		} catch (Exception e) {
			add1(driver, "Unable to deselect the dropdown by index" + values[0], LogAs.FAILED, true, values[1]);
			Assert.fail();
		}
	}

	public void deSelectByValue(WebDriver driver, String xpath, String inputData) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Select selectBox = new Select(webElement);
			selectBox.deselectByValue(inputData);
			add(driver, "Deselect the dropdown by index " + values[0], LogAs.PASSED, true, values[1]);
		} catch (Exception e) {
			add(driver, "Unable to deselect the dropdown by index" + values[0], LogAs.FAILED, true, values[1]);
			Assert.fail();
		}
	}

	public void getWindow(WebDriver driver, String path) {
		try {
			waitTime(driver, "5");
			Main_Window = driver.getWindowHandle();
			String[] values = splitXpath(path);
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.click();
			Thread.sleep(500);
			ArrayList<String> allWindows = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(allWindows.get(1));
		} catch (InterruptedException e) {
		}
	}

	public void switchWindow(WebDriver driver) {
		try {
			driver.switchTo().window(Main_Window);
		} catch (Exception e) {
		}
	}

	public void switchDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public void getAutoit(String exePath) {
		try {
			Runtime.getRuntime().exec(exePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void dragElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			fromElement = webElement;
			add(driver, "Drag an element " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to drag an element " + values[0], LogAs.FAILED, true, values[0]);
		}

	}

	public void dropElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			Actions action = new Actions(driver);
			Action dragDrop = action.dragAndDrop(fromElement, webElement).build();
			dragDrop.perform();
			add(driver, "Drop an element " + values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Unable to drag an element " + values[0], LogAs.FAILED, true, values[0]);
		}
	}

	public boolean isElementSelected(WebDriver driver, String xpaths) {
		String[] values = splitXpath(xpaths);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.isSelected();
			add(driver, "Verified Element is selected " + values[0], LogAs.PASSED, true, values[0]);
			return true;
		} catch (NoSuchElementException e) {

			add1(driver, "Element is not selected " + values[0], LogAs.FAILED, true, values[0]);
			return false;
		}
	}

	public void inVisibilityElement(WebDriver driver, String NormalXpath) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(NormalXpath)));
		} catch (Exception e) {
		}
	}

	public void IstextPresent(WebDriver driver, String inputData) {
		if (driver.getPageSource().contains(inputData)) {
			add(driver, "Text is Present " + inputData, LogAs.PASSED, true, inputData);
		} else {
			add1(driver, "Text is not Present " + inputData, LogAs.FAILED, true, inputData);
		}
	}

	public void waitTillTextIsLoaded(WebDriver driver, String xpath, String inputData) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			WebDriverWait waits = new WebDriverWait(driver, WaitElementSeconds);
			waits.until(ExpectedConditions.textToBePresentInElement(webElement, inputData));
			add(driver, "Waited till the text " + inputData+" is loaded", LogAs.PASSED, true, inputData);
		} catch (Exception e) {
			add1(driver, "Unable to Wait till the text " + inputData+" is loaded", LogAs.FAILED, true, inputData);
		}
	}

	public void verifyTextIsNotPresent(WebDriver driver, String NormalXpath, String inputData) {
		try {
			WebDriverWait waits = new WebDriverWait(driver, WaitElementSeconds);
			waits.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath(NormalXpath), inputData));
			add(driver, "Text is not present"+inputData, LogAs.PASSED, true, inputData);
		} catch (Exception e) {
			add1(driver, "Text is present"+inputData, LogAs.FAILED, true, inputData);
		}

	}

	public void isElementClickable(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			WebDriverWait waits = new WebDriverWait(driver, WaitElementSeconds);
			waits.until(ExpectedConditions.elementToBeClickable(webElement));
			add(driver, "Element is clickable "+values[0], LogAs.PASSED, true, values[0]);
		} catch (Exception e) {
			add1(driver, "Element is not clickable "+values[0], LogAs.FAILED, true, values[0]);
		}
	}

	public void isElementSelectable(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		WebDriverWait waits = new WebDriverWait(driver, WaitElementSeconds);
		waits.until(ExpectedConditions.elementToBeSelected(webElement));
		add(driver, "Element is selectable "+values[0], LogAs.PASSED, true, values[0]);
	} catch (Exception e) {
		add1(driver, "Element is not selectable "+values[0], LogAs.FAILED, true, values[0]);
	}
	}

	public void waitUntilVisibilityOfElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds);
		wait.until(ExpectedConditions.visibilityOf(webElement));
		add(driver, "Wait till the Element is visible "+values[0], LogAs.PASSED, true, values[0]);
		}catch (Exception e) {
			add1(driver, "Element is not visible "+values[0], LogAs.FAILED, true, values[0]);
		}
		
	}

	public void waitForElementNotpresent(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		try {
		WebDriverWait wait = new WebDriverWait(driver, WaitElementSeconds);
		wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))));
		add(driver, "Wait till the Element is visible "+values[0], LogAs.PASSED, true, values[0]);
	}catch (Exception e) {
		add1(driver, "Element is not visible "+values[0], LogAs.FAILED, true, values[0]);
	}
	}

	public String dynamicSendkeys(WebDriver driver, String inputData, String xpath) {
		String[] values = splitXpath(xpath);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		webElement.clear();
		try {
			Thread.sleep(500);
			String currenttime = new SimpleDateFormat("E_yyyyMMddHHmmssa").format(Calendar.getInstance().getTime());
			String originalValue = inputData;
			String combinedValues = currenttime + originalValue;
			sendKeys(driver, xpath, combinedValues);
			return combinedValues;
		} catch (InterruptedException e) {

			return null;
		}

	}

	public void partialTextVerify(String sentence, String word) {
		if (sentence.contains(word)) {
		} else {
		}

	}

	public String enterUniquePhone(WebDriver driver, String path) {
		String[] values = splitXpath(path);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		webElement.clear();
		try {
			Thread.sleep(500);
			String phonenumber = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
			sendKeys(driver, path, phonenumber);
			return phonenumber;
		} catch (InterruptedException e) {
			return null;
		}

	}

	public String dynamicTypeName(WebDriver driver, String inputData, String webElementxPath) {
		String[] values = splitXpath(webElementxPath);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		webElement.clear();
		try {
			Thread.sleep(500);
			String currenttime = new SimpleDateFormat("HH_mmss").format(Calendar.getInstance().getTime());
			String combinedValues = inputData + currenttime;
			sendKeys(driver, webElementxPath, combinedValues);
			return combinedValues;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String sumOfTwoNumbers(String GetText1, String GetText2) {
		try {
			int string1 = Integer.parseInt(GetText1);
			int string2 = Integer.parseInt(GetText2);
			int sum1 = string1 + string2;
			String sum = Integer.toString(sum1);
			return sum;
		} catch (Exception e) {
			return null;
		}
	}

	public void quit(WebDriver driver) {
		try {
			driver.quit();
		} catch (Exception e) {
		}
	}

	public void refreshPage(WebDriver driver) {
		try {
			waitTime(driver, "5");
			driver.navigate().refresh();
			waitTime(driver, "5");
		} catch (Exception e) {
			Assert.fail();
		}
	}

	public void maximize(WebDriver driver) {
		try {
			driver.manage().window().maximize();
		} catch (Exception e) {
			Assert.fail();
		}
	}

	public void keyTab(WebDriver driver) {
		try {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.TAB).build().perform();

		} catch (Exception e) {
			Assert.fail();
		}
	}

	public void uploadFileRobot(String fileLocation) {
		try {
			StringSelection stringSelection = new StringSelection(fileLocation);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (Exception exp) {
			Assert.fail();
		}
	}

	public void goForward(WebDriver driver) {
		try {
			driver.navigate().forward();

		} catch (Exception e) {
			Assert.fail();
		}
	}

	public void keyboardTab(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).perform();
	}

	public void enter(WebDriver driver) {
		try {
			Actions actionObject = new Actions(driver);
			actionObject.sendKeys(Keys.ENTER).build().perform();
		} catch (Exception e) {
			Assert.fail();
		}
	}

	public String alertAccept(WebDriver driver, String path) {
		String[] values = splitXpath(path);

		try {
			WebElement webElement = driver.findElement(By.xpath(values[1]));
			webElement.click();
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			alert.accept();
			return alertText;
		} catch (Exception e) {
			Assert.fail();
			return null;
		}
	}

	public void dismissAlert(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	public String promptBox(WebDriver driver, String path, String inputData) {
		String[] values = splitXpath(path);
		try {

			WebElement element = driver.findElement(By.xpath(values[1]));
			element.click();
			Alert alert = driver.switchTo().alert();
			driver.switchTo().alert().sendKeys(inputData);
			String alertText = alert.getText();
			alert.accept();
			return alertText;
		} catch (Exception e) {
			return null;
		}
	}

	public void switchToFrame(WebDriver driver, String frameName) {
		String[] values = splitXpath(frameName);
		try {
			WebElement element = driver.findElement(By.xpath(values[1]));
			driver.switchTo().frame(element);

		} catch (NoSuchFrameException e) {

		}
	}

	public void switchToDefaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			Assert.fail();
		}
	}

	public void keyDown(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject = actionObject.sendKeys(Keys.ARROW_DOWN);
		actionObject.perform();
	}

	public void keyUp(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject = actionObject.sendKeys(Keys.ARROW_UP);
		actionObject.perform();
	}

	public void keyboardPageUp(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.PAGE_UP).perform();
	}

	public void refreshUsingKeys(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.sendKeys(Keys.F5).perform();
	}

	public void keyboardPageDown(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.PAGE_DOWN).perform();
		waitTime(driver, "5");
	}

	public void keyboardEnd(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		waitTime(driver, "5");
	}

	public void keyboardHome(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).perform();
		waitTime(driver, "5");
	}

	public void keyboardArrowUp(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).perform();
	}

	public void keyboardArrowDown(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_DOWN).perform();
	}

	public void keyboardArrowLeft(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).perform();
	}

	public void keyboardArrowRight(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_RIGHT).perform();
	}

	public void pageMaximizeUsingKey(WebDriver driver) {
		Actions actionObject = new Actions(driver);
		actionObject = actionObject.sendKeys(Keys.F11);
		actionObject.perform();
	}

	public void deleteAllCookies(WebDriver driver) {
		driver.manage().deleteAllCookies();
	}

	public void navigateUrl(WebDriver driver, String inputData) {
		driver.navigate().to(inputData);
	}

	public void highLightElement(WebDriver driver, String xpath) {
		String[] values = splitXpath(xpath);
		WebElement webElement = driver.findElement(By.xpath(values[1]));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", webElement,
				"color: red; border: 3px solid red;");
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", webElement, "");
	}

	/*
	 * public void newTab(WebDriver driver) { try { Robot r = new Robot();
	 * r.keyPress(KeyEvent.VK_CONTROL); r.keyPress(KeyEvent.VK_T);
	 * if(Config.browserName.equalsIgnoreCase("firefox")) { ArrayList<String> tabs =
	 * new ArrayList<String> (driver.getWindowHandles());
	 * driver.switchTo().window(tabs.get(1)); } else
	 * if(Config.browserName.equalsIgnoreCase("chrome")) {
	 * System.out.println("askjdfkdlaj"); ArrayList<String> tabs = new
	 * ArrayList<String> (driver.getWindowHandles());
	 * System.out.println("321356132"); driver.switchTo().window(tabs.get(0));
	 * System.out.println("!@$@#%"); driver.get("http://facebook.com");
	 * System.out.println("{{{{{{{{{{"); } } catch (Exception e) { // TODO: handle
	 * exception }
	 *
	 * }
	 */
	public void newTab(WebDriver driver) {
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_T);
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			get(driver, "http://stackoverflow.com/questions/2001586/how-to-comment-a-block-in-eclipse");
		} catch (Exception e) {
		}
	}

	public void get(WebDriver driver, String url) {
		Capabilities localCapabilities = ((RemoteWebDriver) driver).getCapabilities();
		String browser = localCapabilities.getBrowserName().toLowerCase();
		driver.get(url);
		if (browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("UnKnown")) {
			//wait(driver, "5");
			driver.get("javascript:document.getElementById('overridelink').click();");
			//wait(driver, "5");
		}

	}

	public void closeTab(WebDriver driver) {
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
		// tabs.remove(tabs.get(0));
		driver.switchTo().defaultContent();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<String> tabs1 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs1.get(0));
	}

	public void switchtotab(WebDriver driver, int inputData) {
		Capabilities localCapabilities = ((RemoteWebDriver) driver).getCapabilities();
		String BROWSER_NAME = localCapabilities.getBrowserName().toLowerCase();
		if (BROWSER_NAME.equalsIgnoreCase("firefox")) {
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "\t");
			driver.switchTo().defaultContent();
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(inputData));
		}
		if (BROWSER_NAME.equalsIgnoreCase("chrome")) {
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "\t");
			driver.switchTo().defaultContent();
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(inputData));
		}
	}
	
	
	
	
	
	
	

	public Response apiValidRequest(String URL1, String method, String header, String body, String APIName,
			int statusCode1) throws IOException, JSONException {
		String line = body;
		Response res = null;
		JSONObject jsonObject = null;
		
		
		
		try {
			String URL = URL1;
			String values[] = header.split(":");
			
			String credentials = Credentials.basic(values[0],values[1]);
			
			String[] value = body.split(",");
			//System.out.println(value[0] + value[1]);
			String[] from = value[0].split(":");
			//System.out.println(from[0]);
			String[] callerId = value[1].split(":");
			String[] to = value[2].split(":");
			
			/*
			Map<String, Object> params = (Map<String, Object>) new LinkedHashMap<>().replace("=",":");
			
			params.put(from[0], from[1]);
			params.put(callerId[0], callerId[1]);
			params.put(to[0], to[1]);
			System.out.println(params);
			*/
			
			if (method.equalsIgnoreCase("POST")) {
				res = RestAssured.given().queryParams("From",from[1],"CallerId",callerId[1],"To",to[1]).with().contentType("application/json")
						.then().when().post(URL);
			} else if (method.equalsIgnoreCase("GET")) {
				res = RestAssured.given().contentType("application/json").when().get(URL).then().extract().response();
			}
							
			if(res.statusCode()==statusCode1)
			{
				add("Requested API " + APIName + " and got  " + res.statusCode() + " response code", LogAs.PASSED, true,
						res.print().toString(), (res.getTime()));	
				
			}
			else
			{
				add1("Unsuccessfull API hit:" + APIName + " and got  " + res.statusCode() + " response code"  , LogAs.FAILED, true, res.print().toString());
				Assert.fail();
			}

		} catch (Exception e) {
			add1("Unable to hit the API " + APIName  , LogAs.FAILED, true, "");
			Assert.fail();
		}
		return res;
	
	}
	
	
	
	public Response status_reuse(String URL1, String method, String header,String body, String APIName,
			int statusCode1) throws IOException, JSONException {
		Response res = null;
		JSONObject jsonObject = null;
		
		
		
		try {
			String URL = URL1;
			String values[] = header.split(":");
			
			String credentials = Credentials.basic(values[0],values[1]);
			String[] value = body.split(",");
			String[] from = value[0].split(":");
			String[] callerId = value[1].split(":");
			String[] to = value[2].split(":");
			String[] callback = value[3].split(":");
			String[] eventtype = value[4].split(":");
			System.out.println(value[4]);
			System.out.println(eventtype[1]);
			String[] contenttype = value[5].split(":");
			System.out.println(contenttype[1]);
			String replaceEvent = eventtype[1].replaceAll("^[\"']+|[\"']+$", "");
		
			
		
			if (method.equalsIgnoreCase("POST")) {
				res = RestAssured.given().queryParams("From",from[1],"CallerId",callerId[1],"To",to[1],"StatusCallback","http:" + callback[2],"StatusCallbackEvents[0]",replaceEvent,"StatusCallbackContentType","application/json").with().contentType("application/json")
						.then().when().post(URL);
			} else if (method.equalsIgnoreCase("GET")) {
				res = RestAssured.given().contentType("application/json").when().get(URL).then().extract().response();
			}
							
			if(res.statusCode()==statusCode1)
			{
				add("Requested API " + APIName + " and got  " + res.statusCode() + " response code", LogAs.PASSED, true,
						res.print().toString(), (res.getTime()));	
				
				
			}
			else
			{
				add1("Unsuccessfull API hit:" + APIName + " and got  " + res.statusCode() + " response code"  , LogAs.FAILED, true, res.print().toString());
				Assert.fail();
			}

		} catch (Exception e) {
			add1("Unable to hit the API " + APIName  , LogAs.FAILED, true, "");
			Assert.fail();
		}
		return res;
	}

	
	
	
	
	
	
	
	
	public Response apiValidget(String URL1, String method, String header, String body, String APIName) throws IOException, JSONException {
		String line = body;
		Response res1 = null;
		JSONObject jsonObject1 = null;
		
		
		
try {
			String URL = URL1;
			String values[] = header.split(":");
			
			String credentials = Credentials.basic(values[0],values[1]);
			
			String[] value = body.split(",");
			//System.out.println(value[0] + value[1]);
			String[] from = value[0].split(":");
			//System.out.println(from[0]);
			String[] callerId = value[1].split(":");
			String[] to = value[2].split(":");
			
			/*
			Map<String, Object> params = (Map<String, Object>) new LinkedHashMap<>().replace("=",":");
			
			params.put(from[0], from[1]);
			params.put(callerId[0], callerId[1]);
			params.put(to[0], to[1]);
			System.out.println(params);
			*/
			
			 if (method.equalsIgnoreCase("GET")) {
				 
				 
				 
				res1 = RestAssured.given().contentType("application/json").when().then().get(URL);
				 
			//	 res1 = RestAssured.given().contentType("application/json").then().get(URL);
			}
			 
			 
							
			
				add("Requested API " + APIName + " and got response code", LogAs.PASSED, true,
					res1.print().toString(), (res1.getTime()));	
				

		} catch (Exception e) {
			add1("Unable to hit the API " + APIName  , LogAs.FAILED, true, "");
			Assert.fail();
		}
		
		
	
		
		return res1;
	}
	

	
	
	
	
	
	
	
	
	
	
	public Response apiValidRequest2(String URL1, String method, String header, String body, String APIName,String statusCode1) throws IOException, JSONException {
		String line = null;
		Response res = null;
		try {
			String URL = URL1;
			//String values[] = header.split(":");
			if (method.equalsIgnoreCase("POST")) {
				
				res = RestAssured.given().header("Authorization",header).body(line).with().contentType("application/json")
						.then().expect().when().post(URL);
			} else if (method.equalsIgnoreCase("GET")) {
				res = RestAssured.given().header("Authorization",header).with().contentType("application/json").then()
						.expect().when().get(URL);
			}
			add("Requested API " + APIName + " and got  " + res.statusCode() + " response code", LogAs.PASSED, true,
					res.print().toString(), (res.getTime()));

		} catch (Exception e) {

			add1("Requested API " + APIName + " and got the " + res.statusCode() + " response code", LogAs.FAILED, true, "");
			Assert.fail();
		}
		return res;
	}
	
	
	public  Response apiValidRequest3( String url,String method,String header,String body,String APIName,int statuscode1)
			throws IOException, JSONException {
		String body1=body;
		//int statuscode= statuscode1;
		
		
		Response res = null;

		try {
		String URL = url;

		if(method.equalsIgnoreCase("POST"))
		{
			res = RestAssured.given().headers("Authorization",header).body(body1).with().contentType("application/json").then().expect()
					.when().post(URL);
			
			
			
		}
		else if (method.equalsIgnoreCase("GET"))
		{
			res = RestAssured.given().headers("Authorization",header).with().contentType("application/json").then().expect()
					.when().get(URL);	
		}add("Requested API " + APIName + " and got  " + res.statusCode() + " as response code", LogAs.PASSED, true,res.print().toString(), (res.getTime()));

	} catch (Exception e) {

		add1("Requested API " + APIName + " and got the " + res.statusCode() + " as response code", LogAs.FAILED, false, "");
		Assert.fail();
	}
		

		JSONObject  jsonObject = null;
		jsonObject = new JSONObject(res.asString());
		System.out.println("---output----"+jsonObject.toString()+"---output---");
		return res;
	}
	
	
	public static String apigetRequest( String url,String method)
			throws IOException, JSONException {
		
		
		Response res1 = null;


		String URL = url;

		
		if (method.equalsIgnoreCase("GET"))
		{
			res1 = RestAssured.given().with().contentType("application/json").then().expect()
					.when().get(URL);	
		}
		else {
			
			
		}
		//ATUReports.add("Response is retrieved successfully", res.asString(), false);
		//add("Requested API "+APIName+" and got  "+res.statusCode()+"response", LogAs.PASSED, true, res.print().toString(),(res.getTime()));



		//add1("Requested API "+APIName+" and got the "+res.statusCode()+"response", LogAs.FAILED, true, "");

		JSONObject  jsonObject = null;
		jsonObject = new JSONObject(res1.asString());
		System.out.println("---output----"+jsonObject.toString()+"---output---");
		return res1.asString();
	}
	
	
	public static String apiPutBodyRequest(String url,String method,String header,String body) throws JSONException {
	    String body1=body;
		String URL=url;
		
		Response res = null;

		if(method.equalsIgnoreCase("PUT"))
		{
			res = RestAssured.given().headers("Authorization",header).body(body1).with().contentType("application/json").then().expect()
					.when().put(URL);
		}


		JSONObject  jsonObject = null;
		jsonObject = new JSONObject(res.asString());
		System.out.println("---output----"+jsonObject.toString()+"---output---");
		return res.asString();


	}

	public static String apiputrequest(String url,String method,String header) throws JSONException {
		
		String URL=url;
		
		Response res = null;

		if(method.equalsIgnoreCase("PUT"))
		{
			res = RestAssured.given().headers("Authorization",header).with().contentType("application/json").then().expect()
					.when().put(URL);
		}


		JSONObject  jsonObject = null;
		jsonObject = new JSONObject(res.asString());
		System.out.println("---output----"+jsonObject.toString()+"---output---");
		return res.asString();


	}
	public static int GenerateRandomNumber (){
	       
	       
        System.out.println("Random Numbers:");
        Random rand = new Random();
        int num = rand.nextInt(900000) + 100000;
        System.out.println("***************");
        
        System.out.println(num);
        
        return num;
     }
	

}
