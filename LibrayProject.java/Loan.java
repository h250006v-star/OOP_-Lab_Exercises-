import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** One item borrowed by one member, with the date it was borrowed and the date it is due. */
public class Loan {

    private final LibraryItem item;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    /** New loan: the due date comes from the item's own loan period (polymorphism). */
    Loan(LibraryItem item, Member member, LocalDate borrowDate) {
        this(item, member, borrowDate, borrowDate.plusDays(item.loanPeriodDays()));
    }

    /** Used when reloading a saved loan, so the original due date is kept. */
    Loan(LibraryItem item, Member member, LocalDate borrowDate, LocalDate dueDate) {
        if (item == null || member == null || borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Loan needs an item, member and dates");
        }
        if (dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Due date cannot be before the borrow date");
        }
        this.item = item;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public LibraryItem getItem() { return item; }
    public Member getMember() { return member; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }

    /** Overdue means strictly after the due date (due today is not yet overdue). */
    public boolean isOverdue(LocalDate today) { return today.isAfter(dueDate); }

    public long daysOverdue(LocalDate today) {
        return Math.max(0, ChronoUnit.DAYS.between(dueDate, today));
    }

    @Override
    public String toString() {
        return String.format("%s \"%s\" -> %s %s, borrowed %s, due %s",
            item.typeName(), item.getTitle(), member.getId(), member.getName(), borrowDate, dueDate);
    }
}