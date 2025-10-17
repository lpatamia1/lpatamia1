import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

class JobApplication {
    String company, role, type, location, status, source;
    LocalDate dateApplied;

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MD  = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MD_DASH  = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public JobApplication(String company, String role, String type, String location,
                          String status, String dateApplied, String source) {
        this.company   = company.trim();
        this.role      = role.trim();
        this.type      = type.trim();
        this.location  = location.trim();
        this.status    = status.trim();
        this.source    = source.trim();
        this.dateApplied = parseFlexible(dateApplied.trim());
    }

    private static LocalDate parseFlexible(String s) {
        String t = s.trim();
        // Try common formats in order: MM/dd/yyyy, yyyy-MM-dd, MM-dd-yyyy
        try { return LocalDate.parse(t, MD); }         catch (DateTimeParseException ignore) {}
        try { return LocalDate.parse(t, ISO); }        catch (DateTimeParseException ignore) {}
        try { return LocalDate.parse(t, MD_DASH); }    catch (DateTimeParseException ignore) {}
        // As a last resort, try replacing `/` with `-` and parse ISO if it happens to be yyyy/MM/dd
        try { return LocalDate.parse(t.replace('/', '-'), ISO); } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Unrecognized date format: " + s + " (expected MM/dd/yyyy or yyyy-MM-dd)");
        }
    }

    // file format (7 fields, | delimited)
    public String toFileLine() {
        return String.join("|",
                company, role, type, location, status, dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), source);
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
        // Ensure README dates use Markdown MM/DD/YYYY style
        return String.format("| %s | %s | %s | %s | %s | %s | %s |",
                company, role, type, location, status, dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), source);
    }
}

public class JobApplicationTracker {
    private static final String FILE   = "applications.txt";
    private static final String README = "README.md";
    private static final DateTimeFormatter HUMAN = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // SEED: 103 entries (original 100 + 3 new: Museum of Ice Cream, Eataly x2, Comfort Dental)
private static final String SEED_MARKDOWN = 
        "| [MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers) | Show Ambassador (Weekends Only) | Retail / Customer Service | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cheesemonger / Salumi & Formaggi Artisan | Culinary / Retail | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cashier / Front End Associate – Seasonal | Retail / Service | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Comfort Dental Fairwood](https://www.comfortdental.com/) | Office Assistant | Admin / Office Support | Chicago, IL | Applied | 10/15/2025 | ZipRecruiter |\n" +
        "| [RealtyAds](https://www.realtyads.com/careers) | Junior Software Engineer | Software | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [University of Chicago – Harris School of Public Policy](https://harris.uchicago.edu/) | Harris Social Impact Fellowship | Policy / Research | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [SPAATECH, Inc](https://www.spaatech.com/) | Business Development Representative – Technology Solutions | Business / IT | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [METTLER TOLEDO](https://www.mt.com/us/en/home/careers.html) | Software Engineer | Software | Changzhou Shi, China | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Entry Level Software Engineer | Software | Chicago, IL | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Data Analyst | Data | Chicago, IL | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Magnifact](https://www.magnifact.com/careers) | Data Analyst – SQL / ETL | Data | Chicago, IL | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Dominican University](https://www.dom.edu/hr/employment-opportunities) | Web Developer | Software / Web | Chicago, IL | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Capgemini America Inc.](https://www.capgemini.com/us-en/careers/) | Chicago Junior Developer | Software | Chicago, IL / New York, NY | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Oak Street Health](https://www.oakstreethealth.com/careers) | Medical Scribe | Healthcare / IT | Chicago, IL | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SFORCE IT](https://www.sforceit.com/) | Robotic Process Automation Developer | Automation / IT | Chicago, IL / Dallas, TX | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Sunixa Solutions Inc.](https://sunixasolutions.com/careers) | AI/ML Engineer | AI / Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SoftStandard Solutions](https://www.softstandard.com/careers) | Data Engineer – Python Development with AI/ML | Data / AI | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SoftStandard Solutions](https://www.softstandard.com/careers) | Python Developer | Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Fovus Corporation](https://fovus.com/careers) | Software Development Engineer | Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Washington State Department of Ecology](https://ecology.wa.gov/About-us/Jobs) | IT Business Analyst – Journey | IT / Analysis | Lacey, WA | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [The UPS Store](https://www.theupsstore.com/careers) | Student Position — Rogers Park | Retail / Customer Service | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Blick Art Materials](https://www.dickblick.com/careers/) | Retail Sales Associate — Loop | Retail | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [AA Software & Networking](https://aasoftware.com/) | Java Developer | Software | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Tubman Technologies](#) | Java Developer | Software | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Chicago Youth Centers](https://chicagoyouthcenters.org/careers) | STEAM Intern | Education / Nonprofit | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [The Bridge Teen Center](https://thebridgeteencenter.org/) | Horticulture Plant Propagation Intern | Environmental / Education | Orland Park, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [American Red Cross](https://www.redcross.org/about-us/careers.html) | Fundraising & Strategy Intern | Nonprofit / Strategy | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Momotaro](https://www.bokagrp.com/careers) | Food Runner | Service | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [Wakamono](#) | Food Runner | Service | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [My Net Security, Inc.](#) | IT Technician | IT Support | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Taylor Farms Illinois](https://www.taylorfarms.com/careers/) | Label Room Technician | Manufacturing / Ops | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Creative Tech](#) | IT Field Technician | IT Support | Skokie, IL | Not Selected | 09/04/2025 | Indeed |\n" +
        "| [Fernwood Property Mgmt](#) | Maintenance Admin Intern | Admin / Operations | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Trump Intl. Hotel Chicago](https://www.trumphotels.com/chicago/careers) | Security Intern | Security / Ops | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Green Paws Chicago](https://greenpawschicago.com/) | Dog Walker & Pet Sitter | Animal Care | Chicago, IL | Applied (closed) | 09/04/2025 | Indeed |\n" +
        "| [AESLIN Pup Hub](#) | Dog Daycare & Boarding Handler | Animal Care | Chicago, IL | Applied (closed) | 09/05/2025 | Indeed |\n" +
        "| [PAWS Chicago](https://www.pawschicago.org/about-us/careers) | Veterinary Assistant Apprentice | Animal Care | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [PAWS Chicago](https://www.pawschicago.org/about-us/careers) | Adoption Services Associate | Animal Care / Service | Chicago, IL | Applied | 07/20/2025 | Indeed |\n" +
        "| [Ravenswoof](#) | Pet Bather | Animal Care | Chicago, IL | Applied (closed) | 09/07/2025 | Indeed |\n" +
        "| [Bark Bark Club](https://www.barkbarkclub.com/) | Dog Daycare Attendant (Full-Time) | Animal Care | Chicago, IL | Not Selected | 09/23/2025 | Indeed |\n" +
        "| [Bark Bark Club](https://www.barkbarkclub.com/) | Dog Daycare Attendant (Part-Time) | Animal Care | Chicago, IL | Applied (closed) | 04/18/2025 | Indeed |\n" +
        "| [Lawrence Fish Market](#) | Sushi Chef Assistant | Culinary | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Eataly North America](https://www.eataly.com/us_en/) | Fishmonger — Raw Bar & Oyster | Culinary | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [City Winery Chicago](https://citywinery.com/chicago) | Service Assistant | Service | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Cadinho Bakery & Cafe](#) | Food Service Associate | Food Service | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Montelimar Bread Co.](#) | Farmers Market Vendor | Food Service | Evanston, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Anti-Cruelty Society](https://anticruelty.org/careers) | Animal Care Specialist | Animal Care | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Fire Hydrant Pet Sitting Co.](https://www.firehydrantpetsitting.com/) | Overnight Pet Care Specialist | Animal Care | Chicago, IL | Not Selected | 07/20/2025 | Indeed |\n" +
        "| [Thornton Tomasetti](https://www.thorntontomasetti.com/careers) | IT Support Technician | IT Support | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Kids STEM Studio](https://kidsstemstudio.com/) | After School Instructor | Education / Tech | Evanston, IL | Not Selected | 05/16/2025 | Indeed |\n" +
        "| [Sweet Rabbit Bakery](#) | Part-Time FOH | Service | Chicago, IL | Not Selected | 05/08/2025 | Indeed |\n" +
        "| [Fusion92](https://www.fusion92.com/careers) | Internship Program | Marketing / Data | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [APCIA](https://www.apci.org/) | IT Systems Support Intern | IT Support | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [C4 Chicago](https://www.c4chicago.org/) | Development Intern | Nonprofit / Dev | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [Lincoln Park Art Gallery](#) | Art Gallery Assistant | Arts / Admin | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Advance Spine Rehab Center](#) | IT Personnel — As Needed | IT Support | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Desk Support Technician | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Part-Time Admin Assistant | Admin | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Medical Coder | Healthcare / IT | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Azure Data Architect | Cloud / Data | Remote | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | IT Site Discovery & Infrastructure Coordinator | IT Infrastructure | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Help Desk Analyst II | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Desktop Support Analyst | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Help Desk Analyst | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Optimum Healthcare IT](https://www.optimumhit.com/careers) | Entry-Level Healthcare IT Analyst | IT / Healthcare | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [GreenLoop IT Solutions](https://www.greenloopit.com/careers) | IT Support Specialist | IT Support | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Pimlico Enterprises](https://www.pimlicoenterprises.com/careers) | Data Analyst Intern | Data | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Kemon Marketing](https://www.kemonmarketing.com/careers) | Prompt Engineer | AI / Tech | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Musing AI](https://www.musing.ai/careers) | Software Engineer Intern | Software | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Reverb](https://reverb.com/jobs) | IT Support Specialist | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [RKON](https://www.rkon.com/careers) | Analyst (Tier I) — End User Engineering | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Fairlife](https://www.fairlife.com/careers) | Data Science Intern | Data | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Senior Enterprise IT Support Tech | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Internship | Software / Data | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Product Manager Intern | Product | Chicago, IL | Rejected | 09/10/2025 | LinkedIn |\n" +
        "| [Capgemini (France)](https://www.capgemini.com/careers/) | Apprenticeship | Software | France | Rejected | 09/08/2025 | LinkedIn |\n" +
        "| [Capgemini](https://www.capgemini.com/careers) | Business Analyst | Business / IT | Remote | Applied | 09/01/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | English Support | IT / Support | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Enterprise Cloud Domain Support | Cloud / IT | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Intelligent Identity Engineer | Security / IT | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Software Eng. Code Review (Rust) | Software | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Data Engineer | Data | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [RWE](https://www.rwe.com/careers) | IT Graduate Program | IT / Graduate | Remote | Rejected | 09/17/2025 | LinkedIn |\n" +
        "| [Morningstar](https://www.morningstar.com/careers) | Development Program for Technologists | Software / Data | Chicago, IL | Applied | 09/17/2025 | LinkedIn |\n" +
        "| [Epsilon](https://www.epsilon.com/us/about-us/careers) | New Grad Program | Software | Chicago, IL | Applied | 09/17/2025 | LinkedIn |\n" +
        "| [AllJoined](https://alljoined.com) | Software Engineering Research Intern | Software / Research | Remote | Applied | 10/02/2025 | LinkedIn |\n" +
        "| [TDS](https://tdstelecom.com/careers) | GIS Intern | GIS / Environmental | Remote | Applied | 10/13/2025 | LinkedIn |\n" +
        "| [LeoLabs](https://www.leolabs.space/careers) | Electrical Engineer | Engineering | Remote | Applied | 10/13/2025 | LinkedIn |";


    private static final List<JobApplication> applications = new ArrayList<>();

    public static void main(String[] args) {
        loadApplications();
        if (applications.isEmpty()) {
            System.out.println("ℹ️ No applications found. You can import your seeded rows via option 5.");
        }
        showMenu();
    }

    private static void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\\n=== Job Application Tracker ===");
            System.out.println("1. View summary");
            System.out.println("2. Add new application");
            System.out.println("3. Export README (stats + sorted table)");
            System.out.println("4. Add multiple from shell (instructions)");
            System.out.println("5. Import from embedded Markdown seed (once)");
            System.out.println("6. Update job status");
            System.out.println("7. Exit");
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
                    updateStatus(sc);
                    break;
                case 7:
                    saveApplications();
                    System.out.println("Goodbye!");
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

        try {
            JobApplication app = new JobApplication(company, role, type, location, status, date, source);
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
            String row = trimmed.substring(1, trimmed.endsWith("|") ? trimmed.length()-1 : trimmed.length());
            String[] cols = row.split("\\|", -1);
            if (cols.length != 7) continue;
            for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();
            try {
                JobApplication a = new JobApplication(cols[0], cols[1], cols[2], cols[3], cols[4], cols[5], cols[6]);
                boolean dup = applications.stream().anyMatch(
                    x -> x.company.equals(a.company) && x.role.equals(a.role) && x.dateApplied.equals(a.dateApplied)
                );
                if (!dup) applications.add(a);
            } catch (Exception ignore) {}
        }
        return applications.size() - before;
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
        // 🔒 Safety: Backup existing file before overwriting
        File original = new File(FILE);
        if (original.exists()) {
            File backup = new File(FILE + ".bak");
            if (backup.exists()) backup.delete(); // replace old backup
            original.renameTo(backup);
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (JobApplication a : applications) pw.println(a.toFileLine());
        } catch (IOException e) {
            System.out.println("Failed to save " + FILE + ": " + e.getMessage());
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
        System.out.println("✅ Status updated and saved.");

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
                .filter(s -> s.contains("hired")).count();
        long interviews = applications.stream().map(a -> a.status.toLowerCase())
                .filter(s -> s.contains("interview")).count();
        long active = total - rejected - hired;

        System.out.printf("\\n📊 Total: %d | Active: %d | Rejected: %d | Interviews: %d | Hired: %d\\n",
                total, active, rejected, interviews, hired);

        Map<String, Long> byType = applications.stream()
                .collect(Collectors.groupingBy(a -> a.type, TreeMap::new, Collectors.counting()));
        System.out.println("\\n💼 Breakdown by Type:");
        for (Map.Entry<String, Long> e : byType.entrySet()) {
            System.out.printf("- %s: %d\\n", e.getKey(), e.getValue());
        }
    }

private static void exportMarkdown() throws IOException {
    applications.sort(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed());

    int total = applications.size();
    long rejected = applications.stream()
            .map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("reject") || s.contains("not selected"))
            .count();
    long hired = applications.stream()
            .map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("hired") || s.contains("offer"))
            .count();
    long interviews = applications.stream()
            .map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("interview"))
            .count();
    long closed = applications.stream()
            .map(a -> a.status.toLowerCase())
            .filter(s -> s.contains("closed"))
            .count();

    // Active = total - (rejected + hired + closed)
    long active = total - rejected - hired - closed;

    String today = LocalDate.now().format(HUMAN);

    StringBuilder md = new StringBuilder();

    // --- HEADER ---
    md.append("# 🗂️ Job Application Tracker — Lilyana Patamia\n\n");
    md.append("Comprehensive record of job applications, interviews, and outcomes across **IT**, **Data**, and **Software Engineering** roles.\n\n");
    md.append("> *Includes submissions from LinkedIn, Indeed, and recruiter referrals.*\n\n");

    // --- SUMMARY STATS ---
    md.append("## 📊 Application Overview\n\n");
    md.append(String.format("- **Total Applications:** %d\n", total));
    md.append(String.format("- 🕐 **Active / Pending:** %d\n", active));
    md.append(String.format("- ❌ **Rejected:** %d\n", rejected));
    md.append(String.format("- 💬 **Interviewed:** %d\n", interviews));
    md.append(String.format("- ✅ **Hired / Offer:** %d\n", hired));
    md.append(String.format("- 🗓️ **Last Updated:** %s\n\n", today));

    // --- TABLE SECTION ---
    md.append("## 📋 Master Application Log\n\n");
    md.append("<details>\n<summary>Click to expand full job application list</summary>\n\n");
    md.append("| Company | Role | Type | Location | Status | Date Applied | Source |\n");
    md.append("|----------|------|------|-----------|----------|---------------|---------|\n");

    for (JobApplication a : applications) {
        md.append(a.toMarkdownRow()).append("\n");
    }

    md.append("\n</details>\n\n");

    // Add summary below the collapsible list
    md.append("\n**Summary:** ")
    .append(String.format("%d total — %d active, %d rejected, %d interviews, %d hired.**\n",
            total, active, rejected, interviews, hired));

    md.append("---\n");
    md.append("*Generated automatically by the Java Job Application Tracker.*\n");
    md.append("*Last updated ").append(today).append(".*\n");

    try (FileWriter w = new FileWriter(README)) {
        w.write(md.toString());
    }

    System.out.println("✅ README updated with " + total + " jobs (" + active + " active, " + rejected + " rejected).");
}
    private static void printEchoInstructions() {
        System.out.println("\\nYou can append from the shell like this (outside the program):\\n");
        System.out.println("echo \"[Eataly](https://www.eataly.com/us_en/)|Cashier / Front End Associate – Seasonal|Retail / Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("echo \"[MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers)|Show Ambassador (Weekends Only)|Retail / Customer Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("\\nThen re-run option 3 to export an updated README.");
    }
}
