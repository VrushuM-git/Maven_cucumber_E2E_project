package com.ExtentReport_practice;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class BasicExtentReport {
	public WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	//helps to generate the logs in test report.
	public ExtentTest test;

	
	@BeforeTest
	public void setExtent(){
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"/test-output/myReport.html");
		htmlReporter.config().setDocumentTitle("Automation Report"); //title of report
		htmlReporter.config().setReportName("Functional report");  //name of report
		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		//passing general info
		extent.setSystemInfo("Host name", "localhost");
		extent.setSystemInfo("Enviroment", "QA");
		extent.setSystemInfo("user","Tester Name");
	}
	@AfterTest
	public void endReport(){
		extent.flush();
	}
	@BeforeMethod 
	public void setup(){
		System.setProperty("webdriver.chrome.driver","c:\\chromedriver.exe" );
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://demo.nopcommerce.com/");
	}
	@Test
	public void noCommerceTitleTest(){
		test = extent.createTest("noCommerceTitleTest");
		String title = driver.getTitle();
		System.out.println(title);
		AssertJUnit.assertEquals(title, "ecommere demo store");
	}
	@Test
	public void noCommerceLogoTest(){
		test = extent.createTest("noCommerceLogoTest");
		boolean b = driver.findElement(By.xpath("//img@alt='nopCommerce demo store']")).isDisplayed();
		AssertJUnit.assertTrue(b);
	}
	@Test
	public void noCommerceLoginTest(){
		test = extent.createTest("noCommerceLoginTest");

		test.createNode("login wih valid input");
		AssertJUnit.assertTrue(true);

		test.createNode("login wih in-valid input");
		AssertJUnit.assertTrue(true);

	}
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException{
		if(result.getStatus() == ITestResult.FAILURE){
			test.log(Status.FAIL, "TEST CASE FAILED IS" + result.getName());
			test.log(Status.FAIL, "TEST CASE FAILED IS" + result.getThrowable());
			String screenshootPath =BasicExtentReport.getScreenshot(driver,result.getName());
			test.addScreenCaptureFromPath(screenshootPath);

		}
		else if (result.getStatus() == ITestResult.SKIP){
			test.log(Status.SKIP,"test case skipped is" + result.getName());

		}
		else if (result.getStatus() == ITestResult.SUCCESS){
			test.log(Status.PASS,"test case skipped is" + result.getName());
		}
		driver.quit();

	}
	public static String getScreenshot(WebDriver driver,String screenshotName) throws IOException{
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName +dateName +".png";
		File finalDestination = new File(destination);
		Object fileUtils;
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
}
