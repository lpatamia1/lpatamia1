/**
 * 🎨 Handles rendering of the main ASCII menu layout for the Job Application Tracker CLI.
 * 
 * Centralizes all layout code so that JobApplicationTracker.java stays lightweight and focused.
 * 
 * ✨ Features:
 * - Prints a two-column interactive menu.
 * - Uses {@link UIHelper} for color formatting.
 * - Keeps visual consistency with the app’s cat intro and dashboard style.
 */

package tracker.ui;

public class MenuPrinter {

    public static void printMainMenu() {
        // 🌈 Title banner
        System.out.println(UIHelper.BLUE +
            "╭─────────────────────────────────────────────────────────────────────────────────────────╮\n" +
            "│                                 JOB APPLICATION TRACKER                                 │\n" +
            "╰─────────────────────────────────────────────────────────────────────────────────────────╯");

        // Left column options
        String[] leftCol = {
            "📊  1. View Progress Summary",
            "📝  2. Add New Job Entry",
            "🗒️   3. Export Markdown Report",
            "➕  4. Quick-Add (Batch via Shell)",
            "📥  5. Import Sample Seed Dataset"
        };

        // Right column options
        String[] rightCol = {
            " 🕒  6. View Recent Applications",
            " 🔍  7. Search & Manage (Edit Status/Notes)",
            "   🗑️   8. Remove Application",
            "📈  9. Visual Dashboard",
            " 🚪 10. Save and Close Tracker"
        };

        // 📋 Two-column layout
        for (int i = 0; i < Math.max(leftCol.length, rightCol.length); i++) {
            String left  = i < leftCol.length ? leftCol[i] : "";
            String right = i < rightCol.length ? rightCol[i] : "";
            System.out.printf("  %-45s %s%n", left, right);
        }

        System.out.println("═".repeat(91) + UIHelper.RESET);
    }
}
