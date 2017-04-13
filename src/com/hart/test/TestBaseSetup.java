package com.hart.test;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utils.CommonUtils;

public class TestBaseSetup extends CommonUtils {
	public static final Logger logger=Logger.getLogger(TestBaseSetup.class);
	static {
		PropertyConfigurator.configure("resources/log4j.properties");
	}

	@Parameters({ "browserType", "appURL" })
	@BeforeClass(alwaysRun = true)
	public void initializeTestBaseSetup(String browserType, String appURL) {
		try {
			logger.info("Setting browser and appURL");
			setDriver(browserType, appURL);
		} catch (Exception e) {
			logger.info("Error setting browser/appurl: " + e.getStackTrace());
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() {
		logger.info("shutting down driver");
		quitDriver();
	}

}
