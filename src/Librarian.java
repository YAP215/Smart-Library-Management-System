public class Librarian {

    private String librarianId;
    private String name;
    private String password;

    public Librarian(String librarianId, String name, String password) {
        this.librarianId = librarianId;
        this.name = name;
        this.password = password;
    }

    public String getLibrarianId() {
        return librarianId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void auditInventory(Book[] books, int bookCount) {
        System.out.println("\n--- Inventory Audit ---");

        if (bookCount == 0) {
            System.out.println("[INFO] No books found.");
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