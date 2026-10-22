import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class UserModule {

    private LibraryData data;
    private Catalog catalog;
    private Scanner scanner;
    private LibraryUser currentUser;

    public UserModule(LibraryData data) {
        this.data = data;
        this.catalog = new Catalog(data.books, data.bookCount);
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int choice = 0;

        if (currentUser == null) {
            System.out.println("[ERROR] Please login first.");
            pressEnterToContinue();
            return;
        }

        do {
            catalog.updateCatalog(data.books, data.bookCount);

            try {
                System.out.println("\n+----------------------------------------+");
                System.out.println("|               USER MODULE              |");
                System.out.println("+----------------------------------------+");
                System.out.println("| 1. Borrow Book                         |");
                System.out.println("| 2. Return Book                         |");
                System.out.println("| 3. Reserve Book                        |");
                System.out.println("| 4. View All Loan Records               |");
                System.out.println("| 5. View All Reservation Records        |");
                System.out.println("| 6. View All Fine Records               |");
                System.out.println("| 7. Pay Fine                            |");
                System.out.println("| 8. Search Catalog                      |");
                System.out.println("| 9. View All Books                      |");
                System.out.println("| 10. Back to Main Menu                  |");
                System.out.println("+----------------------------------------+");
                System.out.print("Enter choice: ");

                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        borrowBook();
                        break;
                    case 2:
                        returnBook();
                        break;
                    case 3:
                        reserveBook();
                        break;
                    case 4:
                        viewAllLoans();
                        break;
                    case 5:
                        viewAllReservations();
                        break;
                    case 6:
                        viewAllFines();
                        break;
                    case 7:
                        payFine();
                        break;
                    case 8:
                        searchCatalogMenu();
                        break;
                    case 9:
                        viewAllBooks();
                        break;
                    case 10:
                        System.out.println("Returning to main menu...");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter numbers only.");
            }

        } while (choice != 10);
    }

    private void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void borrowBook() {
        System.out.println("\n--- Borrow Book ---");

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        LibraryUser user = currentUser;
        Book book = findBookById(bookId);

        if (user == null) {
            System.out.println("[ERROR] User not found.");
        } else if (book == null) {
            System.out.println("[ERROR] Book not found.");
        } else if (!book.getStatus().equalsIgnoreCase("Available")) {
            System.out.println("[ERROR] Book is not available.");
        } else {
            int activeLoans = countActiveLoansByUser(user.getUserId());

            if (activeLoans >= user.getBorrowLimit()) {
                System.out.println("[ERROR] Borrowing limit exceeded.");
            } else {
                String loanId = "L" + String.format("%03d", data.loanCount + 1);
                Loan newLoan = new Loan(loanId, user, book, new Date());

                data.loans[data.loanCount++] = newLoan;
                book.setStatus("On Loan");

                data.saveLoansToFile();
                data.saveBooksToFile();

                System.out.println("[SUCCESS] Borrowing successful.");
                System.out.println(newLoan);
            }
        }

        pressEnterToContinue();
    }

    private void returnBook() {
        System.out.println("\n--- Return Book ---");

        System.out.print("Enter Loan ID: ");
        String loanId = scanner.nextLine();

        Loan loan = findLoanById(loanId);

        if (loan == null) {
            System.out.println("[ERROR] Loan record not found.");
        } else if (loan.getStatus().equalsIgnoreCase("Returned")) {
            System.out.println("[ERROR] This book has already been returned.");
        } else {
            Date returnDate = new Date();
            loan.returnBook(returnDate);

            data.saveLoansToFile();
            data.saveBooksToFile();

            System.out.println("[SUCCESS] Return successful.");

            if (loan.isOverdue(returnDate)) {
                long lateDays = loan.calculateLateDays(returnDate);
                String fineId = "F" + String.format("%03d", data.fineCount + 1);

                Fine newFine = new Fine(fineId, loan, lateDays);
                data.fines[data.fineCount++] = newFine;

                System.out.println("[INFO] Fine created.");
                System.out.println(newFine);
            } else {
                System.out.println("[INFO] Book returned on time.");
            }
        }

        pressEnterToContinue();
    }

    private void reserveBook() {
        System.out.println("\n--- Reserve Book ---");

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        LibraryUser user = currentUser;
        Book book = findBookById(bookId);

        if (user == null) {
            System.out.println("[ERROR] User not found.");
        } else if (book == null) {
            System.out.println("[ERROR] Book not found.");
        } else if (book.getStatus().equalsIgnoreCase("Available")) {
            System.out.println("[ERROR] Book is available. No need to reserve.");
        } else if (hasActiveReservation(user.getUserId(), bookId)) {
            System.out.println("[ERROR] Reservation already exists.");
        } else {
            String reservationId = "R" + String.format("%03d", data.reservationCount + 1);
            Reservation reservation = new Reservation(reservationId, user, book, new Date());

            data.reservations[data.reservationCount++] = reservation;
            data.saveReservationsToFile();

            System.out.println("[SUCCESS] Reservation created.");
            System.out.println(reservation);
        }

        pressEnterToContinue();
    }

    private void viewAllLoans() {
        System.out.println("\n--- All Loan Records ---");

        if (data.loanCount == 0) {
            System.out.println("[ERROR] No loan records found.");
        } else {
            for (int i = 0; i < data.loanCount; i++) {
                System.out.println(data.loans[i]);
            }
        }

        pressEnterToContinue();
    }

    private void viewAllReservations() {
        System.out.println("\n--- All Reservation Records ---");

        if (data.reservationCount == 0) {
            System.out.println("[ERROR] No reservation records found.");
        } else {
            for (int i = 0; i < data.reservationCount; i++) {
                System.out.println(data.reservations[i]);
            }
        }

        pressEnterToContinue();
    }

    private void viewAllFines() {
        System.out.println("\n--- All Fine Records ---");

        if (data.fineCount == 0) {
            System.out.println("[ERROR] No fine records found.");
        } else {
            for (int i = 0; i < data.fineCount; i++) {
                System.out.println(data.fines[i]);
            }
        }

        pressEnterToContinue();
    }

    private void viewAllBooks() {
        System.out.println("\n--- All Books ---");

        if (data.bookCount == 0) {
            System.out.println("[ERROR] No books found.");
        } else {
            for (int i = 0; i < data.bookCount; i++) {
                if (data.books[i] != null) {
                    System.out.println(data.books[i]);
                    System.out.println("------------------------------------------");
                }
            }
        }

        pressEnterToContinue();
    }

    private void payFine() {
        System.out.println("\n--- Pay Fine ---");
        System.out.print("Enter Fine ID: ");
        String fineId = scanner.nextLine();

        Fine fine = findFineById(fineId);

        if (fine == null) {
            System.out.println("[ERROR] Fine not found.");
        } else if (fine.isPaid()) {
            System.out.println("[ERROR] Fine already paid.");
        } else {
            fine.payFine();
            System.out.println("[SUCCESS] Fine paid successfully.");
            System.out.println(fine);
        }

        pressEnterToContinue();
    }

    private void searchCatalogMenu() {
        int choice = 0;

        do {
            try {
                System.out.println("\n+----------------------------------------+");
                System.out.println("|             SEARCH CATALOG             |");
                System.out.println("+----------------------------------------+");
                System.out.println("| 1. Search by Book ID                   |");
                System.out.println("| 2. Search by Title                     |");
                System.out.println("| 3. Search by Author                    |");
                System.out.println("| 4. Search by Genre                     |");
                System.out.println("| 5. Search by ISBN                      |");
                System.out.println("| 6. Check Book Availability             |");
                System.out.println("| 7. Back                                |");
                System.out.println("+----------------------------------------+");
                System.out.print("Enter choice: ");

                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter Book ID: ");
                        String bookId = scanner.nextLine();
                        Book bookById = catalog.searchByBookId(bookId);
                        if (bookById == null) {
                            System.out.println("[ERROR] Book not found.");
                        } else {
                            System.out.println(bookById);
                        }
                        pressEnterToContinue();
                        break;

                    case 2:
                        System.out.print("Enter Title: ");
                        catalog.searchByTitle(scanner.nextLine());
                        pressEnterToContinue();
                        break;

                    case 3:
                        System.out.print("Enter Author: ");
                        catalog.searchByAuthor(scanner.nextLine());
                        pressEnterToContinue();
                        break;

                    case 4:
                        System.out.print("Enter Genre: ");
                        catalog.searchByGenre(scanner.nextLine());
                        pressEnterToContinue();
                        break;

                    case 5:
                        System.out.print("Enter ISBN: ");
                        Book bookByIsbn = catalog.searchByISBN(scanner.nextLine());
                        if (bookByIsbn == null) {
                            System.out.println("[ERROR] Book not found.");
                        } else {
                            System.out.println(bookByIsbn);
                        }
                        pressEnterToContinue();
                        break;

                    case 6:
                        System.out.print("Enter Title: ");
                        catalog.searchAvailability(scanner.nextLine());
                        pressEnterToContinue();
                        break;

                    case 7:
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter numbers only.");
            }
        } while (choice != 7);
    }

    private LibraryUser findUserById(String userId) {
        for (int i = 0; i < data.userCount; i++) {
            if (data.users[i].getUserId().equalsIgnoreCase(userId)) {
                return data.users[i];
            }
        }
        return null;
    }

    private Book findBookById(String bookId) {
        for (int i = 0; i < data.bookCount; i++) {
            if (data.books[i] != null && data.books[i].getBookId().equalsIgnoreCase(bookId)) {
                return data.books[i];
            }
        }
        return null;
    }

    private Loan findLoanById(String loanId) {
        for (int i = 0; i < data.loanCount; i++) {
            if (data.loans[i].getLoanId().equalsIgnoreCase(loanId)) {
                return data.loans[i];
            }
        }
        return null;
    }

    private Fine findFineById(String fineId) {
        for (int i = 0; i < data.fineCount; i++) {
            if (data.fines[i].getFineId().equalsIgnoreCase(fineId)) {
                return data.fines[i];
            }
        }
        return null;
    }

    private int countActiveLoansByUser(String userId) {
        int count = 0;
        for (int i = 0; i < data.loanCount; i++) {
            if (data.loans[i].getUser().getUserId().equalsIgnoreCase(userId)
                    && data.loans[i].getStatus().equalsIgnoreCase("On Loan")) {
                count++;
            }
        }
        return count;
    }

    private boolean hasActiveReservation(String userId, String bookId) {
        for (int i = 0; i < data.reservationCount; i++) {
            if (data.reservations[i].getUser().getUserId().equalsIgnoreCase(userId)
                    && data.reservations[i].getBook().getBookId().equalsIgnoreCase(bookId)
                    && data.reservations[i].getStatus().equalsIgnoreCase("Active")) {
                return true;
            }
        }
        return false;
    }

    public String login() {
        System.out.println("\n--- Login ---");

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (userId.equals("L001") && password.equals("admin123")) {
            System.out.println("[SUCCESS] Logged in as Admin Librarian");
            return "ADMIN";
        }

        LibraryUser user = findUserById(userId);

        if (user == null) {
            System.out.println("[ERROR] User not found.");
            return "FAIL";
        } else if (!user.getPassword().equals(password)) {
            System.out.println("[ERROR] Incorrect password.");
            return "FAIL";
        } else {
            currentUser = user;
            System.out.println("[SUCCESS] Logged in as " + user.getName());
            return "USER";
        }
    }

    public void register() {
        System.out.println("\n--- Register ---");

        if (data.userCount >= data.users.length) {
            System.out.println("[ERROR] Storage full.");
            return;
        }

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        if (findUserById(userId) != null) {
            System.out.println("[ERROR] User already exists.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Type (1=Student, 2=Faculty, 3=Public Member): ");
        int type;

        try {
            type = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid input.");
            return;
        }

        LibraryUser user;

        switch(type) {
            case 1:
                user = new Student(userId, name, password);
                break;
            case 2:
                user = new Faculty(userId, name, password);
                break;
            case 3:
                user = new PublicMember(userId, name, password);
                break;
            default:
                System.out.println("[ERROR] Invalid user type.");
                return;
        }

        data.users[data.userCount++] = user;

        System.out.println("[SUCCESS] Registered.");

        saveUsersToFile();
        pressEnterToContinue();
    }

    public void saveUsersToFile() {
        try {
            FileWriter writer = new FileWriter("users.txt");

            for (int i = 0; i < data.userCount; i++) {
                LibraryUser user = data.users[i];

                writer.write(
                    user.getUserId() + "," +
                    user.getName() + "," +
                    user.getPassword() + "," +
                    user.getClass().getSimpleName()
                    + "\n"
                );
            }

            writer.close();
            System.out.println("[SUCCESS] Users saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save users.");
        }
    }
}