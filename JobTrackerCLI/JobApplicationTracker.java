import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

public class JobApplicationTracker {
    private static final String FILE   = "applications.txt";
    private static final String README = "README.md";
    private static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // 🎨 Color constants
    private static final String RESET  = "\u001B[0m";
    private static final String BLACK  = "\u001B[30m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN   = "\u001B[36m";
    private static final String WHITE  = "\u001B[37m";

    // Bright / custom
    private static final String ORANGE = "\u001B[38;2;255;165;0m";
    private static final String PEACH  = "\u001B[38;2;255;200;150m";
    private static final String PINK   = "\u001B[38;2;255;105;180m";
    private static final String BRIGHT_ORANGE = "\u001B[38;2;255;200;60m";    // lighter bright orange-gold
    private static final String LAVENDER      = "\u001B[38;2;200;160;255m";   // soft purple
    private static final String MINT  = "\u001B[38;2;152;255;204m"; // soft mint green
    private static final String TEAL  = "\u001B[38;2;0;191;188m";   // calm teal blue

    // Import seed data from separate file
    private static final String SEED_MARKDOWN = SeedData.SEED_MARKDOWN;

    private static final List<JobApplication> applications = new ArrayList<>();

    public static void main(String[] args) {
        loadApplications();
        if (applications.isEmpty()) {
            System.out.println("ℹ️ No applications found. You can import your seeded rows via option 5.");
        }
        catIntro();
        showMenu();
    }

    private static void showMenu() {
        Scanner sc = new Scanner(System.in);

        // Colors

        System.out.println("            ┌──────┐       ／l、                                                          ");                                               
        System.out.println("            │  🖥️   │     （ﾟ､ ｡７   ~ meow! keeping tabs on your career ~                 ");
        System.out.println("            └──────┘      l、 ~ヽ     keep applying, you got this! 🐾                     ");
        System.out.println("              (--)        じしf_, )ノ                                                     ");
        System.out.println("───────────────────────────────────────────────────────────────────────────────────────────");

        while (true) {
            System.out.println(BLUE +
            "╭─────────────────────────────────────────────────────────────────────────────────────────╮\n" +
            "│                                 JOB APPLICATION TRACKER                                 │\n" +
            "╰─────────────────────────────────────────────────────────────────────────────────────────╯" );

            // Two-column layout
            String leftCol[] = {
                "📊  1. View Summary",
                "📝  2. Add New Application",
                "📤  3. Export README",
                "➕  4. Batch-Add Multiple from Shell"
            };

            String rightCol[] = {
                " 📥  5. Import Seed Dataset",
                " ✏️   6. Update Status",
                " 🔍  7. Search Applications by Keywords",
                "🚪  8. Save and Close Tracker"
            };
            // Print both columns side by side
            for (int i = 0; i < leftCol.length; i++) {
                System.out.printf("  %-45s %s%n", leftCol[i], rightCol[i]);
            }
            System.out.println("═".repeat(91) + RESET);

            System.out.print("> ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter 1–8.");
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
                        System.out.println("✅ Exported " + README);
                    } catch (IOException e) {
                        System.out.println("Export failed: " + e.getMessage());
                    }
                    break;
                case 4:
                    printEchoInstructions();
                    break;
                case 5:
                    int added = importFromSeedMarkdown(SEED_MARKDOWN);
                    saveApplications();
                    System.out.println("✅ Imported " + added + " applications from seed.");
                    break;
                case 6:
                    updateStatus(sc);
                    break;
                case 7:
                    searchApplications(sc);
                    break;
                case 8:
                    saveApplications();
                    System.out.println(CYAN + "ฅ^•ﻌ•^ฅ Bye-bye human! Career cat curls up for a nap. 💤");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

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
            saveApplications();
            System.out.println("✅ Added & saved.");
        } catch (Exception ex) {
            System.out.println("❌ Could not add application: " + ex.getMessage());
        }
    }

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

    private static void catIntro() {

        String pad = " ".repeat(35);
        String[] frames = {
            CYAN + pad + "  ／l、\n" +
            pad + "（=‐ ω ‐=） zzz...\n" +
            pad + "  じしf_, )ノ" + RESET,

            CYAN + pad + "  ／l、\n" +
            pad + "（=・ω・=） blink blink\n" +
            pad + "  じしf_, )ノ" + RESET,

            CYAN + pad + "  ／l、\n" +
            pad + "（=｀ω´ =） ready to work!\n" +
            pad + "  じしf_, )ノ" + RESET
        };

        for (String frame : frames) {
            // Move cursor up and erase previous 3 lines before drawing the next frame
            System.out.print("\r\033[3A\033[J");
            System.out.println(frame);
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        }

        // Clean up final cat before menu appears
        System.out.print("\r\033[3A\033[J");
        System.out.println(PINK +
        "\n╭─────────────────────────────────────────────────────────────────────────────────────────╮\n" +
        "│                        🐾 Meow! Time to check your job hunt 💼                          │\n" +
        "╰─────────────────────────────────────────────────────────────────────────────────────────╯" +
        RESET);
    }

    private static void loadApplications() {
        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println("No existing file found, starting fresh.");
            return;
        }
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                JobApplication app = JobApplication.fromFileLine(line);
                if (app != null) {
                    applications.add(app);
                    count++;
                }
            }
            System.out.println("📂 Loaded " + count + " applications.");
        } catch (IOException e) {
            System.out.println("Failed to load " + FILE + ": " + e.getMessage());
        }
    }

    private static void saveApplications() {
        // 🔒 Safety: Create a timestamped backup before overwriting
        File original = new File(FILE);
        if (original.exists()) {
            String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            File backup = new File(FILE.replace(".txt", "_" + timestamp + ".bak"));
            if (backup.exists()) backup.delete(); // replace same-day backup
            boolean renamed = original.renameTo(backup);
            if (renamed) {
                System.out.println("📦 Backup created: " + backup.getName());
            } else {
                System.out.println("⚠️ Warning: Could not create backup file.");
            }
        }

        // 📝 Write all applications to the main file
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (JobApplication a : applications) {
                pw.println(a.toFileLine());
            }
            System.out.printf("💾 Saved %d applications to %s at %s%n",
                    applications.size(), FILE,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        } catch (IOException e) {
            System.out.println("❌ Failed to save " + FILE + ": " + e.getMessage());
        }
        
        // 🧹 Cleanup old backups (keep only 5 most recent)
        cleanupOldBackups();
    }

    // --- Optional cleanup: keep only 5 most recent backups ---
    private static void cleanupOldBackups() {
        File dir = new File("."); // current working directory
        File[] backups = dir.listFiles((d, name) -> name.startsWith("applications_") && name.endsWith(".bak"));
        if (backups == null || backups.length <= 5) return;

        // Sort backups by last modified date (oldest first)
        Arrays.sort(backups, Comparator.comparingLong(File::lastModified));

        int filesToDelete = backups.length - 5;
        for (int i = 0; i < filesToDelete; i++) {
            if (backups[i].delete()) {
                System.out.println("🧹 Deleted old backup: " + backups[i].getName());
            } else {
                System.out.println("⚠️ Could not delete: " + backups[i].getName());
            }
        }
    }

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

        System.out.println("\n🔍 Found " + results.size() + " match" + (results.size() == 1 ? "" : "es") + ":");
        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < results.size(); i++) {
            JobApplication a = results.get(i);
            System.out.printf("%2d. %-35s | %-25s | %-12s | %s\n",
                    i + 1, a.company, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
        System.out.println("-------------------------------------------------------------");

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

        System.out.println("\nDetails:");
        System.out.println("Company:   " + a.company);
        System.out.println("Role:      " + a.role);
        System.out.println("Type:      " + a.type);
        System.out.println("Location:  " + a.location);
        System.out.println("Status:    " + a.status);
        System.out.println("Applied:   " + a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("Source:    " + a.source);
        System.out.println("Notes:     " + (a.notes == null || a.notes.isEmpty() ? "—" : a.notes));

        System.out.print("\n✏️  Update status? (leave blank to skip): ");
        String newStatus = sc.nextLine().trim();
        if (!newStatus.isEmpty()) {
            a.status = newStatus;
            saveApplications();
            System.out.println("✅ Status updated and saved.");

            try {
                exportMarkdown();
                System.out.println("✅ README automatically updated after status change.");
            } catch (IOException e) {
                System.out.println("⚠️ Could not update README: " + e.getMessage());
            }
        }

        // 📝 Option to update notes after viewing details
        System.out.print("\n📝 Update notes? (leave blank to skip): ");
        String newNotes = sc.nextLine().trim();
        if (!newNotes.isEmpty()) {
            a.setNotes(newNotes);
            saveApplications();
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

    private static void updateStatus(Scanner sc) {
        if (applications.isEmpty()) {
            System.out.println("No applications to update.");
            return;
        }

        System.out.print("Enter part of the company or role name to search: ");
        String query = sc.nextLine().trim().toLowerCase();

        // Find matches
        List<JobApplication> matches = new ArrayList<>();
        for (JobApplication a : applications) {
            if (a.company.toLowerCase().contains(query) || a.role.toLowerCase().contains(query)) {
                matches.add(a);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No matching applications found.");
            return;
        }

        // Display matches
        System.out.println("\nMatches found:");
        for (int i = 0; i < matches.size(); i++) {
            JobApplication a = matches.get(i);
            System.out.printf("%d. %s — %s (%s) [%s]\n",
                    i + 1, a.company, a.role, a.location, a.status);
        }

        System.out.print("\nEnter the number of the job to update: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
            if (choice < 1 || choice > matches.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        JobApplication selected = matches.get(choice - 1);
        System.out.printf("Current status for %s — %s: %s\n",
                selected.company, selected.role, selected.status);
        System.out.print("Enter new status (e.g. Rejected, Interviewed, Offer, Hired, Applied (closed)): ");
        String newStatus = sc.nextLine().trim();

        if (newStatus.isEmpty()) {
            System.out.println("No status entered. Cancelled.");
            return;
        }

        selected.status = newStatus;
        saveApplications();
        System.out.println("✅ Status updated and saved! 🐱✨ (your career cat approves!)");

        try { exportMarkdown();
        System.out.println("✅ README automatically updated after status change.");
        } catch (IOException e) {
            System.out.println("⚠️ Could not auto-export README: " + e.getMessage());
        }
    }

private static void showSummary() {

    int total = applications.size();
    long rejected = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("reject") || s.contains("not selected")).count();
    long hired = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("hired") || s.contains("offer")).count();
    long interviews = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("interview")).count();
    long closed = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("closed")).count();
    long active = total - rejected - hired - closed;

    // ⏳ Likely inactive (applied > 60 days ago and still "Applied")
    long stale = applications.stream()
            .filter(a -> a.status.toLowerCase().contains("applied"))
            .filter(a -> java.time.temporal.ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()) > 60)
            .count();

    long trulyActive = active - stale;
    if (trulyActive < 0) trulyActive = 0;
    
    double successRate = total == 0 ? 0 : (double) (hired + interviews) / total * 100;

    // 🐾 Top section with colors
    System.out.println(LAVENDER + "╔═════════════════════════════════════════════════════════════════════════════════════════╗");
    System.out.println("║                               🌸  APPLICATION SUMMARY  🌸                               ║");
    System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝" + RESET);

    System.out.printf(
        "  %sTotal:%s %-3d  %sActive:%s %-3d  %sLikely Inactive:%s %-3d  %sRejected:%s %-3d  %sInterviews:%s %-3d  %sHired:%s %-3d%n",
            CYAN, RESET, total,
            GREEN, RESET, trulyActive,
            ORANGE, RESET, stale,
            RED, RESET, rejected,
            YELLOW, RESET, interviews,
            PINK, RESET, hired);
    System.out.println(LAVENDER + "-".repeat(91) + RESET);
    
    // Success rate and closed count
    int barLength = 30;
    int filled = (int) (barLength * successRate / 100);
    String bar = "█".repeat(filled) + "░".repeat(barLength - filled);
    System.out.println();
    System.out.printf("%s📈 Success Rate:%s %.1f%% %s%s%s%n", ORANGE, RESET, successRate, GREEN, bar, RESET);
    System.out.printf("%s📦 Closed:%s %d%n", CYAN, RESET, closed);
    String topSource = applications.stream()
        .collect(Collectors.groupingBy(a -> a.source, Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Unknown");
    System.out.printf("%s🌐 Top Source:%s %s%n", CYAN, RESET, topSource);
    long daysSpan = ChronoUnit.DAYS.between(
        applications.stream().map(a -> a.dateApplied).min(LocalDate::compareTo).orElse(LocalDate.now()),
        LocalDate.now());
    double perWeek = total / Math.max(daysSpan / 7.0, 1.0);
    System.out.printf("%s⚡ Avg Applications per Week:%s %.1f%n", GREEN, RESET, perWeek);
    long openApps = applications.stream()
        .filter(a -> a.status.equalsIgnoreCase("applied"))
        .count();
    System.out.printf("%s🕐 Still Waiting (Applied Only):%s %d%n", CYAN, RESET, openApps);
    String topLocation = applications.stream()
        .collect(Collectors.groupingBy(a -> a.location, Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Unknown");
    System.out.printf("%s📍 Top Location:%s %s%n", GREEN, RESET, topLocation);
    JobApplication latest = applications.stream()
        .max(Comparator.comparing(a -> a.dateApplied))
        .orElse(null);
    if (latest != null)
        System.out.printf("%s🆕 Most Recent:%s %s — %s (%s)%n",
            PINK, RESET, latest.company, latest.role,
            latest.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

    // Breakdown
    // 💼 Breakdown by type
    Map<String, Long> byType = applications.stream()
        .collect(Collectors.groupingBy(a -> simplifyBroadCategory(a.type), TreeMap::new, Collectors.counting()));

    System.out.println(LAVENDER + "-".repeat(91));
    System.out.println("                                    Breakdown by Type:");
    System.out.println("-".repeat(91));

    // Sort from largest → smallest
    List<Map.Entry<String, Long>> entries = new ArrayList<>(byType.entrySet());
    entries.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

    int colWidth = 37; // fits nicely in 91-char width terminals

    for (int i = 0; i < entries.size(); i += 2) {
        String left = String.format("• %-"+colWidth+"s %3d", 
            entries.get(i).getKey(), entries.get(i).getValue());

        String right = (i + 1 < entries.size())
            ? String.format("   • %-"+colWidth+"s %3d", 
                entries.get(i + 1).getKey(), entries.get(i + 1).getValue())
            : "";

        System.out.println(CYAN + left + right + RESET);
    }
    System.out.print(LAVENDER + "-".repeat(91));
    System.out.printf(MINT + "\n📬 %d applications logged — %d active, %.1f%% showing progress.%n" + RESET, 
        total, trulyActive, successRate);
    System.out.println(TEAL + "🐾 Career Cat: you’re doing great — keep applying!" + RESET);
    
    // 💫 Recently applied stats (within the last 7 days)
    long recent = applications.stream()
        .filter(a -> ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()) <= 7)
        .count();

    if (recent > 0) {
        System.out.printf(MINT + "🕊️  You’ve applied to %d job%s in the last 7 days.%n" + RESET,
            recent, recent == 1 ? "" : "s");
    } else {
        System.out.println(ORANGE + " 🌼 No new applications this week — time to find a few more leads!" + RESET);
    }

}


private static void exportMarkdown() throws IOException {
    applications.sort(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed());

    int total = applications.size();
    long rejected = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("reject") || s.contains("not selected")).count();
    long hired = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("hired") || s.contains("offer")).count();
    long interviews = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("interview")).count();
    long closed = applications.stream().map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("closed")).count();

    long active = total - rejected - hired - closed;

    // --- Likely inactive applications (applied > 60 days ago and still marked Applied) ---
    long stale = applications.stream()
        .filter(a -> a.status.toLowerCase().contains("applied"))
        .filter(a -> ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()) > 60)
        .count();

    // Adjust active to exclude likely inactive ones
    long trulyActive = active - stale;
    if (trulyActive < 0) trulyActive = 0; // safety clamp\
    
    double successRate = total == 0 ? 0 : (double) (hired + interviews) / total * 100;

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
        total, trulyActive, stale, interviews, interviews == 1 ? "" : "s"
    ));

    // --- APPLICATION OVERVIEW ---
    md.append("<div align=\"center\">\n");
    md.append("  <h2>📊 Application Overview</h2>\n");
    md.append("</div>\n\n");    
    md.append("<table align=\"center\">\n");
    md.append("<tr>\n");
    md.append("<td align=\"left\" width=\"50%\">\n\n");
    md.append("- **Total Applications:** ").append(total).append("  \n");
    md.append("- 🕐 **Active / Pending:** ").append(trulyActive).append("  \n");
    md.append("- ⏳ **Likely Inactive:** ").append(stale).append("  \n");
    md.append("- ❌ **Rejected:** ").append(rejected).append("  \n\n");
    md.append("</td>\n");
    md.append("<td align=\"left\" width=\"50%\">\n\n");
    md.append("- 💬 **Interviewed:** ").append(interviews).append("  \n");
    md.append("- ✅ **Hired / Offer:** ").append(hired).append("  \n");
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

    String topSource = applications.stream()
        .collect(Collectors.groupingBy(a -> a.source, Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Unknown");

    long daysSpan = ChronoUnit.DAYS.between(
        applications.stream().map(a -> a.dateApplied).min(LocalDate::compareTo).orElse(LocalDate.now()),
        LocalDate.now());
    double perWeek = total / Math.max(daysSpan / 7.0, 1.0);

    long openApps = applications.stream()
        .filter(a -> a.status.equalsIgnoreCase("applied"))
        .count();

    String topLocation = applications.stream()
        .collect(Collectors.groupingBy(a -> a.location, Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Unknown");

    JobApplication latest = applications.stream()
        .max(Comparator.comparing(a -> a.dateApplied))
        .orElse(null);

    // 🪄 Append stats to Markdown
    md.append("<table align=\"center\"><tr><td align=\"left\">\n\n");
    md.append(String.format("- 📈 **Success Rate:** %.1f%%  \n", successRate));
    md.append(String.format("- 📦 **Closed:** %d  \n", closed));
    md.append(String.format("- 🌐 **Top Source:** %s  \n", topSource));
    md.append(String.format("- ⚡ **Avg Applications per Week:** %.1f  \n", perWeek));
    md.append(String.format("- 🕐 **Still Waiting (Applied Only):** %d  \n", openApps));
    md.append(String.format("- 📍 **Top Location:** %s  \n", topLocation));
    md.append(String.format("- 📆 **Avg Days Since Application:** %d days  \n", avgDays));
    if (latest != null)
        md.append(String.format("- 🆕 **Most Recent:** %s — %s (%s)  \n",
            latest.company, latest.role,
            latest.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
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
        total, trulyActive, stale, rejected, interviews, hired
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
            total, active, rejected);
}

    private static void printEchoInstructions() {
        System.out.println("\n💡 To append new jobs from the shell (outside the program):\n");
        System.out.println("echo \"[Eataly](https://www.eataly.com/us_en/)|Cashier / Front End Associate – Seasonal|Retail / Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("echo \"[MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers)|Show Ambassador (Weekends Only)|Retail / Customer Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("\nThen run option 3 in the tracker menu to regenerate the README.\n");
    }

    // --- Simplify subtypes into broader job categories ---
    private static String simplifyBroadCategory(String rawType) {
        String t = rawType.toLowerCase();

        if (t.contains("software") || t.contains("developer") || t.contains("engineer"))
            return "Software / Development";
        if (t.contains("it") || t.contains("support") || t.contains("infrastructure"))
            return "IT & Tech Support";
        if (t.contains("data") || t.contains("ai") || t.contains("analytics") || t.contains("machine learning"))
            return "Data & AI";
        if (t.contains("admin") || t.contains("office") || t.contains("operations"))
            return "Administration & Operations";
        if (t.contains("business"))
            return "Business & Strategy";
        if (t.contains("finance") || t.contains("insurance"))
            return "Finance";
        if (t.contains("security") || t.contains("cyber"))
            return "Cybersecurity";
        if (t.contains("education") || t.contains("teaching"))
            return "Education";
        if (t.contains("animal"))
            return "Animal Care";
        if (t.contains("healthcare") || t.contains("medical") || t.contains("biotech"))
            return "Healthcare & Life Sciences";
        if (t.contains("policy") || t.contains("research"))
            return "Policy & Research";
        if (t.contains("retail") || t.contains("service") || t.contains("customer"))
            return "Retail & Service";
        if (t.contains("environmental") || t.contains("energy") || t.contains("gis"))
            return "Environmental & Sustainability";
        if (t.contains("marketing") || t.contains("communications"))
            return "Marketing & Communications";
        if (t.contains("nonprofit") || t.contains("community"))
            return "Nonprofit & Outreach";
        if (t.contains("culinary") || t.contains("food"))
            return "Culinary & Food Service";
        if (t.contains("arts") || t.contains("design"))
            return "Arts & Design";
        if (t.contains("product"))
            return "Product Management";
        if (t.contains("automation") || t.contains("robotics"))
            return "Automation & Robotics";
        if (t.contains("engineering"))
            return "Engineering";

        return "Other";
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
