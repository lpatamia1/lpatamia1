import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class JobApplication {
    String company, role, type, location, status, source;
    LocalDate dateApplied;

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MD = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MD_DASH = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public JobApplication(String company, String role, String type, String location,
                          String status, String dateApplied, String source) {
        this.company = company.trim();
        this.role = role.trim();
        this.type = type.trim();
        this.location = location.trim();
        this.status = status.trim();
        this.source = source.trim();
        this.dateApplied = parseFlexible(dateApplied.trim());
    }

    private static LocalDate parseFlexible(String s) {
        String t = s.trim();
        try { return LocalDate.parse(t, MD); }
        catch (DateTimeParseException ignore) {}
        try { return LocalDate.parse(t, ISO); }
        catch (DateTimeParseException ignore) {}
        try { return LocalDate.parse(t, MD_DASH); }
        catch (DateTimeParseException ignore) {}
        try { return LocalDate.parse(t.replace('/', '-'), ISO); }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date: " + s);
        }
    }

    public String toFileLine() {
        return String.join("|",
                company, role, type, location, status,
                dateApplied.format(MD), source);
    }

    public static JobApplication fromFileLine(String line) {
        if (line == null || line.isBlank() || line.startsWith("#")) return null;
        String[] p = line.trim().split("\\|", -1);
        if (p.length != 7) return null;
        return new JobApplication(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    public String toMarkdownRow() {
        return String.format("| %s | %s | %s | %s | %s | %s | %s |",
                company, role, type, location, status,
                dateApplied.format(MD), source);
    }
}
