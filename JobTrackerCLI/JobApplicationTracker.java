// 🌸 Job Application Tracker CLI — v3.0
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

/**
 * Main class for the Job Application Tracker CLI.
 * 
 * Handles loading, saving, displaying, and exporting job applications 
 * using color-coded console output and Markdown report generation.
 * 
 * ✅ Key Features:
 * - Persistent data storage via `applications.txt` with auto-backups.
 * - Enum-based status handling (ApplicationStatus) for type safety.
 * - Markdown README export with live analytics and category summaries.
 * - Interactive CLI menu for adding, searching, and updating applications.
 *
 * 💡 Design Notes:
 * - Uses Java Streams for concise filtering and analytics.
 * - Employs `ChronoUnit` for date calculations (e.g., recent apps, inactivity).
 * - Produces dynamic Markdown reports for GitHub portfolio integration.
 * - Built with Java 17 for cross-platform CLI compatibility.
 */

public class JobApplicationTracker {
    public static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    public static final String README = "README.md";

    // 📦 Preloaded Markdown seed data for first-time use
    public static final String SEED_MARKDOWN = SeedData.SEED_MARKDOWN;

    // 🧾 Central in-memory list storing all job applications
    public static final List<JobApplication> applications = new ArrayList<>();
    
    // 🚀 Entry point: loads existing data, shows ASCII intro, then launches the CLI
    public static void main(String[] args) {
        FileManager.loadApplications(applications);
        if (applications.isEmpty()) {
            System.out.println("ℹ️ No applications found. You can import your seeded rows via option 5.");
        }
        UIHelper.catIntro(); // Display ASCII cat intro animation
        showMenu(); // Launch interactive menu
    }

    // 🧭 Displays main interactive CLI menu for user actions
    private static void showMenu() {
        Scanner sc = new Scanner(System.in);

        // Colors

        System.out.println("            ┌──────┐       ／l、                                                          ");                                               
        System.out.println("            │  🖥️   │     （ﾟ､ ｡７   ~ meow! keeping tabs on your career ~                 ");
        System.out.println("            └──────┘      l、 ~ヽ     keep applying, you got this! 🐾                     ");
        System.out.println("              (--)        じしf_, )ノ                                                     ");
        System.out.println("───────────────────────────────────────────────────────────────────────────────────────────");

        while (true) {
            System.out.println(UIHelper.BLUE +
            "╭─────────────────────────────────────────────────────────────────────────────────────────╮\n" +
            "│                                 JOB APPLICATION TRACKER                                 │\n" +
            "╰─────────────────────────────────────────────────────────────────────────────────────────╯" );

            // Two-column layout
            String leftCol[] = {
                "📊  1. View Progress Summary",
                "📝  2. Add New Job Entry",
                "🗒️   3. Export Markdown Report",
                "➕  4. Quick-Add (Batch via Shell)",
                "📥  5. Import Sample Seed Dataset"

            };

            String rightCol[] = {
                " 🕒  6. View Recent Applications",
                " 🔍  7. Search & Manage (Edit Status/Notes)",
                "   🗑️   8. Remove Application",
                "📈  9. Visual Dashboard",
                " 🚪 10. Save and Close Tracker"
            };
            // Print both columns side by side (fix ensures option 9 appears)
            for (int i = 0; i < Math.max(leftCol.length, rightCol.length); i++) {
                String left  = i < leftCol.length ? leftCol[i] : "";
                String right = i < rightCol.length ? rightCol[i] : "";
                System.out.printf("  %-45s %s%n", left, right);
            }
            System.out.println("═".repeat(91) + UIHelper.RESET);


            System.out.print("> ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter 1–9.");
                sc.nextLine();
                continue;
            }
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1:
                    showSummary();
                    break;
                case 2:
                    addApplication(sc);
                    break;
                case 3:
                    try {
                        exportMarkdown();
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
                    int added = importFromSeedMarkdown(SEED_MARKDOWN);
                    FileManager.saveApplications(applications);
                    System.out.println("✅ Imported " + added + " applications from seed.");
                    break;
                case 6:
                    viewRecentApplications();
                    break;
                case 7:
                    searchApplications(sc);
                    break;
                case 8:
                    removeApplication(sc);
                    break;
                case 9:
                    showCareerDashboard();
                    break;
                case 10:
                    FileManager.saveApplications(applications);
                    System.out.println(UIHelper.CYAN + "ฅ^•ﻌ•^ฅ Bye-bye human! Career cat curls up for a nap. 💤");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // 🗑️ Remove an application (same search style as Search & Manage)
    private static void removeApplication(Scanner sc) {
        System.out.print("🔎 Enter keyword to search (company or role): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No keyword entered." + UIHelper.RESET);
            return;
        }

        // Same search logic as Search & Manage
        List<JobApplication> matches = applications.stream()
            .filter(a -> a.company.toLowerCase().contains(keyword)
                    || a.role.toLowerCase().contains(keyword))
            .sorted(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed())
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            System.out.println(UIHelper.RED + "❌ No matching jobs found for \"" + keyword + "\"." + UIHelper.RESET);
            return;
        }

        System.out.println("\n🔍 Found " + matches.size() + " match" + (matches.size() == 1 ? "" : "es") + ":");
        System.out.println("---------------------------------------------------------------------------------------");
        for (int i = 0; i < matches.size(); i++) {
            JobApplication a = matches.get(i);
            System.out.printf("%2d. %-35s | %-25s | %-12s | %s\n",
                    i + 1, a.company, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
        System.out.println("------------------------------------------------------------------------------------------");

        System.out.print("\n❓ Enter the number of the job to remove (or press Enter to cancel): ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "🕊️  Cancelled." + UIHelper.RESET);
            return;
        }

        int index;
        try {
            index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= matches.size()) {
                System.out.println(UIHelper.RED + "❌ Invalid selection." + UIHelper.RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(UIHelper.RED + "⚠️ Invalid input. Please enter a number." + UIHelper.RESET);
            return;
        }

        JobApplication toRemove = matches.get(index);
        applications.remove(toRemove);
        FileManager.saveApplications(applications);

        // Optional: auto-refresh exports
        try {
            exportMarkdown();
            FileManager.exportCSV(applications);
            System.out.println(UIHelper.MINT + "🧾 CSV and README updated after deletion." + UIHelper.RESET);
        } catch (IOException e) {
            System.out.println(UIHelper.ORANGE + "⚠️ Could not auto-update exports: " + e.getMessage() + UIHelper.RESET);
        }

        System.out.println(UIHelper.GREEN + "✅ Removed: " + toRemove.company + " — " + toRemove.role + UIHelper.RESET);
    }

    // ✍️ Add a new job application interactively via console prompts
    private static void addApplication(Scanner sc) {
        System.out.print("Company (Markdown link ok): ");
        String company = sc.nextLine();
        System.out.print("Role: ");
        String role = sc.nextLine();
        System.out.print("Type: ");
        String type = sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();
        System.out.print("Status: ");
        String status = sc.nextLine();
        System.out.print("Date Applied (MM/DD/YYYY or YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Source: ");
        String source = sc.nextLine();
        System.out.print("Notes (optional): ");
        String notes = sc.nextLine();

        try {
            JobApplication app = new JobApplication(company, role, type, location, status, date, source, notes);
            applications.add(app);
            FileManager.saveApplications(applications);
            System.out.println("✅ Added & saved.");
        } catch (Exception ex) {
            System.out.println("❌ Could not add application: " + ex.getMessage());
        }
    }

    // 📥 Import seed dataset (Markdown table rows) into the tracker
    private static int importFromSeedMarkdown(String md) {
        if (md == null || md.trim().isEmpty()) {
            System.out.println("Seed is empty.");
            return 0;
        }

        int before = applications.size();
        String[] lines = md.split("\\r?\\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("|")) continue;

            String row = trimmed.substring(1, trimmed.endsWith("|") ? trimmed.length() - 1 : trimmed.length());
            String[] cols = row.split("\\|", -1);
            if (cols.length < 7) continue;

            for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();
            String notes = cols.length >= 8 ? cols[7] : "";

            try {
                JobApplication a = new JobApplication(
                    cols[0], cols[1], cols[2], cols[3],
                    cols[4], cols[5], cols[6], notes
                );

                boolean dup = applications.stream().anyMatch(
                    x -> x.company.equals(a.company) &&
                        x.role.equals(a.role) &&
                        x.dateApplied.equals(a.dateApplied)
                );
                if (!dup) applications.add(a);

            } catch (Exception ignore) {}
        }

        return applications.size() - before;
    }
    
    // 🔎 Search and edit applications by company or role
    private static void searchApplications(Scanner sc) {
        System.out.print("🔎 Enter keyword to search (company or role): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("⚠️  No keyword entered.");
            return;
        }

        List<JobApplication> results = applications.stream()
                .filter(a -> a.company.toLowerCase().contains(keyword)
                        || a.role.toLowerCase().contains(keyword))
                .sorted(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed())
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("❌ No matching applications found for \"" + keyword + "\".");
            return;
        }

        System.out.println(UIHelper.GREEN +
        "\n╔═════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                      SEARCH RESULTS                                     ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝"
        + UIHelper.RESET);

        System.out.println("🔍 Found " + results.size() + " match" + (results.size() == 1 ? "" : "es") + ":");
        System.out.println("------------------------------------------------------------------------------------------");
        for (int i = 0; i < results.size(); i++) {
            JobApplication a = results.get(i);
            System.out.printf("%2d. %-35s | %-25s | %-12s | %s\n",
                    i + 1, a.company, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
        System.out.println("------------------------------------------------------------------------------------------");

        System.out.print("\n💡 View details or update status (enter number, or press Enter to skip): ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        int index;
        try {
            index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= results.size()) {
                System.out.println("Invalid number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        JobApplication a = results.get(index);

        System.out.println("\n──────────────────────────────────────────────────────────────");
        System.out.println("\nDetails:");
        System.out.println("Company:   " + a.company);
        System.out.println("Role:      " + a.role);
        System.out.println("Type:      " + a.type);
        System.out.println("Location:  " + a.location);
        System.out.println("Status:    " + a.status);
        System.out.println("Applied:   " + a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("Source:    " + a.source);
        System.out.println("Notes:     " + (a.notes == null || a.notes.isEmpty() ? "—" : a.notes));
        System.out.println("──────────────────────────────────────────────────────────────");

        // ⚙️ Quick Edit Section
        System.out.print("\n⚙️  Quick Edit this application? (y/n): ");
        String editChoice = sc.nextLine().trim().toLowerCase();

        if (editChoice.equals("y")) {
            System.out.print("✏️  New Status (leave blank to keep): ");
            String newStatus = sc.nextLine().trim();
            if (!newStatus.isEmpty()) {
                a.status = ApplicationStatus.from(newStatus);
            }

            System.out.print("📍 New Location (leave blank to keep): ");
            String newLocation = sc.nextLine().trim();
            if (!newLocation.isEmpty()) {
                a.setLocation(newLocation);
            }

            System.out.print("📝 New Notes (leave blank to keep): ");
            String newNotes = sc.nextLine().trim();
            if (!newNotes.isEmpty()) {
                a.setNotes(newNotes);
            }

            FileManager.saveApplications(applications);
            System.out.println("✅ Application updated and saved.");

            try {
                exportMarkdown();
                FileManager.exportCSV(applications);
                System.out.println("✅ README and CSV automatically updated.");
            } catch (IOException e) {
                System.out.println("⚠️ Could not update README or CSV: " + e.getMessage());
            }
        }

        // 📝 Option to update notes after viewing details
        System.out.print("\n📝 Update notes? (leave blank to skip): ");
        String newNotes = sc.nextLine().trim();
        if (!newNotes.isEmpty()) {
            a.setNotes(newNotes);
            FileManager.saveApplications(applications);
            System.out.println("✅ Notes updated and saved.");

            try {
                exportMarkdown();
                System.out.println("✅ README automatically updated after notes change.");
            } catch (IOException e) {
                System.out.println("⚠️ Could not update README: " + e.getMessage());
            }
        }

        System.out.print("\n📝 Export these search results to file? (y/n): ");
        String export = sc.nextLine().trim().toLowerCase();
        if (export.equals("y")) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("search_results.txt"))) {
                for (JobApplication j : results) pw.println(j.toFileLine());
                System.out.println("✅ Saved results to search_results.txt");
            } catch (IOException e) {
                System.out.println("⚠️ Failed to export results: " + e.getMessage());
            }
        }
    }

    // 🗂️ View recent (≤3-day) applications for quick progress tracking
    private static void viewRecentApplications() {
        if (applications.isEmpty()) {
            System.out.println("⚠️  No applications recorded yet.");
            return;
        }

        List<JobApplication> recent = applications.stream()
                .filter(a -> ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()) <= 3)
                .sorted(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed())
                .collect(Collectors.toList());

        System.out.println(UIHelper.LAVENDER + "\n╔═════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              🕒  RECENT APPLICATIONS (Last 3 Days)                      ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝" + UIHelper.RESET);

        if (recent.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "🌼 No new applications this week — time to find a few more leads!" + UIHelper.RESET);
            return;
        }

        for (JobApplication a : recent) {
            System.out.printf("%s%-35s%s | %-25s | %-12s | %s\n",
                    UIHelper.CYAN, a.company, UIHelper.RESET, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }

        System.out.printf(UIHelper.MINT + "\n🕊️  You’ve applied to %d job%s in the last 3 days.%n" + UIHelper.RESET,
                recent.size(), recent.size() == 1 ? "" : "s");
    }

    private static void showSummary() {
        if (applications.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No applications recorded yet." + UIHelper.RESET);
            return;
        }
        Stats s = Stats.compute(applications);
        s.printSummary();
    }

    // 🎨 Compact visual dashboard before exiting
    private static void showCareerDashboard() {
        if (applications.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No applications recorded yet." + UIHelper.RESET);
            return;
        }
        Stats s = Stats.compute(applications);
        s.printDashboard();
    }

    private static void exportMarkdown() throws IOException {
        applications.sort(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed());
        Stats s = Stats.compute(applications);
        String today = LocalDate.now().format(HUMAN);

        StringBuilder md = new StringBuilder();

        // --- HEADER ---
        md.append("<div align=\"center\">\n");
        md.append("  <h1>🗂️ Job Application Tracker</h1>\n");
        md.append("</div>\n\n");
        md.append("A living record of my 2025 job applications, interview progress, and outcomes across **IT**, **Data**, and **Software Engineering** roles — alongside opportunities in design, research, education, public health, and community-focused organizations.\n\n");
        md.append("> *Includes submissions from LinkedIn, Indeed, Handshake, and recruiter referrals.*\n\n");
        md.append("---\n\n");

        // --- HIGHLIGHTS ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>💡 Highlights</h2>\n");
        md.append("</div>\n\n");
        md.append(String.format(
            "So far, I've applied to **%d positions** across multiple industries. Currently, **%d applications remain active**, " +
            "and **%d likely inactive** (older than 60 days), with **%d interview%s** completed.  \n" +
            "Most applications came through LinkedIn and Handshake, spanning software, IT, and data roles.  \n" +
            "This tracker provides a transparent snapshot of growth, persistence, and progress through the 2025 job season.\n\n",
            s.total, s.trulyActive, s.stale, s.interviews, s.interviews == 1 ? "" : "s"
        ));

        // --- APPLICATION OVERVIEW ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>📊 Application Overview</h2>\n");
        md.append("</div>\n\n");
        md.append("<table align=\"center\">\n");
        md.append("<tr>\n");
        md.append("<td align=\"left\" width=\"50%\">\n\n");
        md.append("- **Total Applications:** ").append(s.total).append("  \n");
        md.append("- 🕐 **Active / Pending:** ").append(s.trulyActive).append("  \n");
        md.append("- ⏳ **Likely Inactive:** ").append(s.stale).append("  \n");
        md.append("- ❌ **Rejected:** ").append(s.rejected).append("  \n\n");
        md.append("</td>\n");
        md.append("<td align=\"left\" width=\"50%\">\n\n");
        md.append("- 💬 **Interviewed:** ").append(s.interviews).append("  \n");
        md.append("- ✅ **Hired / Offer:** ").append(s.hired).append("  \n");
        md.append("- 🗓️ **Last Updated:** ").append(today).append("  \n\n");
        md.append("</td>\n");
        md.append("</tr>\n");
        md.append("</table>\n\n");

        // --- ADDITIONAL INSIGHTS ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>📈 Additional Insights</h2>\n");
        md.append("</div>\n\n");

        // 🧮 Calculate extra stats
        long avgDays = (long) applications.stream()
            .mapToLong(a -> ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()))
            .average()
            .orElse(0);

        // 🪄 Append stats to Markdown
        md.append("<table align=\"center\"><tr><td align=\"left\">\n\n");
        md.append(String.format("- 📈 **Success Rate:** %.1f%%  \n", s.successRate));
        md.append(String.format("- 📦 **Closed:** %d  \n", s.closed));
        md.append(String.format("- 🌐 **Top Source:** %s  \n", s.topSource));
        md.append(String.format("- ⚡ **Avg Applications per Week:** %.1f  \n", s.perWeek));
        md.append(String.format("- 🕐 **Still Waiting (Applied Only):** %d  \n", s.openApps));
        md.append(String.format("- 📍 **Top Location:** %s  \n", s.topLocation));
        md.append(String.format("- 📆 **Avg Days Since Application:** %d days  \n", avgDays));
        if (s.latest != null)
            md.append(String.format("- 🆕 **Most Recent:** %s — %s (%s)  \n",
                s.latest.company, s.latest.role,
                s.latest.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
        md.append("</td></tr></table>\n\n");

        // --- CATEGORY SUMMARY ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>🧾 Breakdown by Job Type</h2>\n");
        md.append("</div>\n\n");

        Map<String, Long> byCategory = applications.stream()
            .collect(Collectors.groupingBy(a -> simplifyType(a.type), TreeMap::new, Collectors.counting()));

        List<Map.Entry<String, Long>> entries = new ArrayList<>(byCategory.entrySet());
        entries.sort((a, b) -> Long.compare(b.getValue(), a.getValue())); // biggest first

        int mid = (entries.size() + 1) / 2;
        md.append("<table align=\"center\"><tr><td valign='top' align='left'>\n\n");

        for (int i = 0; i < mid; i++) {
            Map.Entry<String, Long> e = entries.get(i);
            md.append(String.format("• **%s:** %d<br>\n", e.getKey(), e.getValue()));
        }

        md.append("</td><td valign='top' align='left'>\n\n");

        for (int i = mid; i < entries.size(); i++) {
            Map.Entry<String, Long> e = entries.get(i);
            md.append(String.format("• **%s:** %d<br>\n", e.getKey(), e.getValue()));
        }

        md.append("</td></tr></table>\n\n");

        // --- ABOUT THIS TRACKER SECTION ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>💻 About This Tracker</h2>\n");
        md.append("</div>\n\n");
        md.append("Built with **Java 17**, this app demonstrates file handling, date parsing, Markdown generation, and console-based UI design. ");
        md.append("It helps organize applications efficiently while serving as both a **career log** and a **personal software project**. ");
        md.append("The tracker calculates dynamic statistics, success rates, and updates this file in real-time.\n\n");

        // --- HOW TO USE ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>⚙️ How to Use</h2>\n");
        md.append("</div>\n\n");
        md.append("This CLI tool built in **Java 17** automatically stores job data in `applications.txt`, allowing you to:\n");
        md.append("1. Add new applications interactively\n");
        md.append("2. Import a pre-seeded dataset (option 5)\n");
        md.append("3. Search, update, and export to this Markdown report (option 3)\n");
        md.append("4. Generate timestamped backups each time the file is saved\n\n");
        md.append("To refresh this README, run **Option 3: Export README** from the main menu.\n\n");

        // --- MASTER LOG ---
        md.append("<div align=\"center\">\n");
        md.append("  <h2>📋 Master Application Log</h2>\n");
        md.append("</div>\n\n");
        md.append("<details>\n<summary>Click to expand full job application list</summary>\n\n");
        md.append("| Company | Role | Type | Location | Status | Date Applied | Source | Notes |\n");
        md.append("|----------|------|------|-----------|----------|---------------|---------|--------|\n");

        for (JobApplication a : applications) {
            md.append(a.toMarkdownRow()).append("\n");
        }

        md.append("\n</details>\n\n");
        md.append(String.format(
            "**Summary:** 📋 %d total — 🕐 %d active — ⏳ %d likely inactive — ❌ %d rejected — 💬 %d interviews — ✅ %d hired.**\n\n",
            s.total, s.trulyActive, s.stale, s.rejected, s.interviews, s.hired
        ));

        md.append("---\n");
        md.append("🌸 *Maintained by lpatamia1 — powered by the Java Job Application Tracker.*\n");
        md.append("*Last updated ").append(today).append(".*\n");

        // --- WRITE FILE ---
        File output = new File(README);
        try (FileWriter w = new FileWriter(output)) {
            w.write(md.toString());
        }

        System.out.printf("📁 Writing README to: %s%n", output.getAbsolutePath());
        System.out.printf("📝 Markdown length: %d characters (%d lines)%n",
                md.length(), md.toString().split("\n").length);
        System.out.printf("✅ README updated successfully with %d jobs (%d active, %d rejected).%n",
                s.total, s.trulyActive, s.rejected);
    }

    // --- Helper function for simplifying job types ---
    private static String simplifyType(String rawType) {
        String t = rawType.toLowerCase();

        if (t.contains("software")) return "Software / Development";
        if (t.contains("ai") || t.contains("machine learning")) return "AI / Data Science";
        if (t.contains("data")) return "Data / Analytics";
        if (t.contains("it")) return "IT / Support";
        if (t.contains("cloud")) return "Cloud / Infrastructure";
        if (t.contains("admin")) return "Administration";
        if (t.contains("business")) return "Business / Operations";
        if (t.contains("finance") || t.contains("insurance")) return "Finance";
        if (t.contains("security")) return "Cybersecurity / Infrastructure";
        if (t.contains("education")) return "Education / Training";
        if (t.contains("animal")) return "Animal Care";
        if (t.contains("healthcare") || t.contains("medical") || t.contains("biotech")) return "Healthcare / Life Sciences";
        if (t.contains("policy") || t.contains("research")) return "Policy / Research";
        if (t.contains("retail") || t.contains("service") || t.contains("customer")) return "Retail / Service";
        if (t.contains("environmental") || t.contains("energy")) return "Environmental / Sustainability";
        if (t.contains("marketing") || t.contains("communications")) return "Marketing / Communications";
        if (t.contains("nonprofit")) return "Nonprofit / Community";
        if (t.contains("engineering")) return "Engineering";
        if (t.contains("culinary") || t.contains("food")) return "Culinary / Food Service";
        if (t.contains("arts")) return "Arts / Creative";
        if (t.contains("product")) return "Product / Design";
        if (t.contains("automation")) return "Automation / Robotics";
        if (t.contains("gis")) return "GIS / Mapping";

        return "Other";
    }


}
