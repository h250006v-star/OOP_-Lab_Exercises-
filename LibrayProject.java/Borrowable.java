/**
 * Anything that can be lent out and handed back.
 * LibraryItem implements this, so every Book and Dvd is Borrowable.
 */
public interface Borrowable {

    /** Lends this item to the member, or throws if that is not allowed. */
    void borrow(Member member) throws ItemNotAvailableException, MemberLimitExceededException;

    /** Marks this item as returned. */
    void giveBack();
}