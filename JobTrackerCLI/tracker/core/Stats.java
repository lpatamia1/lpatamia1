/**
 * Provides analytics and dashboard summaries for the Job Application Tracker.
 * Computes key statistics like total, success rate, active vs. stale applications,
 * and generates clean, color-coded console summaries.
 * 
 * - Uses Java Streams for efficient aggregation and filtering.
 * - Leverages {@link ApplicationStatus} for status-based calculations.
 * - Generates formatted dashboards via {@link UIHelper}.
 * - Supports Markdown export compatibility for GitHub profiles. 
 */
package tracker.core;

import tracker.data.JobApplication;
import tracker.ui.UIHelper;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class Stats {

    public int total;
    public long rejected, hired, interviews, closed, active, stale, trulyActive, openApps;
    public double successRate, perWeek;
    public String topSource, topLocation;
    public JobApplication latest;

    public Stats(int total, long rejected, long hired, long interviews, long closed,
                 long active, long stale, long trulyActive, double successRate,
                 double perWeek, long openApps, String topSource, String topLocation,
                 JobApplication latest) {
        this.total = total;
        this.rejected = rejected;
        this.hired = hired;
        this.interviews = interviews;
        this.closed = closed;
        this.active = active;
        this.stale = stale;
        this.trulyActive = trulyActive;
        this.successRate = successRate;
        this.perWeek = perWeek;
        this.openApps = openApps;
        this.topSource = topSource;
        this.topLocation = topLocation;
        this.latest = latest;
    }

    // 🧮 Static factory method to compute statistics from all applications
    public static Stats compute(List<JobApplication> applications) {
        int total = applications.size();

        long rejected = applications.stream()
                .filter(a -> a.status == ApplicationStatus.REJECTED)
                .count();

        long hired = applications.stream()
                .filter(a -> a.status == ApplicationStatus.HIRED)
                .count();

        long interviews = applications.stream()
                .filter(a -> a.status == ApplicationStatus.INTERVIEW)
                .count();

        long closed = applications.stream()
                .filter(a -> a.status == ApplicationStatus.CLOSED)
                .count();

        long active = total - rejected - hired - closed;

        long stale = applications.stream()
                .filter(a -> a.status == ApplicationStatus.APPLIED)
                .filter(a -> ChronoUnit.DAYS.between(a.dateApplied, LocalDate.now()) > 60)
                .count();

        long trulyActive = Math.max(0, active - stale);
        double successRate = total == 0 ? 0 : (double) (hired + interviews) / total * 100;

        String topSource = applications.stream()
                .collect(Collectors.groupingBy(a -> a.source, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");

        String topLocation = applications.stream()
                .collect(Collectors.groupingBy(a -> a.location, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");

        long daysSpan = ChronoUnit.DAYS.between(
                applications.stream()
                        .map(a -> a.dateApplied)
                        .min(LocalDate::compareTo)
                        .orElse(LocalDate.now()),
                LocalDate.now());

        double perWeek = total / Math.max(daysSpan / 7.0, 1.0);

        long openApps = applications.stream()
                .filter(a -> a.status == ApplicationStatus.APPLIED)
                .count();

        JobApplication latest = applications.stream()
                .max(Comparator.comparing(a -> a.dateApplied))
                .orElse(null);

        return new Stats(total, rejected, hired, interviews, closed, active, stale, trulyActive,
                successRate, perWeek, openApps, topSource, topLocation, latest);
    }

    // 🧭 Compact console summary (Option 1)
    public void printSummary() {
        System.out.println(UIHelper.LAVENDER +
            "╔═════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                               🌸  APPLICATION SUMMARY  🌸                               ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝"
                + UIHelper.RESET);

        System.out.printf(
            "  %sTotal:%s %-3d  %sActive:%s %-3d  %sStale:%s %-3d  %sRejected:%s %-3d  %sInterviews:%s %-3d  %sHired:%s %-3d%n",
            UIHelper.CYAN, UIHelper.RESET, total,
            UIHelper.GREEN, UIHelper.RESET, trulyActive,
            UIHelper.ORANGE, UIHelper.RESET, stale,
            UIHelper.RED, UIHelper.RESET, rejected,
            UIHelper.YELLOW, UIHelper.RESET, interviews,
            UIHelper.PINK, UIHelper.RESET, hired
        );

        String bar = progressBar(successRate, 30);
        System.out.printf("%s📈 Success Rate:%s %.1f%% %s%s%s%n",
            UIHelper.ORANGE, UIHelper.RESET, successRate,
            UIHelper.GREEN, bar, UIHelper.RESET);

        System.out.printf("%s⚡ Avg per Week:%s %.1f%n",
            UIHelper.GREEN, UIHelper.RESET, perWeek);
        System.out.printf("%s📍 Top Location:%s %s%n",
            UIHelper.GREEN, UIHelper.RESET, topLocation);

        if (latest != null)
            System.out.printf("%s🆕 Most Recent:%s %s — %s (%s)%n",
                UIHelper.PINK, UIHelper.RESET,
                latest.company, latest.role, latest.dateApplied);
    }

    // 🌈 Dashboard Snapshot (Option 9)
    public void printDashboard() {
        System.out.println(UIHelper.LAVENDER +
            "\n╔═════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             🌈  CAREER DASHBOARD SNAPSHOT  🌈                           ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════════════╝"
                + UIHelper.RESET);

        System.out.printf("%s📁 Total Applications:%s %d%n",
            UIHelper.CYAN, UIHelper.RESET, total);
        System.out.printf("%s💬 Interviews:%s %d   %s✅ Hired:%s %d   %s❌ Rejected:%s %d%n",
            UIHelper.YELLOW, UIHelper.RESET, interviews,
            UIHelper.GREEN, UIHelper.RESET, hired,
            UIHelper.RED, UIHelper.RESET, rejected);

        String bar = progressBar(successRate, 40);
        System.out.printf("\n%s📈 Success Rate:%s %.1f%% %s%s%s%n",
            UIHelper.ORANGE, UIHelper.RESET, successRate,
            UIHelper.GREEN, bar, UIHelper.RESET);

        if (latest != null) {
            System.out.println("\n" + UIHelper.PINK +
                "🌸 Most Recent Application:" + UIHelper.RESET);
            System.out.printf("%s → %s (%s on %s)%n",
                latest.company, latest.role, latest.status, latest.dateApplied);
        }

        System.out.println(UIHelper.TEAL +
            "\n🐾 Career Cat purrs approvingly — you’re building momentum!" +
            UIHelper.RESET);
    }

    // 🧩 Simple helper for bars
    private String progressBar(double percent, int length) {
        int filled = (int)(length * percent / 100);
        return "█".repeat(filled) + "░".repeat(length - filled);
    }
}
