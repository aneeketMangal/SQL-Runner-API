package assignment_1.exceptions;

public class CannotConnectToDatabaseException extends RuntimeException {
    public CannotConnectToDatabaseException(Exception e)
    {  
        super(e);
    }  
}  
