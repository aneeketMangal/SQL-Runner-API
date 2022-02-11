package assignment_1.exceptions;

// Thrown when multiple records are fetched in a selectOne() API call.
public class MultipleResultsFoundException extends RuntimeException{
    public MultipleResultsFoundException(String string)
    {  
        super("Multiple results found for the query: " + string);  
    }  
}
