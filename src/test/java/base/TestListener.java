package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {
	
	TestBase test = new TestBase();
	

	public void onStart(ITestContext context) {
		System.out.println("*** Test Suite " + context.getName() + " started ***");
		System.out.println("webdrive att from testlistener is:"+ context.getAttribute("webDriver"));
	}

	public void onFinish(ITestContext context) {
		System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();
	}

	public void onTestStart(ITestResult result) {
		System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
		ExtentTestManager.startTest(result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
		ExtentTestManager.getTest().log(Status.PASS, "Test passed");
	}
	
	public void onTestSkipped(ITestResult result) {
		System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
	}

	public void onTestFailure(ITestResult result) {
		
		ExtentTestManager.getTest().log(Status.FAIL, result.getMethod().getMethodName() + " failed!");
		System.out.println(result.getMethod().getMethodName() + " failed!");
		WebDriver driver = test.getDriver();

		Date currentdate = new Date();
		String userdir = System.getProperty("user.dir");

		String testClassName = result.getInstanceName().trim();
		String testMethodName = result.getName().toString().trim();
		
		String screenshotfilename = testClassName + "--"+ testMethodName + "--" + currentdate.toString().replace(" ", "-").replace(":", "-");
		File screenshotfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		String screenshotfilepath = userdir + "TestReport/screenshots/" + screenshotfilename + ".png";
		System.out.println("screenshotfilepath is" + screenshotfilepath);
		
		ExtentTestManager.getTest().addScreenCaptureFromPath(screenshotfilepath,screenshotfilepath);
		
		
		try {
			FileUtils.copyFile(screenshotfile, new File(screenshotfilepath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		// attach screenshots to report


	}

}
