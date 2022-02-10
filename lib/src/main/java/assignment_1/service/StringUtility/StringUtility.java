package assignment_1.service.StringUtility;

import assignment_1.exceptions.NotAComponentTypeException;
import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.model.QueryObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class StringUtility extends TypeCheckUtility{
    /**
     * This method populates an unprocessed query (query that contains placeholders).
     * This method fills those placeholders with values of fields of object queryParam
     * @param qObj An object of type  {@link assignment_1.model.QueryObject} that contains details of unprocessed query
     * @param queryParam An object of generic type which contains values of placeholders in unprocessed query.
     * @return - populated query of string.
     */
    public <T> String populateQuery(QueryObject qObj, T queryParam) {
        // check for the case where queryParam is null
        // In case paramType in the XML file is "null", then the query is processed as it is
        // and no placeholder is filled. Otherwise, ParamTypeDifferentException will be thrown.
        if(queryParam == null){
            if(qObj.paramType.equals("null"))
                return qObj.query;
            else
                throw new ParamTypeDifferentException(qObj.paramType, "null Java Object provided", qObj.id);
        }
        checkParamTypes(qObj, queryParam);

        String populatedQuery = qObj.query;

        /*
        * First we test if the queryParam is of a "Component type" as mentioned in the README.
        * If it is a component type then it is replaced with ${value} placeholder in the query.
        * Otherwise, NotAComponentTypeException is thrown.
        * And then we look for a user defined object.
        */
        try {
            return this.replaceUtility((Object) queryParam, populatedQuery, "value", true);
        } catch (NotAComponentTypeException e) {
            try {
                // fetching the field object of the class using reflection API
                Field[] fields = queryParam.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fieldObject;
                    fieldObject = field.get(queryParam); // converting Field object into a general object
                    String fieldName = field.getName(); // fetching name of the field
                    // checking if the unprocessed query has placeholder of name ${fieldName}
                    if(populatedQuery.contains("${" + fieldName + "}")){
                        populatedQuery = this.replaceUtility(fieldObject, populatedQuery, fieldName, false);
                    }}
                return populatedQuery;
            }
            // following exception might consist of IllegalAccessException, IllegalArguementException
            catch (Exception ee){
                throw new RuntimeException(ee);
            }
        }
    }


    /**
     * replaces placeholder of String type in the query. This will put double quotes around the replace word.
     * @param populatedQuery
     * @param old
     * @param newValue
     * @return
     */
    private String replaceString(String populatedQuery, String old, String newValue) {

        return populatedQuery.replace("${" + old + "}", "\"" +newValue+ "\"");
    }

    /**
     * replaces placeholder of primitive type and their wrappers in the query.
     * @param populatedQuery
     * @param old
     * @param newValue
     * @return
     */

    private String replacePrimitives(String populatedQuery, String old, String newValue) {
        return populatedQuery.replace("${" + old + "}", newValue);
    }

    /**
     * replaces placeholder of Array type in the query. Handles both string type array and primitive type array
     * puts double quote around each element in array
     * @param populatedQuery
     * @param old
     * @param queryParam
     * @return
     */

    private String replaceArray(String populatedQuery, String old, Object queryParam){

            int temp = Array.getLength(queryParam);
            StringBuilder arrayStringBuilder = new StringBuilder("(");
            for (int i = 0; i < temp; i++) {
                if (isStringType(Array.get(queryParam, i))) {
                    arrayStringBuilder.append("\"").append(Array.get(queryParam, i).toString()).append("\",");
                } else {
                    arrayStringBuilder.append(Array.get(queryParam, i).toString()).append(",");
                }
            }
            String arrayString = arrayStringBuilder.toString();
            if (arrayString.length() > 1) {
                arrayString = arrayString.substring(0, arrayString.length() - 1);
            }
            arrayString = arrayString + ")";
            return replacePrimitives(populatedQuery, old, arrayString);
        }

    /**
     * replaces placeholder of Collection type in the query. Handles both string type collection and primitive type collection
     * puts double quote around each element in collection.
     * @param populatedQuery
     * @param old
     * @return
     */


    private String replaceCollection(String populatedQuery, String old, Object queryParam) {
        // returns in the format of [1,2,3]
            String arrayString = "(";
            // for iterating over the collection
            for (Object objectInIterable : (Iterable<?>) queryParam) {
                // we need to check if the component is of string type, we have to use double quotes around it.
                if (isStringType(objectInIterable)) {
                    arrayString = arrayString + ("\"" + objectInIterable.toString() + "\",");
                } else {
                    arrayString += objectInIterable.toString() + ",";
                }
            }
            if (arrayString.length() > 1) {
                arrayString = arrayString.substring(0, arrayString.length() - 1);
            }
            // arrayString = arrayString.substring(0, arrayString.length());
            arrayString = arrayString + ")";
            return replacePrimitives(populatedQuery, old, arrayString);

    }

    /**
     * A helper function for replacing the placeholder value. It checks the type of the object queryParam and replaces accordingly
     * @param queryParam
     * @param populatedQuery
     * @param old
     * @param checkComponent A boolean flag variable that checks if we are checking for component type or fields of Object
     *                       true if we are checking for component type
     * @param <T>
     * @return
     */


    private <T> String replaceUtility(T queryParam, String populatedQuery, String old, boolean checkComponent) {

        if (isStringType(queryParam)) {
            return replaceString(populatedQuery, old, queryParam.toString());
        } else if (isPrimitiveType(queryParam)) {
            return replacePrimitives(populatedQuery, old, queryParam.toString());
        }
        else if (isArrayType(queryParam)) {
            return replaceArray(populatedQuery, old, queryParam);
        } else if (isCollectionType(queryParam)) {
           return replaceCollection(populatedQuery, old, queryParam);
        }
        else{
            if(checkComponent)
                throw new NotAComponentTypeException();
            else
                return replacePrimitives(populatedQuery, old, queryParam.toString());
        }
    }

    /**
     * This function checks if the Fully Qualified Name of the queryParam
     * is equal to the paramType attribute of the sql tag that corresponds
     * to required queryId.
     * @param qObj
     * @param queryParam
     * @param <T>
     */
    private <T> void checkParamTypes(QueryObject qObj, T queryParam) {
        String paramType = queryParam.getClass().getName();
        String paramTypeInXML = qObj.paramType;
        if (!paramType.equals(paramTypeInXML)) {
            throw new ParamTypeDifferentException(paramTypeInXML, paramType, qObj.id);
        }
    }



}
