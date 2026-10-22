public class Student extends LibraryUser{

    public Student(String userId, String name, String password){
        super(userId, name, password);
    }

    //Student borrow limit : 5
    @Override
    public int getBorrowLimit(){
        return 5;
    }

    // Student loan Duration : 14days
    @Override
    public int getLoanDuration(){
        return 14;
    }
    
}