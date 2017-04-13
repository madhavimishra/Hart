package utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestException;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class CommonUtils {
	public static final Logger logger=Logger.getLogger(CommonUtils.class);
	static {
		PropertyConfigurator.configure("resources/log4j.properties");
	}
	public WebDriverWait wait;
	public Actions actions;
	public Select select;	
	public static WebDriver driver;
	
	public void setDriver(String browserType, String appURL) {
		switch (browserType) {
		case "chrome":
			driver = initChromeDriver(appURL);
			break;
		case "firefox":
			driver = initFirefoxDriver(appURL);
			break;
		default:
			logger.info("browser : " + browserType
					+ " is invalid, Launching Firefox as browser of choice..");
			driver = initFirefoxDriver(appURL);
		}
	}
	
	public WebDriver initChromeDriver(String appURL) {
		logger.info("Launching Chrome browser..");
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		logger.info("Navigating to page: " + appURL);
		navigateToURL(appURL);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver initFirefoxDriver(String appURL) {
		logger.info("Launching Firefox browser..");
		System.setProperty("webdriver.gecko.driver","lib/geckodriver");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		logger.info("Navigating to page: " + appURL);
		navigateToURL(appURL);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
	}
	
	public void quitDriver () {
		driver.quit();
	}
	
    public void navigateToURL(String URL) {
    	logger.info("Navigating to: " + URL);
        try {
            driver.navigate().to(URL);
        } catch (Exception e) {
        	logger.info("URL did not load: " + URL);
        	captureScreenshot(URL);
            throw new TestException("URL did not load");
        }
    }
    
    public String getPageTitle() {
        try {
        	logger.info(String.format("The title of the page is: %s\n\n", driver.getTitle()));
            return driver.getTitle();
        } catch (Exception e) {
        	captureScreenshot("pageTitle");
            throw new TestException(String.format("Current page title is: %s", driver.getTitle()));
        }
    }
    
    public WebElement getElement(By selector) {
        try {
            return driver.findElement(selector);
        } catch (Exception e) {
        	logger.info(String.format("Element %s does not exist - proceeding", selector));
        	captureScreenshot(selector.toString());
        }
        return null;
    }
    
    public List<WebElement> getElements(By selector) {
        try {
        	List<WebElement> elements = driver.findElements(selector);
        	if (elements.size() > 0)
        		return elements;
        } catch (Exception e) {
        	logger.info(String.format("Element %s does not exist - proceeding", selector));
        	captureScreenshot(selector.toString());
        }
        return null;
    }
    
    public String getElementText(By selector){
        waitForElementToBeVisible(selector);
        try{
            return StringUtils.trim(driver.findElement(selector).getText());
        }catch (Exception e){
        	logger.info(String.format("Element %s does not exist - proceeding", selector));
        	captureScreenshot(selector.toString());
        }
        return null;
    }
    
    public void sendKeys(By selector, String value) {
        WebElement element = getElement(selector);
        clearField(element);
        try {
            element.sendKeys(value);
        } catch (Exception e) {
        	captureScreenshot(selector.toString());
            throw new TestException(String.format("Error in sending [%s] to the following element: [%s]", value, selector.toString()));
        }
    }
    
    public void clearField(WebElement element) {
        try {
            element.clear();
        } catch (Exception e) {
        	logger.info(String.format("The following element could not be cleared: [%s]", element.getText()));
        	captureScreenshot(element.toString());
        }
    }
    
    public void click(By selector) {
        WebElement element = getElement(selector);
        waitForElementToBeClickable(selector);
        try {
            element.click();
        } catch (Exception e) {
        	captureScreenshot(selector.toString());
            throw new TestException(String.format("The following element is not clickable: [%s]", selector));
        }
    }

    public void waitForElementToBeClickable(By selector ) {
        try {
            wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(selector));
        } catch (Exception e) {
        	captureScreenshot(selector.toString());
            throw new TestException("The following element is not clickable: " + selector);
        }
    }
    
    public void waitForElementToBeVisible(By selector) {
        try {
            wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        } catch (Exception e) {
        	captureScreenshot(selector.toString());
            throw new NoSuchElementException(String.format("The following element was not visible within [%s] seconds: %s ", "10".toString(), selector));
        }
    }
    
    public void waitForPageToLoad() {
    	driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }
    
    public void captureScreenshot(String screenshotName) {
    	try {
    		TakesScreenshot ts=(TakesScreenshot)driver;
    		File source=ts.getScreenshotAs(OutputType.FILE);
    		FileUtils.copyFile(source, new File("./Screenshots/"+screenshotName+".png"));
    		logger.info("Screenshot taken");
    	} catch (Exception e) {
    		logger.info("Exception while taking screenshot " + e.getMessage());
    	}
    }

}
