package assignment_1.service.StringUtility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;


public class TypeCheckUtilityTest {
    @Test
    public void checkIsPrimitiveType (){
        int a  = 4;
        assertTrue(TypeCheckUtility.isPrimitiveType(a));
        String b = "Test String";
        assertFalse(TypeCheckUtility.isPrimitiveType(b));
    }
    @Test
    public void checkIsStringType(){
        String a = "Test String";
        assertTrue(TypeCheckUtility.isStringType(a));
    }

    @Test
    public void checkIsArrayType(){
        int[] a = {1,2,3};
        assertTrue(TypeCheckUtility.isArrayType(a));
    }

    @Test
    public void checkIsCollectionType(){

        Collection<Integer> a = new ArrayList<>();
        a.add(5);
        assertTrue(TypeCheckUtility.isCollectionType(a));
    }
}


