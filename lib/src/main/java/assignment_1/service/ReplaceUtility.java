package assignment_1.service;
import java.util.Collection;
import java.lang.reflect.Array;

public class ReplaceUtility {

    public static boolean isJavaLang(Object check) {
        return check.getClass().getName().startsWith("java.lang");
    }

    public static boolean isPrimitiveType(Object check) {
        String className = check.getClass().getName();
        return (className == "java.lang.Integer"
                || className == "java.lang.Double"
                || className == "java.lang.String"
                || className == "java.lang.Boolean"
                || className == "java.lang.Character"
                || className == "java.lang.Byte"
                || className == "java.lang.Short"
                || className == "java.lang.Long");
    }

    public static boolean isCollectionType(Object check) {
        System.out.println("royi royi");
        return check instanceof Collection<?>;
    }

    public static boolean isArrayType(Object check) {
        return check.getClass().isArray();
    }

    public static boolean isStringType(Object check) {
        return check.getClass().getName() == "java.lang.String"
                || check.getClass().getName() == "java.lang.Character"
                || check.getClass().getName() == "java.lang.char";
    }

    public String replaceString(String populatedQuery, String old, String newValue) {

        String outputQuery = populatedQuery.replace("${" + old + "}", "\"" + newValue + "\"");
        return outputQuery;
    }

    public String replacePrimitives(String populatedQuery, String old, String newValue) {
        String outputQuery = populatedQuery.replace("${" + old + "}", newValue);
        return outputQuery;
    }

    public <T> String replaceUtility(T queryParam, String populatedQuery, String old) {
        Class<?> classType = ((Object) queryParam).getClass();
        String className = classType.getName();
        if (isStringType(queryParam)) {
            return replaceString(populatedQuery, old, queryParam.toString());
        } else if (isPrimitiveType(queryParam)) {
            return replacePrimitives(populatedQuery, old, queryParam.toString());
        }

        else if (isArrayType(queryParam)) {
            int temp = Array.getLength(queryParam);

            // Object [] queryParamArray = ArrayUtils.toObject(queryParam);
            String arrayString = "(";
            for (int i = 0; i < temp; i++) {
                if (isStringType(Array.get(queryParam, i))) {
                    arrayString += "\"" + Array.get(queryParam, i).toString() + "\",";
                } else {
                    arrayString += Array.get(queryParam, i).toString() + ",";
                }
            }
            if (arrayString.length() > 1) {
                arrayString = arrayString.substring(0, arrayString.length() - 1);
            }
            // arrayString = arrayString.substring(0, arrayString.length());
            arrayString = arrayString + ")";
            return replacePrimitives(populatedQuery, old, arrayString);

        } else if (isCollectionType(queryParam)) {
            // returns in the format of [1,2,3]
            String arrayString = "(";
            for (Object objectInIterable : (Iterable<?>) queryParam) {
                if (isStringType(objectInIterable)) {
                    arrayString += "\"" + objectInIterable.toString() + "\",";
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
        // else if(T isinstance)
        else{
            // TODO: make this exception work in the near future
            throw new RuntimeException("A user defined type");
        }
    }

}
