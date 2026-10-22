import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LibraryData {
    public LibraryUser[] users;
    public Book[] books;
    public Loan[] loans;
    public Reservation[] reservations;
    public Fine[] fines;

    public int userCount;
    public int bookCount;
    public int loanCount;
    public int reservationCount;
    public int fineCount;

    public LibraryData() {
        users = new LibraryUser[20];
        books = new Book[20];
        loans = new Loan[50];
        reservations = new Reservation[50];
        fines = new Fine[50];

        userCount = 0;
        bookCount = 0;
        loanCount = 0;
        reservationCount = 0;
        fineCount = 0;

        preloadData();
    }

    private void preloadData() {
        // Load users
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String userId = parts[0];
                String name = parts[1];
                String password = parts[2];
                String type = parts[3];

                LibraryUser user;

                switch (type) {
                    case "Student":
                        user = new Student(userId, name, password);
                        break;
                    case "Faculty":
                        user = new Faculty(userId, name, password);
                        break;
                    case "PublicMember":
                        user = new PublicMember(userId, name, password);
                        break;
                    default:
                        continue;
                }

                users[userCount++] = user;
            }

            reader.close();
            System.out.println("[INFO] Users loaded from file.");

        } catch (IOException e) {
            System.out.println("[WARNING] users.txt not found. Using default data.");

            users[userCount++] = new Student("A1001", "Ying", "password123");
            users[userCount++] = new Faculty("A1002", "Jackson", "password456");
            users[userCount++] = new PublicMember("A1003", "Bei", "password789");
        }

        // Load books
        try {
            BufferedReader reader = new BufferedReader(new FileReader("books.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String bookId = parts[0];
                String title = parts[1];
                String author = parts[2];
                String isbn = parts[3];
                String genre = parts[4];
                String status = parts[5];

                Book book = new Book(bookId, title, author, isbn, genre);
                book.setStatus(status);

                books[bookCount++] = book;
            }

            reader.close();
            System.out.println("[INFO] Books loaded from file.");

        } catch (IOException e) {
            System.out.println("[WARNING] books.txt not found. Using default data.");

            books[bookCount++] = new Book("B1001", "Blink", "Malcolm Gladwell", "978000111", "Psychology");
            books[bookCount++] = new Book("B1002", "Drive", "Daniel H. Pink", "978000222", "Motivation");
            books[bookCount++] = new Book("B1003", "Grit", "Angela Duckworth", "978000333", "Self-Improvement");
            books[bookCount++] = new Book("B1004", "Nature Journal Vol.1", "Various Authors", "978100444", "Journal");
            books[bookCount++] = new Book("B1005", "Science Journal Vol.2", "Various Authors", "978100555", "Journal");
            books[bookCount++] = new Book("B1006", "Medical Journal Vol.3", "Various Authors", "978100666", "Journal");
            books[bookCount++] = new Book("B1007", "Java Programming PDF", "James Gosling", "978200111", "Digital Resource");
            books[bookCount++] = new Book("B1008", "Data Science eBook", "Andrew Ng", "978200222", "Digital Resource");
            books[bookCount++] = new Book("B1009", "AI Fundamentals Guide", "Stuart Russell", "978200333", "Digital Resource");

            saveBooksToFile();
        }

        // Load loans
        try {
            BufferedReader reader = new BufferedReader(new FileReader("loans.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String loanId = parts[0];
                String userId = parts[1];
                String bookId = parts[2];
                long issueTime = Long.parseLong(parts[3]);
                long returnTime = Long.parseLong(parts[4]);
                String status = parts[5];

                LibraryUser user = findUserById(userId);
                Book book = findBookById(bookId);

                if (user != null && book != null) {
                    Loan loan = new Loan(loanId, user, book, new Date(issueTime));

                    if (status.equalsIgnoreCase("Returned") && returnTime != -1) {
                        loan.returnBook(new Date(returnTime));
                    }

                    loans[loanCount++] = loan;
                }
            }

            reader.close();
            System.out.println("[INFO] Loans loaded from file.");

        } catch (IOException e) {
            System.out.println("[WARNING] loans.txt not found.");
        }

        // Load reservations
        try {
            BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String reservationId = parts[0];
                String userId = parts[1];
                String bookId = parts[2];
                long reservationTime = Long.parseLong(parts[3]);
                String status = parts[4];

                LibraryUser user = findUserById(userId);
                Book book = findBookById(bookId);

                if (user != null && book != null) {
                    Reservation reservation = new Reservation(
                        reservationId,
                        user,
                        book,
                        new Date(reservationTime)
                    );

                    if (status.equalsIgnoreCase("Cancelled")) {
                        reservation.cancelReservation();
                    } else if (status.equalsIgnoreCase("Completed")) {
                        reservation.completeReservation();
                    }

                    reservations[reservationCount++] = reservation;
                }
            }

            reader.close();
            System.out.println("[INFO] Reservations loaded from file.");

        } catch (IOException e) {
            System.out.println("[WARNING] reservations.txt not found.");
        }
    }

    private LibraryUser findUserById(String userId) {
        for (int i = 0; i < userCount; i++) {
            if (users[i] != null && users[i].getUserId().equalsIgnoreCase(userId)) {
                return users[i];
            }
        }
        return null;
    }

    private Book findBookById(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i] != null && books[i].getBookId().equalsIgnoreCase(bookId)) {
                return books[i];
            }
        }
        return null;
    }

    public void saveBooksToFile() {
        try {
            FileWriter writer = new FileWriter("books.txt");

            for (int i = 0; i < bookCount; i++) {
                Book b = books[i];

                writer.write(
                    b.getBookId() + "," +
                    b.getTitle() + "," +
                    b.getAuthor() + "," +
                    b.getIsbn() + "," +
                    b.getGenre() + "," +
                    b.getStatus() +
                    "\n"
                );
            }

            writer.close();
            System.out.println("[SUCCESS] Books saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save books.");
        }
    }

    public void saveLoansToFile() {
        try {
            FileWriter writer = new FileWriter("loans.txt");

            for (int i = 0; i < loanCount; i++) {
                Loan l = loans[i];

                long returnTime = -1;
                if (l.getReturnDate() != null) {
                    returnTime = l.getReturnDate().getTime();
                }

                writer.write(
                    l.getLoanId() + "," +
                    l.getUser().getUserId() + "," +
                    l.getBook().getBookId() + "," +
                    l.getIssueDate().getTime() + "," +
                    returnTime + "," +
                    l.getStatus() +
                    "\n"
                );
            }

            writer.close();
            System.out.println("[SUCCESS] Loans saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save loans.");
        }
    }

    public void saveReservationsToFile() {
        try {
            FileWriter writer = new FileWriter("reservations.txt");

            for (int i = 0; i < reservationCount; i++) {
                Reservation r = reservations[i];

                writer.write(
                    r.getReservationId() + "," +
                    r.getUser().getUserId() + "," +
                    r.getBook().getBookId() + "," +
                    r.getReservationDate().getTime() + "," +
                    r.getStatus() +
                    "\n"
                );
            }

            writer.close();
            System.out.println("[SUCCESS] Reservations saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save reservations.");
        }
    }
}