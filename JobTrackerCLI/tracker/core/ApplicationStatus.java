/**
 * Enum for standardized job application statuses.
 * 
 * Used to replace fragile string comparisons with consistent, type-safe values.
 * The {@code from()} method maps free-form text (e.g. "Offer received", "Not selected")
 * to the closest matching status constant.
 *
 * Example:
 *   ApplicationStatus.from("Interview scheduled") → INTERVIEW
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
     * Uses Locale.ROOT for consistent lowercase conversions.
     */
    public static ApplicationStatus from(String status) {
        if (status == null || status.isBlank()) return OTHER;
        String s = status.toLowerCase(Locale.ROOT).trim();
        if (s.contains("interview")) return INTERVIEW;
        if (s.contains("reject")) return REJECTED;
        if (s.contains("hire") || s.contains("offer")) return HIRED;
        if (s.contains("closed")) return CLOSED;
        if (s.contains("applied")) return APPLIED;
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
