import java.util.Date;

public class Loan {
    private String loanId;
    private LibraryUser user;
    private Book book;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private String status;

    public Loan(String loanId, LibraryUser user, Book book, Date issueDate) {
        this.loanId = loanId;
        this.user = user;
        this.book = book;
        this.issueDate = issueDate;

        long days = user.getLoanDuration();
        this.dueDate = new Date(issueDate.getTime() + days * 24L * 60 * 60 * 1000);

        this.returnDate = null;
        this.status = "On Loan";
    }

    public String getLoanId() {
        return loanId;
    }

    public LibraryUser getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
        this.dueDate = new Date(issueDate.getTime() + user.getLoanDuration() * 24L * 60 * 60 * 1000);
    }

    public boolean isOverdue(Date actualReturnDate) {
        return actualReturnDate.after(dueDate);
    }

    public long calculateLateDays(Date actualReturnDate) {
        if (actualReturnDate.after(dueDate)) {
            long diff = actualReturnDate.getTime() - dueDate.getTime();
            return diff / (1000 * 60 * 60 * 24);
        }
        return 0;
    }

    public void returnBook(Date actualReturnDate) {
        this.returnDate = actualReturnDate;
        this.status = "Returned";
        book.setStatus("Available");
    }

    @Override
    public String toString() {
        String returnInfo = (returnDate == null) ? "-" : returnDate.toString();

        return "\n=========== LOAN INFORMATION =========" +
               "\nLoan ID      : " + loanId +
               "\nUser ID      : " + user.getUserId() +
               "\nUser Name    : " + user.getName() +
               "\nBook ID      : " + book.getBookId() +
               "\nBook Title   : " + book.getTitle() +
               "\nIssue Date   : " + issueDate +
               "\nDue Date     : " + dueDate +
               "\nReturn Date  : " + returnInfo +
               "\nStatus       : " + status +
               "\n======================================";
    }
}