package assignment_1.service.StringUtility;

import assignment_1.exceptions.NotAComponentTypeException;
import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.model.QueryObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class StringUtility extends TypeCheckUtility{
    //todo: handle date

    public String replaceString(String populatedQuery, String old, String newValue) {

        return populatedQuery.replace("${" + old + "}", "\"" +newValue+ "\"");
    }

    public String replacePrimitives(String populatedQuery, String old, String newValue) {
        return populatedQuery.replace("${" + old + "}", newValue);
    }

    public String replaceArray(String populatedQuery, String old, Object queryParam){
        try {
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
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String replaceCollection(String populatedQuery, String old, Object queryParam) {
        // returns in the format of [1,2,3]
        try {
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
        catch (Exception e){
            throw(new RuntimeException(e));
        }
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
            throw new ParamTypeDifferentException(paramType, paramType, qObj.id);
        }
    }

    public <T> String populateQuery(QueryObject qObj, T queryParam) {
         this.checkParamTypes(qObj, queryParam);

        String populatedQuery = qObj.query;
        Object paramObject = (Object) queryParam;

        try {
            return this.replaceUtility(paramObject, populatedQuery, "value", true);
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
                    }
                    else{
                        continue;
                    }
                }
                return populatedQuery;

            }
            catch (Exception ee){
                throw new RuntimeException(ee);
            }
        }
    }

}
