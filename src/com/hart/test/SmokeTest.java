package com.hart.test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.google.common.base.Verify;
import com.hart.pageobjects.LoginPage;
import com.hart.pageobjects.HomePage;
import com.hart.test.TestBaseSetup;

public class SmokeTest extends TestBaseSetup {
	
	public static final Logger logger=Logger.getLogger(TestBaseSetup.class);
	static {
		PropertyConfigurator.configure("resources/log4j.properties");
	}

	@Test (groups = "smoketest")
	public void verifyLoginPage() {
		logger.info("Running Hart Login Page test...");
		LoginPage loginPage = new LoginPage();
		Assert.assertTrue(loginPage.verifyLoginPageTitle(), "Hart Login page title not as expected");
	}
	
	@Parameters({ "userName", "userPassword" })
	@Test (groups = "smoketest", dependsOnMethods = "verifyLoginPage")
	public void verifyUserLogin(String userName, String userPassword) {
		logger.info("Running Hart User Login test...");
		LoginPage loginPage = new LoginPage();
		loginPage.verifyLogin(userName, userPassword);
	}
	@Test (groups = "smoketest", dependsOnMethods = "verifyLoginPage")
	public void verifyHomePage() {
		logger.info("Running Hart Home Page test...");
		HomePage homePage = new HomePage();
		Assert.assertTrue(homePage.verifyHomePageTitle(), "Home page title not as expected");
	}
	
	@Test (groups = "smoketest", dependsOnMethods = "verifyHomePage")
	public void verifyAllergiesList() {
		logger.info("Running Allergies test...");
		HomePage homePage = new HomePage();
		Assert.assertTrue(homePage.verifyAllergiesDisplayed(),"Allergies section is not displayed");
		Verify.verify(homePage.verifyAllergiesList(),"Allergies list size less than 8");
		Verify.verify(homePage.verifySoyAllergyDisplayed(), "Soy allergy is not displayed");
	}

}
