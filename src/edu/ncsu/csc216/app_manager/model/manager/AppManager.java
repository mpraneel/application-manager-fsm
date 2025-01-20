package edu.ncsu.csc216.app_manager.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.io.AppReader;
import edu.ncsu.csc216.app_manager.model.io.AppWriter;

/**
 * The AppManager class oversees the application management system 
 * and adheres to the Singleton design pattern.
 */
public class AppManager {

    /** Singleton instance of the AppManager */
    private static AppManager instance;

    /** List of applications managed by AppManager */
    private AppList appList;

    /** Private constructor to prevent instantiation */
    private AppManager() {
        appList = new AppList();
    }

    /**
     * Retrieves the singleton instance of AppManager.
     * 
     * @return the single instance of AppManager
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * Saves the current application list to a specified file.
     * 
     * @param filename the name of the file to save the applications
     */
    public void saveAppsToFile(String filename) {
        try {
            AppWriter.writeAppsToFile(filename, appList.getApps());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input while saving applications.");
        }
    }

    /**
     * Creates a new empty application list.
     */
    public void createNewAppList() {
        appList = new AppList();
    }

    /**
     * Converts the application list to a 2D Object array for display purposes.
     * 
     * @return a 2D Object array containing applications
     */
    public Object[][] getAppListAsArray() {
        List<Application> apps = appList.getApps();
        Object[][] appArray = new Object[apps.size()][4];

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            appArray[i][0] = app.getAppId();
            appArray[i][1] = app.getStateName();
            appArray[i][2] = app.getAppType();
            appArray[i][3] = app.getSummary();
        }
        return appArray;
    }
    
    /**
     * Loads applications from a specified file into the application list.
     * 
     * @param filename the name of the file to load applications from
     */
    public void loadAppsFromFile(String filename) {
        try {
            List<Application> applications = AppReader.readAppsFromFile(filename);
            appList.addApps(applications);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input while loading applications.");
        }
    }

    /**
     * Filters the application list by a specified type and returns a 2D Object array.
     * 
     * @param appType the type of applications to filter
     * @return a 2D Object array of applications filtered by type
     */
    public Object[][] getAppListAsArrayByAppType(String appType) {
        if (appType == null) {
            throw new IllegalArgumentException("The application type cannot be null.");
        }

        List<Application> filteredApps = new ArrayList<>();
        for (Application app : appList.getApps()) {
            if (appType.equals(app.getAppType())) {
                filteredApps.add(app);
            }
        }

        Object[][] appArray = new Object[filteredApps.size()][4];
        for (int i = 0; i < filteredApps.size(); i++) {
            Application app = filteredApps.get(i);
            appArray[i][0] = app.getAppId();
            appArray[i][1] = app.getStateName();
            appArray[i][2] = app.getAppType();
            appArray[i][3] = app.getSummary();
        }
        return appArray;
    }

    /**
     * Retrieves an application by its unique ID.
     * 
     * @param id the ID of the application to retrieve
     * @return the Application associated with the specified ID
     */
    public Application getAppById(int id) {
        return appList.getAppById(id);
    }

    /**
     * Executes a given command on the application specified by its ID.
     * 
     * @param id the ID of the application to modify
     * @param command the command to be executed
     */
    public void executeCommand(int id, Command command) {
        appList.executeCommand(id, command);
    }

    /**
     * Deletes an application from the list by its ID.
     * 
     * @param id the ID of the application to delete
     */
    public void deleteAppById(int id) {
        appList.deleteAppById(id);
    }

    /**
     * Adds a new application to the list with the provided details.
     * 
     * @param appType the type of the application
     * @param summary a brief summary of the application
     * @param note additional notes regarding the application
     */
    public void addAppToList(AppType appType, String summary, String note) {
        if (appType == null || summary == null || summary.isEmpty() || note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Application parameters cannot be null or empty.");
        }
        appList.addApp(appType, summary, note);
    }
}
