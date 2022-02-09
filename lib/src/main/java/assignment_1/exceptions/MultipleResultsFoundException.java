package assignment_1.exceptions;

public class MultipleResultsFoundException extends RuntimeException{
    public MultipleResultsFoundException(String string)
    {  
        super("Multiple results found for the query: " + string);  
    }  
}
