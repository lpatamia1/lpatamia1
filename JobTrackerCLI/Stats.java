import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// 📊 Encapsulates analytics computed from job applications
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
}
