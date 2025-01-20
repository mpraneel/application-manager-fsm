package edu.ncsu.csc216.app_manager.model.command;

/**
 * Encapsulates a user command that can trigger state changes in an application.
 * Contains information about the type of command, reviewer, resolution, and notes.
 * @author Praneel Magapu
 */
public class Command {

    /**
     * Enumeration of possible command values that a user can execute.
     * These values determine the action to be performed on the application.
     */
    public enum CommandValue { ACCEPT, REJECT, STANDBY, REOPEN }

    /**
     * Enumeration of possible resolutions associated with a command.
     * These values represent the outcomes of certain commands when executed.
     */
    public enum Resolution { REVCOMPLETED, INTCOMPLETED, REFCHKCOMPLETED, OFFERCOMPLETED }

    /** Constant string for the "ReviewCompleted" resolution. */
    public static final String R_REVCOMPLETED = "ReviewCompleted";
    
    /** Constant string for the "InterviewCompleted" resolution. */
    public static final String R_INTCOMPLETED = "InterviewCompleted";
    
    /** Constant string for the "OfferCompleted" resolution. */
    public static final String R_OFFERCOMPLETED = "OfferCompleted";
    
    /** Constant string for the "ReferenceCheckCompleted" resolution. */
    public static final String R_REFCHKCOMPLETED = "ReferenceCheckCompleted";

    /** The command value indicating the action taken. */
    private CommandValue commandValue;
    
    /** The ID of the reviewer associated with this command. */
    private String reviewerId;
    
    /** The resolution resulting from this command, if applicable. */
    private Resolution resolution;
    
    /** Additional notes related to the command. */
    private String note;

    /**
     * Constructs a Command object with the specified command value, reviewer ID, resolution, and note.
     * 
     * @param c the command value, must not be null
     * @param reviewerId the ID of the reviewer, required if the command value is ACCEPT
     * @param r the resolution, required for STANDBY and REJECT command values
     * @param note any additional notes, must not be null or empty
     * @throws IllegalArgumentException if invalid parameters are provided
     */
    public Command(CommandValue c, String reviewerId, Resolution r, String note) {
        if (c == null) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if (c == CommandValue.ACCEPT && (reviewerId == null || reviewerId.isEmpty())) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if ((c == CommandValue.STANDBY || c == CommandValue.REJECT) && r == null) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if (note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Invalid information.");
        }
        
        this.commandValue = c;
        this.reviewerId = reviewerId;
        this.resolution = r;
        this.note = note;
    }

    /**
     * Returns the reviewer ID associated with the command.
     * 
     * @return the reviewer ID, or null if not applicable
     */
    public String getReviewerId() {
        return reviewerId;
    }

    /**
     * Returns the resolution of the command.
     * 
     * @return the resolution, or null if not applicable
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Returns any notes associated with the command.
     * 
     * @return the notes associated with the command
     */
    public String getNote() {
        return note;
    }
    
    /**
     * Returns the command value associated with this Command.
     * 
     * @return the CommandValue of this Command
     */
    public CommandValue getCommand() {
        return commandValue;
    }
}