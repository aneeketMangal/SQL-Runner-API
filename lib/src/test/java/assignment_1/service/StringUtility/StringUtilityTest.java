package assignment_1.service.StringUtility;

import assignment_1.exceptions.NotAComponentTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment_1.service.StringUtility.StringUtility;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilityTest {

    private StringUtility stringUtility;
    private String testString;
    private String testOld;
    @BeforeEach
    void init(){
        stringUtility = new StringUtility();
        testString = "x = ${value}";
        testOld = "value";
    }


    @Test
    public void testReplaceString(){
        String testNew = "Success";
        String testResult = stringUtility.replaceString(testString, testOld, testNew);
        assertEquals(testResult, "x = \"Success\"");
    }

    @Test
    public void testReplacePrimitive(){
        String testNew = "2";
        String testResult = stringUtility.replacePrimitives(testString, testOld, testNew);
        assertEquals(testResult, "x = 2");
    }

    @Test
    public void testReplaceArray(){
        String [] testNew = {"one", "two", "three"};
        String testResult = stringUtility.replaceArray(testString, testOld, testNew);
        assertEquals(testResult, "x = (\"one\",\"two\",\"three\")");

        int [] testNewTwo = {1, 2, 3};
        String testResultTwo = stringUtility.replaceArray(testString, testOld, testNewTwo);
        assertEquals(testResultTwo, "x = (1,2,3)");

        int testNewThree = 3;
        assertThrows(RuntimeException.class,  () -> {
            stringUtility.replaceArray(testString, testOld, testNewThree);
        });

    }

    @Test
    public void testReplaceCollection(){
        List<String> testNew = new ArrayList<String>();
        testNew.add("one");
        testNew.add("two");
        testNew.add("three");
        String testResult = stringUtility.replaceCollection(testString, testOld, testNew);
        assertEquals(testResult, "x = (\"one\",\"two\",\"three\")");

        Queue<Integer> testNewTwo = new LinkedList<Integer>();
        testNewTwo.add(1);
        testNewTwo.add(2);
        testNewTwo.add(3);

        String testResultTwo = stringUtility.replaceCollection(testString, testOld, testNewTwo);
        assertEquals(testResultTwo, "x = (1,2,3)");

        int testNewThree = 3;
        assertThrows(RuntimeException.class,  () -> {
            stringUtility.replaceCollection(testString, testOld, testNewThree);
        });
    }

    @Test
    public void testReplaceUtility(){
        List<String> testNew = new ArrayList<String>();
        testNew.add("one");
        testNew.add("two");
        testNew.add("three");
        String testResult = stringUtility.replaceUtility(testNew, testString, testOld, true);
        assertEquals(testResult, "x = (\"one\",\"two\",\"three\")");

        HashMap<String, String> testNewTwo= new HashMap<String, String>();
        assertThrows(NotAComponentTypeException.class,  () -> {
            stringUtility.replaceUtility(testNewTwo, testOld, testOld, true);
        });

    }
}


