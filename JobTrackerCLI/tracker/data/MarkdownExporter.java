/**
 * 📘 Handles Markdown report generation for the Job Application Tracker.
 * 
 * This class builds the GitHub-ready README.md file that summarizes all job applications,
 * including analytics, stats, and tables. It replaces the exportMarkdown() logic from
 * JobApplicationTracker.java.
 *
 */

package tracker.data;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import tracker.core.*;
import tracker.ui.*;

public class MarkdownExporter {

    public static void export(List<JobApplication> applications, String outputPath) throws IOException {
        applications.sort(Comparator.comparing(JobApplication::getDateApplied).reversed());
        Stats s = Stats.compute(applications);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        StringBuilder md = new StringBuilder();

        // --- HEADER ---
        md.append("<div align=\"center\">\n");
        md.append("  <h1>🗂️ Job Application Tracker</h1>\n");
        md.append("</div>\n\n");
        md.append("A living record of my 2025 job applications, interview progress, and outcomes across **IT**, **Data**, and **Software Engineering** roles — alongside opportunities in design, research, education, public health, and community-focused organizations.\n\n");
        md.append("> *Includes submissions from LinkedIn, Indeed, Handshake, and recruiter referrals.*\n\n");
        md.append("---\n\n");

        // --- HIGHLIGHTS ---
        md.append("<div align=\"center\">\n  <h2>💡 Highlights</h2>\n</div>\n\n");
        md.append(String.format(
            "<p align=\"center\">\n" +
            "  <img src=\"https://img.shields.io/badge/Total-%d-blue\"> " +
            "  <img src=\"https://img.shields.io/badge/Active-%d-green\"> " +
            "  <img src=\"https://img.shields.io/badge/Rejected-%d-red\"> " +
            "  <img src=\"https://img.shields.io/badge/Interviews-%d-yellow\">\n</p>\n\n",
            s.total, s.trulyActive, s.rejected, s.interviews));

        md.append("<hr style='width:60%;border:1px solid #f0d7ff;margin:20px auto;'>\n\n");
        md.append(String.format(
            "So far, I've applied to **%d positions** across multiple industries. Currently, **%d applications remain active**, " +
            "and **%d likely inactive**, with **%d interview%s** completed.\n\n",
            s.total, s.trulyActive, s.stale, s.interviews, s.interviews == 1 ? "" : "s"));

        // --- APPLICATION OVERVIEW ---
        md.append("<div align=\"center\">\n  <h2>📊 Application Overview</h2>\n</div>\n\n");
        md.append("<table align=\"center\"><tr><td align=\"left\" width=\"50%\">\n\n");
        md.append("- **Total Applications:** ").append(s.total).append("  \n");
        md.append("- 🕐 **Active / Pending:** ").append(s.trulyActive).append("  \n");
        md.append("- ⏳ **Likely Inactive:** ").append(s.stale).append("  \n");
        md.append("- ❌ **Rejected:** ").append(s.rejected).append("  \n\n");
        md.append("</td><td align=\"left\" width=\"50%\">\n\n");
        md.append("- 💬 **Interviewed:** ").append(s.interviews).append("  \n");
        md.append("- ✅ **Hired / Offer:** ").append(s.hired).append("  \n");
        md.append("- 🗓️ **Last Updated:** ").append(today).append("  \n\n");
        md.append("</td></tr></table>\n\n");

        // --- ADDITIONAL INSIGHTS ---
        long avgDays = (long) applications.stream()
            .mapToLong(a -> ChronoUnit.DAYS.between(a.getDateApplied(), LocalDate.now()))
            .average().orElse(0);

        md.append("<div align=\"center\">\n  <h2>📈 Additional Insights</h2>\n</div>\n\n");
        md.append("<table align=\"center\"><tr><td align=\"left\">\n\n");
        md.append(String.format("- 📈 **Success Rate:** %.1f%%  \n", s.successRate));
        md.append(String.format("- 📦 **Closed:** %d  \n", s.closed));
        md.append(String.format("- 🌐 **Top Source:** %s  \n", s.topSource));
        md.append(String.format("- ⚡ **Avg Applications per Week:** %.1f  \n", s.perWeek));
        md.append(String.format("- 🕐 **Still Waiting (Applied Only):** %d  \n", s.openApps));
        md.append(String.format("- 📍 **Top Location:** %s  \n", s.topLocation));
        md.append(String.format("- 📆 **Avg Days Since Application:** %d days  \n", avgDays));
        if (s.latest != null)
            md.append(String.format("- 🆕 **Most Recent:** %s — %s (%s)  \n",
                s.latest.getCompany(), s.latest.getRole(),
                s.latest.getDateApplied().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
        md.append("</td></tr></table>\n\n");

        // --- CATEGORY SUMMARY ---
        Map<String, Long> byCategory = applications.stream()
            .collect(Collectors.groupingBy(a -> simplifyType(a.getType()), TreeMap::new, Collectors.counting()));

        md.append("<div align=\"center\">\n  <h2>🧾 Breakdown by Job Type</h2>\n</div>\n\n");
        List<Map.Entry<String, Long>> entries = new ArrayList<>(byCategory.entrySet());
        entries.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        int mid = (entries.size() + 1) / 2;
        md.append("<table align=\"center\"><tr><td valign='top' align='left'>\n\n");
        for (int i = 0; i < mid; i++)
            md.append(String.format("• **%s:** %d<br>\n", entries.get(i).getKey(), entries.get(i).getValue()));
        md.append("</td><td valign='top' align='left'>\n\n");
        for (int i = mid; i < entries.size(); i++)
            md.append(String.format("• **%s:** %d<br>\n", entries.get(i).getKey(), entries.get(i).getValue()));
        md.append("</td></tr></table>\n\n");

        // --- MASTER LOG ---
        md.append("<div align=\"center\">\n  <h2>📋 Master Application Log</h2>\n</div>\n\n");
        md.append("<details>\n<summary>Click to expand full job application list</summary>\n\n");
        md.append("| Company | Role | Type | Location | Status | Date Applied | Source | Notes |\n");
        md.append("|----------|------|------|-----------|----------|---------------|---------|--------|\n");
        for (JobApplication a : applications) md.append(a.toMarkdownRow()).append("\n");
        md.append("\n</details>\n\n");

        md.append(String.format(
            "**Summary:** 📋 %d total — 🕐 %d active — ⏳ %d likely inactive — ❌ %d rejected — 💬 %d interviews — ✅ %d hired.**\n\n",
            s.total, s.trulyActive, s.stale, s.rejected, s.interviews, s.hired));
        md.append("---\n🌸 *Maintained by lpatamia1 — powered by the Java Job Application Tracker.*\n");
        md.append("*Last updated ").append(today).append(".*\n");

        // --- WRITE FILE ---
        try (FileWriter w = new FileWriter(outputPath)) {
            w.write(md.toString());
        }

        System.out.printf("📁 Markdown exported: %s (%d jobs)%n", outputPath, s.total);
    }

    // 🧭 Helper: Simplify job type names
    private static String simplifyType(String rawType) {
        String t = rawType.toLowerCase();
        if (t.contains("software")) return "Software / Development";
        if (t.contains("ai") || t.contains("machine learning")) return "AI / Data Science";
        if (t.contains("data")) return "Data / Analytics";
        if (t.contains("it")) return "IT / Support";
        if (t.contains("cloud")) return "Cloud / Infrastructure";
        if (t.contains("admin")) return "Administration";
        if (t.contains("business")) return "Business / Operations";
        if (t.contains("finance") || t.contains("insurance")) return "Finance";
        if (t.contains("security")) return "Cybersecurity / Infrastructure";
        if (t.contains("education")) return "Education / Training";
        if (t.contains("animal")) return "Animal Care";
        if (t.contains("healthcare") || t.contains("medical") || t.contains("biotech")) return "Healthcare / Life Sciences";
        if (t.contains("policy") || t.contains("research")) return "Policy / Research";
        if (t.contains("retail") || t.contains("service") || t.contains("customer")) return "Retail / Service";
        if (t.contains("environmental") || t.contains("energy")) return "Environmental / Sustainability";
        if (t.contains("marketing") || t.contains("communications")) return "Marketing / Communications";
        if (t.contains("nonprofit")) return "Nonprofit / Community";
        if (t.contains("engineering")) return "Engineering";
        if (t.contains("culinary") || t.contains("food")) return "Culinary / Food Service";
        if (t.contains("arts")) return "Arts / Creative";
        if (t.contains("product")) return "Product / Design";
        if (t.contains("automation")) return "Automation / Robotics";
        if (t.contains("gis")) return "GIS / Mapping";
        return "Other";
    }
}
