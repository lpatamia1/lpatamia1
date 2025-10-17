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
        "| [LeoLabs](https://www.leolabs.space/careers) | Electrical Engineer | Engineering | Remote | Applied | 10/13/2025 | LinkedIn |\n" +
        "| [Council of International Programs Chicago](https://www.cipchicago.org/) | Web Designer / Developer Intern | Nonprofit / Web | Remote / Chicago, IL / Bensenville, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [Wesco](https://www.wesco.com/careers) | IT Development Program (2026) | IT / Graduate | Hybrid / Glenview, IL / Pittsburgh, PA | Applied | 07/20/2025 | Handshake |\n" +
        "| [DataCapable](https://www.datacapable.com/careers) | Software Engineer | Software | Remote / Chicago, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [GenieAI](https://www.genieai.co/) | Software Engineer | AI / Software | Remote | Applied | 07/20/2025 | Handshake |\n" +
        "| [Northwestern Mutual Chicagoland](https://chicagoland.nm.com/careers.htm) | Financial Advisor | Finance / Insurance | Onsite / Chicago, IL | Applied | 07/09/2025 | Handshake |\n" +
        "| [Blue Cross Blue Shield (HCSC)](https://jobs.hcsc.com/) | Associate Systems Analyst | Healthcare / IT | Hybrid / Chicago, IL | Applied | 06/27/2025 | Handshake |\n" +
        "| [Epic Systems Corporation](https://careers.epic.com/) | Software Developer | Software | Onsite / Verona, WI | Applied | 06/27/2025 | Handshake |\n" +
        "| [Belay Diagnostics](https://belaydiagnostics.com/careers) | Intern, Technical Project & Systems Management | Biotech / IT | Hybrid / Chicago, IL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Siemens Digital Industries Software](https://www.siemens.com/global/en/company/jobs.html) | Software Development Intern | Software | Hybrid / Marlborough, MA / Huntsville, AL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Vantero](https://vantero.ai/) | Software Engineering / LLM Intern | AI / Software | Remote | Applied | 06/26/2025 | Handshake |\n" +
        "| [Catholic Extension](https://www.catholicextension.org/about/careers/) | AI Data Strategy Intern | AI / Nonprofit | Onsite / Chicago, IL | Declined | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Scientist | Data | Remote / U.S. | Applied | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Engineer | Data | Remote / U.S. | Declined | 06/26/2025 | Handshake |\n" +
        "| [Google](https://careers.google.com/students/) | Student Researcher (BS/MS) | Research / Software | Hybrid / Ann Arbor, MI / Austin, TX | Applied | 05/12/2025 | Handshake |\n" +
        "| [Blueera Technologies, Inc.](https://blueeratech.com/careers) | DevOps Engineer | Software / IT | Remote / Indiana / TX | Applied | 05/07/2025 | Handshake |\n" +
        "| [The Public Interest Network Creative Team](https://publicinterestnetwork.org/jobs/) | Web & Digital Content Intern | Communications / Web | Remote / Chicago, IL / Boston, MA | Applied | 05/07/2025 | Handshake |\n" +
        "| [DataCapable](https://www.datacapable.com/careers) | Software Engineer | Software | Remote / Chicago, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [GenieAI](https://www.genieai.co/) | Software Engineer | AI / Software | Remote | Applied | 07/20/2025 | Handshake |\n" +
        "| [Northwestern Mutual Chicagoland](https://chicagoland.nm.com/careers.htm) | Financial Advisor | Finance / Insurance | Onsite / Chicago, IL | Applied | 07/09/2025 | Handshake |\n" +
        "| [Blue Cross Blue Shield (HCSC)](https://jobs.hcsc.com/) | Associate Systems Analyst | Healthcare / IT | Hybrid / Chicago, IL | Applied | 06/27/2025 | Handshake |\n" +
        "| [Epic Systems Corporation](https://careers.epic.com/) | Software Developer | Software | Onsite / Verona, WI | Applied | 06/27/2025 | Handshake |\n" +
        "| [Belay Diagnostics](https://belaydiagnostics.com/careers) | Intern, Technical Project & Systems Management | Biotech / IT | Hybrid / Chicago, IL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Siemens Digital Industries Software](https://www.siemens.com/global/en/company/jobs.html) | Software Development Intern | Software | Hybrid / Marlborough, MA / Huntsville, AL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Vantero](https://vantero.ai/) | Software Engineering / LLM Intern | AI / Software | Remote | Applied | 06/26/2025 | Handshake |\n" +
        "| [Catholic Extension](https://www.catholicextension.org/about/careers/) | AI Data Strategy Intern | AI / Nonprofit | Onsite / Chicago, IL | Declined | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Scientist | Data | Remote / U.S. | Applied | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Engineer | Data | Remote / U.S. | Declined | 06/26/2025 | Handshake |\n" +
        "| [Google](https://careers.google.com/students/) | Student Researcher (BS/MS) | Research / Software | Hybrid / Ann Arbor, MI / Austin, TX | Applied | 05/12/2025 | Handshake |\n" +
        "| [Blueera Technologies, Inc.](https://blueeratech.com/careers) | DevOps Engineer | Software / IT | Remote / Indiana / TX | Applied | 05/07/2025 | Handshake |\n" +
        "| [The Public Interest Network Creative Team](https://publicinterestnetwork.org/jobs/) | Web & Digital Content Intern | Communications / Web | Remote / Chicago, IL / Boston, MA | Applied | 05/07/2025 | Handshake |";


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
        final String PINK = "\u001B[95m";
        final String CYAN = "\u001B[96m";
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[94m";

        while (true) {
            System.out.println(BLUE + "═".repeat(90));
            System.out.println("||                       ／l、                                                          ||");                                               
            System.out.println("||                     （ﾟ､ ｡７   ~ meow! keeping tabs on your career ~                 ||");
            System.out.println("||                      l、 ~ヽ     keep applying, you got this! 🐾                     ||");
            System.out.println("||                      じしf_, )ノ                                                     ||");
            System.out.println("═".repeat(90));
            System.out.println("                                  JOB APPLICATION TRACKER");
            System.out.println("═".repeat(90));
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
            System.out.println("═".repeat(90) + RESET);

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

    private static void catIntro() {
        final String CYAN = "\u001B[96m";
        final String RESET = "\u001B[0m";

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
        System.out.println("\n                        🐾 Meow! Time to check your job hunt 💼");
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
    final String PINK = "\u001B[95m";
    final String CYAN = "\u001B[96m";
    final String GREEN = "\u001B[92m";
    final String YELLOW = "\u001B[93m";
    final String RED = "\u001B[91m";
    final String ORANGE = "\u001B[38;2;255;165;0m";
    final String PEACH = "\u001B[38;2;255;200;150m";
    final String RESET = "\u001B[0m";

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
    double successRate = total == 0 ? 0 : (double) (hired + interviews) / total * 100;


    // 🐾 Top section with colors
    System.out.println(ORANGE + "═".repeat(90));
    System.out.println("                                   APPLICATION SUMMARY");
    System.out.println("═".repeat(90) + RESET);

    System.out.printf("%sTotal:%s %-9d  %sActive:%s %-9d  %sRejected:%s %-9d  %sInterviews:%s %-9d  %sHired:%s %-9d%n",
            CYAN, RESET, total,
            GREEN, RESET, active,
            RED, RESET, rejected,
            YELLOW, RESET, interviews,
            PINK, RESET, hired);

    System.out.println();
    
    // Success rate and closed count
    int barLength = 30;
    int filled = (int) (barLength * successRate / 100);
    String bar = "█".repeat(filled) + "░".repeat(barLength - filled);
    System.out.println();
    System.out.printf("%s📈 Success Rate:%s %.1f%% %s%s%s%n", ORANGE, RESET, successRate, GREEN, bar, RESET);
    System.out.printf("%s📦 Closed:%s %d%n%n", CYAN, RESET, closed);

    // Breakdown
    // 💼 Breakdown by type
    Map<String, Long> byType = applications.stream()
        .collect(Collectors.groupingBy(a -> simplifyBroadCategory(a.type), TreeMap::new, Collectors.counting()));

    System.out.println(PEACH + "-".repeat(90));
    System.out.println("                                    Breakdown by Type:");
    System.out.println("-".repeat(90));

    // Sort from largest → smallest
    List<Map.Entry<String, Long>> entries = new ArrayList<>(byType.entrySet());
    entries.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

    int colWidth = 37; // fits nicely in 90-char width terminals

    for (int i = 0; i < entries.size(); i += 2) {
        String left = String.format("• %-"+colWidth+"s %3d", 
            entries.get(i).getKey(), entries.get(i).getValue());

        String right = (i + 1 < entries.size())
            ? String.format("   • %-"+colWidth+"s %3d", 
                entries.get(i + 1).getKey(), entries.get(i + 1).getValue())
            : "";

        System.out.println(CYAN + left + right + RESET);
    }
    System.out.println("═".repeat(90));

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
    String today = LocalDate.now().format(HUMAN);

    StringBuilder md = new StringBuilder();

    // --- HEADER ---
    md.append("<div align=\"center\">\n");
    md.append("# 🗂️ Job Application Tracker\n");
    md.append("</div>\n\n");
    md.append("A living record of my 2025 job applications, interview progress, and outcomes across **IT**, **Data**, and **Software Engineering** roles — alongside opportunities in design, research, education, public health, and community-focused organizations.\n\n");
    md.append("> *Includes submissions from LinkedIn, Indeed, Handshake, and recruiter referrals.*\n\n");
    md.append("---\n\n");

    // --- HIGHLIGHTS ---
    md.append("<div align=\"center\">\n## 💡 Highlights\n</div>\n\n");
    md.append(String.format(
        "So far, I've applied to **%d positions** across multiple industries. Currently, **%d applications remain active**, with **%d interview%s** completed.  \n" +
        "Most applications came through LinkedIn and Handshake, spanning software, IT, and data roles.  \n" +
        "This tracker provides a transparent snapshot of growth, persistence, and progress through the 2025 job season.\n\n",
        total, active, interviews, interviews == 1 ? "" : "s"
    ));

    // --- APPLICATION OVERVIEW ---
    md.append("<div align=\"center\">\n## 📊 Application Overview\n</div>\n\n");
    md.append("<table align=\"center\">\n");
    md.append("<tr>\n");
    md.append("<td align=\"left\" width=\"50%\">\n\n");
    md.append("- **Total Applications:** ").append(total).append("  \n");
    md.append("- 🕐 **Active / Pending:** ").append(active).append("  \n");
    md.append("- ❌ **Rejected:** ").append(rejected).append("  \n\n");
    md.append("</td>\n");
    md.append("<td align=\"left\" width=\"50%\">\n\n");
    md.append("- 💬 **Interviewed:** ").append(interviews).append("  \n");
    md.append("- ✅ **Hired / Offer:** ").append(hired).append("  \n");
    md.append("- 🗓️ **Last Updated:** ").append(today).append("  \n\n");
    md.append("</td>\n");
    md.append("</tr>\n");
    md.append("</table>\n\n");

    // --- CATEGORY SUMMARY ---
    md.append("<div align=\"center\">\n## 🧾 Breakdown by Job Type\n</div>\n\n");

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
    md.append("<div align=\"center\">\n## 💻 About This Tracker\n</div>\n\n");
    md.append("Built with **Java 17**, this app demonstrates file handling, date parsing, Markdown generation, and console-based UI design. ");
    md.append("It helps organize applications efficiently while serving as both a **career log** and a **personal software project**. ");
    md.append("The tracker calculates dynamic statistics, success rates, and updates this file in real-time.\n\n");
    
    // --- HOW TO USE ---
    md.append("<div align=\"center\">\n## ⚙️ How to Use\n</div>\n\n");
    md.append("This CLI tool built in **Java 17** automatically stores job data in `applications.txt`, allowing you to:\n");
    md.append("1. Add new applications interactively\n");
    md.append("2. Import a pre-seeded dataset (option 5)\n");
    md.append("3. Search, update, and export to this Markdown report (option 3)\n");
    md.append("4. Generate timestamped backups each time the file is saved\n\n");
    md.append("To refresh this README, run **Option 3: Export README** from the main menu.\n\n");
 
    // --- MASTER LOG ---
    md.append("<div align=\"center\">\n## 📋 Master Application Log\n</div>\n\n");
    md.append("<details>\n<summary>Click to expand full job application list</summary>\n\n");
    md.append("| Company | Role | Type | Location | Status | Date Applied | Source |\n");
    md.append("|----------|------|------|-----------|----------|---------------|---------|\n");

    for (JobApplication a : applications) {
        md.append(a.toMarkdownRow()).append("\n");
    }

    md.append("\n</details>\n\n");
    md.append(String.format("**Summary:** 📋 %d total — 🕐 %d active — ❌ %d rejected — 💬 %d interviews — ✅ %d hired.**\n\n",
            total, active, rejected, interviews, hired));

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
