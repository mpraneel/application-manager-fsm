package edu.ncsu.csc216.app_manager.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

/** Tests the functionality of the AppList class.
 * 
 *  @author Praneel Magapu
 *  
 */
public class AppListTest {

    /** Reference to the AppList object */
    private AppList appList;

    /** Sets up a new AppList instance before each test. */
    @BeforeEach
    void setUp() {
        appList = new AppList();
    }

    /** Tests the addition of applications and verifies the counter. */
    @Test
    void testAddAppAndCounter() {
        assertEquals(1, appList.addApp(AppType.NEW, "Summary 1", "Note 1"));
        assertEquals(3, appList.addApp(AppType.OLD, "Summary 2", "Note 2"));
        assertEquals(5, appList.addApp(AppType.NEW, "Summary 3", "Note 3"));
    }

    /** Tests the retrieval of applications by ID when the ID is invalid. */
    @Test
    void testGetAppByIdInvalid() {
        assertNull(appList.getAppById(999));
        assertNull(appList.getAppById(1));
    }

    /** Tests the deletion of applications by ID when the ID is invalid. */
    @Test
    void testDeleteAppByIdInvalid() {
        appList.deleteAppById(999);
        assertEquals(0, appList.getApps().size());
    }

    /** Tests the addition of applications with null or empty summaries. */
    @Test
    void testAddAppNullSummary() {
        assertThrows(IllegalArgumentException.class, () -> appList.addApp(AppType.NEW, null, "Note 1"));
        assertThrows(IllegalArgumentException.class, () -> appList.addApp(AppType.NEW, "", "Note 1"));
    }

    /** Tests the addition of duplicate applications. */
    @Test
    void testAddDuplicateApps() {
        Application app1 = new Application(2, AppType.NEW, "Summary 1", "Note 1");
        Application app2 = new Application(2, AppType.NEW, "Summary 2", "Note 2");
        appList.addApps(List.of(app1, app2));
        assertEquals(1, appList.getApps().size());
    }

    /** Tests that applications are sorted by ID when added to the AppList. */
    @Test
    void testSortingOnAdd() {
        Application app1 = new Application(4, AppType.NEW, "Summary 3", "Note 3");
        Application app2 = new Application(2, AppType.OLD, "Summary 1", "Note 1");
        Application app3 = new Application(3, AppType.NEW, "Summary 2", "Note 2");
        appList.addApps(List.of(app1, app2, app3));
        List<Application> apps = appList.getApps();
        assertEquals(2, apps.get(0).getAppId());
        assertEquals(3, apps.get(1).getAppId());
        assertEquals(4, apps.get(2).getAppId());
    }

    /** Tests retrieval of applications by type in a case-insensitive manner. */
    @Test
    void testGetAppsByTypeCaseInsensitive() {
        appList.addApp(AppType.NEW, "New App", "Note 1");
        appList.addApp(AppType.OLD, "Old App", "Note 2");
        List<Application> newApps = appList.getAppsByType("NEW");
        List<Application> oldApps = appList.getAppsByType("old");
        assertEquals(1, newApps.size());
        assertEquals(1, oldApps.size());
    }

    /** Tests the execution of multiple commands on an application. */
    @Test
    void testMultipleCommandsExecution() {
        int id = appList.addApp(AppType.NEW, "Summary", "Initial Note");
        Application app = appList.getAppById(id);
        Command command1 = new Command(CommandValue.ACCEPT, "Reviewer1", Resolution.REVCOMPLETED, "Review Complete");
        Command command2 = new Command(CommandValue.STANDBY, "Reviewer1", Resolution.INTCOMPLETED, "Interview Complete");
        appList.executeCommand(id, command1);
        appList.executeCommand(id, command2);
        assertEquals("Waitlist", app.getStateName());
        assertEquals("Reviewer1", app.getReviewer());
    }
}