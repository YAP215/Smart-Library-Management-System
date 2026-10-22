import java.util.Date;

public class Reservation {
    private String reservationId;
    private LibraryUser user;
    private Book book;
    private Date reservationDate;
    private String status;

    public Reservation(String reservationId, LibraryUser user, Book book, Date reservationDate) {
        this.reservationId = reservationId;
        this.user = user;
        this.book = book;
        this.reservationDate = reservationDate;
        this.status = "Active";
    }

    public String getReservationId() {
        return reservationId;
    }

    public LibraryUser getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void cancelReservation() {
        this.status = "Cancelled";
    }

    public void completeReservation() {
        this.status = "Completed";
    }

    @Override
    public String toString() {
        return "\n========= RESERVATION INFORMATION =========" +
               "\nReservation ID : " + reservationId +
               "\nUser ID        : " + user.getUserId() +
               "\nUser Name      : " + user.getName() +
               "\nBook ID        : " + book.getBookId() +
               "\nBook Title     : " + book.getTitle() +
               "\nReservation Date : " + reservationDate +
               "\nStatus         : " + status +
               "\n===========================================";
    }
}