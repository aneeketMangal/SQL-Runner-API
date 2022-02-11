package assignment_1.exceptions;
// Thrown when paramType and FQN of queryParam are not same.
public class ParamTypeDifferentException extends RuntimeException{
    public ParamTypeDifferentException(String paramType1, String paramType2, String Id)  
    {  
        super("Param type provided in XML is different from the param type provided in the method"+
        "paramType in XML: " + paramType1 + "\n" + 
        "paramType in Method: " + paramType2 + "\n" + 
        " Id: "+Id);  
    }  
}
