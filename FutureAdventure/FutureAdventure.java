/*
 *  Short, text-based "choose your own adventure" game written in Java. 
 *  You make choices by typing letters, guiding a character through a short sci-fi story. 
 *  Your decisions create branching paths that lead to one of several different endings, and the game uses ASCII art to set the mood.
 */
import java.util.Scanner;

public class FutureAdventure {

    // ASCII Art for different scenes
    private static final String ART_WELCOME = 
          "              ,----------------,              ,---------, \n"
        + "         ,-----------------------,          ,\"        ,\"| \n"
        + "       ,\"                      ,\"|        ,\"        ,\"  | \n"
        + "      +-----------------------+  |      ,\"        ,\"    | \n"
        + "      |  .-----------------.  |  |     +---------+      | \n"
        + "      |  |                 |  |  |     | -==----'|      | \n"
        + "      |  |  WELCOME TO THE |  |  |     |         |      | \n"
        + "      |  |  FUTURE - 3433  |  |  |/----|`---=   '|      | \n"
        + "      |  |                 |  |  |   ,/|==== ooo |      ; \n"
        + "      |  |                 |  |  |  // |(((([3]))|    / \n"
        + "      |  '-----------------'  |,' .;'| |((((   ))|  ,' \n"
        + "      +-----------------------+  ;;  | |         |,'   \n"
        + "         /_)______________(_/  //'   | +---------+ \n"
        + "    ___________________________/___  `, \n"
        + "   /  oooooooooooooooo  .o.  oooo /,   \\,\"----------- \n"
        + " /_==__==========__==_ooo__ooo=_/'   /___________,\"";

    private static final String ART_HOUSE =
          "                 /--\\ \n"
        + "                /    \\ \n"
        + "               /      \\ \n"
        + "              /________\\ \n"
        + "              |   __   | \n"
        + "              |  |  |  | \n"
        + "              |  |__|  | \n"
        + "              |________| ";

    private static final String ART_FOREST = 
          "          &&& &&  & && \n"
        + "      && &\\/&\\|& ()|/ @, && \n"
        + "      &\\/(/&/&||/& /_/)_&/_& \n"
        + "   &() &\\/&|()|/&\\/ '%'. &() \n"
        + "  &_\\_&&_\\ |& |&&/&__%_/_& && \n"
        + "&&   && & &| &| /& & % ()& /&& \n"
        + " ()&_---()&\\&|&&-&&--%---()~ \n"
        + "     &&     \\||| \n"
        + "             ||| \n"
        + "             ||| \n"
        + "             ||| \n"
        + "       , -=-~'-^-^-'~=-. ";
        
    private static final String ART_GAME_OVER = 
        "  ____                         ___\n" + //
        " / ___| __ _ _ __ ___   ___   / _ \\__   __ ___  _ ___                \n" + // 
        "| | __ / _` | '_ ` _ \\ / _ \\ | | | \\ \\ / // _ \\| /-\\_|    \n" + //
        "| |_| | (_| | | | | | |  __/ | |_| |\\ V /|  __/| |    _      \n" + // 
        " \\____|\\__,_|_| |_| |_|\\___|  \\___/  \\_/  \\___||_|   |_|          \n"

        + "                                         ";

    /**
     * The main method controls the game loop.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playAgain;

        do {
            playGame(scanner); // Run one instance of the game
            
            System.out.println("\n==============================================");
            System.out.print("Do you want to play again? (y/n) >> ");
            playAgain = scanner.nextLine();
            
        } while (playAgain.equalsIgnoreCase("y"));

        System.out.println("\nThanks for playing! Goodbye.");
        scanner.close(); // Close the scanner once the loop is finished
    }

    /**
     * Contains the core logic for a single playthrough of the adventure game.
     * @param scanner The Scanner object to read user input.
     */
    public static void playGame(Scanner scanner) {
        System.out.println(ART_WELCOME);
        System.out.println("\n==============================================");
        System.out.println("You wake up under a strange, purple sky... Gravity feels lighter.");
        System.out.println("Ahead of you is a glowing HOUSE. Behind you is a DARK FOREST.");
        System.out.println("What do you do?");
        System.out.println("[a] Walk towards the house");
        System.out.println("[b] Call out to the house");
        System.out.println("[c] Explore the forest");
        System.out.print(">> ");
        String choice1 = scanner.nextLine();

        if (choice1.equalsIgnoreCase("a")) {
            // ================== HOUSE PATH ==================
            System.out.println(ART_HOUSE);
            System.out.println("\nYou walk up to the house. The door slides open silently.");
            System.out.println("Inside is a single, humming SUPERCOMPUTER.");
            System.out.println("A calm, synthesized voice speaks: 'To proceed, answer my riddle.'");
            System.out.println("\n'I have cities, but no houses.'");
            System.out.println("'I have mountains, but no trees.'");
            System.out.println("'I have water, but no fish.'");
            System.out.println("'What am I?'");
            System.out.print("Your answer >> ");
            String riddleAnswer = scanner.nextLine();

            if (riddleAnswer.equalsIgnoreCase("a map") || riddleAnswer.equalsIgnoreCase("map")) {
                System.out.println("\n'Correct,' the computer states. 'Your logic is sound. You may access the archive.'");
                System.out.println("You are safe and have access to the knowledge of the past.");
                System.out.println("====== ENDING 1: THE ARCHIVIST ======");
            } else {
                System.out.println("\n'Incorrect. The illogical must be purged.'");
                System.out.println("A high-energy beam envelops you. You disintegrate instantly.");
                System.out.println(ART_GAME_OVER);
                System.out.println("====== ENDING 2: DEEMED UNWORTHY ======");
            }
        } 
        else if (choice1.equalsIgnoreCase("b")) {
            // ================== CALLING OUT PATH ==================
            System.out.println("\nYou shout, 'Hello?!' Your voice is swallowed by the silence.");
            System.out.println("Suddenly, the ground beneath you glows with a bright red grid pattern.");
            System.out.println("A voice from the house booms: 'Perimeter breach detected. Subject will be neutralized.'");
            System.out.println("Do you stand your ground or run for the forest?");
            System.out.println("[a] Stand your ground");
            System.out.println("[b] Run for the forest");
            System.out.print(">> ");
            String callChoice = scanner.nextLine();

            if (callChoice.equalsIgnoreCase("a")) {
                System.out.println("\nYou stand defiantly as the light intensifies, vaporizing you.");
                System.out.println(ART_GAME_OVER);
                System.out.println("====== ENDING 3: VAPORIZED BY DEFENSES ======");
            } else {
                System.out.println("\nYou turn to run, but the system is too fast. A laser wall appears in your path.");
                System.out.println(ART_GAME_OVER);
                System.out.println("====== ENDING 3: VAPORIZED BY DEFENSES ======");
            }
        } 
        else if (choice1.equalsIgnoreCase("c")) {
            // ================== FOREST PATH ==================
            System.out.println(ART_FOREST);
            System.out.println("\nYou wander into the eerie, bioluminescent forest and discover glowing berries.");
            System.out.println("A small, furry creature with large eyes watches you from a branch.");
            System.out.println("Do you EAT the berries or OFFER one to the creature?");
            System.out.println("[a] Eat the berries");
            System.out.println("[b] Offer a berry to the creature");
            System.out.print(">> ");
            String forestChoice = scanner.nextLine();

            if (forestChoice.equalsIgnoreCase("a")) {
                System.out.println("\nYou pop a berry in your mouth. It's incredibly sour! Your vision blurs.");
                System.out.println("The last thing you see is the small creature shaking its head in pity.");
                System.out.println(ART_GAME_OVER);
                System.out.println("====== ENDING 4: POISONED BERRIES ======");
            } else {
                System.out.println("\nYou hold out a berry. The creature scampers down, sniffs it, and chirps happily.");
                System.out.println("It gestures for you to follow. It leads you to a hidden sanctuary deep in the woods.");
                System.out.println("You have found a new home, protected by the forest's inhabitants.");
                System.out.println("====== ENDING 5: FRIEND OF THE FOREST ======");
            }
        } 
        else {
            // ================== INDECISION PATH ==================
            System.out.println("\nYou hesitate for too long. The strange atmosphere is not breathable long-term.");
            System.out.println("You collapse as your lungs give out.");
            System.out.println(ART_GAME_OVER);
            System.out.println("====== ENDING 6: FATAL INDECISION ======");
        }
    }
}