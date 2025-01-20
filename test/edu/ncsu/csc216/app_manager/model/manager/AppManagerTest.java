package edu.ncsu.csc216.app_manager.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

/** Tests the functionality of the AppManager class. */
public class AppManagerTest {
	
	/** Directory for expected test file */
	private static final String VALID_TEST_FILE = "test-files/exp_app_interview.txt";
	
	/** Directory for actual test file */
    private static final String ACTUAL_TEST_FILE = "test-files/act_app_interview.txt";

    /** Reference to the AppManager instance */
    private AppManager manager;

    /** Sets up the AppManager instance before each test. */
    @BeforeEach
    void setUp() {
        manager = AppManager.getInstance();
        manager.createNewAppList();
    }

    /** Tests the singleton pattern of AppManager. */
    @Test
    void testGetInstance() {
        assertNotNull(manager);
        assertEquals(manager, AppManager.getInstance());
    }

    /** Tests adding an application to the list. */
    @Test
    void testAddAppToList() {
        manager.addAppToList(AppType.NEW, "Test App", "Test Note");
        assertNotNull(manager.getAppById(1)); // Adjusted ID expectation based on counter logic
    }

    /** Tests retrieving an application by ID. */
    @Test
    void testGetAppById() {
        manager.addAppToList(AppType.NEW, "Test App", "Test Note");
        Application app = manager.getAppById(1); // Adjusted ID expectation based on counter logic
        assertNotNull(app);
        assertEquals("Test App", app.getSummary());
    }

   /** Tests executing a command on an application. */
   @Test
   void testExecuteCommand() {
       manager.addAppToList(AppType.NEW, "Test App", "Test Note");
       Command command = new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "Accepted");
       manager.executeCommand(1, command); // Adjusted ID expectation based on counter logic
       assertEquals("Interview", manager.getAppById(1).getStateName());
   }

   /** Tests retrieving the entire application list as an array. */
   @Test
   void testGetAppListAsArray() {
       manager.addAppToList(AppType.NEW, "App 1", "Note");
       manager.addAppToList(AppType.OLD, "App 2", "Note");
       Object[][] allApps = manager.getAppListAsArray();
       assertEquals(2, allApps.length);
   }

   /** Tests deleting an application by ID. */
   @Test
   void testDeleteAppById() {
       manager.addAppToList(AppType.NEW, "Test App", "Note");
       assertNotNull(manager.getAppById(1)); // Adjusted ID expectation based on counter logic
       manager.deleteAppById(1); // Adjusted ID expectation based on counter logic
       assertNull(manager.getAppById(1)); // Adjusted ID expectation based on counter logic
   }


   /** Tests creating a new application list. */
   @Test
   void testCreateNewAppList() {
       manager.addAppToList(AppType.NEW, "Test App", "Note");
       manager.createNewAppList();
       assertNull(manager.getAppById(1)); // Adjusted ID expectation based on counter logic after reset
   }
   
   /** Tests adding an application with null type. */
   @Test
   void testAddAppWithNullType() {
       assertThrows(IllegalArgumentException.class, () -> {
           manager.addAppToList(null, "Test App", "Test Note");
       });
   }

   /** Tests retrieving applications by a non-existent ID. */
   @Test
   void testGetAppByNonExistentId() {
       manager.createNewAppList(); // Reset list
       assertNull(manager.getAppById(999)); // Assuming 999 is a non-existent ID
   }


   /** Tests loading applications from an invalid file. */
   @Test
   void testLoadAppsFromInvalidFile() {
       assertThrows(RuntimeException.class, () -> {
           manager.loadAppsFromFile("invalid_file.txt");
       });
   }


   /** Tests retrieving app list as array by invalid app type. */
   @Test
   void testGetAppListAsArrayByInvalidType() {
       assertThrows(IllegalArgumentException.class, () -> {
           manager.getAppListAsArrayByAppType(null);
       });
   }
   
   /** Tests loading applications from a valid file. */
   @Test
   public void testLoadAppsFromFile() {
       // Load applications from the expected file
       manager.loadAppsFromFile(VALID_TEST_FILE);

       // Retrieve the list of apps and verify their details
       Object[][] appsArray = manager.getAppListAsArray();
       
       assertEquals(1, appsArray.length);

       // Validate the first application
       assertEquals(5, appsArray[0][0]); // App ID
       assertEquals("Interview", appsArray[0][1]); // State Name
       assertEquals("Old", appsArray[0][2]); // App Type
       assertEquals("Application summary", appsArray[0][3]); // Summary
       
   }

   /** Tests saving applications from a valid file. */
   @Test
   public void testSaveAppsToFile() {
       // Load apps from file
       manager.loadAppsFromFile(VALID_TEST_FILE);

       // Save the apps to a new file
       manager.saveAppsToFile(ACTUAL_TEST_FILE);

       // Compare the content of the saved file with the expected file
       try {
           List<String> expectedLines = Files.readAllLines(new File(VALID_TEST_FILE).toPath());
           List<String> actualLines = Files.readAllLines(new File(ACTUAL_TEST_FILE).toPath());

           // Normalize whitespace for comparison, while preserving multi-line notes
           for (int i = 0; i < expectedLines.size(); i++) {
               String expected = expectedLines.get(i).trim();
               String actual = actualLines.get(i).trim();
               assertEquals("File content mismatch on line " + i, expected, actual);
           }
       } catch (IOException e) {
           fail("Error reading file: " + e.getMessage());
       }
   }

}