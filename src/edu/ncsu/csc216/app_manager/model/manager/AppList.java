package edu.ncsu.csc216.app_manager.model.manager;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;

/**
 * Manages a list of applications in the system.
 * 
 * @author Praneel Magapu
 */
public class AppList {

	/**
	 * A list of applications managed by the AppList.
	 */
	private List<Application> applications;

	/**
	 * A counter for tracking the number of applications added to the AppList.
	 */
	private int counter;

    /**
     * Constructs an empty AppList.
     */
    public AppList() {
    	applications = new ArrayList<>();
        counter = 0;
    }

    /**
     * Returns the list of all applications.
     * 
     * @return a list of all applications
     */
    public List<Application> getApps() {
    	return new ArrayList<>(applications);
    }

    /**
     * Returns the application with the specified ID.
     * 
     * @param id the ID of the application to retrieve
     * @return the application with the specified ID, or null if not found
     */
    public Application getAppById(int id) {
        for (Application app : applications) {
            if (app.getAppId() == id) {
                return app;
            }
        }
        return null;
    }

    /**
     * Executes a command on the application with the specified ID.
     * 
     * @param id the ID of the application
     * @param command the command to execute
     */
    public void executeCommand(int id, Command command) {
    	Application app = getAppById(id);
        if (app != null) {
            app.update(command);
        }
    }

    /**
     * Deletes the application with the specified ID.
     * 
     * @param id the ID of the application to delete
     */
    public void deleteAppById(int id) {
    	applications.removeIf(app -> app.getAppId() == id);
    }

    /**
     * Adds a new application to the list.
     * 
     * @param type the type of the application
     * @param summary the summary of the application
     * @param note the initial note for the application
     * @return the ID of the newly added application
     */
    public int addApp(AppType type, String summary, String note) {
    	if (type == null || summary == null || note == null || summary.isEmpty() || note.isEmpty()) {
            throw new IllegalArgumentException("Type, summary, and note must not be null or empty");
        }
    	Application newApp = new Application(++counter, type, summary, note);
        addApp(newApp);
        return newApp.getAppId();
    }

    /**
     * Adds a single application in sorted order and checks for duplicates.
     *
     * @param app Application to add
     */
    private void addApp(Application app) {
    	for (Application existingApp : applications) {
            if (existingApp.getAppId() == app.getAppId()) {
                return; // Duplicate found, do not add
            }
        }        
        int index = 0;
        while (index < applications.size() && applications.get(index).getAppId() < app.getAppId()) {
            index++;
        }
        
        applications.add(index, app);
        
        // Update counter to last application's id + 1
        if (!applications.isEmpty()) {
        	counter = applications.get(applications.size() - 1).getAppId() + 1;
        }
    }

	/**
     * Returns a list of applications of the specified type.
     * 
     * @param type the type of applications to retrieve
     * @return a list of applications of the specified type
     */
    public List<Application> getAppsByType(String type) {
        List<Application> filteredApps = new ArrayList<>();
        for (Application app : applications) {
            if (app.getAppType().equalsIgnoreCase(type)) {
                filteredApps.add(app);
            }
        }
        return filteredApps;
    }

    /**
     * Adds a list of applications to the current list.
     * 
     * @param newApps the list of applications to add
     */
    public void addApps(List<Application> newApps) {
    	for (Application app : newApps) {
            addApp(app);
        }
    }
    
}