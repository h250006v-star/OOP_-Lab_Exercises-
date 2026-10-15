import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves a Library to three CSV files and loads it back (Lab 10).
 *   items.csv   : type,id,title,extra
 *   members.csv : id,name
 *   loans.csv   : memberId,itemId,borrowDate,dueDate
 * Loading order matters: items and members first, then loans (which refer to both).
 */
public class FileStore {

    private final Path dir;

    public FileStore(Path dir) { this.dir = dir; }

    // ============================================================ SAVE
    public void save(Library library) throws IOException {
        Files.createDirectories(dir);

        // try-with-resources closes each writer automatically, even if an exception occurs
        try (BufferedWriter w = Files.newBufferedWriter(dir.resolve("items.csv"))) {
            w.write("type,id,title,extra");
            w.newLine();
            for (LibraryItem i : library.getItems()) {
                w.write(String.join(",", i.typeName(), escape(i.getId()),
                                    escape(i.getTitle()), escape(i.extraInfo())));
                w.newLine();
            }
        }
        try (BufferedWriter w = Files.newBufferedWriter(dir.resolve("members.csv"))) {
            w.write("id,name");
            w.newLine();
            for (Member m : library.getMembers()) {
                w.write(escape(m.getId()) + "," + escape(m.getName()));
                w.newLine();
            }
        }
        try (BufferedWriter w = Files.newBufferedWriter(dir.resolve("loans.csv"))) {
            w.write("memberId,itemId,borrowDate,dueDate");
            w.newLine();
            for (Loan l : library.getLoans()) {
                w.write(String.join(",", escape(l.getMember().getId()), escape(l.getItem().getId()),
                                    l.getBorrowDate().toString(), l.getDueDate().toString()));
                w.newLine();
            }
        }
    }

    // ============================================================ LOAD
    /**
     * Fills the (empty) library from the files. A missing file simply means "first run".
     * A damaged line is skipped and reported, so one bad line cannot stop the whole load.
     * @return a list of warning messages (empty if everything loaded cleanly)
     */
    public List<String> load(Library library) throws IOException {
        List<String> warnings = new ArrayList<>();

        for (String[] row : readRows("items.csv", warnings)) {
            // each row is guarded separately
            tryRow("items.csv", row, warnings, () ->
                library.addItem(createItem(row[0], row[1], row[2], row[3])));
        }
        for (String[] row : readRows("members.csv", warnings)) {
            tryRow("members.csv", row, warnings, () ->
                library.registerMember(new Member(row[0], row[1])));
        }
        for (String[] row : readRows("loans.csv", warnings)) {
            tryRow("loans.csv", row, warnings, () ->
                library.restoreLoan(row[0], row[1], LocalDate.parse(row[2]), LocalDate.parse(row[3])));
        }
        return warnings;
    }

    /** FACTORY (Lab 11): turns a saved type name into the right subclass. */
    static LibraryItem createItem(String type, String id, String title, String extra) {
        return switch (type.trim().toUpperCase()) {
            case "BOOK" -> new Book(id, title, extra);
            case "DVD"  -> new Dvd(id, title, Integer.parseInt(extra.trim()));
            default     -> throw new IllegalArgumentException("Unknown item type: " + type);
        };
    }

    private interface RowAction { void run(); }

    private void tryRow(String file, String[] row, List<String> warnings, RowAction action) {
        try {
            action.run();
        } catch (IllegalArgumentException | IndexOutOfBoundsException | DateTimeException e) {
            warnings.add(file + ": skipped line [" + String.join(",", row) + "] - " + e.getMessage());
        }
    }

    /** Reads every data line (header skipped) as a list of fields. Missing file = no rows. */
    private List<String[]> readRows(String fileName, List<String> warnings) throws IOException {
        Path file = dir.resolve(fileName);
        List<String[]> rows = new ArrayList<>();
        if (!Files.exists(file)) return rows;

        try (BufferedReader r = Files.newBufferedReader(file)) {
            r.readLine();                                   // skip the header line
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                rows.add(split(line).toArray(new String[0]));
            }
        }
        return rows;
    }

    // ============================================================ CSV helpers
    /** Wraps a field in quotes if it contains a comma or a quote ("" stands for one quote). */
    static String escape(String field) {
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /** Splits one CSV line into fields, understanding quoted fields. */
    static List<String> split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');                    // "" inside quotes = a literal quote
                    i++;
                } else if (c == '"') {
                    inQuotes = false;
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}