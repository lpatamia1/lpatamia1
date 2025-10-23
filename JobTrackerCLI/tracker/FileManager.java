package tracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all file operations for the Job Application Tracker:
 * - Loading & saving applications
 * - Exporting CSVs
 * - Cleaning up old backups
 */
public class FileManager {

    public static final String FILE = "applications.txt";
    public static final String README = "README.md";

    // 💾 Load saved applications
    public static void loadApplications(List<JobApplication> applications) {
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
            System.out.println("❌ Failed to load " + FILE + ": " + e.getMessage());
        }
    }

    // 💿 Save applications with backup
    public static void saveApplications(List<JobApplication> applications) {
        File original = new File(FILE);
        if (original.exists()) {
            String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            File backup = new File(FILE.replace(".txt", "_" + timestamp + ".bak"));
            if (backup.exists()) backup.delete();
            original.renameTo(backup);
            System.out.println("📦 Backup created: " + backup.getName());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (JobApplication a : applications) {
                pw.println(a.toFileLine());
            }
            System.out.printf("💾 Saved %d applications to %s%n", applications.size(), FILE);
        } catch (IOException e) {
            System.out.println("❌ Failed to save " + FILE + ": " + e.getMessage());
        }

        cleanupOldBackups();
    }

    // 🧹 Keep 5 most recent backups
    private static void cleanupOldBackups() {
        File[] backups = new File(".").listFiles((d, name) ->
                name.startsWith("applications_") && name.endsWith(".bak"));
        if (backups == null || backups.length <= 5) return;

        Arrays.sort(backups, Comparator.comparingLong(File::lastModified));
        for (int i = 0; i < backups.length - 5; i++) backups[i].delete();
    }

    // 📤 Export CSV for dashboards or data visualization
    public static void exportCSV(List<JobApplication> applications) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("applications.csv"))) {
            pw.println("Company,Role,Type,Location,Status,Date Applied,Source,Notes");
            for (JobApplication a : applications) {
                pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        a.company, a.role, a.type, a.location, a.status.name(),
                        a.dateApplied, a.source, a.notes.replace("\"", "'"));
            }
            System.out.println("✅ Exported applications.csv for web dashboard.");
        } catch (IOException e) {
            System.out.println("❌ Failed to export CSV: " + e.getMessage());
        }
    }
}
