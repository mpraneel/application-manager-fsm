package edu.ncsu.csc216.app_manager.model.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * Writes application data to a file.
 * @author Praneel Magapu
 * 
 */
public class AppWriter {


    /**
     * Writes the list of applications to the specified file.
     * 
     * @param fileName the name of the file to write to
     * @param apps the list of applications to write
     */
    public static void writeAppsToFile(String fileName, List<Application> apps) {
    	
    	if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Unable to save file");
        }
        if (apps == null) {
            throw new IllegalArgumentException("Unable to save file");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Application app : apps) {
                writer.write(app.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to save file.");
        }
    }
}