/**
 * ✍️ InputHandler — manages user prompts and data entry.
 *
 * Collects and validates user input to create
 * {@link JobApplication} objects for the tracker.
 */

package tracker.ui;

import tracker.data.*;
import tracker.core.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class InputHandler {
    public static JobApplication createApplicationFromInput(Scanner sc) {
        System.out.print("Company (Markdown link ok): ");
        String company = sc.nextLine().trim();

        System.out.print("Role: ");
        String role = sc.nextLine().trim();

        System.out.print("Type (Job Category): ");
        String type = sc.nextLine().trim();

        System.out.print("Location: ");
        String location = sc.nextLine().trim();

        System.out.print("Status (Applied / Interview / Rejected / etc): ");
        String statusRaw = sc.nextLine().trim();
        ApplicationStatus status = ApplicationStatus.from(statusRaw);

        System.out.print("Date Applied (MM/DD/YYYY or YYYY-MM-DD): ");
        String dateRaw = sc.nextLine().trim();
        String date = parseDateOrToday(dateRaw);

        System.out.print("Source: ");
        String source = sc.nextLine().trim();

        System.out.print("Notes (optional): ");
        String notes = sc.nextLine().trim();
        if (notes.isEmpty()) {
            notes = null;
        }   

        printEncouragement();

        return new JobApplication(company, role, type, location, status.name(), date, source, notes);
    }
    
    private static String parseDateOrToday(String dateRaw) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate parsedDate = LocalDate.parse(dateRaw, formatter);
                return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }

        // Default to today if parsing fails
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static void printEncouragement() {
        String[] messages = {
            "Another application down. Give them runway to worship you. 💅",
            "They’ll be lucky to even *read* your résumé. Keep applying. 💖",
            "Boss energy only. Jobs should be applying to YOU. 👑",
            "You just clicked submit like it’s nothing. Meanwhile careers are shaking. 😌",
            "Collecting interviews like Pokémon soon. 🎤✨",
            "Rejection? Please. You’re the plot twist. 📚🔥",
            "Putting your name in their database is an act of charity. 🤲",
            "Soon they’ll realize the main character has arrived. ⭐",
            "Every recruiter will remember you. Especially the ones who fumbled. 💅📉",
            "You don't chase jobs. You grace them with interest. 👠"
        };

        Random rand = new Random();
        String message = messages[rand.nextInt(messages.length)];
        System.out.println("\n" + UIHelper.PINK + message + UIHelper.RESET + "\n");
    }

}
