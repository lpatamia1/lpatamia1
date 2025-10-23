package tracker.test;

import tracker.data.JobApplication;
import tracker.core.ApplicationStatus;
import tracker.core.FileManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.io.File;

import java.time.LocalDate;

public class JobApplicationTest {

    // ✅ Basic constructor and getter coverage
    @Test
    void testConstructorAndGetters() {
        JobApplication app = new JobApplication(
                "Capgemini",
                "Software Engineer",
                "Full-time",
                "Chicago, IL",
                "APPLIED",
                "10/21/2025",
                "LinkedIn",
                "Excited!"
        );

        assertEquals("Capgemini", app.getCompany());
        assertEquals("Software Engineer", app.getRole());
        assertEquals("Full-time", app.getType());
        assertEquals("Chicago, IL", app.getLocation());
        assertEquals(ApplicationStatus.APPLIED, app.getStatus());
        assertEquals("LinkedIn", app.getSource());
        assertEquals("Excited!", app.getNotes());
        assertEquals(LocalDate.of(2025, 10, 21), app.getDateApplied());
    }

    // ✅ Ensure setters properly update state
    @Test
    void testSettersUpdateValues() {
        JobApplication app = new JobApplication(
                "Company",
                "Dev",
                "Internship",
                "Remote",
                "APPLIED",
                "2025-10-22",
                "Website",
                ""
        );

        app.setCompany("Google");
        app.setRole("IT Support");
        app.setStatus(ApplicationStatus.INTERVIEW);
        app.setNotes("Phone interview scheduled");

        assertEquals("Google", app.getCompany());
        assertEquals("IT Support", app.getRole());
        assertEquals(ApplicationStatus.INTERVIEW, app.getStatus());
        assertEquals("Phone interview scheduled", app.getNotes());
    }

    // ✅ File and Markdown export behavior
    @Test
    void testToFileLineAndMarkdown() {
        JobApplication app = new JobApplication(
                "Walsh Group",
                "Project Engineer",
                "Full-time",
                "Chicago, IL",
                "HIRED",
                "10/10/2025",
                "Handshake",
                ""
        );

        String fileLine = app.toFileLine();
        assertTrue(fileLine.contains("Walsh Group"));
        assertTrue(fileLine.contains("HIRED"));

        String markdown = app.toMarkdownRow();
        assertTrue(markdown.contains("| Walsh Group |"));
        assertTrue(markdown.contains("Hired"));
    }

    // ✅ Editing method should update all fields
    @Test
    void testEditDetailsChangesAll() {
        JobApplication app = new JobApplication(
                "OldCo", "Old Role", "Intern", "Remote", "APPLIED", "2025-10-20", "Indeed"
        );

        app.editDetails("NewCo", "Engineer", "Full-time", "Chicago", "INTERVIEW", "2025-10-22", "LinkedIn", "Follow-up done");

        assertEquals("NewCo", app.getCompany());
        assertEquals("Engineer", app.getRole());
        assertEquals("Full-time", app.getType());
        assertEquals("Chicago", app.getLocation());
        assertEquals(ApplicationStatus.INTERVIEW, app.getStatus());
        assertEquals("LinkedIn", app.getSource());
        assertEquals("Follow-up done", app.getNotes());
        assertEquals(LocalDate.of(2025, 10, 22), app.getDateApplied());
    }

    // ✅ Robustness: handle bad date formats gracefully
    @Test
    void testInvalidDateFormatsFallback() {
        JobApplication app = new JobApplication(
                "ACME Corp", "Tester", "Full-time", "Remote", "APPLIED", "32/13/2025", "Email", "Invalid date test"
        );
        assertNotNull(app.getDateApplied(), "DateApplied should fallback to current date if invalid");
    }

    // ✅ Status parsing: ensure case-insensitive matching works
    @Test
    void testStatusParsingIsCaseInsensitive() {
        JobApplication app = new JobApplication(
                "Meta", "Analyst", "Full-time", "Menlo Park", "interview", "2025-10-21", "LinkedIn", ""
        );
        assertEquals(ApplicationStatus.INTERVIEW, app.getStatus());
    }

    // ✅ Equality and duplication prevention logic (if implemented)
    @Test
    void testEqualityOrDuplicateCheck() {
        JobApplication a1 = new JobApplication("Apple", "Engineer", "Full-time", "Cupertino", "APPLIED", "2025-10-21", "LinkedIn", "");
        JobApplication a2 = new JobApplication("Apple", "Engineer", "Full-time", "Cupertino", "APPLIED", "2025-10-21", "LinkedIn", "");
        assertNotSame(a1, a2);
        assertEquals(a1.getCompany(), a2.getCompany());
        assertEquals(a1.getDateApplied(), a2.getDateApplied());
    }

    // ✅ Markdown row should never be null or crash even with empty fields
    @Test
    void testMarkdownHandlesNulls() {
        JobApplication app = new JobApplication("", "", "", "", "APPLIED", "2025-10-21", "", null);
        String markdown = app.toMarkdownRow();
        assertNotNull(markdown);
        assertTrue(markdown.contains("|"));
    }

    // ✅ ApplicationStatus enum toString and from() helper validation
    @Test
    void testApplicationStatusEnum() {
        assertEquals(ApplicationStatus.APPLIED, ApplicationStatus.from("applied"));
        assertEquals(ApplicationStatus.REJECTED, ApplicationStatus.from("REJECTED"));
        assertEquals(ApplicationStatus.INTERVIEW, ApplicationStatus.from("Interview"));
        assertEquals("Hired", ApplicationStatus.HIRED.toString());
    }

    // ✅ Confirms new toString() for all statuses
    @Test
    void testPrettyPrintStatusFormatting() {
        assertEquals("Applied", ApplicationStatus.APPLIED.toString());
        assertEquals("Interview", ApplicationStatus.INTERVIEW.toString());
        assertEquals("Rejected", ApplicationStatus.REJECTED.toString());
        assertEquals("Hired", ApplicationStatus.HIRED.toString());
        assertEquals("Closed", ApplicationStatus.CLOSED.toString());
        assertEquals("Other", ApplicationStatus.OTHER.toString());
    }

    // ✅ Ensures toFileLine() and fromFileLine() are reversible and no data is lost or misread
    @Test
    void testFileLineRoundTripPreservesData() {
        JobApplication original = new JobApplication(
                "S&C Electric",
                "IT Analyst",
                "Full-time",
                "Chicago, IL",
                "APPLIED",
                "10/23/2025",
                "Company Site",
                "Excited about automation projects"
        );

        // Convert to file line, then back to object
        String line = original.toFileLine();
        JobApplication restored = JobApplication.fromFileLine(line);

        // Verify key fields remain identical
        assertNotNull(restored);
        assertEquals(original.getCompany(), restored.getCompany());
        assertEquals(original.getRole(), restored.getRole());
        assertEquals(original.getLocation(), restored.getLocation());
        assertEquals(original.getStatus(), restored.getStatus());
        assertEquals(original.getSource(), restored.getSource());
        assertEquals(original.getNotes(), restored.getNotes());
        assertEquals(original.getDateApplied(), restored.getDateApplied());
    }
    @Test
    void testCSVExportCreatesValidFile() throws Exception {
        // Prepare a single sample application
        List<JobApplication> applications = List.of(
            new JobApplication(
                "Loyola University Chicago",
                "Data Analyst Intern",
                "Internship",
                "Chicago, IL",
                "APPLIED",
                "10/23/2025",
                "Handshake",
                "Testing CSV export"
            )
        );

        // Export to CSV
        tracker.core.FileManager.exportCSV(applications);

        // Validate file existence and content
        File csv = new File("applications.csv");
        assertTrue(csv.exists(), "CSV file should exist after export");

        String content = new String(java.nio.file.Files.readAllBytes(csv.toPath()));
        assertTrue(content.contains("Loyola University Chicago"), "Company name should appear");
        assertTrue(content.contains("Data Analyst Intern"), "Role should appear");
        assertTrue(content.contains("APPLIED"), "Status should appear");
    }
}
