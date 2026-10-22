public class Catalog {
    private Book[] books;
    private int bookCount;

    public Catalog(Book[] books, int bookCount) {
        this.books = books;
        this.bookCount = bookCount;
    }

    public void updateCatalog(Book[] books, int bookCount) {
        this.books = books;
        this.bookCount = bookCount;
    }

    public Book searchByBookId(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null && books[i].getBookId().equalsIgnoreCase(bookId)) {
                return books[i];
            }
        }
        return null;
    }

    public Book searchByISBN(String isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null && books[i].getIsbn().equalsIgnoreCase(isbn)) {
                return books[i];
            }
        }
        return null;
    }

    public void searchByTitle(String title) {
        boolean found = false;

        System.out.println("\n--- Search Result by Title ---");
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null &&
                books[i].getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(books[i]);
                System.out.println("------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("[ERROR] No book found with that title.");
        }
    }

    public void searchByAuthor(String author) {
        boolean found = false;

        System.out.println("\n--- Search Result by Author ---");
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null &&
                books[i].getAuthor().toLowerCase().contains(author.toLowerCase())) {
                System.out.println(books[i]);
                System.out.println("------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("[ERROR] No book found with that author.");
        }
    }

    public void searchByGenre(String genre) {
        boolean found = false;

        System.out.println("\n--- Search Result by Genre ---");
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null &&
                books[i].getGenre().toLowerCase().contains(genre.toLowerCase())) {
                System.out.println(books[i]);
                System.out.println("------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("[ERROR] No book found in that genre.");
        }
    }

    public void searchAvailability(String title) {
        boolean found = false;

        System.out.println("\n--- Availability Search Result ---");
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null &&
                books[i].getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println("Title  : " + books[i].getTitle());
                System.out.println("Status : " + books[i].getStatus());
                System.out.println("------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("[ERROR] No matching book found.");
        }
    }

    public void displayAllBooks() {
        System.out.println("\n--- Catalog Book List ---");

        if (bookCount == 0) {
            System.out.println("[ERROR] No books found.");
            return;
        }

        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null) {
                System.out.println(books[i]);
                System.out.println("------------------------------------------");
            }
        }
    }
}