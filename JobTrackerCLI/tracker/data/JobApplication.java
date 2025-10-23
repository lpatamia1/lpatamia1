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

package tracker.data;

import tracker.core.ApplicationStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JobApplication {
    // 🧱 Private fields (encapsulated)
    private String company;
    private String role;
    private String type;
    private String location;
    private String source;
    private String notes;
    private ApplicationStatus status;
    private LocalDate dateApplied;

    // 📅 Supported date formats
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MD = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MD_DASH = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    // 🧩 Constructors
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

    // 🪞 Getters (public read-only access)
    public String getCompany()       { return company; }
    public String getRole()          { return role; }
    public String getType()          { return type; }
    public String getLocation()      { return location; }
    public String getSource()        { return source; }
    public String getNotes()         { return notes; }
    public ApplicationStatus getStatus() { return status; }
    public LocalDate getDateApplied(){ return dateApplied; }

    // 🛠️ Setters (controlled modification)
    public void setCompany(String v)       { this.company = v; }
    public void setRole(String v)          { this.role = v; }
    public void setType(String v)          { this.type = v; }
    public void setLocation(String v)      { this.location = v; }
    public void setSource(String v)        { this.source = v; }
    public void setNotes(String v)         { this.notes = v == null ? "" : v.trim(); }
    public void setStatus(ApplicationStatus v) { this.status = v; }
    public void setDateApplied(LocalDate v){ this.dateApplied = v; }

    // ✏️ Edit all details at once
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

    // 💾 Save to file format
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

    // 📝 Convert to Markdown table row
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

    // 🧩 Debug print helper
    @Override
    public String toString() {
        return String.format("%s - %s (%s, %s)",
                company, role, status.name(),
                dateApplied.format(MD));
    }
}
