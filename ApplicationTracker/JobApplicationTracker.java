import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class JobApplication {
    String company, role, type, location, status, source;
    LocalDate dateApplied;

    public JobApplication(String company, String role, String type, String location,
                          String status, String dateApplied, String source) {
        this.company   = company.trim();
        this.role      = role.trim();
        this.type      = type.trim();
        this.location  = location.trim();
        this.status    = status.trim();
        this.source    = source.trim();
        // Accept YYYY-MM-DD or YYYY/MM/DD
        this.dateApplied = LocalDate.parse(dateApplied.trim().replace("/", "-"));
    }

    // file format (7 fields, | delimited)
    public String toFileLine() {
        return String.join("|",
                company, role, type, location, status, dateApplied.toString(), source);
    }

    public static JobApplication fromFileLine(String line) {
        if (line == null) return null;
        String raw = line.trim();
        if (raw.isEmpty() || raw.startsWith("#")) return null;
        String[] p = raw.split("\\|", -1);
        if (p.length != 7) return null;
        return new JobApplication(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    public String toMarkdownRow() {
        return String.format("| %s | %s | %s | %s | %s | %s | %s |",
                company, role, type, location, status, dateApplied, source);
    }
}

public class JobApplicationTracker {
    private static final String FILE   = "applications.txt";
    private static final String README = "README.md";
    private static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // If applications.txt is empty/missing, we offer to seed from this Markdown block.
    // 👉 Paste your *full* 100-row Markdown table rows (header not needed) into SEED_MARKDOWN below.
    //    You can leave it as-is and import later via menu option 5.
    private static final String SEED_MARKDOWN =
        // ---- PASTE ONLY MARKDOWN ROWS BELOW (no header line). Examples included. ----
        "| [MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers) | Show Ambassador (Weekends Only) | Retail / Customer Service | Chicago, IL | Applied | 2025-10-15 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cheesemonger / Salumi & Formaggi Artisan | Culinary / Retail | Chicago, IL | Applied | 2025-10-15 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cashier / Front End Associate – Seasonal | Retail / Service | Chicago, IL | Applied | 2025-10-15 | LinkedIn |\n" +
        "| [Comfort Dental Fairwood](https://www.comfortdental.com/) | Office Assistant | Admin / Office Support | Chicago, IL | Applied | 2025-10-15 | ZipRecruiter |\n" +
        "| [RealtyAds](https://www.realtyads.com/careers) | Junior Software Engineer | Software | Chicago, IL | Applied | 2025-09-05 | LinkedIn |\n" +
        "| [University of Chicago – Harris School of Public Policy](https://harris.uchicago.edu/) | Harris Social Impact Fellowship | Policy / Research | Chicago, IL | Applied | 2025-09-05 | LinkedIn |\n" +
        "| [SPAATECH, Inc](https://www.spaatech.com/) | Business Development Representative – Technology Solutions | Business / IT | Chicago, IL | Applied | 2025-09-05 | LinkedIn |\n" +
        "| [METTLER TOLEDO](https://www.mt.com/us/en/home/careers.html) | Software Engineer | Software | Changzhou Shi, China | Applied | 2025-08-07 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Entry Level Software Engineer | Software | Chicago, IL | Applied | 2025-08-07 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Data Analyst | Data | Chicago, IL | Applied | 2025-08-07 | LinkedIn |\n";
        
        
        
        
        
        // ---- Add the rest of your 100 rows above (copy from your tracker). ----

    private static final List<JobApplication> applications = new ArrayList<>();

    public static void main(String[] args) {
        loadApplications();                 // load existing persistent data
        if (applications.isEmpty()) {
            System.out.println("ℹ️ No applications found. You can import your 100-row Markdown via option 5.");
        }
        showMenu();
    }

    private static void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Job Application Tracker ===");
            System.out.println("1. View summary");
            System.out.println("2. Add new application");
            System.out.println("3. Export README (stats + sorted table)");
            System.out.println("4. Add multiple from shell (instructions)");
            System.out.println("5. Import from embedded Markdown seed (once)");
            System.out.println("6. Exit");
            System.out.print("> ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter 1–6.");
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
                    saveApplications();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ===== Add single =====
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
        System.out.print("Date Applied (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Source: ");
        String source = sc.nextLine();

        try {
            JobApplication app = new JobApplication(company, role, type, location, status, date, source);
            applications.add(app);
            saveApplications(); // persist immediately
            System.out.println("✅ Added & saved.");
        } catch (Exception ex) {
            System.out.println("❌ Could not add application: " + ex.getMessage());
        }
    }

    // ===== Bulk import from embedded Markdown =====
    // Accepts rows like:
    // | [Company](https://link) | Role | Type | Location | Status | 2025-10-15 | LinkedIn |
    
    
    
    private static int importFromSeedMarkdown(String md) {
        if (md == null || md.trim().isEmpty()) {
            System.out.println("Seed is empty. Edit SEED_MARKDOWN in the source file to paste your 100 rows.");
            return 0;
        }
        int before = applications.size();
        String[] lines = md.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("|")) continue; // skip non-rows
            // strip leading/trailing pipe and split
            String row = trimmed.substring(1, trimmed.endsWith("|") ? trimmed.length()-1 : trimmed.length());
            String[] cols = row.split("\\|", -1);
            if (cols.length != 7) continue;
            // trim each col
            for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();
            // create
            try {
                JobApplication a = new JobApplication(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5], cols[6]);
                // avoid exact duplicate (company+role+date)
                boolean dup = applications.stream().anyMatch(
                        x -> x.company.equals(a.company) && x.role.equals(a.role) && x.dateApplied.equals(a.dateApplied)
                );
                if (!dup) applications.add(a);
            } catch (Exception ignore) {
                // skip malformed lines
            }
        }
        return applications.size() - before;
    }

    // ===== Load/Save =====
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
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (JobApplication a : applications) pw.println(a.toFileLine());
        } catch (IOException e) {
            System.out.println("Failed to save " + FILE + ": " + e.getMessage());
        }
    }

    // ===== Summary =====
    private static void showSummary() {
        int total = applications.size();
        long rejected = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("reject") || s.contains("not selected")).count();
        long hired = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("hired")).count();
        long interviews = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("interview")).count();
        long active = total - rejected - hired;

        System.out.printf("\n📊 Total: %d | Active: %d | Rejected: %d | Interviews: %d | Hired: %d\n",
                total, active, rejected, interviews, hired);

        Map<String, Long> byType = applications.stream()
                .collect(Collectors.groupingBy(a -> a.type, TreeMap::new, Collectors.counting()));
        System.out.println("\n💼 Breakdown by Type:");
        for (Map.Entry<String, Long> e : byType.entrySet()) {
            System.out.printf("- %s: %d\n", e.getKey(), e.getValue());
        }
    }

    // ===== Export README =====
    private static void exportMarkdown() throws IOException {
        // newest → oldest
        applications.sort(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed());

        // stats
        int total = applications.size();
        long rejected = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("reject") || s.contains("not selected")).count();
        long hired = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("hired")).count();
        long interviews = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("interview")).count();
        long active = total - rejected - hired;

        StringBuilder md = new StringBuilder();
        md.append("# 🗂️ Job Application Tracker — Lilyana Patamia\n\n");
        md.append("A comprehensive record of my job applications, interviews, and outcomes across IT, Data, and Software Engineering roles.\n");
        md.append("Includes LinkedIn, Indeed, and recruiter-based submissions.\n\n");
        md.append(String.format("- **Total applications:** %d\n", total));
        md.append(String.format("- **Active / pending:** %d\n", active));
        md.append(String.format("- **Rejected:** %d\n", rejected));
        md.append(String.format("- **Interviewed:** %d\n", interviews));
        md.append(String.format("- **Hired:** %d\n", hired));
        md.append(String.format("- **Last updated:** %s\n\n", LocalDate.now().format(HUMAN)));

        md.append("## 📋 Master Application Log\n\n");
        md.append("| Company | Role | Type | Location | Status | Date Applied | Source |\n");
        md.append("|----------|------|------|-----------|----------|---------------|---------|\n");
        for (JobApplication a : applications) {
            md.append(a.toMarkdownRow()).append("\n");
        }

        try (FileWriter w = new FileWriter(README)) {
            w.write(md.toString());
        }
    }

    // ===== Help =====
    private static void printEchoInstructions() {
        System.out.println("\nYou can append from the shell like this (outside the program):\n");
        System.out.println("echo \"[Eataly](https://www.eataly.com/us_en/)|Cashier / Front End Associate – Seasonal|Retail / Service|Chicago, IL|Applied|2025-10-15|LinkedIn\" >> applications.txt");
        System.out.println("echo \"[MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers)|Show Ambassador (Weekends Only)|Retail / Customer Service|Chicago, IL|Applied|2025-10-15|LinkedIn\" >> applications.txt");
        System.out.println("\nThen re-run option 3 to export an updated README.");
    }
}
