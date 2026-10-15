/**
 * Abstract parent of everything the library lends (Book, Dvd).
 * It holds what all items share and forces each subclass to supply
 * its own loan period (polymorphism).
 */
public abstract class LibraryItem implements Borrowable {

    private final String id;
    private final String title;
    private boolean available = true;

    protected LibraryItem(String id, String title) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Item id is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Item title is required");
        }
        this.id = id.trim().toUpperCase();      // ids are not case-sensitive for the user
        this.title = title.trim();
    }

    // ---- what each subclass must decide for itself (abstraction) ----
    public abstract int loanPeriodDays();      // how long this kind of item may be kept
    public abstract String typeName();         // "BOOK" or "DVD" (also written to the CSV)
    public abstract String extraInfo();        // raw extra field for the CSV (author / minutes)
    protected abstract String details();       // human-readable extra detail for toString

    // ---- getters (encapsulation: fields stay private) ----
    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return available; }

    // package-private on purpose: only the library package code (Library, FileStore) may force it
    void setAvailable(boolean available) { this.available = available; }

    // ---- Borrowable ----
    @Override
    public void borrow(Member member) throws ItemNotAvailableException, MemberLimitExceededException {
        // Both checks happen BEFORE any change, so a failed borrow leaves everything untouched.
        if (!available) {
            throw new ItemNotAvailableException(id,
                "\"" + title + "\" (" + id + ") is already on loan");
        }
        if (!member.canBorrow()) {
            throw new MemberLimitExceededException(member.getId(), Member.MAX_LOANS,
                member.getName() + " already has " + Member.MAX_LOANS + " loans (the maximum)");
        }
        available = false;
    }

    @Override
    public void giveBack() {
        available = true;
    }

    @Override
    public String toString() {
        return String.format("%-4s %-6s %s (%s) - %s",
            typeName(), id, title, details(), available ? "available" : "ON LOAN");
    }
}