public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String status;

    public Book (String bookId,String title,String author,String isbn, String genre ){
        this.bookId=bookId;
        this.title=title;
        this.author=author;
        this.isbn=isbn;
        this.genre=genre;
        this.status="Available";

    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getIsbn(){
        return isbn;
    }

    public String getGenre(){
        return genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status ){
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "Book ID   : %s\n" +
            "Title     : %s\n" +
            "Author    : %s\n" +
            "ISBN      : %s\n" +
            "Genre     : %s\n" +
            "Status    : %s",
            bookId, title, author, isbn, genre, status
        );
    }
}
