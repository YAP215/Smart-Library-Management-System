public abstract class LibraryUser{

    private String userId;
    private String name;
    private String password;

    public LibraryUser(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public abstract int getBorrowLimit();
    public abstract int getLoanDuration();
    
    
    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        LibraryUser other = (LibraryUser) obj;
        return userId.equals(other.userId);
    }        
}