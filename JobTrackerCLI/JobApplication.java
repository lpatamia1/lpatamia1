import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class JobApplication {
    String company, role, type, location, status, source, notes;
    LocalDate dateApplied;

    // 📅 Supported date formats
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MD = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MD_DASH = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    // 🧱 Constructors
    public JobApplication(String company, String role, String type, String location,
                          String status, String dateApplied, String source) {
        this(company, role, type, location, status, dateApplied, source, "");
    }

    public JobApplication(String company, String role, String type, String location,
                          String status, String dateApplied, String source, String notes) {
        this.company = company.trim();
        this.role = role.trim();
        this.type = type.trim();
        this.location = location.trim();
        this.status = status.trim();
        this.source = source.trim();
        this.notes = notes == null ? "" : notes.trim();
        this.dateApplied = parseFlexible(dateApplied.trim());
    }

    // 🗓️ Flexible date parsing
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

    // 💾 Save to file
    public String toFileLine() {
        return String.join("|",
                company, role, type, location, status,
                dateApplied.format(MD), source, notes);
    }

    // 📂 Load from file (backward compatible with old 7-field format)
    public static JobApplication fromFileLine(String line) {
        if (line == null || line.isBlank() || line.startsWith("#")) return null;
        String[] p = line.trim().split("\\|", -1);
        if (p.length < 7) return null;
        String notes = p.length >= 8 ? p[7] : "";
        return new JobApplication(p[0], p[1], p[2], p[3], p[4], p[5], p[6], notes);
    }

    // 📝 Convert to Markdown row (styled for README)
    public String toMarkdownRow() {
        return String.format(
            "| %s | %s | %s | %s | %s | %s | %s | %s |",
            company,
            role,
            type,
            location,
            status,
            dateApplied.format(MD),
            source,
            notes.isEmpty() ? "<span style='color:#999;'>—</span>" : notes
        );
    }

    // ✏️ Edit all details interactively or programmatically
    public void editDetails(String company, String role, String type, String location,
                            String status, String dateApplied, String source, String notes) {
        this.company = company.trim();
        this.role = role.trim();
        this.type = type.trim();
        this.location = location.trim();
        this.status = status.trim();
        this.source = source.trim();
        this.notes = notes == null ? "" : notes.trim();
        this.dateApplied = parseFlexible(dateApplied.trim());
    }

    // 🔧 Optional individual field setters
    public void setNotes(String notes) { this.notes = notes == null ? "" : notes.trim(); }
    public void setStatus(String status) { this.status = status.trim(); }
}
