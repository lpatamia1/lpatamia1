package tracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a single job application entry.
 * Stores key attributes such as company, role, location, and status, 
 * with flexible data parsing and Markdown/file export helpers.
 * 
 * ✅ Highlights:
 * - Uses {@link ApplicationStatus} enum for type-safe status handling.
 * - Parses multiple date formats automatically (MM/DD/YYYY, ISO, etc.).
 * - Converts data seamlessly between file lines and Markdown table rows.
 */

class JobApplication {
    String company, role, type, location, source, notes;
    ApplicationStatus status;
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
        this.status = ApplicationStatus.from(status); 
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
                company, role, type, location, status.name(),  
                dateApplied.format(MD), source, notes);
    }

    // 📂 Load from file
    public static JobApplication fromFileLine(String line) {
        if (line == null || line.isBlank() || line.startsWith("#")) return null;
        String[] p = line.trim().split("\\|", -1);
        if (p.length < 7) return null;
        String notes = p.length >= 8 ? p[7] : "";
        return new JobApplication(p[0], p[1], p[2], p[3], p[4], p[5], p[6], notes);
    }

    // 📝 Convert to Markdown row
    public String toMarkdownRow() {
        String displayStatus = status.name().charAt(0) + status.name().substring(1).toLowerCase();
        return String.format(
            "| %s | %s | %s | %s | %s | %s | %s | %s |",
            company,
            role,
            type,
            location,
            displayStatus,
            dateApplied.format(MD),
            source,
            notes.isEmpty() ? "<span style='color:#999;'>—</span>" : notes
        );
    }

    // ✏️ Edit details
    public void editDetails(String company, String role, String type, String location,
                            String status, String dateApplied, String source, String notes) {
        this.company = company.trim();
        this.role = role.trim();
        this.type = type.trim();
        this.location = location.trim();
        this.status = ApplicationStatus.from(status); 
        this.source = source.trim();
        this.notes = notes == null ? "" : notes.trim();
        this.dateApplied = parseFlexible(dateApplied.trim());
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? "" : notes.trim();
    }

    public void setStatus(String status) {
        this.status = ApplicationStatus.from(status); 
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status; 
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
