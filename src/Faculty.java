public class Faculty extends LibraryUser {
    
    public Faculty(String userId,String name, String password){
        super(userId, name, password);
    }

    //Faculty borrow limit: 10
    @Override
    public int getBorrowLimit(){
        return 10;
    }

    //Faculty loan durantion: 30days
    @Override
    public int getLoanDuration(){
        return 30;
    }
}
