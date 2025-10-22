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

public enum ApplicationStatus {
    APPLIED, INTERVIEW, REJECTED, HIRED, CLOSED, OTHER;

    public static ApplicationStatus from(String status) {
        String s = status.toLowerCase();
        if (s.contains("interview")) return INTERVIEW;
        if (s.contains("reject")) return REJECTED;
        if (s.contains("hire") || s.contains("offer")) return HIRED;
        if (s.contains("closed")) return CLOSED;
        if (s.contains("applied")) return APPLIED;
        return OTHER;

    }
}
