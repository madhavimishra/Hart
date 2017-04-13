package com.hart.pageobjects;

import org.openqa.selenium.By;
import utils.CommonUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoginPage extends CommonUtils {
	
	public static final Logger logger=Logger.getLogger(LoginPage.class);
	static {
		PropertyConfigurator.configure("resources/log4j.properties");
	}
	private By email = By.name("email");
	private By password = By.name("password");
	private By loginButton = By.cssSelector(".action.form-full.large");
	private String loginPageTitle = "Hart - Login";
	
	public boolean verifyLoginPageTitle() {
		logger.info("Verifying Login Page Title");
		String title = getPageTitle();
		logger.info("Login Page Title: " + title);
		return title.contains(loginPageTitle);
	}
	
	public HomePage verifyLogin(String userName, String userPassword) {
		logger.info("Verifying User Login");
		sendKeys(email, userName);
		sendKeys(password, userPassword);
		logger.info("User :: Password = " + userName + " :: " + userPassword);
		click(loginButton);
		return new HomePage();
	}

}
