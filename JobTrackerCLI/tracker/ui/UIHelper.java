/**
 * 🌸 UIHelper.java — color palette + ASCII UI element
 * 
 * Centralized helper for CLI colors and ASCII-based UI elements
 * used across the Job Application Tracker. Keeps the interface visually
 * consistent, readable, and fun.
 * 
 * - Stores reusable color codes (ANSI + RGB pastel).
 * - Provides animated cat intro sequence (`catIntro()`).
 * - Prints example echo commands for appending data.
 * - Designed for cross-platform CLI display (best on Unix/macOS).
 */

package tracker.ui;

public class UIHelper {

    // 🎨 CLI Colors
    public static final String RESET  = "\u001B[0m";
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    // 💅 Pastel Theme
    public static final String ORANGE = "\u001B[38;2;255;165;0m";
    public static final String PEACH  = "\u001B[38;2;255;200;150m";
    public static final String PINK   = "\u001B[38;2;255;105;180m";
    public static final String BRIGHT_ORANGE = "\u001B[38;2;255;200;60m";
    public static final String LAVENDER      = "\u001B[38;2;200;160;255m";
    public static final String MINT          = "\u001B[38;2;152;255;204m";
    public static final String TEAL          = "\u001B[38;2;0;191;188m";

    // 🐱 Intro Animation
    public static void catIntro() {
        String pad = " ".repeat(35);
        String[] frames = {
            "\n" + CYAN + pad + "  ／l、\n" +
            pad + "（=‐ ω ‐=） zzz...\n" +
            pad + "  じしf_, )ノ" + RESET,

            CYAN + pad + "  ／l、\n" +
            pad + "（=・ω・=） blink blink\n" +
            pad + "  じしf_, )ノ" + RESET,

            CYAN + pad + "  ／l、\n" +
            pad + "（=｀ω´ =） ready to work!\n" +
            pad + "  じしf_, )ノ" + RESET
        };

        for (String frame : frames) {
            System.out.print("\r\033[3A\033[J");
            System.out.println(frame);
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        }

        System.out.print("\r\033[3A\033[J");
        System.out.println(PINK +
        "\n╭─────────────────────────────────────────────────────────────────────────────────────────╮\n" +
        "│                        🐾 Meow! Time to check your job hunt 💼                          │\n" +
        "╰─────────────────────────────────────────────────────────────────────────────────────────╯" +
        RESET);
    }

    // 💡 Echo instructions
    public static void printEchoInstructions() {
        System.out.println("\n💡 To append new jobs from the shell (outside the program):\n");
        System.out.println("echo \"[Eataly](https://www.eataly.com/us_en/)|Cashier / Front End Associate – Seasonal|Retail / Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("echo \"[MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers)|Show Ambassador (Weekends Only)|Retail / Customer Service|Chicago, IL|Applied|10/15/2025|LinkedIn\" >> applications.txt");
        System.out.println("\nThen run option 3 in the tracker menu to regenerate the README.\n");
    }
}
