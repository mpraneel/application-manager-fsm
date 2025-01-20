package edu.ncsu.csc216.app_manager.model.io;

import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.app_manager.model.application.Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.List;

/**
 * Test class for AppReader functionality.
 * 
 * @author Praneel Magapu
 */
public class AppReaderTest {

    /**
     * Tests reading applications from a valid file.
     */
    @Test
    public void testReadAppsFromFile() {
    	
    	@SuppressWarnings("unused")
    	AppReader reader = new AppReader();
    	
        List<Application> application = AppReader.readAppsFromFile("test-files/app17.txt");
        assertEquals(1, application.size());

        assertThrows(IllegalArgumentException.class, () -> AppReader.readAppsFromFile("invalid.txt"));
    }
    
    /**
     * Tests the handling of malformed application data.
     */
    @Test
    public void testProcessApplicationErrorViaFile() {
        // Create a malformed application data file
        String malformedAppData = "1,Submitted,,Summary,Reviewer,false\nResolution without notes";
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get("test-files/malformed_app.txt"), malformedAppData.getBytes());
        } catch (java.io.IOException e) {
            fail("Failed to create malformed app data file.");
        }

        // Attempt to read the malformed file and expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> AppReader.readAppsFromFile("test-files/malformed_app.txt"));
    }
}
