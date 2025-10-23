/**
 * 🌸 Job Application Tracker CLI — v3.0
 * 
 * 💻 Overview:
 * This is the main entry point for the Job Application Tracker — a colorful,
 * terminal-based Java CLI that helps organize and analyze job applications.
 * It loads saved entries, handles menu navigation, and coordinates
 * exports to Markdown and CSV for GitHub portfolio use.
 *
 * 🧩 Modular Architecture:
 * - tracker.core — Core logic (FileManager, Stats, MenuHandler)
 * - tracker.data — Data models & Markdown export utilities
 * - tracker.ui   — User interface utilities (MenuPrinter, DashboardPrinter, InputHandler)
 *
 * 🧭 Key Responsibilities:
 * - Launch CLI and initialize data from `applications.txt`
 * - Display the interactive menu via {@link MenuPrinter}
 * - Handle user actions (add, search, export, delete)
 * - Delegate dashboard printing to {@link DashboardPrinter}
 * - Export analytics to README.md via {@link MarkdownExporter}
 *
 * 🧠 Design Notes:
 * - Follows Single Responsibility Principle for clarity and testing.
 * - Uses Java Streams, ChronoUnit, and DateTimeFormatter for modern APIs.
 * - Compatible with GitHub Codespaces and Makefile automation.
 */

package tracker;

import tracker.core.*;
import tracker.ui.*;
import tracker.data.*;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JobApplicationTracker {
    public static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    public static final String README = "README.md";

    // 📦 Preloaded Markdown seed data for first-time use
    public static final String SEED_MARKDOWN = SeedData.SEED_MARKDOWN;

    // 🧾 Central in-memory list storing all job applications
    public static final List<JobApplication> applications = new ArrayList<>();
    private static MenuHandler menu;

    // 🚀 Entry point: loads existing data, shows ASCII intro, then launches the CLI
    public static void main(String[] args) {
        FileManager.loadApplications(applications);
        menu = new MenuHandler(applications);

        if (applications.isEmpty()) {
            System.out.println("ℹ️ No applications found. You can import your seeded rows via option 5.");
        }

        UIHelper.catIntro(); // Display ASCII cat intro animation

        // 🐾 Cute ASCII header
        System.out.println("            ┌──────┐       ／l、");
        System.out.println("            │  🖥️   │     （ﾟ､ ｡７   ~ meow! keeping tabs on your career ~");
        System.out.println("            └──────┘      l、 ~ヽ     keep applying, you got this! 🐾");
        System.out.println("              (--)        じしf_, )ノ\n");

        // 💾 Show how many backups exist
        File[] backups = new File(".").listFiles((d, n) -> n.endsWith(".bak"));
        int backupCount = (backups == null) ? 0 : backups.length;
        System.out.printf(UIHelper.MINT + "💾 %d backups found%n" + UIHelper.RESET, backupCount);

        new MenuController(applications).start();
    }
}

// 🌸 End of JobApplicationTracker.java — Main entry point for the modular CLI
