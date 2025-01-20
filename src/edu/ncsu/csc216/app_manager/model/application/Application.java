package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

import java.util.ArrayList;

/**
 * Represents an application in the system.
 * @author Praneel Magapu
 */
public class Application {

    /** constant for new application*/
    public static final String A_NEW = "New";

    /**constant for old application*/
    public static final String A_OLD = "Old";

    /** constant for hired application */
    public static final String A_HIRED = "Hired";

    /** Constant for review name*/
    public static final String REVIEW_NAME = "Review";

    /** Constant for Interview Name */
    public static final String INTERVIEW_NAME = "Interview";

    /** Constant for Reference Check name */
    public static final String REFCHK_NAME = "RefCheck";

    /** Constant for Offer name */
    public static final String OFFER_NAME = "Offer";

    /** Constant for Wait-list name */
    public static final String WAITLIST_NAME = "Waitlist";

    /** Constant for a Closed application */
    public static final String CLOSED_NAME = "Closed";

    /** Private field for application ID */
    private int appId;

    /** Private field for the application state */
    private AppState state;

    /** Private field for the type of application */
    private AppType appType;

    /** Private field for the application summary */
    private String summary;

    /** Private field for the reviewer */
    private String reviewer;

    /** Private boolean for if the paperwork is processed or not */
    private boolean processPaperwork;

    /** Private field for the resolution*/
    private Resolution resolution;

    /** Private ArrayList of all the notes as a string */
    private ArrayList<String> notes;

    /**
     * Enum representing the type of application.
     */
    public enum AppType {
        
        /**
         * Represents a new application that has just been submitted.
         */
        NEW,

        /**
         * Represents an older application that was previously processed.
         */
        OLD,

        /**
         * Represents an application that has been completed, and the candidate has been hired.
         */
        HIRED
    }


    /**
     * Constructs a new Application with the specified id, application type, summary, and note.
     * 
     * @param id Unique id of the application
     * @param appType Type of the application
     * @param summary Summary information of the application
     * @param note Initial note to be added to the application
     */
    public Application(int id, AppType appType, String summary, String note) {

        if (id < 1 || appType == null || summary == null || summary.isEmpty() || note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Application cannot be created.");
        }
        this.appId = id;
        this.appType = appType;
        this.summary = summary;
        this.reviewer = null;
        this.processPaperwork = false;
        this.resolution = null;
        this.notes = new ArrayList<>();
        this.state = new ReviewState();
        addNote(note);

    }

    /**
     * Constructs a new Application with full details, used for reading from a data source.
     * 
     * @param id Unique id of the application
     * @param state Current state of the application as a string
     * @param appType Type of the application as a string
     * @param summary Summary of the application
     * @param reviewer Reviewer of the application
     * @param processPaperwork Paperwork status of the application
     * @param resolution Resolution of the application
     * @param notes List of notes associated with the application
     */
    public Application(int id, String state, String appType, String summary, String reviewer, boolean processPaperwork, String resolution, ArrayList<String> notes) {
        if (id < 1 || state == null || state.isEmpty() || appType == null || appType.isEmpty() || 
                summary == null || summary.isEmpty() || reviewer == null || resolution == null
                || notes == null || notes.isEmpty()) {
            throw new IllegalArgumentException("Application cannot be created.");
        }
        this.appId = id;
        setState(state); // Helper method to set state from a string
        setAppType(appType); // Helper method to set appType from a string
        this.summary = summary;
        this.reviewer = reviewer;
        this.processPaperwork = processPaperwork;
        setResolution(resolution); // Helper method to set resolution from a string
        this.notes = notes;
    }

    /**
     * Sets the resolution for the application based on the provided resolution name.
     * If the resolution name is null or empty, the resolution is set to null.
     * 
     * @param resolution the name of the resolution to set
     * @throws IllegalArgumentException if the resolution name is invalid
     */
    private void setResolution(String resolution) {
    	 if (resolution.equalsIgnoreCase(Command.R_REVCOMPLETED)) {
             this.resolution = Resolution.REVCOMPLETED;
         }
         else if (resolution.equalsIgnoreCase(Command.R_INTCOMPLETED)) {
             this.resolution = Resolution.INTCOMPLETED;
         }
         else if (resolution.equalsIgnoreCase(Command.R_REFCHKCOMPLETED)) {
             this.resolution = Resolution.REFCHKCOMPLETED;
         }
         else if (resolution.equalsIgnoreCase(Command.R_OFFERCOMPLETED)) {
             this.resolution = Resolution.OFFERCOMPLETED;
         }
         else {
             this.resolution = null;
         }
    }

    /**
     * Sets the application type based on the provided application type name.
     * 
     * @param appTypeName the name of the application type
     * @throws IllegalArgumentException if the application type is invalid
     */
    private void setAppType(String appTypeName) {
        if (A_NEW.equals(appTypeName)) {
            this.appType = AppType.NEW;
        } else if (A_OLD.equals(appTypeName)) {
            this.appType = AppType.OLD;
        } else {
            throw new IllegalArgumentException("Invalid app type.");
        }

    }

    /**
     * Sets the application state based on the provided state name.
     * 
     * @param stateName the name of the state to set
     * @throws IllegalArgumentException if the state name is invalid
     */
    private void setState(String stateName) {
        if (stateName.equalsIgnoreCase(REVIEW_NAME)) {
            this.state = new ReviewState();
        } else if (stateName.equalsIgnoreCase(INTERVIEW_NAME)) {
            this.state = new InterviewState();
        } else if (stateName.equalsIgnoreCase(WAITLIST_NAME)) {
            this.state = new WaitlistState();
        } else if (stateName.equalsIgnoreCase(REFCHK_NAME)) {
            this.state = new RefChkState();    
        } else if (stateName.equalsIgnoreCase(OFFER_NAME)) {
            this.state = new OfferState();         
        } else if (stateName.equalsIgnoreCase(CLOSED_NAME)) {
            this.state = new ClosedState();
        } else {
            throw new IllegalArgumentException("Invalid state.");
        }
    }


    /**
     * Updates the application state based on the given command.
     * 
     * @param c Command to process
     */
    public void update(Command c) {
        if (this.state != null) {
            this.state.updateState(c); // Delegate to current state's updateState method
        } else {
            throw new UnsupportedOperationException("No valid state.");
        }
    }

    /**
     * Gets the state name of the application.
     * 
     * @return The state name
     */
    public String getStateName() {
        return state.getStateName();
    }

    /**
     * Gets the application type.
     * 
     * @return The application type
     */
    public String getAppType() {
        switch (appType) {
        case NEW:
            return A_NEW;
        case OLD:
            return A_OLD;
        case HIRED:
            return A_HIRED;
        default:
            return null;
        }
    }

    /**
     * Gets the summary of the application.
     * 
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Gets the reviewer of the application.
     * 
     * @return The reviewer
     */
    public String getReviewer() {
        return reviewer;
    }

    /**
     * Checks if the application is processed.
     * 
     * @return True if processed, false otherwise
     */
    public boolean isProcessed() {
        return processPaperwork;
    }

    /**
     * Gets the resolution of the application.
     * 
     * @return The resolution
     */
    public String getResolution() {
        if (resolution == null) {
            return null;
        }
        switch (resolution) {
        case REVCOMPLETED:
            return Command.R_REVCOMPLETED;
        case INTCOMPLETED:
            return Command.R_INTCOMPLETED;
        case REFCHKCOMPLETED:
            return Command.R_REFCHKCOMPLETED;
        case OFFERCOMPLETED:
            return Command.R_OFFERCOMPLETED;
        default:
            return null;
        }
    }

    /**
     * Gets the notes as a string.
     * 
     * @return The notes string
     */
    public String getNotesString() {
        String result = "";
        for (String note : notes) {
            result += "-" + note + "\n";
        }
        return result;
    }

    /**
     * Gets the notes.
     * 
     * @return The notes
     */
    public ArrayList<String> getNotes() {
        return this.notes;
    }

    /**
     * Gets the app ID.
     * 
     * @return The app ID
     */
    public int getAppId() {
        return this.appId;
    }

    // Inner interface for AppState
    private interface AppState {
        void updateState(Command command);
        String getStateName();
    }

    // Concrete State Classes
    private class ReviewState implements AppState {
        @Override
        public void updateState(Command command) {
            switch (command.getCommand()) {
            case ACCEPT:
                state = new InterviewState(); // Transition to InterviewState
                reviewer = command.getReviewerId();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case REJECT:
                state = new ClosedState(); // Transition to ClosedState
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case STANDBY:
                state = new WaitlistState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            default:
                throw new UnsupportedOperationException("Invalid information.");
            }
        }

        @Override
        public String getStateName() {
            return REVIEW_NAME;
        }
    }

    private class InterviewState implements AppState {
        @Override
        public void updateState(Command command) {
            switch (command.getCommand()) {
            case ACCEPT:
                if (appType == AppType.NEW) {
                    state = new RefChkState();
                } else {
                    state = new OfferState();
                    processPaperwork = true;
                }
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case REJECT:
                state = new ClosedState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case STANDBY:
                state = new WaitlistState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            default:
                throw new UnsupportedOperationException("Invalid information.");
            }
        }

        @Override
        public String getStateName() {
            return INTERVIEW_NAME;
        }
    }

    private class WaitlistState implements AppState {
        @Override
        public void updateState(Command command) {
            switch (command.getCommand()) {
            case REOPEN:
                state = new InterviewState();
                addNote(command.getNote());
                break;
            case REJECT:
                state = new ClosedState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case ACCEPT:
                state = new OfferState();
                processPaperwork = true; // Assuming paperwork is processed on offer
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            default:
                throw new UnsupportedOperationException("Invalid information.");
            }
        }

        @Override
        public String getStateName() {
            return WAITLIST_NAME;
        }
    }

    private class RefChkState implements AppState {
        @Override
        public void updateState(Command command) {
            switch (command.getCommand()) {
            case ACCEPT:
                state = new OfferState();
                processPaperwork = true;
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case REJECT:
                state = new ClosedState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            default:
                throw new UnsupportedOperationException("Invalid information.");
            }
        }

        @Override
        public String getStateName() {
            return REFCHK_NAME;
        }
    }

    private class OfferState implements AppState {
        @Override
        public void updateState(Command command) {
            switch (command.getCommand()) {
            case ACCEPT:
                state = new ClosedState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            case REJECT:
                state = new ClosedState();
                resolution = command.getResolution();
                addNote(command.getNote());
                break;
            default:
                throw new UnsupportedOperationException("Invalid information.");
            }
        }

        @Override
        public String getStateName() {
            return OFFER_NAME;
        }
    }

    private class ClosedState implements AppState {
        @Override
        public void updateState(Command command) {
            throw new UnsupportedOperationException("Cannot update a closed application.");
        }

        @Override
        public String getStateName() {
            return CLOSED_NAME;
        }
    }

    /**
     * Adds a note to the application.
     * 
     * @param note the note to add
     * @throws IllegalArgumentException if the note is null or empty
     */
    public void addNote(String note) {
        if (note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Note cannot be empty.");
        }
        notes.add("[" + state.getStateName() + "] " + note);
    }

    /**
     * Returns the string representation of the application, including the ID, state name, 
     * type, summary, reviewer, whether the application is processed, and notes.
     * 
     * @return the string representation of the application
     */
    @Override
    public String toString() {
        // Initialize the result with common fields
        String result = "*" + appId + "," + getStateName() + "," + getAppType() + "," + getSummary() + ",";

        // Append reviewer if it's not null, otherwise append an empty string
        if (getReviewer() != null) {
            result += getReviewer() + ",";
        }
        result += processPaperwork + ",";

        // Append resolution if it's not null
        if (getResolution() != null) {
            result += getResolution() + ",";
        }

        // Append notes, ensuring they are prefixed correctly
        result += "\n" + getNotesString().trim();

        return result;
    }

}