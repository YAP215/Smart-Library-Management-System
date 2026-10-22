import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LibrarianModule {
    private LibraryData data;
    private Librarian librarian;
    private Catalog catalog;
    private Scanner scanner;

    public LibrarianModule(LibraryData data, Librarian librarian) {
        this.data = data;
        this.librarian = librarian;
        this.catalog = new Catalog(data.books, data.bookCount);
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int choice = 0;

        do {
            catalog.updateCatalog(data.books, data.bookCount);

            try {
                System.out.println("\n+----------------------------------------+");
                System.out.println("|            LIBRARIAN MODULE            |");
                System.out.println("+----------------------------------------+");
                System.out.println("| 1. Add New Book                        |");
                System.out.println("| 2. Remove Book                         |");
                System.out.println("| 3. Audit Inventory                     |");
                //System.out.println("| 4. View All Books                      |");
                System.out.println("| 4. View All Users                      |");
                System.out.println("| 5. Create User                         |");
                System.out.println("| 6. Update User                         |");
                System.out.println("| 7. Delete User                         |");
                System.out.println("| 8. Back to Main Menu                   |");
                System.out.println("+----------------------------------------+");
                System.out.print("Enter choice: ");

                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addNewBook();
                        break;
                    case 2:
                        removeBook();
                        break;
                    case 3:
                        auditInventory();
                        break;
                    /*case 4:
                        viewAllBooks();
                        break;*/
                    case 4:
                        viewAllUsers();
                        break;
                    case 5:
                        createUser();
                        break;
                    case 6:
                        updateUser();
                        break;
                    case 7:
                        deleteUser();
                        break;
                    case 8:
                        System.out.println("Returning to main menu...");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter numbers only.");
            }

        } while (choice != 8);
    }

    private void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void addNewBook() {
        System.out.println("\n--- Add New Book ---");

        if (data.bookCount >= data.books.length) {
            System.out.println("[ERROR] Book storage is full.");
            pressEnterToContinue();
            return;
        }

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        if (findBookById(bookId) != null) {
            System.out.println("[ERROR] Book ID already exists.");
            pressEnterToContinue();
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        Book newBook = new Book(bookId, title, author, isbn, genre);
        data.books[data.bookCount++] = newBook;

        data.saveBooksToFile();

        System.out.println("[SUCCESS] New book added by librarian: " + librarian.getName());
        System.out.println(newBook);
        pressEnterToContinue();
    }

    private void removeBook() {
        System.out.println("\n--- Remove Book ---");
        System.out.print("Enter Book ID to remove: ");
        String bookId = scanner.nextLine();

        int index = -1;

        for (int i = 0; i < data.bookCount; i++) {
            if (data.books[i] != null && data.books[i].getBookId().equalsIgnoreCase(bookId)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("[ERROR] Book not found.");
            pressEnterToContinue();
            return;
        }

        if (data.books[index].getStatus().equalsIgnoreCase("On Loan")) {
            System.out.println("[ERROR] Cannot remove a book that is currently on loan.");
            pressEnterToContinue();
            return;
        }

        for (int i = index; i < data.bookCount - 1; i++) {
            data.books[i] = data.books[i + 1];
        }

        data.books[data.bookCount - 1] = null;
        data.bookCount--;

        data.saveBooksToFile();

        System.out.println("[SUCCESS] Book removed by librarian: " + librarian.getName());
        pressEnterToContinue();
    }

    private void auditInventory() {
        System.out.println("\n[INFO] Inventory audit by librarian: " + librarian.getName());
        librarian.auditInventory(data.books, data.bookCount);
        pressEnterToContinue();
    }

    /*private void viewAllBooks() {
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
    }*/

    private void viewAllUsers() {
        System.out.println("\n--- All Users ---");

        if (data.userCount == 0) {
            System.out.println("[ERROR] No users found.");
        } else {
            for (int i = 0; i < data.userCount; i++) {
                System.out.println("User ID       : " + data.users[i].getUserId());
                System.out.println("User Name     : " + data.users[i].getName());
                System.out.println("Borrow Limit  : " + data.users[i].getBorrowLimit());
                System.out.println("Loan Duration : " + data.users[i].getLoanDuration() + " days");
                System.out.println("------------------------------------------");
            }
        }

        pressEnterToContinue();
    }

    private Book findBookById(String bookId) {
        for (int i = 0; i < data.bookCount; i++) {
            if (data.books[i] != null && data.books[i].getBookId().equalsIgnoreCase(bookId)) {
                return data.books[i];
            }
        }
        return null;
    }

    private LibraryUser findUserById(String userId) {
        for (int i = 0; i < data.userCount; i++) {
            if (data.users[i].getUserId().equalsIgnoreCase(userId)) {
                return data.users[i];
            }
        }
        return null;
    }

    private void createUser() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        if (findUserById(userId) != null) {
            System.out.println("[ERROR] User already exists.");
            pressEnterToContinue();
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();

        System.out.print("Type (1=Student, 2=Faculty, 3=Public Member) : ");
        int type = Integer.parseInt(scanner.nextLine());

        LibraryUser user;

        if (type == 1) {
            user = new Student(userId, name, pwd);
        } else if(type == 2){
            user = new Faculty(userId, name, pwd);
        }else{
            user = new PublicMember(userId, name, pwd);
        }

        data.users[data.userCount++] = user;

        System.out.println("[SUCCESS] User added.");
        saveUsersToFile();
        pressEnterToContinue();
    }

    private void updateUser() {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();

        LibraryUser user = findUserById(id);

        if (user == null) {
            System.out.println("[ERROR] User not found.");
            pressEnterToContinue();
            return;
        }

        System.out.print("New Name: ");
        user.setName(scanner.nextLine());

        System.out.print("New Password: ");
        user.setPassword(scanner.nextLine());

        System.out.println("[SUCCESS] User updated.");
        saveUsersToFile();
        pressEnterToContinue();
    }

    private void deleteUser() {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();

        int index = -1;

        for (int i = 0; i < data.userCount; i++) {
            if (data.users[i].getUserId().equalsIgnoreCase(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("[ERROR] User not found.");
            pressEnterToContinue();
            return;
        }

        for (int i = index; i < data.userCount - 1; i++) {
            data.users[i] = data.users[i + 1];
        }

        data.users[data.userCount - 1] = null;
        data.userCount--;

        System.out.println("[SUCCESS] User deleted.");
        saveUsersToFile();
        pressEnterToContinue();
    }

    private void saveUsersToFile() {
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
            //System.out.println("[INFO] Users saved to file.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save users.");
        }
    }
}