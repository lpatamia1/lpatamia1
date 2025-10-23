/**
 * Handles all file operations for the Job Application Tracker:
 * 
 * ✅ Responsibilities:
 * - Load and save job applications to applications.txt
 * - Create timestamped backups under /backups (keep 5 newest)
 * - Export CSV for the Flask dashboard
 * - Display pastel color CLI messages via UIHelper
 */

package tracker.core;

import tracker.ui.UIHelper;
import tracker.data.JobApplication;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileManager {

    public static final String FILE = "applications.txt";
    public static final String README = "README.md";

    // 💾 Load saved applications
    public static void loadApplications(List<JobApplication> applications) {
        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No existing file found, starting fresh." + UIHelper.RESET);
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
            System.out.println(UIHelper.MINT + "📂 Loaded " + count + " applications." + UIHelper.RESET);
        } catch (IOException e) {
            System.out.println(UIHelper.RED + "❌ Failed to load " + FILE + ": " + e.getMessage() + UIHelper.RESET);
        }

        // 💾 Show existing backups
        File backupDir = new File("backups");
        File[] backups = backupDir.listFiles((d, n) -> n.endsWith(".bak"));
        int backupCount = (backups == null) ? 0 : backups.length;
        System.out.printf(UIHelper.MINT + "💾 %d backups found in /backups%n" + UIHelper.RESET, backupCount);
    }

    // 💿 Save applications and create a timestamped backup inside /backups
    public static void saveApplications(List<JobApplication> applications) {
        File original = new File(FILE);
        File backupDir = new File("backups");
        if (!backupDir.exists()) backupDir.mkdirs(); // create folder if missing

        // 🧠 Create a backup with date + time for uniqueness
        if (original.exists()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File backup = new File(backupDir, "applications_" + timestamp + ".bak");

            try (InputStream in = new FileInputStream(original);
                 OutputStream out = new FileOutputStream(backup)) {
                in.transferTo(out);
                System.out.println(UIHelper.MINT + "📦 Backup saved → backups/" + backup.getName() + UIHelper.RESET);
            } catch (IOException e) {
                System.out.println(UIHelper.ORANGE + "⚠️ Backup creation failed: " + e.getMessage() + UIHelper.RESET);
            }
        }

        // ✏️ Write the new data to applications.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (JobApplication a : applications) {
                pw.println(a.toFileLine());
            }
            System.out.printf(UIHelper.GREEN + "💾 Saved %d applications to %s%n" + UIHelper.RESET,
                    applications.size(), FILE);
        } catch (IOException e) {
            System.out.println(UIHelper.RED + "❌ Failed to save " + FILE + ": " + e.getMessage() + UIHelper.RESET);
        }

        // 🧹 Clean up old backups
        cleanupOldBackups(backupDir);
        exportCSV(applications);
        System.out.println(UIHelper.BLUE +
            "🕓 Saved on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")) +
            UIHelper.RESET);
    }

    // 🧹 Keep only 5 most recent backups in /backups
    private static void cleanupOldBackups(File backupDir) {
        File[] backups = backupDir.listFiles((d, name) ->
                name.startsWith("applications_") && name.endsWith(".bak"));

        if (backups == null || backups.length == 0) {
            System.out.println(UIHelper.ORANGE + "⚠️ No backups found to clean." + UIHelper.RESET);
            return;
        }

        Arrays.sort(backups, Comparator.comparingLong(File::lastModified));

        int deleteCount = 0;
        if (backups.length > 5) {
            deleteCount = backups.length - 5;
            for (int i = 0; i < deleteCount; i++) {
                backups[i].delete();
            }
        }

        System.out.printf(UIHelper.PEACH + "🧹 Backup check complete — %d total, %d old deleted (kept %d latest)%n" + UIHelper.RESET,
                backups.length, deleteCount, Math.min(5, backups.length));
    }

    // 📤 Export CSV for dashboards or data visualization
    public static void exportCSV(List<JobApplication> applications) {
        File csvFile = new File("applications.csv");
        try (PrintWriter pw = new PrintWriter(
            new OutputStreamWriter(
                new FileOutputStream(csvFile), 
                java.nio.charset.StandardCharsets.UTF_8))) {

            pw.println("Company,Role,Type,Location,Status,Date Applied,Source,Notes");

        for (JobApplication a : applications) {
            String safeCompany  = clean(a.getCompany());
            String safeRole     = clean(a.getRole());
            String safeType     = clean(a.getType());
            String safeLocation = clean(a.getLocation());
            String safeSource   = clean(a.getSource());
            String safeNotes    = clean(a.getNotes());

            pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    safeCompany,
                    safeRole,
                    safeType,
                    safeLocation,
                    a.getStatus().name(),
                    a.getDateApplied().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    safeSource,
                    safeNotes);
        }

            System.out.println(UIHelper.GREEN + "✅ Exported applications.csv for web dashboard." + UIHelper.RESET);
            System.out.println(UIHelper.MINT +
                    "📊 CSV updated → " + csvFile.getAbsolutePath() +
                    " (" + applications.size() + " rows)" + UIHelper.RESET);
        } catch (IOException e) {
            System.out.println(UIHelper.RED + "❌ Failed to export CSV: " + e.getMessage() + UIHelper.RESET);
        }
    }
    // 🧼 Helper to sanitize text for CSV
    private static String clean(String value) {
        if (value == null) return "";
        return value.replace(",", ";").replace("\"", "'").trim();
    }

}
