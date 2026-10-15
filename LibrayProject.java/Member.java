import java.util.ArrayList;
import java.util.List;

/** A library member. A member may hold at most MAX_LOANS items at once. */
public class Member {

    public static final int MAX_LOANS = 3;

    private final String id;
    private String name;
    private final List<Loan> activeLoans = new ArrayList<>();

    public Member(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Member id is required");
        }
        this.id = id.trim().toUpperCase();
        this.name = checkName(name);                // same validation as the setter
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) { this.name = checkName(name); }

    private static String checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name is required");
        }
        return name.trim();
    }

    public boolean canBorrow() { return activeLoans.size() < MAX_LOANS; }

    public int getLoanCount() { return activeLoans.size(); }

    /** A COPY, so outside code cannot add or remove loans behind our back (Lab 3 trap). */
    public List<Loan> getActiveLoans() { return List.copyOf(activeLoans); }

    // package-private: only Library changes a member's loans
    void addLoan(Loan loan) { activeLoans.add(loan); }
    void removeLoan(Loan loan) { activeLoans.remove(loan); }

    @Override
    public String toString() {
        return String.format("%-5s %s (%d/%d loans)", id, name, activeLoans.size(), MAX_LOANS);
    }
}