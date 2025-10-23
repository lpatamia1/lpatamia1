/**
 * Handles all user-facing menu actions for the Job Application Tracker CLI.
 * Provides an interactive interface for searching, editing, deleting,
 * viewing recent applications, and importing seed datasets.
 * 
 * - Uses {@link JobApplication}, {@link FileManager}, and {@link UIHelper} 
 *   to perform user actions with clean visual feedback.
 * - Implements keyword-based search with optional quick edit mode.
 * - Includes safety prompts and colorful console UI elements.
 * - Automatically saves edits and deletions via FileManager.
 * - Supports importing Markdown-based seed data for bulk loading.
 */
package tracker.ui;

import tracker.data.*;
import tracker.core.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class MenuHandler {

    private final List<JobApplication> applications;

    public MenuHandler(List<JobApplication> applications) {
        this.applications = applications;
    }

    // 🔎 Search applications by keyword and optionally edit them
    public void searchApplications(Scanner sc) {
        System.out.print("🔎 Enter keyword to search (company or role): ");
        String keyword = sc.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No keyword entered." + UIHelper.RESET);
            return;
        }

        List<JobApplication> results = applications.stream()
                .filter(a -> a.company.toLowerCase().contains(keyword)
                        || a.role.toLowerCase().contains(keyword))
                .sorted(Comparator.comparing((JobApplication a) -> a.dateApplied).reversed())
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println(UIHelper.RED + "❌ No matching applications found for \"" + keyword + "\"." + UIHelper.RESET);
            return;
        }

        System.out.println(UIHelper.GREEN +
                "\n╔═════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                      SEARCH RESULTS                                     ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝"
                + UIHelper.RESET);

        for (int i = 0; i < results.size(); i++) {
            JobApplication a = results.get(i);
            System.out.printf("%2d. %-35s | %-25s | %-12s | %s\n",
                    i + 1, a.company, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }

        System.out.print("\n💡 View details or update status (enter number, or press Enter to skip): ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        int index;
        try {
            index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= results.size()) {
                System.out.println(UIHelper.RED + "Invalid number." + UIHelper.RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(UIHelper.RED + "Invalid input." + UIHelper.RESET);
            return;
        }

        JobApplication a = results.get(index);

        System.out.println("\n──────────────────────────────────────────────────────────────");
        System.out.println("Company:   " + a.company);
        System.out.println("Role:      " + a.role);
        System.out.println("Status:    " + a.status);
        System.out.println("Applied:   " + a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("Source:    " + a.source);
        System.out.println("Notes:     " + (a.notes == null || a.notes.isEmpty() ? "—" : a.notes));
        System.out.println("──────────────────────────────────────────────────────────────");

        System.out.print("\n⚙️  Quick Edit this application? (y/n): ");
        String editChoice = sc.nextLine().trim().toLowerCase();
        if (editChoice.equals("y")) {
            System.out.print("✏️  New Status (leave blank to keep): ");
            String newStatus = sc.nextLine().trim();
            if (!newStatus.isEmpty()) a.status = ApplicationStatus.from(newStatus);

            System.out.print("📍 New Location (leave blank to keep): ");
            String newLocation = sc.nextLine().trim();
            if (!newLocation.isEmpty()) a.setLocation(newLocation);

            System.out.print("📝 New Notes (leave blank to keep): ");
            String newNotes = sc.nextLine().trim();
            if (!newNotes.isEmpty()) a.setNotes(newNotes);

            FileManager.saveApplications(applications);
            System.out.println(UIHelper.GREEN + "✅ Application updated and saved." + UIHelper.RESET);
        }
    }

    // 🗑️ Remove application by keyword
    public void removeApplication(Scanner sc) {
        System.out.print("🔎 Enter keyword to search (company or role): ");
        String keyword = sc.nextLine().trim().toLowerCase();
        if (keyword.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No keyword entered." + UIHelper.RESET);
            return;
        }

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
        for (int i = 0; i < matches.size(); i++) {
            JobApplication a = matches.get(i);
            System.out.printf("%2d. %-35s | %-25s | %-12s | %s\n",
                    i + 1, a.company, a.role, a.status,
                    a.dateApplied.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }

        System.out.print("\n❓ Enter the number of the job to remove (or press Enter to cancel): ");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "🕊️  Cancelled." + UIHelper.RESET);
            return;
        }

        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= matches.size()) throw new NumberFormatException();
            JobApplication toRemove = matches.get(index);
            applications.remove(toRemove);
            FileManager.saveApplications(applications);
            System.out.println(UIHelper.GREEN + "✅ Removed: " + toRemove.company + " — " + toRemove.role + UIHelper.RESET);
        } catch (NumberFormatException e) {
            System.out.println(UIHelper.RED + "⚠️ Invalid input. Please enter a number." + UIHelper.RESET);
        }
    }

    // 🕒 View recent applications (<= 3 days)
    public void viewRecentApplications() {
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

    // 📥 Import seed dataset
    public int importFromSeedMarkdown(String md) {
        if (md == null || md.trim().isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  Seed is empty." + UIHelper.RESET);
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
                        x -> x.company.equals(a.company)
                                && x.role.equals(a.role)
                                && x.dateApplied.equals(a.dateApplied)
                );
                if (!dup) applications.add(a);
            } catch (Exception ignore) {}
        }

        int added = applications.size() - before;
        System.out.println(UIHelper.GREEN + "✅ Imported " + added + " applications from seed." + UIHelper.RESET);
        return added;
    }
}
