package com.qsgsoft.EMTrack.Requirement;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qsgsoft.EMTrack.Shared.DashBoardPage;
import com.qsgsoft.EMTrack.Shared.FiltersPage;
import com.qsgsoft.EMTrack.Shared.LoginPage;
import com.qsgsoft.EMTrack.Support.Date_Time_settings;
import com.qsgsoft.EMTrack.Support.ElementId_properties;
import com.qsgsoft.EMTrack.Support.OfficeCommonFunctions;
import com.qsgsoft.EMTrack.Support.Paths_Properties;
import com.qsgsoft.EMTrack.Support.ReadData;
import com.qsgsoft.EMTrack.Support.ReadEnvironment;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/****************************************************************************************
 * Description       :This class contains test cases from 'HDShareDashboard' requirement '
 * Requirement       :Share Dashboard '
 * Requirement Group :Dashboard Managment '
 * Product           :EMTrack 3.0 ' 
 * Date              :10/30/2013 ' 
 * Author            :Rahul 
 ****************************************************************************************/
public class HDShareDashboardTest{

	// Log4j object to write log entries to the Log files
	static Logger log4j = Logger.getLogger("com.qsgsoft.EMTrack.features.HDShareDashboard");
	
	static{
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http","warn");
		PropertyConfigurator.configure("D:/Selenium/com.qsgsoft.EMTrack3.0/src/test/resources/PropertiesFiles/log4j.properties");
	}

	// Objects to access the common functions
	OfficeCommonFunctions objOFC;
	ReadData objrdExcel;

	/*
	 * Global variables to store the test case details – TestCaseID, Test
	 * Objective,Result, Reason for failure
	 */
	String gstrTCID, gstrTO, gstrResult, gstrReason;

	// Selenium Object
	Selenium selenium;

	// Object for date time settings
	Date_Time_settings dts = new Date_Time_settings();

	public Properties propElementDetails; // Property variable for ElementID  file								
	public Properties propEnvDetails;// Property variable for Environment data
	public Properties propPathDetails; // Property variable for Path information
	public Properties propAutoITDetails;// Property variable for AutoIT file details								
	public static Properties browserProps = new Properties();
	public static String gstrBrowserName;// Variable to store browser name
	public static String gstrTimetaken, gstrdate, gstrtime, gstrBuild;// Result  Variables																	
	@SuppressWarnings("unused")
	private String browser;
	double gdbTimeTaken; // Variable to store the time taken
	public static Date gdtStartDate;// Date variable
	@SuppressWarnings("unused")
	private String json;
	public static long sysDateTime;
	public static long gsysDateTime = 0;
	public static String gstrTimeOut = "";
	public static String sessionId_FF36, sessionId_FF20, sessionId_FF3,
			sessionId_FF35, sessionId_IE6, sessionId_IE7, sessionId_IE8,
			session_SF3, session_SF4, session_op9, session_op10, session_GC,
			StrSessionId = "", StrSessionId1, StrSessionId2;

	/****************************************************************************************************************
	 * This function is called the setup() function which is executed before every test.
	 * 
	 * The function will take care of creating a new selenium session for every test
	 * 
	 ****************************************************************************************************************/
	@Before
	public void setUp() throws Exception {

		Paths_Properties objPathsProps = new Paths_Properties();
		propAutoITDetails = objPathsProps.ReadAutoit_FilePath();

		propPathDetails = objPathsProps.Read_FilePath();

		// Create object to read environment properties file
		ReadEnvironment objReadEnvironment = new ReadEnvironment();
		propEnvDetails = objReadEnvironment.ReadEnvironment();
		
		// Retrieve browser information
		browser = propEnvDetails.getProperty("Browser");
		gstrBrowserName = propEnvDetails.getProperty("Browser").substring(1)
				+ " " + propEnvDetails.getProperty("BrowserVersion");
		
		// Retrieve the value of page load time limit
		gstrTimeOut = propEnvDetails.getProperty("TimeOut");
		
		// create an object to refer to Element ID properties file
		@SuppressWarnings("unused")
		ElementId_properties objelementProp = new ElementId_properties();
		
		// Create a new selenium session
		selenium = new DefaultSelenium(propEnvDetails.getProperty("Server"),
				4444, propEnvDetails.getProperty("Browser"),
				propEnvDetails.getProperty("urlEU"));
		
		// Start Selenium RC
		selenium.start();
		
		// Maximize the window
		selenium.windowMaximize();
		selenium.setTimeout(propEnvDetails.getProperty("TimeOut"));
		
		// Define object to call support functions to read excel, date etc
		gdtStartDate = new Date();
		objOFC = new OfficeCommonFunctions();
		objrdExcel = new ReadData();
	}

	/***************************************************************************************************************************
	 * * This function is called the teardown() function which is executed after every test.
	 * 
	 * The function will take care of stopping the selenium session for every  test and writing the execution result of the test.
	 * 
	 ***************************************************************************************************************************/
	@After
	public void tearDown() throws Exception {
		// kill browser
		selenium.close();
		selenium.stop();
		
		// determine log message
		if (gstrResult.toUpperCase().equals("PASS")) {
			log4j.info("-------------------Test Case Execution " + gstrTCID
					+ " has PASSED------------------");
		} else if (gstrResult.toUpperCase().equals("SKIP")) {
			log4j.info("-------------------Test Case Execution " + gstrTCID
					+ " was SKIPPED------------------");
		} else {
			log4j.info("-------------------Test Case Execution " + gstrTCID
					+ " has FAILED------------------");
		}

		// Retrieve the path of the Result file
		String FILE_PATH = "";
		Paths_Properties objAP = new Paths_Properties();
		Properties pathProps = objAP.Read_FilePath();
		FILE_PATH = pathProps.getProperty("Resultpath");
		
		// Retrieve the total execution time
		gdbTimeTaken = objOFC.TimeTaken(gdtStartDate);
		Date_Time_settings dts = new Date_Time_settings();
		
		// Get the current date
		gstrdate = dts.getCurrentDate(selenium, "yyyy-MM-dd");
		
		// Get the Build ID of the application
		gstrBuild = propEnvDetails.getProperty("Build");
		
		// Check if result should be written to Excel or Test Management Tool
		String blnresult = propEnvDetails.getProperty("writeresulttoexcel/DB");
		boolean blnwriteres = blnresult.equals("true");
		
		// Write Result of the test.
		objOFC.WriteResultdb_Excel(gstrTCID, gstrTO, gstrResult, gstrReason,
				gdbTimeTaken, FILE_PATH, blnwriteres, gstrTimetaken, gstrdate,
				sysDateTime, gstrBrowserName, gstrBuild, StrSessionId);
	}
	
	//start//testBQS127521//
	/*************************************************************************************
	'Description	:Verify user B cannot modify the dashboard which is shared from user A
	'Precondition	:
	'Arguments		:None
	'Returns		:None
	'Date			:10/30/2013
	'Author			:Rahul
	**************************************************************************************/
	@Test
	public void testBQS127521() throws Exception {
		String strFuncResult = "";

		LoginPage objLoginEMTrack = new LoginPage();
		FiltersPage objFiltersPage = new FiltersPage();
		DashBoardPage objDashBoardPage = new DashBoardPage();
		
		try {
			gstrTCID = "127521"; // Test Case Id
			gstrTO = " Verify user B cannot modify the dashboard which is shared from user A";// Test Objective
			gstrReason = "";
			gstrResult = "FAIL";
			
			gstrTimetaken = dts.timeNow("hh:mm:ss");
			String varUserName = objrdExcel.ReadData("Login", 2, 1);
			String varPassword = objrdExcel.ReadData("Login", 2, 2);
			String strFilterName = "Auto Filter " + System.currentTimeMillis();
			String strFilterDescription = "Testing save Filter";
			String strCriteria = "Tracking Status";
			String strDashboardName = "Auto Shared Dashboard "+ System.currentTimeMillis();
			String strDashBoardDescription = "Testing Share DashBoard";
			String strRole = "Regional Admin";
			String varUserName1 = objrdExcel.ReadData("Login", 3, 1);
			String varPassword1 = objrdExcel.ReadData("Login", 3, 2);

			log4j.info("~~~~~TEST CASE - " + gstrTCID+ " EXECUTION STARTS~~~~~");
			
			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginEMTrack.login(selenium, varUserName,
						varPassword);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.navToFiltersPage(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.selSearchCriteria(selenium,
						strCriteria);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.clkSaveAsBtn(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.saveFilter(selenium,
						strFilterName, strFilterDescription);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.verifyCreatedFilter(selenium,
						strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.navigateToDashboardPage(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.selectNewDashboardOption(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.selFilterInClientListGadget(
						selenium, strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.clkClientListApplyBtn(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}
			
			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.vfyFilterNameInClientListGadgetTitle(selenium, strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.selectSaveDashboardOption(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.shareDashBoard(selenium,
						strDashboardName, strDashBoardDescription, strRole);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.verifyCurrentDashBoardInput(
						selenium, strDashboardName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginEMTrack.logOut(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginEMTrack.login(selenium, varUserName1,
						varPassword1);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.verifySharedDashboard(
						selenium, strDashboardName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}
			
			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.vfyFilterNameInClientListGadgetTitle(selenium, strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}
			
			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.vfySharedDashboardCannotBeModified(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}
			
			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginEMTrack.logOut(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				gstrResult = "PASS";
			} catch (AssertionError Ae) {
				gstrResult = "FAIL";
				gstrReason = gstrReason + " " + strFuncResult;
			}

			log4j.info("~~~~~TEST CASE " + gstrTCID + " EXECUTION ENDS~~~~~");
			log4j.info("----------------------------------------------------------");

		} catch (Exception e) {
			gstrTCID = "BQS-127521";
			gstrTO = "Verify user B cannot modify the dashboard which is shared from user A";
			gstrResult = "FAIL";
			log4j.info(e);
			log4j.info("========== Test Case '" + gstrTCID
					+ "' has FAILED ==========");
			log4j.info("~~~~~TEST CASE " + gstrTCID + " EXECUTION ENDS~~~~~");
			log4j.info("----------------------------------------------------------");
			gstrReason = e.toString();
		}
	}
	// end//testBQS127521//
	
	//start//testBQS127520//
	/********************************************************************
	'Description   :Verify that user A can share dashboard with user B
	'Precondition  :
	'Arguments	   :None
	'Returns	   :None
	'Date		   :10/31/2013
	'Author		   :Rahul
	*********************************************************************/
	@Test
	public void testBQS127520() throws Exception {
		String strFuncResult = "";

		FiltersPage objFiltersPage = new FiltersPage();
		DashBoardPage objDashBoardPage = new DashBoardPage();
		LoginPage objLoginPage = new LoginPage();
		
		try {
			gstrTCID = "127520"; // Test Case Id
			gstrTO = " Verify that user A can share dashboard with user B";// Test Objective													
			gstrReason = "";
			gstrResult = "FAIL";
			log4j.info("~~~~~TEST CASE - " + gstrTCID
					+ " EXECUTION STARTS~~~~~");
			gstrTimetaken = dts.timeNow("hh:mm:ss");
			String varUserName = objrdExcel.ReadData("Login", 2, 1);
			String varPassword = objrdExcel.ReadData("Login", 2, 2);
			String strFilterName = "Auto Filter" + System.currentTimeMillis();;
			String strFilterDescription = "Testing save Filter";
			String strCriteria = "Tracking Status";
			String strDashboardName = "Auto Shared Dashboard "+ System.currentTimeMillis();;
			String strDashBoardDescription = "Testing Share DashBoard";
			String strRole = "Regional Admin";
			String varUserName1 = objrdExcel.ReadData("Login", 3, 1);
			String varPassword1 = objrdExcel.ReadData("Login", 3, 2);

			/*
			 * STEP : Action:Launch EMTrack, Add and Share the dashboard
			 * Expected Result:Verify the shared dashboard
			 */
			// 663762

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginPage.login(selenium, varUserName,
						varPassword);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.navToFiltersPage(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.selSearchCriteria(selenium,
						strCriteria);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.clkSaveAsBtn(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.saveFilter(selenium,
						strFilterName, strFilterDescription);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objFiltersPage.verifyCreatedFilter(selenium,
						strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.navigateToDashboardPage(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.selectNewDashboardOption(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.selFilterInClientListGadget(
						selenium, strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.clkClientListApplyBtn(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.vfyFilterNameInClientListGadgetTitle(selenium,
								strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.selectSaveDashboardOption(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.shareDashBoard(selenium,
						strDashboardName, strDashBoardDescription, strRole);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.verifyCurrentDashBoardInput(
						selenium, strDashboardName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginPage.logOut(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginPage.login(selenium, varUserName1,
						varPassword1);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage.verifySharedDashboard(
						selenium, strDashboardName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objDashBoardPage
						.vfyFilterNameInClientListGadgetTitle(selenium,
								strFilterName);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				strFuncResult = objLoginPage.logOut(selenium);
			} catch (AssertionError Ae) {
				gstrReason = strFuncResult;
			}

			try {
				assertEquals("", strFuncResult);
				gstrResult = "PASS";
			} catch (AssertionError Ae) {
				gstrResult = "FAIL";
				gstrReason = gstrReason + " " + strFuncResult;
			}

			log4j.info("~~~~~TEST CASE " + gstrTCID + " EXECUTION ENDS~~~~~");
			log4j.info("----------------------------------------------------------");

		} catch (Exception e) {
			gstrTCID = "BQS-127520";
			gstrTO = "Verify that user A can share dashboard with user B";
			gstrResult = "FAIL";
			log4j.info(e);
			log4j.info("========== Test Case '" + gstrTCID
					+ "' has FAILED ==========");
			log4j.info("~~~~~TEST CASE " + gstrTCID + " EXECUTION ENDS~~~~~");
			log4j.info("----------------------------------------------------------");
			gstrReason = e.toString();
		}
	}
	// end//testBQS127520//
}