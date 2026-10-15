/**
 * Thrown when a member already holds the maximum number of loans.
 * Checked, for the same reason as ItemNotAvailableException.
 */
public class MemberLimitExceededException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String memberId;
    private final int limit;

    public MemberLimitExceededException(String memberId, int limit, String message) {
        super(message);
        this.memberId = memberId;
        this.limit = limit;
    }

    public String getMemberId() { return memberId; }
    public int getLimit() { return limit; }
}