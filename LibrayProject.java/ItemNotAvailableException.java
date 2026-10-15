/**
 * Thrown when someone tries to borrow an item that is already on loan.
 * Checked (extends Exception) because it is a normal, recoverable situation
 * that the caller must deal with.
 */
public class ItemNotAvailableException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String itemId;

    public ItemNotAvailableException(String itemId, String message) {
        super(message);
        this.itemId = itemId;
    }

    public String getItemId() { return itemId; }
}
