import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The core service: owns the items, members and loans and enforces the rules.
 * It knows nothing about the menu or about files (single responsibility).
 */
public class Library {

    // Programmed to the Map interface; keyed by id; LinkedHashMap keeps insertion order for neat listings
    private final Map<String, LibraryItem> items = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Loan> loans = new ArrayList<>();      // active loans only

    // ------------------------------------------------------------ adding
    public void addItem(LibraryItem item) {
        if (items.containsKey(item.getId())) {
            throw new IllegalArgumentException("An item with id " + item.getId() + " already exists");
        }
        items.put(item.getId(), item);
    }

    public void registerMember(Member member) {
        if (members.containsKey(member.getId())) {
            throw new IllegalArgumentException("A member with id " + member.getId() + " already exists");
        }
        members.put(member.getId(), member);
    }

    // ------------------------------------------------------------ lookups
    public LibraryItem findItem(String itemId) {
        LibraryItem item = items.get(key(itemId));
        if (item == null) throw new IllegalArgumentException("No item with id " + itemId);
        return item;
    }

    public Member findMember(String memberId) {
        Member member = members.get(key(memberId));
        if (member == null) throw new IllegalArgumentException("No member with id " + memberId);
        return member;
    }

    private static String key(String id) {
        return id == null ? "" : id.trim().toUpperCase();
    }

    // ------------------------------------------------------------ borrowing
    public Loan borrow(String memberId, String itemId)
            throws ItemNotAvailableException, MemberLimitExceededException {
        return borrow(memberId, itemId, LocalDate.now());
    }

    /** Same as above but with an explicit "today", which makes overdue behaviour easy to test. */
    public Loan borrow(String memberId, String itemId, LocalDate today)
            throws ItemNotAvailableException, MemberLimitExceededException {
        // 1. look up member and item (IllegalArgumentException if missing)
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);

        // 2. the item checks availability and the member's limit; it throws BEFORE changing anything
        item.borrow(member);

        // 3. only now create the loan and record it everywhere
        Loan loan = new Loan(item, member, today);
        member.addLoan(loan);
        loans.add(loan);
        return loan;
    }

    public Loan giveBack(String itemId) {
        LibraryItem item = findItem(itemId);
        Loan loan = loans.stream()
            .filter(l -> l.getItem() == item)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "\"" + item.getTitle() + "\" is not currently on loan"));

        item.giveBack();
        loan.getMember().removeLoan(loan);
        loans.remove(loan);
        return loan;
    }

    // ------------------------------------------------------------ streams
    public List<LibraryItem> searchByTitle(String text) {
        String needle = text == null ? "" : text.trim().toLowerCase();
        return items.values().stream()
            .filter(i -> i.getTitle().toLowerCase().contains(needle))
            .sorted(Comparator.comparing(LibraryItem::getTitle, String.CASE_INSENSITIVE_ORDER))
            .toList();
    }

    public List<Loan> listOverdue(LocalDate today) {
        return loans.stream()
            .filter(l -> l.isOverdue(today))
            .sorted(Comparator.comparing(Loan::getDueDate))
            .toList();
    }

    // ------------------------------------------------------------ read-only views
    public List<LibraryItem> getItems() { return List.copyOf(items.values()); }
    public List<Member> getMembers() { return List.copyOf(members.values()); }
    public List<Loan> getLoans() { return List.copyOf(loans); }

    // ------------------------------------------------------------ used when reloading from file
    void restoreLoan(String memberId, String itemId, LocalDate borrowDate, LocalDate dueDate) {
        Member member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        if (!item.isAvailable()) {
            throw new IllegalArgumentException("Item " + item.getId() + " is already on loan");
        }
        Loan loan = new Loan(item, member, borrowDate, dueDate);
        item.setAvailable(false);
        member.addLoan(loan);
        loans.add(loan);
    }
}