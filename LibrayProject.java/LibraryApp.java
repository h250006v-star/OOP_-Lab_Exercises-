import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * The menu-driven program. It only talks to the user; all the rules live in Library.
 *
 * Run:  java LibraryApp                 (uses today's date)
 *       java LibraryApp 2026-12-01      (pretends today is that date, handy for testing overdue items)
 * Data is saved in the "data" folder when you choose 0 (Save and exit) and reloaded next start.
 */
public class LibraryApp {

    private final Library library = new Library();
    private final FileStore store = new FileStore(Path.of("data"));
    private final Scanner in = new Scanner(System.in);
    private final LocalDate today;

    public LibraryApp(LocalDate today) { this.today = today; }

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        if (args.length > 0) {
            try {
                today = LocalDate.parse(args[0]);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date \"" + args[0] + "\". Use the form yyyy-MM-dd.");
                return;
            }
        }
        new LibraryApp(today).run();
    }

    // ============================================================ main loop
    private void run() {
        System.out.println("=== Library Management System ===");
        System.out.println("Today's date for this session: " + today);
        loadData();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = ask("Choose an option: ");
            if (choice == null) {                        // input ended (e.g. piped input): save and leave
                saveData();
                break;
            }
            try {
                switch (choice) {
                    case "1" -> addItem();
                    case "2" -> registerMember();
                    case "3" -> borrowItem();
                    case "4" -> returnItem();
                    case "5" -> listOverdue();
                    case "6" -> searchItems();
                    case "7" -> listItems();
                    case "8" -> listMembers();
                    case "0" -> running = !saveData();   // stay in the menu if saving failed
                    default  -> System.out.println("Please choose a number from 0 to 8.");
                }
            } catch (ItemNotAvailableException | MemberLimitExceededException e) {
                System.out.println("Cannot borrow: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add item");
        System.out.println("2. Register member");
        System.out.println("3. Borrow item");
        System.out.println("4. Return item");
        System.out.println("5. List overdue items");
        System.out.println("6. Search items by title");
        System.out.println("7. List all items");
        System.out.println("8. List members and their loans");
        System.out.println("0. Save and exit");
    }

    // ============================================================ menu actions
    private void addItem() {
        String type = ask("Type - (B)ook or (D)vd: ").toUpperCase();
        String id = ask("Item id (e.g. B001): ");
        String title = ask("Title: ");
        LibraryItem item;
        if (type.startsWith("B")) {
            item = new Book(id, title, ask("Author: "));
        } else if (type.startsWith("D")) {
            String minutes = ask("Duration in minutes: ");
            try {
                item = new Dvd(id, title, Integer.parseInt(minutes));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Duration must be a whole number");
            }
        } else {
            throw new IllegalArgumentException("Type must be B or D");
        }
        library.addItem(item);
        System.out.println("Added: " + item);
    }

    private void registerMember() {
        Member m = new Member(ask("Member id (e.g. M001): "), ask("Name: "));
        library.registerMember(m);
        System.out.println("Registered: " + m);
    }

    private void borrowItem() throws ItemNotAvailableException, MemberLimitExceededException {
        String memberId = ask("Member id: ");
        String itemId = ask("Item id: ");
        Loan loan = library.borrow(memberId, itemId, today);
        System.out.println("Borrowed. Due back on " + loan.getDueDate()
            + " (" + loan.getItem().loanPeriodDays() + "-day loan).");
    }

    private void returnItem() {
        Loan loan = library.giveBack(ask("Item id to return: "));
        System.out.print("Returned \"" + loan.getItem().getTitle() + "\" from " + loan.getMember().getName() + ".");
        if (loan.isOverdue(today)) {
            System.out.print(" It was " + loan.daysOverdue(today) + " day(s) overdue.");
        }
        System.out.println();
    }

    private void listOverdue() {
        List<Loan> overdue = library.listOverdue(today);
        if (overdue.isEmpty()) {
            System.out.println("No overdue items.");
            return;
        }
        System.out.println("Overdue items as of " + today + ":");
        for (Loan l : overdue) {
            System.out.println("  " + l + "  [" + l.daysOverdue(today) + " day(s) overdue]");
        }
    }

    private void searchItems() {
        List<LibraryItem> found = library.searchByTitle(ask("Search text: "));
        if (found.isEmpty()) {
            System.out.println("No items match.");
        } else {
            found.forEach(i -> System.out.println("  " + i));
        }
    }

    private void listItems() {
        List<LibraryItem> all = library.getItems();
        if (all.isEmpty()) System.out.println("No items yet.");
        all.forEach(i -> System.out.println("  " + i));
    }

    private void listMembers() {
        List<Member> all = library.getMembers();
        if (all.isEmpty()) System.out.println("No members yet.");
        for (Member m : all) {
            System.out.println("  " + m);
            for (Loan l : m.getActiveLoans()) {
                System.out.println("      " + l.getItem().getTitle() + " (due " + l.getDueDate() + ")");
            }
        }
    }

    // ============================================================ persistence
    private void loadData() {
        try {
            List<String> warnings = store.load(library);
            warnings.forEach(w -> System.out.println("Warning: " + w));
            System.out.println("Loaded " + library.getItems().size() + " item(s), "
                + library.getMembers().size() + " member(s), " + library.getLoans().size() + " loan(s).");
        } catch (IOException e) {
            System.out.println("Could not read saved data: " + e.getMessage() + ". Starting empty.");
        }
    }

    /** @return true if the data was saved */
    private boolean saveData() {
        try {
            store.save(library);
            System.out.println("Data saved to the \"data\" folder.");
            return true;
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage() + " (choose 0 again to retry)");
            return false;
        }
    }

    // ============================================================ input helper
    /** Prints a prompt and reads one line. Returns null if the input has ended. */
    private String ask(String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) return null;
        String line = in.nextLine().trim();
        if (System.console() == null) System.out.println(line);   // echo, so piped sessions read like a real one
        return line;
    }
}