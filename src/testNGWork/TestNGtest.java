package testNGWork;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testNGWork.Util;

public class TestNGtest {

	public static WebDriver driver;  // Selenium control driver
	
	@DataProvider(name="authentication")   
	public Object[][] credentials() throws Exception {
		return new Object[][] { {"mngr144444","remyvEj"}, {"invalid","remyvEj"}, {"mngr144444", "invalid"}, {"invalid","invalid"}};
	}
	@BeforeMethod
	public static void launchFirefox() throws Exception{ 
		System.setProperty("webdriver.gecko.driver",Util.GECKODRIVERLOC); 
		driver = new FirefoxDriver();		
		driver.manage().timeouts().implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);			
		driver.get(Util.BASEURL);
	}
	
	//Calling DataProvider
	@Test(dataProvider="authentication")
	public void test(String sUsername, String sPassword) {
		try {
			
			 driver.findElement(By.name("uid")).sendKeys(sUsername);
			 driver.findElement(By.name("password")).sendKeys(sPassword);
			 driver.findElement(By.name("btnLogin")).click();
			//Wait for the results to appear
			 Thread.sleep(2000);
			 takeScreenshot();
			
			 //Verify the Manager ID
		    WebElement managerID = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/table/tbody/tr[3]/td"));
		    System.out.println("ManagerID = "+managerID.getText());
		    String verifyManagerID = managerID.toString();		    
			driver.quit();
			 
			 /* Determine Pass Fail Status of the Script
		         * If login credentials are correct,  Alert(Pop up) is NOT present. 
		         * An Exception is thrown and code in catch block is executed.  Manager ID will be verified	
		         * If login credentials are invalid, Alert is present. Code in try block is executed 	    
		        
		         */
			    try{ 
			    
			       	Alert alt = driver.switchTo().alert();
					String actualBoxMsg = alt.getText(); // get content of the Alter Message
					alt.accept();
					 // Compare Error Text with Expected Error Value					
					assertEquals(actualBoxMsg,Util.EXPECT_ERROR);
					
				}    
			    catch (NoAlertPresentException Ex){ 
			    	// On Successful login verify manager ID
			    	assertTrue(verifyManagerID.contains(sUsername));
		        } 			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	/**
	 * Complete the testing
	 * 
	 * @throws Exception
	 */
	//@AfterMethod
	//public void tearDown() throws Exception {
		//driver.quit();
	//}
	
	
	public static void takeScreenshot() throws Exception {
		String timeStamp;
		File screenShotName;
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//The below method will save the screen shot in d drive with name "screenshot.png"
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()); 
		screenShotName = new File("C:\\Users\\Nav\\eclipse-workspace\\FifthAssignment\\src\\ScreenShots\\"+timeStamp+".png");
		FileUtils.copyFile(scrFile, screenShotName);
		 
		String filePath = screenShotName.toString();
		String path = "<img src=\"file://" + filePath + "\" alt=\"\"/>";
		Reporter.log(path);
		 
		}
	
}