package assignment_1.service.StringUtility;

import assignment_1.exceptions.NotAComponentTypeException;
import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.model.QueryObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class StringUtility extends TypeCheckUtility{
    public String replaceString(String populatedQuery, String old, String newValue) {

        return populatedQuery.replace("${" + old + "}", "\"" +newValue+ "\"");
    }

    public String replacePrimitives(String populatedQuery, String old, String newValue) {
        return populatedQuery.replace("${" + old + "}", newValue);
    }

    public String replaceArray(String populatedQuery, String old, Object queryParam){

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



    public String replaceCollection(String populatedQuery, String old, Object queryParam) {
        // returns in the format of [1,2,3]
            String arrayString = "(";
            for (Object objectInIterable : (Iterable<?>) queryParam) {
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

    public <T> String replaceUtility(T queryParam, String populatedQuery, String old, boolean checkComponent) {

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
    public <T> void checkParamTypes(QueryObject qObj, T queryParam) {
        String paramType = queryParam.getClass().getName();
        String paramTypeInXML = qObj.paramType;
        if (!paramType.equals(paramTypeInXML)) {
            throw new ParamTypeDifferentException(paramTypeInXML, paramType, qObj.id);
        }
    }

    public <T> String populateQuery(QueryObject qObj, T queryParam) {
        // check for query with no parameter
        if(queryParam == null){
            if(qObj.paramType.equals("null"))
                return qObj.query;
            else
                throw new ParamTypeDifferentException(qObj.paramType, "null Java Object provided", qObj.id);
        }
         this.checkParamTypes(qObj, queryParam);

        String populatedQuery = qObj.query;

        try {
            return this.replaceUtility((Object) queryParam, populatedQuery, "value", true);
        } catch (NotAComponentTypeException e) {
            // fetching the fields of the class
            try {
                Field[] fields = queryParam.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fieldObject;
                    fieldObject = field.get(queryParam);
                    String fieldName = field.getName();
                    if(populatedQuery.contains("${" + fieldName + "}")){
                        populatedQuery = this.replaceUtility(fieldObject, populatedQuery, fieldName, false);
                    }}
                return populatedQuery;
            }
            catch (Exception ee){
                throw new RuntimeException(ee);
            }
        }
    }

}
