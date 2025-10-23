/**
 * 🧭 MenuController — handles user navigation and CLI menu flow.
 * Coordinates user choices, delegating logic to MenuPrinter, InputHandler,
 * DashboardPrinter, and FileManager.
 */

package tracker.ui;

import tracker.core.*;
import tracker.data.*;

import java.io.*;
import java.util.*;

public class MenuController {

    private final List<JobApplication> applications;
    private final MenuHandler menu;
    private final String README = "README.md";

    public MenuController(List<JobApplication> applications) {
        this.applications = applications;
        this.menu = new MenuHandler(applications);
    }

    // 🎯 Main interactive loop — previously showMenu()
    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Print the menu layout
            MenuPrinter.printMainMenu();

            System.out.print("> ");
            if (!sc.hasNextInt()) {
                System.out.println("Please enter 1–10.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    DashboardPrinter.showSummary(applications);
                    break;

                case 2:
                    try {
                        JobApplication app = InputHandler.createApplicationFromInput(sc);
                        applications.add(app);
                        FileManager.saveApplications(applications);
                        FileManager.exportCSV(applications);
                        System.out.println("✅ Added & saved.");
                    } catch (Exception ex) {
                        System.out.println("❌ Could not add application: " + ex.getMessage());
                    }
                    break;

                case 3:
                    try {
                        MarkdownExporter.export(applications, README);
                        FileManager.exportCSV(applications);
                        System.out.println("✅ Exported " + README + " and applications.csv");
                    } catch (IOException e) {
                        System.out.println("Export failed: " + e.getMessage());
                    }
                    break;

                case 4:
                    UIHelper.printEchoInstructions();
                    break;

                case 5:
                    int added = menu.importFromSeedMarkdown(SeedData.SEED_MARKDOWN);
                    FileManager.saveApplications(applications);
                    FileManager.exportCSV(applications);
                    System.out.println("✅ Imported " + added + " applications from seed.");
                    break;

                case 6:
                    menu.viewRecentApplications();
                    break;

                case 7:
                    menu.searchApplications(sc);
                    break;

                case 8:
                    menu.removeApplication(sc);
                    FileManager.exportCSV(applications);
                    break;

                case 9:
                    DashboardPrinter.showCareerDashboard(applications);
                    break;

                case 10:
                    FileManager.saveApplications(applications);
                    FileManager.exportCSV(applications);
                    System.out.println(UIHelper.CYAN +
                        "ฅ^•ﻌ•^ฅ Bye-bye human! Career cat curls up for a nap. 💤");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
