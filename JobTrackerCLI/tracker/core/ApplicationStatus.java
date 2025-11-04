/**
 * Enum for standardized job application statuses.
 * 
 * Expanded to align with all badge types shown in the CLI (technical, final, ghosted, etc.).
 * Enables direct status entry like "Technical" or "Final Interview" without errors.
 *
 * Example:
 *   ApplicationStatus.from("technical interview") → TECHNICAL
 *   ApplicationStatus.from("Offer received") → HIRED
 */

package tracker.core;

import java.util.Locale;

public enum ApplicationStatus {
    APPLIED,
    INTERVIEW,
    TECHNICAL,
    FINAL,
    PHONE,
    REAPPLY,
    REJECTED,
    HIRED,
    CLOSED,
    GHOSTED,
    OTHER;

    /**
     * Maps a string to a matching status, case-insensitively.
     * Handles flexible text input for CLI quick edits.
     */
    public static ApplicationStatus from(String status) {
        if (status == null || status.isBlank()) return OTHER;
        String s = status.toLowerCase(Locale.ROOT).trim();

        // 🎯 Rejections & closures
        if (s.contains("reject") || s.contains("decline") || s.contains("not selected")) return REJECTED;
        if (s.contains("close")) return CLOSED;

        // 🎉 Positive outcomes
        if (s.contains("hire") || s.contains("offer")) return HIRED;
        if (s.contains("reapply")) return REAPPLY;

        // 🎤 Stages
        if (s.contains("final")) return FINAL;
        if (s.contains("tech")) return TECHNICAL;
        if (s.contains("phone")) return PHONE;
        if (s.contains("interview")) return INTERVIEW;

        // ⏳ Other common inputs
        if (s.contains("apply") || s.contains("submitted") || s.contains("sent")) return APPLIED;
        if (s.contains("ghost")) return GHOSTED;

        // Fallback
        return OTHER;
    }

    /**
     * Formats enum names into human-friendly titles (e.g. HIRED → Hired).
     */
    @Override
    public String toString() {
        String lower = name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
