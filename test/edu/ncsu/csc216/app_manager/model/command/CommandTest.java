package edu.ncsu.csc216.app_manager.model.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests the Command class.
 * 
 * @author Praneel Magapu
 */
public class CommandTest {

    /**
     * Test method for the Command constructor with valid inputs.
     */
    @Test
    public void testValidCommandConstructor() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer1", Command.Resolution.REVCOMPLETED, "Accepted");
        assertEquals(Command.CommandValue.ACCEPT, command.getCommand());
        assertEquals("reviewer1", command.getReviewerId());
        assertEquals(Command.Resolution.REVCOMPLETED, command.getResolution());
        assertEquals("Accepted", command.getNote());
    }

    /**
     * Test method for the Command constructor with null CommandValue.
     */
    @Test
    public void testNullCommandValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(null, "reviewer1", Command.Resolution.REVCOMPLETED, "Test");
        });
    }

    /**
     * Test method for the Command constructor with ACCEPT and null reviewerId.
     */
    @Test
    public void testAcceptWithNullReviewer() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.ACCEPT, null, Command.Resolution.REVCOMPLETED, "Test");
        });
    }

    /**
     * Test method for the Command constructor with STANDBY and null resolution.
     */
    @Test
    public void testStandbyWithNullResolution() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.STANDBY, "reviewer1", null, "Test");
        });
    }

    /**
     * Test method for the Command constructor with null note.
     */
    @Test
    public void testNullNote() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.REJECT, "reviewer1", Command.Resolution.REVCOMPLETED, null);
        });
    }

    /**
     * Test method for getCommandValue().
     */
    @Test
    public void testGetCommandValue() {
        Command command = new Command(Command.CommandValue.REOPEN, "reviewer1", null, "Reopened");
        assertEquals(Command.CommandValue.REOPEN, command.getCommand());
    }

    /**
     * Test method for getReviewerId().
     */
    @Test
    public void testGetReviewerId() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer2", Command.Resolution.INTCOMPLETED, "Accepted");
        assertEquals("reviewer2", command.getReviewerId());
    }

    /**
     * Test method for getResolution().
     */
    @Test
    public void testGetResolution() {
        Command command = new Command(Command.CommandValue.REJECT, "reviewer1", Command.Resolution.REFCHKCOMPLETED, "Rejected");
        assertEquals(Command.Resolution.REFCHKCOMPLETED, command.getResolution());
    }

    /**
     * Test method for getNote().
     */
    @Test
    public void testGetNote() {
        Command command = new Command(Command.CommandValue.STANDBY, "reviewer1", Command.Resolution.INTCOMPLETED, "On standby");
        assertEquals("On standby", command.getNote());
    }

    /**
     * Test method for getCommand().
     */
    @Test
    public void testGetCommand() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer1", Command.Resolution.OFFERCOMPLETED, "Offer accepted");
        assertEquals(Command.CommandValue.ACCEPT, command.getCommand());
    }
}