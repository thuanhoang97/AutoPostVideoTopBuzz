package topbuzz.model;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import topbuzz.model.videosource.VideoFile;
import topbuzz.model.videosource.VideoSource;
import topbuzz.views.ErrorDialog;

public class TopBuzzAuto extends Thread{
	public static final String HOME_URL = "https://www.topbuzz.com/";
	private WebDriver driver;
	private String account;
	private String password;
	private VideoSource videoSource;
	private JTextArea viewLog;
	
	public TopBuzzAuto(String account, String password) {
		driver = new ChromeDriver();
		this.account = account;
		this.password = password;
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver.get(HOME_URL);
	}
	
	public void setVideoSource(VideoSource source) {
		videoSource = source;
	}
	
	public void setViewLog(JTextArea viewLog) {
		this.viewLog = viewLog;
	}
	
	public void run() {
		try {
			boolean isLoginSuccess = login();
			if(isLoginSuccess) {
				int count=0;
				for(VideoFile video : videoSource.getFiles()) {
					log("Posting: " + video.getTitle());
					post(video);
					new  File(video.getAbsolutePath()).delete();
					log("Doned " + video.getTitle());
					if(count%10==0)
						deleteRejectedVideo(100);
					count++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.close();
	}
	
	private void log(String log) {
		 viewLog.append(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " :");
		 viewLog.append(log + "\n");
	}
	
	private boolean login() {
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
			ex.printStackTrace();
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
	
	public void post(VideoFile video) throws Exception{
		try {
			driver.get(HOME_URL);
			//go to video studio
			WebDriverWait  wait = new WebDriverWait(driver, 60);
			By videoPostUrl = By.xpath("//a[@href='"+ HtmlElements.videoPostUrl +"']");
			By videotitle = By.cssSelector(HtmlElements.titleVideo);
			wait.until(ExpectedConditions.visibilityOfElementLocated(videoPostUrl));
			driver.findElement(videoPostUrl).click();
			
			//upload file
			((JavascriptExecutor) driver)
				.executeScript("document.querySelector('input[type=\"file\"]').style.display = 'inline';");
			driver.findElement(By.cssSelector("input[type=\"file\"]")).sendKeys(video.getAbsolutePath());
			wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(videotitle));
			driver.findElement(videotitle).sendKeys(video.getTitle().toLowerCase());
			
			//publish file
			wait = new WebDriverWait(driver, 60*60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(HtmlElements.btnReUpload)));
			driver.findElement(By.className(HtmlElements.btnPublish)).click();
			String curUrl = driver.getCurrentUrl();
			//wait for publishing
			while(driver.getCurrentUrl().equals(curUrl)) {
				Thread.currentThread().sleep(1000);
			}
			
			
		}catch(NoSuchElementException ex) {
			System.err.println("error here");
			new ErrorDialog("NoSuchElementError", "Website has changed! Please check again!");
			driver.close();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteRejectedVideo(int number) throws Exception{
		log("Checking rejected video...");
		driver.get("https://www.topbuzz.com/profile_v2");
		By statusClass = By.className(HtmlElements.videoStatus);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(statusClass));
		List<WebElement> statuses = new ArrayList<>();
		try {
			int size = 0;
			do{
				((JavascriptExecutor) driver)
			     .executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(3000);
				size = statuses.size();
				statuses = driver.findElements(statusClass);
				System.out.println("size: " + statuses.size());
				if(size==statuses.size()) break;
			}while(statuses.size() < number);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		List<Integer> indexs = new ArrayList<>();
		for(int i=0; i < statuses.size();i++) {
			WebElement status = statuses.get(i);
//			System.out.println(status.getText());
			if(status.getText().equals("Rejected")) {
				indexs.add(i);
				String title = driver.findElements(By.className("article-title")).get(i).getText();
				log("Deleting rejected video :" + title);
//				viewLog.append("\nDelete video rejected: " + title);
//				deleteVideo(i);
				
			}
		}
		deleteVideos(indexs);
		System.out.println("Number video rejected: " + indexs.size());
	}
	
	private void deleteVideos(List<Integer> indexs) {
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		for(int i=0; i< indexs.size();i++) {
//			WebElement deleteBtn  = driver.findElements(By.cssSelector("div.action")).get(i);
			jsDriver.executeScript("document.querySelectorAll('div.action')["+(indexs.get(i)-i)+ "].click();");
			jsDriver.executeScript("document.querySelector('.tb-btn-primary').click();");
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	private void chosseFile(String absolutePath) {
////		StringSelection ss = new StringSelection(absolutePath);
//////		//Store the current window handle
//////		String currentWindowHandle = driver.getWindowHandle();
//////
//////		//run your javascript and alert code
//////		((JavascriptExecutor)driver).executeScript("alert('Test')"); 
//////		driver.switchTo().alert().accept();
//////
//////		//Switch back to to the window using the handle saved earlier
//////		driver.switchTo().window(currentWindowHandle);
////		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
////		Robot robot;
////		try {
////			robot = new Robot();
////			robot.keyPress(KeyEvent.VK_CONTROL);
////			robot.keyPress(KeyEvent.VK_V);
////			robot.keyRelease(KeyEvent.VK_V);
////			robot.keyRelease(KeyEvent.VK_CONTROL);
////			robot.keyPress(KeyEvent.VK_ENTER);
////			robot.keyRelease(KeyEvent.VK_ENTER);
////		} catch (AWTException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		((JavascriptExecutor) driver)
//			.executeScript("document.querySelector('input[type=\"file\"]').style.display = 'inline'");
//		driver.findElement(By.cssSelector("input[type=\"file\"]")).sendKeys(absolutePath);
//		
//	}
	
	public void close() {
		driver.close();
	}
	
//	public void checkVideoStatus(int timePeding, JTextArea viewLog) {
//		driver.get("https://www.topbuzz.com/profile_v2");
//		driver.get("https://www.topbuzz.com/profile_v2");
//		Timer timer = new Timer();
//		WebDriverWait wait = new WebDriverWait(driver, 20);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("article-status-label")));
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				driver.get("https://www.topbuzz.com/profile_v2");
//				WebDriverWait wait = new WebDriverWait(driver, 20);
//				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("article-status-label")));
//				List<WebElement> lists = driver.findElements(By.className("article-status-label"));
//				for(int i=0; i < lists.size();i++) {
//					WebElement status = lists.get(i);
//					System.out.println(status.getText());
//					if(status.getText().equals("Rejected")) {
//						String title = driver.findElements(By.className("article-title")).get(i).getText();
//						viewLog.append("\nDelete video rejected: " + title);
//						deleteVideo(i);
//					}else if(status.getText().equals("Pending")) {
//						String time = driver.findElements(By.className("time")).get(i)
//								.getText().split(",")[1].split(" ")[2];
//						int hour = Integer.parseInt(time.split(":")[0]);
//						int minute = Integer.parseInt(time.split(":")[1]);
//						Calendar now = Calendar.getInstance();
//						int hourNow = now.get(Calendar.HOUR_OF_DAY);
//						int minuteNow = now.get(Calendar.MINUTE);
//						int dur = (hourNow*60 + minuteNow) - (hour*60 + minute);
//						if(dur >= timePeding) {
//							String title = driver.findElements(By.className("article-title")).get(i).getText();
//							viewLog.append("\nDelete video pending out time: " + title);
//							deleteVideo(i);
//						}
//					}
//					
//				}
//				System.out.println("====================================");
////				for(WebElement status : driver.findElements(By.className("article-status-label"))) {
////					System.out.println(status.getText());
////					if(status.getText().equals("Rejected")) {
////						
////					}
////				}
//				
//			}
//			
//		}, 0, 60000);
//	}
	
	
}
