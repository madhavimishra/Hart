package com.hart.pageobjects;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class HomePage extends CommonUtils {
	
	public static final Logger logger=Logger.getLogger(LoginPage.class);
	static {
		PropertyConfigurator.configure("resources/log4j.properties");
	}
	private String homePageTitle = "Hart - Login";
	private By healthLink = By.xpath("//*[@id='col-navigation']/li[1]/a");
	private By allergiesLink = By.xpath("//*[@id='col-navigation']/li[2]/ul/li[1]/a");
	private By allergiesTitle = By.cssSelector(".section-header.flexy.vert-center.space-out");
	private String expectedAllergiesTitleText = "Allergies";
	private By allergiesList = By.cssSelector(".item-title");
	private String allergyNameText = "Soy";
	
	public boolean verifyHomePageTitle() {
		logger.info("Verifying Home Page Title");
		waitForPageToLoad();
		String title = getPageTitle();
		logger.info("Home Page Title: " + title);
		return title.contains(homePageTitle);
	}
	
	public boolean verifyAllergiesDisplayed() {
		logger.info("Expanding Health Section");
		click(healthLink);
		logger.info("Verifying Allergies list is displayed");
		click(allergiesLink);
		String allergiesTitleText = getElementText(allergiesTitle);
		return allergiesTitleText.contains(expectedAllergiesTitleText);
	}
	
	public boolean verifyAllergiesList() {
		logger.info("Verifying Allergies List has atleast 8 elements");
		List<WebElement> allergiesNumber = getElements(allergiesList);
		if (allergiesNumber.size() >= 8) {
			return true;
		} else {
			logger.info("Allergies List has " +  allergiesNumber.size() + " elements");
			captureScreenshot("screenshot");
			return false;
		}
	}
	
	public boolean verifySoyAllergyDisplayed() {
		logger.info("Verifying Soy Allergy is Displayed");
		List<WebElement> allergiesNumber = getElements(allergiesList);
		for (int n = 0; n < allergiesNumber.size(); n++) {
			WebElement allergyName = allergiesNumber.get(n);
			if (allergyName.getText().equalsIgnoreCase(allergyNameText)) {
				return true;
			}
		}
		return false;
	}

}
