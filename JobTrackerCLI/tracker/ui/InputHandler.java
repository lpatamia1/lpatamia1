package tracker.ui;

import tracker.data.*;
import tracker.core.*;

import java.util.*;

public class InputHandler {

    public static JobApplication createApplicationFromInput(Scanner sc) {
        System.out.print("Company (Markdown link ok): ");
        String company = sc.nextLine();
        System.out.print("Role: ");
        String role = sc.nextLine();
        System.out.print("Type: ");
        String type = sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();
        System.out.print("Status: ");
        String status = sc.nextLine();
        System.out.print("Date Applied (MM/DD/YYYY or YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Source: ");
        String source = sc.nextLine();
        System.out.print("Notes (optional): ");
        String notes = sc.nextLine();

        return new JobApplication(company, role, type, location, status, date, source, notes);
    }
}
