package assignment_1.exceptions;

public class QueryNotFoundException extends RuntimeException{
    public QueryNotFoundException (String id, String filePath)  
    {  
        super("Query with the given ID does not exist in the XML file\n"+
        "Query ID: " + id +
        "Location of XML file: " + filePath
        );  
    }
}
