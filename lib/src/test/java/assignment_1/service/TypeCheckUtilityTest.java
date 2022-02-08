package assignment_1.service;

import org.junit.jupiter.api.Test;

import assignment_1.Library;
import assignment_1.service.StringUtility.TypeCheckUtility;

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

        Collection<Integer> a = new ArrayList<Integer>();
        assertTrue(TypeCheckUtility.isCollectionType(a));
    }
}


