/** A printed book: 14-day loan. */
public class Book extends LibraryItem {

    private final String author;

    public Book(String id, String title, String author) {
        super(id, title);                          // parent validates id and title
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author is required");
        }
        this.author = author.trim();
    }

    public String getAuthor() { return author; }

    @Override public int loanPeriodDays() { return 14; }
    @Override public String typeName() { return "BOOK"; }
    @Override public String extraInfo() { return author; }
    @Override protected String details() { return "by " + author; }
}