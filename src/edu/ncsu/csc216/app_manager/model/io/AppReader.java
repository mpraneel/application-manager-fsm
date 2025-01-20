package edu.ncsu.csc216.app_manager.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * The AppReader class reads applications from a file and converts it to Application objects.
 * 
 * @author Praneel Magapu
 */
public class AppReader {

	/**
	 * Constructor
	 */
	public AppReader(){
		//Intentionally empty constructor
	}

    /**
     * Reads applications from a file and returns a list of applications.
     * 
     * @param fileName the name of the file to read from
     * @return a List of Application objects
     */
    public static List<Application> readAppsFromFile(String fileName) {
        List<Application> applications = new ArrayList<>();
        try (Scanner fileReader = new Scanner(new FileInputStream(fileName))) {
            fileReader.useDelimiter("\\r?\\n?[*]");
            while (fileReader.hasNext()) {
                applications.add(processApplication(fileReader.next()));
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        return applications;
    }


    /**
     * Processes an application and converts it from a string to an Application object.
     * 
     * @param appString the string with the applications
     * @return the Application object from the string
     * @throws IllegalArgumentException if the application cannot be created.
     */
    private static Application processApplication(String appString) {
        try (Scanner appReader = new Scanner(appString)) {
            appReader.useDelimiter(",");
            ArrayList<String> notes = new ArrayList<>();
            
            int id = appReader.nextInt();
            String state = appReader.next();
            String appType = appReader.next();
            String summary = appReader.next();
            String reviewer = appReader.next();
            boolean processPaperwork = appReader.nextBoolean();

            appReader.useDelimiter("\n");
            String fileResolution = appReader.next();
            String resolution = fileResolution.length() > 1 ? fileResolution.substring(1) : "";

            appReader.useDelimiter("\\r?\\n?[-]");
            while (appReader.hasNext()) {
                notes.add(appReader.next().trim());
            }

            return new Application(id, state, appType, summary, reviewer, processPaperwork, resolution, notes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error processing application.");
        }
    }

}