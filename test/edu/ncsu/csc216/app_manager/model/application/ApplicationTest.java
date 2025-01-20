package edu.ncsu.csc216.app_manager.model.application;

import org.junit.Before;
import org.junit.Test;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

/**
 * Unit tests for the Application class.
 * 
 * @author Praneel Magapu
 */
public class ApplicationTest {

    /** Instance of Application used for testing */
    private Application application;

    /**
     * Sets up the Application object for testing before each test.
     */
    @Before
    public void setUp() {
        application = new Application(1, Application.AppType.NEW, "Test Summary", "Initial Note");
    }

    /**
     * Tests the Application constructor, ensuring that all fields are correctly initialized.
     */
    @Test
    public void testConstructor() {
        assertEquals(1, application.getAppId());
        assertEquals(Application.A_NEW, application.getAppType());
        assertEquals("Test Summary", application.getSummary());
        assertEquals(Application.REVIEW_NAME, application.getStateName());
        assertFalse(application.isProcessed());
        assertNull(application.getResolution());
        assertTrue(application.getNotesString().contains("[Review] Initial Note"));
    }

    /**
     * Tests the invalid constructor, expecting an IllegalArgumentException
     * when providing an invalid application ID.
     */
    @Test
    public void testInvalidConstructor() {
        try {
            new Application(-1, Application.AppType.NEW, "Test Summary", "Initial Note");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }

    /**
     * Tests the secondary constructor that accepts additional parameters.
     */
    @Test
    public void testSecondConstructor() {
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Test Note");
        Application app = new Application(1, Application.REVIEW_NAME, Application.A_NEW, "Summary", "Reviewer", false, "Note 3", notes);
        assertEquals(1, app.getAppId());
        assertEquals(Application.REVIEW_NAME, app.getStateName());
        assertEquals(Application.A_NEW, app.getAppType());
        assertEquals("Summary", app.getSummary());
        assertEquals("Reviewer", app.getReviewer());
        assertFalse(app.isProcessed());
        assertNull(app.getResolution());
        assertTrue(app.getNotesString().contains("Test Note"));
    }

    /**
     * Tests the update method to transition the application from Review to Interview state.
     */
    @Test
    public void testUpdateReviewToInterview() {
        Command command = new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "Accepted for interview");
        application.update(command);
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());
        assertEquals(Command.R_REVCOMPLETED, application.getResolution());
        assertEquals("Reviewer1", application.getReviewer());
    }

    /**
     * Tests that an invalid update command throws an UnsupportedOperationException.
     */
    @Test
    public void testInvalidUpdate() {
        Application app = new Application(1, Application.AppType.NEW, "Test Summary", "Initial Note");
        Command invalidCommand = new Command(Command.CommandValue.REOPEN, null, null, "Invalid command");

        try {
            app.update(invalidCommand);
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Invalid information.", e.getMessage());
        }
    }

    /**
     * Tests the addition of a new note to the application.
     */
    @Test
    public void testAddNote() {
        application.addNote("New note");
        assertTrue(application.getNotesString().contains("[Review] New note"));
    }

    /**
     * Tests the toString method for the correct string representation of the application.
     */
    @Test
    public void testToString() {
        // Create an application instance with no resolution
        Application app1 = new Application(1, Application.AppType.NEW, "Test Summary", "Initial Note");

        // Expected output without "null" for resolution
        String expected = "*1,Review,New,Test Summary,false,\n-[Review] Initial Note";
        
        // Assert that the actual output matches the expected output
        assertEquals(expected, app1.toString());
    }
    /**
     * Tests a full state cycle from Review to Closed, ensuring state transitions
     * happen in the correct order.
     */
    @Test
    public void testFullStateCycle() {
        // Review to Interview
        application.update(new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "To Interview"));
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());

        // Interview to Waitlist
        application.update(new Command(CommandValue.STANDBY, null, Resolution.INTCOMPLETED, "To Waitlist"));
        assertEquals(Application.WAITLIST_NAME, application.getStateName());

        // Waitlist to Interview
        application.update(new Command(CommandValue.REOPEN, null, null, "Reopen"));
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());

        // Interview to RefCheck (for NEW application)
        application.update(new Command(CommandValue.ACCEPT, "Reviewer2", Resolution.INTCOMPLETED, "To RefCheck"));
        assertEquals(Application.REFCHK_NAME, application.getStateName());

        // RefCheck to Offer
        application.update(new Command(CommandValue.ACCEPT, "Reviewer3", Resolution.REFCHKCOMPLETED, "To Offer"));
        assertEquals(Application.OFFER_NAME, application.getStateName());
        assertTrue(application.isProcessed());

        // Offer to Closed
        application.update(new Command(CommandValue.ACCEPT, "Reviewer3", Resolution.OFFERCOMPLETED, "Hired"));
        assertEquals(Application.CLOSED_NAME, application.getStateName());
    }

    /**
     * Tests the OLD application flow to ensure state transitions occur correctly
     * for an OLD application type.
     */
    @Test
    public void testOldApplicationFlow() {
        Application oldApp = new Application(2, Application.AppType.OLD, "Old App", "Old Note");

        // Review to Interview
        oldApp.update(new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "To Interview"));
        assertEquals(Application.INTERVIEW_NAME, oldApp.getStateName());

        // Interview directly to Offer (for OLD application)
        oldApp.update(new Command(CommandValue.ACCEPT, "Reviewer2", Resolution.INTCOMPLETED, "To Offer"));
        assertEquals(Application.OFFER_NAME, oldApp.getStateName());
        assertTrue(oldApp.isProcessed());
    }

    /**
     * Tests the getResolution method to ensure it retrieves the correct resolution after updates.
     */
    @Test
    public void testGetResolution() {
    	// By default, resolution should be null
        assertNull(application.getResolution());
        
        application.update(new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "Accepted"));
        assertEquals(Command.R_REVCOMPLETED, application.getResolution());         

        // Set resolution to REVCOMPLETED and check
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer", Command.Resolution.REVCOMPLETED, "Note"));
        assertEquals(Command.R_REVCOMPLETED, application.getResolution());

        // Set resolution to INTCOMPLETED and check
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer", Command.Resolution.INTCOMPLETED, "Note"));
        assertEquals(Command.R_INTCOMPLETED, application.getResolution());

        // Set resolution to OFFERCOMPLETED and check
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer", Command.Resolution.OFFERCOMPLETED, "Note"));
        assertEquals(Command.R_OFFERCOMPLETED, application.getResolution());
    }
    
    /**
     * Tests invalid application with null summary.
     */
    @Test
    public void testInvalidConstructorNullSummary() {
        try {
            new Application(1, Application.AppType.NEW, null, "Initial Note");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }

    /**
     * Tests invalid application with null initial note.
     */
    @Test
    public void testInvalidConstructorNullNote() {
        try {
            new Application(1, Application.AppType.NEW, "Test Summary", null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }
    
    /**
     * Tests transition from Offer to Closed for NEW application.
     */
    @Test
    public void testOfferToClosed() {
        application.update(new Command(CommandValue.ACCEPT, "Reviewer", Resolution.OFFERCOMPLETED, "Offered"));
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());
        
        application.update(new Command(CommandValue.ACCEPT, "Reviewer", Resolution.OFFERCOMPLETED, "Hired"));
        assertEquals(Application.REFCHK_NAME, application.getStateName());
    }

    /**
     * Tests Waitlist to Closed transition.
     */
    @Test
    public void testWaitlistToClosed() {
        application.update(new Command(CommandValue.STANDBY, null, Resolution.INTCOMPLETED, "Waitlisted"));
        assertEquals(Application.WAITLIST_NAME, application.getStateName());

        application.update(new Command(CommandValue.REJECT, null, Resolution.REVCOMPLETED, "Rejected"));
        assertEquals(Application.CLOSED_NAME, application.getStateName());
    }

    /**
     * Tests Interview to Closed transition.
     */
    @Test
    public void testInterviewToClosed() {
        application.update(new Command(CommandValue.REJECT, "Reviewer", Resolution.INTCOMPLETED, "Rejected after interview"));
        assertEquals(Application.CLOSED_NAME, application.getStateName());
    }
    
    /**
     * Tests invalid application with null AppType.
     */
    @Test
    public void testInvalidConstructorNullAppType() {
        try {
            new Application(1, null, "Test Summary", "Initial Note");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }
    
    /**
     * Tests invalid second constructor with null state name.
     */
    @Test
    public void testInvalidSecondConstructorNullStateName() {
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Test Note");
        try {
            new Application(1, null, Application.A_NEW, "Summary", "Reviewer", false, "Note 3", notes);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }

    /**
     * Tests invalid second constructor with null reviewer.
     */
    @Test
    public void testInvalidSecondConstructorNullReviewer() {
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Test Note");
        try {
            new Application(1, Application.REVIEW_NAME, Application.A_NEW, "Summary", null, false, "Note 3", notes);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Application cannot be created.", e.getMessage());
        }
    }

    /**
     * Tests invalid update with null CommandValue.
     */
    @Test
    public void testUpdateWithNullCommandValue() {
        try {
            Command invalidCommand = new Command(null, "Reviewer", Resolution.REVCOMPLETED, "Invalid command");
            application.update(invalidCommand);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid information.", e.getMessage());
        }
    }

    /**
     * Tests valid ACCEPT Command with null resolution (allowed behavior).
     */
    @Test
    public void testAcceptCommandWithNullResolution() {
        Command validCommand = new Command(CommandValue.ACCEPT, "Reviewer", null, "Accepted with null resolution");
        try {
            application.update(validCommand);
            assertEquals(Application.INTERVIEW_NAME, application.getStateName());
            assertEquals("Reviewer", application.getReviewer());
            assertNull(application.getResolution()); 
        } catch (Exception e) {
            fail("No exception should be thrown: " + e.getMessage());
        }
    }
    
    /**
     * Test transition from RefCheck to Closed.
     */
    @Test
    public void testRefCheckToClosedTransition() {
        // First, ensure we are in the Interview state
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer1", Command.Resolution.INTCOMPLETED, "Moved to interview"));
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());  // Check we are in the Interview state

        // Move to RefCheck state
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer1", Command.Resolution.REVCOMPLETED, "Moved to reference check"));
        assertEquals(Application.REFCHK_NAME, application.getStateName());  // Check we are now in RefCheck state

        // Transition to Closed state
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer1", Command.Resolution.REFCHKCOMPLETED, "Offer made after reference check"));
        assertEquals(Application.OFFER_NAME, application.getStateName()); 
        assertEquals(Command.R_REFCHKCOMPLETED, application.getResolution());
    }


    /**
     * Test transition from Waitlist to Offer.
     */
    @Test
    public void testWaitlistToOfferTransition() {
        // Move to Waitlist state
        application.update(new Command(Command.CommandValue.STANDBY, null, Resolution.INTCOMPLETED, "Moved to waitlist")); // Adjust reviewer to null if not needed
        assertEquals(Application.WAITLIST_NAME, application.getStateName());  // Ensure you use the constant

        // Transition to Offer state
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer2", Command.Resolution.OFFERCOMPLETED, "Moved to offer"));
        assertEquals(Application.OFFER_NAME, application.getStateName());  // Ensure you use the constant
        assertEquals(Command.R_OFFERCOMPLETED, application.getResolution());
    }

    /**
     * Test transition from Interview to Closed.
     */
    @Test
    public void testInterviewToClosedTransition() {
        // Move to Interview state
        application.update(new Command(Command.CommandValue.ACCEPT, "Reviewer1", Command.Resolution.INTCOMPLETED, "Moved to interview"));
        assertEquals(Application.INTERVIEW_NAME, application.getStateName());  // Ensure you use the constant

        // Transition to Closed state
        application.update(new Command(Command.CommandValue.REJECT, "Reviewer1", Command.Resolution.INTCOMPLETED, "Rejected after interview"));
        assertEquals(Application.CLOSED_NAME, application.getStateName());  // Ensure you use the constant
        assertEquals(Command.R_INTCOMPLETED, application.getResolution());
    }

}

