import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryData data = new LibraryData();
        Librarian librarian = new Librarian("L001", "Admin Librarian","admin123");

        UserModule userModule = new UserModule(data);
        LibrarianModule librarianModule = new LibrarianModule(data, librarian);

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            try {
                System.out.println("\n+----------------------------------------+");
                System.out.println("|    SMART LIBRARY MANAGEMENT SYSTEM     |");
                System.out.println("+----------------------------------------+");
                System.out.println("| 1. Login                               |");
                System.out.println("| 2. Register                            |");
                System.out.println("| 3. Exit                                |");
                System.out.println("+----------------------------------------+");
                System.out.print("Enter choice: ");

                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        String role = userModule.login();
                        if (role.equals("ADMIN")) {
                            librarianModule.menu();
                        } else if (role.equals("USER")) {
                            userModule.menu();
                        } else {
                            System.out.println("Login failed.");
                        }
                        break;
                    case 2:
                        userModule.register();
                        break;
                    case 3:
                        System.out.println("Exiting system...");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter numbers only.");
            }
        } while (choice != 3);
    }
}