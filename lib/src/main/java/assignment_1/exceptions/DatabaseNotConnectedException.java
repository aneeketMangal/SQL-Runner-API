package assignment_1.exceptions;

public class DatabaseNotConnectedException extends RuntimeException {
    public DatabaseNotConnectedException (String str)  
    {  
        super(str);  
    }  
}  
