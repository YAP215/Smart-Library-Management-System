public class PublicMember extends LibraryUser {

    public PublicMember(String userId,String name, String password){
        super(userId,name,password);
    }

    //Public Member limit borrow: 3
    @Override
    public int getBorrowLimit(){
        return 3;
    }

    //PM LoanDuration: 7days
    @Override
    public int getLoanDuration(){
        return 7;
    }
}
