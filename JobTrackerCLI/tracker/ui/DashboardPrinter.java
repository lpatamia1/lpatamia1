package tracker.ui;

import tracker.core.*;
import tracker.data.*;
import java.util.List;

public class DashboardPrinter {

    // 📊 Prints quick stats summary (Option 1)
    public static void showSummary(List<JobApplication> applications) {
        if (applications.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No applications recorded yet." + UIHelper.RESET);
            return;
        }
        Stats s = Stats.compute(applications);
        s.printSummary();
    }

    // 🖥️ Prints visual dashboard with analytics (Option 9)
    public static void showCareerDashboard(List<JobApplication> applications) {
        if (applications.isEmpty()) {
            System.out.println(UIHelper.ORANGE + "⚠️  No applications recorded yet." + UIHelper.RESET);
            return;
        }
        Stats s = Stats.compute(applications);
        s.printDashboard();
    }
}
