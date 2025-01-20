package edu.ncsu.csc216.app_manager.model.io;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;

/**
 * This class tests the functionality of the AppWriter class
 * 
 */
public class AppWriterTest {

    /** List of applications used in tests */
	@SuppressWarnings("unused")
    private List<Application> apps;

    /** Directory for test files */
    private static final String EXPECTED_FILES_PATH = "test-files/";

    /**
     * Tests the instantiation of the AppWriter class.
     */
    @Test
    public void testAppWriter() {
        AppWriter aw = new AppWriter();
        assertTrue(aw instanceof AppWriter);
    }

    /**
     * Tests writing a list of applications to a file.
     */
    @Test
    public void testWriteAppsToFile() {
        ArrayList<Application> appArray = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<String>();
        notes.add("test note");
        appArray.add(new Application(1, AppType.NEW, "summary", "note"));
        try {
            AppWriter.writeAppsToFile("test-files/act_app_closed.txt", appArray);
        } catch (IllegalArgumentException e) {
            fail("Cannot write to file.");
        }
        // Assert to check file write success
        assertTrue(Files.exists(Paths.get("test-files/act_app_closed.txt")));
    }

    /**
     * Compares the contents of two files line by line.
     *
     * @param expectedFile the expected file path
     * @param actualFile the actual file path
     * @throws IOException if an I/O error occurs reading from the stream
     */
    private void compareFiles(String expectedFile, String actualFile) throws IOException {
        List<String> expectedLines = Files.readAllLines(Paths.get(EXPECTED_FILES_PATH + expectedFile));
        List<String> actualLines = Files.readAllLines(Paths.get(EXPECTED_FILES_PATH + actualFile));
        assertEquals(expectedLines, actualLines);
    }

    /**
     * Tests writing an application with an "Interview" status to a file and compares it with an expected file.
     *
     * @throws IOException if an I/O error occurs reading from or writing to a file
     */
    @Test
    public void testWriteAppInterview() throws IOException {
        List<Application> apps1 = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("[Review] note 1");
        notes.add("[Interview] note 2");

        apps1.add(new Application(5, "Interview", "Old", "Application summary", "reviewer", false, "", notes));

        AppWriter.writeAppsToFile(EXPECTED_FILES_PATH + "act_app_interview.txt", apps1);

        compareFiles("exp_app_interview.txt", "act_app_interview.txt");
        assertTrue(Files.exists(Paths.get(EXPECTED_FILES_PATH + "act_app_interview.txt"))); // Explicit assert
    }

    /**
     * Tests writing an application with an "Offer" status to a file and compares it with an expected file.
     *
     * @throws IOException if an I/O error occurs reading from or writing to a file
     */
    @Test
    public void testWriteAppOffer() throws IOException {
        List<Application> apps3 = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("[Review] note 1");
        notes.add("[Interview] note 2");
        notes.add("[RefCheck] note 3");
        notes.add("[Offer] note 4");

        apps3.add(new Application(1, "Offer", "Old", "Application summary", "reviewer2", true, "", notes));

        AppWriter.writeAppsToFile(EXPECTED_FILES_PATH + "act_app_offer.txt", apps3);

        compareFiles("exp_app_offer.txt", "act_app_offer.txt");
        assertTrue(Files.exists(Paths.get(EXPECTED_FILES_PATH + "act_app_offer.txt"))); // Explicit assert
    }

    /**
     * Tests writing an application with a "RefCheck" status to a file and compares it with an expected file.
     *
     * @throws IOException if an I/O error occurs reading from or writing to a file
     */
    @Test
    public void testWriteAppRefCheck() throws IOException {
        List<Application> apps2 = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("[Review] note 1");
        notes.add("[Interview] note 2");
        notes.add("[RefCheck] note 3");

        apps2.add(new Application(7, "RefCheck", "Old", "Application summary", "reviewer", true, "", notes));

        AppWriter.writeAppsToFile(EXPECTED_FILES_PATH + "act_app_refcheck.txt", apps2);

        compareFiles("exp_app_refcheck.txt", "act_app_refcheck.txt");
        assertTrue(Files.exists(Paths.get(EXPECTED_FILES_PATH + "act_app_refcheck.txt"))); // Explicit assert
    }

    /**
     * Tests that providing a null filename throws an IllegalArgumentException.
     */
    @Test
    public void testNullFileNameThrowsIllegalArgumentException() {
        List<Application> apps4 = new ArrayList<>();

		// Add a valid application to the list for the sake of the test
		apps4.add(new Application(1, AppType.NEW, "summary", "note"));

		assertThrows(IllegalArgumentException.class, () -> {
		    AppWriter.writeAppsToFile(null, apps4);
		});
    }

    /**
     * Tests that providing an empty filename throws an IllegalArgumentException.
     */
    @Test
    public void testEmptyFileNameThrowsIllegalArgumentException() {
		List<Application> apps5 = new ArrayList<>();

		// Add a valid application to the list for the sake of the test
		apps5.add(new Application(1, AppType.NEW, "summary", "note"));

		assertThrows(IllegalArgumentException.class, () -> {
		    AppWriter.writeAppsToFile("", apps5);
		});
	}

	/**
	 * Tests that providing null applications throws an IllegalArgumentException.
	 */
	@Test
	public void testNullAppsThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
		    AppWriter.writeAppsToFile("test-files/act_null_apps.txt", null);
		});
	}

	/**
	 * Tests that providing a null note in Application constructor throws an IllegalArgumentException.
	 */
	@Test
	public void testNullNoteInApplicationConstructorThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
		    new Application(1, AppType.NEW, "summary", null);
		});
	}

	/**
	 * Tests that providing an empty summary in Application constructor throws an IllegalArgumentException.
	 */
	@Test
	public void testEmptySummaryInApplicationConstructorThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
		    new Application(1, AppType.NEW, "", "note");
		});
	}

	/**
     * Tests handling of IOExceptions when attempting to write to an invalid path.
     */
    @Test
    public void testIOExceptionHandling() {
		List<Application> apps6 = new ArrayList<>();
		apps6.add(new Application(1, AppType.NEW, "summary", "note"));

		assertThrows(IllegalArgumentException.class, () -> {
		    AppWriter.writeAppsToFile("/invalid/path/act_io_exception.txt", apps6);
		});
	}
}