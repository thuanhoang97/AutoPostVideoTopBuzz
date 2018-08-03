package topbuzz.model;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import topbuzz.views.ErrorDialog;

public class TopBuzzAuto {
	private WebDriver driver;
	
	public TopBuzzAuto(String homeUrl) {
		HtmlElements.load();
		driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver.get(homeUrl);
	}
	
	public boolean login(String account, String password) {
		try {
			driver.findElement(By.className(HtmlElements.signinMode)).click();
			driver.findElement(By.className(HtmlElements.btnSigninByEmail)).click();
			WebElement elAccount = driver.findElement(By.className(HtmlElements.account));
			WebElement elPassword = driver.findElement(By.className(HtmlElements.password));
			elAccount.sendKeys(account);
			elPassword.sendKeys(password);
			
			driver.findElement(By.className(HtmlElements.btnSignin)).click();
			if(isLoginSuccess()==false) {
				new ErrorDialog("Login Failed", "Account or password is not correct!");
				driver.close();
				return false;
			}
		}catch(NoSuchElementException ex) {
			new ErrorDialog("NoSuchElementError", "Website has changed! Please check again!");
			driver.close();
			return false;
		} 
		
		return true;
	}
	
	private boolean isLoginSuccess() {
		try {
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			WebDriverWait  wait = new WebDriverWait(driver, 10);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(HtmlElements.errorLogin)));
			driver.findElement(By.className(HtmlElements.errorLogin));	
		}catch(NoSuchElementException ex) {
			return true;
		} 
		
		return false;
	}
	
	public void post() {
		try {
			//go to video studio
			WebDriverWait  wait = new WebDriverWait(driver, 60);
			By videoPostUrl = By.xpath("//a[@href='"+ HtmlElements.videoPostUrl +"']");
			By videotitle = By.cssSelector(HtmlElements.titleVideo);
			wait.until(ExpectedConditions.visibilityOfElementLocated(videoPostUrl));
			driver.findElement(videoPostUrl).click();
			
			//upload file
			wait = new WebDriverWait(driver, 2*60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(HtmlElements.btnFileUpload)));
			driver.findElement(By.className(HtmlElements.btnFileUpload))
			.sendKeys("C:\\Users\\Rem\\Desktop\\output\\How Machines Learn.MKV");
//			driver.findElement(By.className(HtmlElements.btnFileUpload)).click();
//			chosseFile("C:\\Users\\Rem\\Desktop\\output\\How Machines Learn.MKV");
			wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(videotitle));
			driver.findElement(videotitle).sendKeys("How Machines Learn");
			
			//publish file
			wait = new WebDriverWait(driver, 60*60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tb-link")));
			driver.findElement(By.className(HtmlElements.btnPublish)).click();
			
			//turn off the notification
		
			
//			wait = new WebDriverWait(driver, 10);
//			driver.get("https://www.topbuzz.com/");
		}catch(NoSuchElementException ex) {
			System.err.println("error here");
			new ErrorDialog("NoSuchElementError", "Website has changed! Please check again!");
			driver.close();
		}
	}
	
	private void chosseFile(String absolutePath) {
		StringSelection ss = new StringSelection(absolutePath);
//		//Store the current window handle
//		String currentWindowHandle = driver.getWindowHandle();
//
//		//run your javascript and alert code
//		((JavascriptExecutor)driver).executeScript("alert('Test')"); 
//		driver.switchTo().alert().accept();
//
//		//Switch back to to the window using the handle saved earlier
//		driver.switchTo().window(currentWindowHandle);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
