package assignment_1.exceptions;

public class MultipleResultsFound extends RuntimeException{
    public MultipleResultsFound (String string)  
    {  
        super("Multiple results found for the query: " + string);  
    }  
}
