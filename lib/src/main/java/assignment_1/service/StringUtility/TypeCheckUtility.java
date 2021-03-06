package assignment_1.service.StringUtility;

import java.util.Collection;

public class TypeCheckUtility {

    /**
     * This function checks if the object is primitive type 
     * or a wrapper object of primitive type.
     * List of primitive types:
     * -> boolean, byte, char, short, int, long, float, double
     * 
     * @param check
     * @return
     */

    public boolean isPrimitiveType(Object check) {
        String className = check.getClass().getName();
        return (className.equals("java.lang.Integer") || className.equals("java.lang.Double") || className.equals("java.lang.Boolean") || className == "java.lang.Character" || className == "java.lang.Byte" || className == "java.lang.Short" || className == "java.lang.Long");
    }
    /**
     * This function check of the given object is of
     * type Collection.
     * Returns true if the object is of type Collection.
     * @param check
     * @return
     */

    public boolean isCollectionType(Object check) {
        return check instanceof Collection<?>;
    }

    /**
     * This function checks if the given object is of type
     * array.
     * @param check
     * @return
     */

    public boolean isArrayType(Object check) {
        return check.getClass().isArray();
    }

    /**
     * This function checks if the given object is of type
     * String, char or and Date type.
     * User needs to provide explicit toString function in case of a java.lang.Date object.
     * @param check
     * @return
     */

    public boolean isStringType(Object check) {
        return check.getClass().getName() == "java.lang.String"|| check.getClass().getName() == "java.lang.Character" || check.getClass().getName() == "java.util.data";
    }
}
