import java.util.Date;

public class Fine {
    private String fineId;
    private Loan loan;
    private long lateDays;
    private double amount;
    private boolean paid;
    private Date paidDate;

    public Fine(String fineId, Loan loan, long lateDays) {
        this.fineId = fineId;
        this.loan = loan;
        this.lateDays = lateDays;
        this.amount = lateDays * 1.0; // RM1 per day
        this.paid = false;
        this.paidDate = null;
    }

    public String getFineId() {
        return fineId;
    }

    public Loan getLoan() {
        return loan;
    }

    public long getLateDays() {
        return lateDays;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void payFine() {
        this.paid = true;
        this.paidDate = new Date();
    }

    @Override
    public String toString() {
        String paidInfo = (paidDate == null) ? "-" : paidDate.toString();

        return "\n============= FINE INFORMATION =============" +
               "\nFine ID       : " + fineId +
               "\nLoan ID       : " + loan.getLoanId() +
               "\nUser ID       : " + loan.getUser().getUserId() +
               "\nUser Name     : " + loan.getUser().getName() +
               "\nBook ID       : " + loan.getBook().getBookId() +
               "\nBook Title    : " + loan.getBook().getTitle() +
               "\nLate Days     : " + lateDays +
               "\nAmount        : RM" + amount +
               "\nPaid Status   : " + (paid ? "Paid" : "Unpaid") +
               "\nPaid Date     : " + paidInfo +
               "\n============================================";
    }
}