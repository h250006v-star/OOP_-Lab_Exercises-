/** A DVD: shorter 7-day loan. */
public class Dvd extends LibraryItem {

    private final int durationMinutes;

    public Dvd(String id, String title, int durationMinutes) {
        super(id, title);
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() { return durationMinutes; }

    @Override public int loanPeriodDays() { return 7; }
    @Override public String typeName() { return "DVD"; }
    @Override public String extraInfo() { return String.valueOf(durationMinutes); }
    @Override protected String details() { return durationMinutes + " min"; }
}