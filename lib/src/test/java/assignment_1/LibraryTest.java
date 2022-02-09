/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package assignment_1;

import assignment_1.exceptions.*;
import assignment_1.test_class.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    public Library library;
    public Connection c;
    @BeforeEach
    void setUp(){
        Properties props = new Properties();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("test.properties");
        try {
            props.load(inputStream);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read properties file");
        }
        try {
              Class.forName("com.mysql.cj.jdbc.Driver");
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          }
          try {
              c = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autocommit=false",
                      props.get("user").toString(),
                      props.get("password").toString()
              );
              library = new Library(c,
                      "C:\\Users\\lenovo\\Desktop\\Adhoora\\academics\\year3\\software\\cs305_2022\\lib\\src\\test\\resources\\queries.xml");
          } catch (Exception e) {
              e.printStackTrace();
          }
    }

    @AfterEach
    void TearDown(){
        try{
            c.commit();
        }
        catch (Exception e){

        }
    }



    @Test
    public void insert(){
        test_five queryParam = new test_five();
        queryParam.first_name = "RandomTest";
        queryParam.last_name = "test";
        String queryId = "testFour";
        for(int i = 5000;i<5010;i++) {
            queryParam.actor_id = i;
            int Result= library.insert(queryId, queryParam);
            assertEquals(Result, 1);

        }
        assertThrows(ParamTypeDifferentException.class,  () -> {
            library.insert("testFive", queryParam);
        });

        assertThrows(QueryNotFoundException.class,  () -> {
            library.insert("testRandom", queryParam);
        });
    }

    @Test
    public void selectOne (){
        // testing basic String types
        String queryParam = "JOHN";
        String queryId = "testOne";
        test Result= library.selectOne(queryId, queryParam, test.class);
        assertEquals(Result.actor_id, 192);

        // testing empty result (no output records)
        String queryParamTwo = "Testblech";
        test ResultTwo = library.selectOne(queryId, queryParamTwo, test.class);
        assertNull(ResultTwo);

        // testing collection types queryParam (here ArrayList)
        test_three queryParamThree = new test_three();
        queryParamThree.propOne = new ArrayList<String>();
        queryParamThree.propOne.add("JOHN");
        queryParamThree.propOne.add("REESE");
        queryParamThree.propTwo = "WEST";
        test_two ResultThree= library.selectOne("testThree", queryParamThree, test_two.class);
        assertEquals(ResultThree.actor_id, 197);

        /* testing the case where input queryParam has
        * 3 fields (actor_id, first_name, last_name)
        * and the XML query only has two fields to fill
        * first_name and last_name.
        * The actor_id returned would be 197
        */
        test_five queryParamFour = new test_five();
        queryParamFour.actor_id = 5001;
        queryParamFour.first_name = "REESE";
        queryParamFour.last_name = "WEST";

        test_two ResultFour= library.selectOne("testSix", queryParamFour, test_two.class);
        assertEquals(ResultFour.actor_id, 197);

        /*
        * Testing the case where output class is test_two
        * which does not have sufficient fields. test_two
        * contains only actor_id while output of the given
        * query has three fields. So it will throw a runtime error.
        */
        String queryParamFive = "JOHN";
        String queryIdFive = "testOne";
        assertThrows(RuntimeException.class, ()->{
            library.selectOne(queryIdFive, queryParamFive, test_two.class);
        });

        /*
        * Testing a custom Runtime exception
        * MultipleResultsFoundException, which is
        * Raised when there are multiple results for a
        * selectOne type query.
        */
        String[] queryParamSix = {"JOHN", "REESE"};
        String queryIdSix = "testTwo";
        assertThrows(MultipleResultsFoundException.class,  () -> {
            library.selectOne(queryIdSix, queryParamSix, test_two.class);
        });

        /*
        * Testing the case of SQL exception in case of
        * invalid SQL query.
        */
        String test = "TestCheck";
        assertThrows(RuntimeException.class, ()->{
            library.selectOne("testEleven", test, test_two.class);
        });
    }

    @Test
    public void selectMany (){
        String[] queryParam = {"JOHN", "REESE"};
        String queryId = "testTwo";
        List<test_two> Result= library.selectMany(queryId, queryParam, test_two.class);
        assertEquals(Result.size(), 3);

        /*
        * Testing RuntimeException case of select many
        */

        String queryParamTwo = "JOHN";
        String queryIdTwo = "testOne";
        assertThrows(RuntimeException.class, ()->{
            library.selectMany(queryIdTwo, queryParamTwo, test_two.class);
        });

        /*
        * Checking Runtime exception raised on
        * Unsupported classes such as hashmap etc
        */
        HashMap<String, String> test = new HashMap<>();
        test.put("test", "test");

        assertThrows(RuntimeException.class,  () -> {
            library.selectOne("testSix", test, test_two.class);
        });

        /*
        * The case where the query is already filled and does not
        * have a placeholder of format ${prop}. In such case user needs
        * to specify paramType as "null"
        */

        List<test_two> ResultFour= library.selectMany("testFourteen", null, test_two.class);
        assertEquals(ResultFour.size(), 1);


    }

    @Test
    public void update() {
        String test = "RandomTestUpdate";
        int Result = library.update("testEight", test);
        assertEquals(Result, 10);


        /*
         * Testing the case where we provide
         * invalid SQL query to update method.
         * This will throw a RuntimeException object
         * of type SQLException
         */
        String testTwo = "TestCheck";
        assertThrows(RuntimeException.class, () -> {
            library.update("testTen", testTwo);
        });
    }

    @Test
    public void delete(){
        String test = "RandomTestUpdate";
        int Result= library.delete("testNine", test);
        assertEquals(Result, 10);

        /*
        * Testing the case when the queryParam (object passed in the API call)
        * is null but the paramType(i.e. FQN of class in XML is not equal
        * to the string "null"
        */

        assertThrows(ParamTypeDifferentException.class, ()->{
            library.delete("testFifteen", null);
        });
    }

     @Test
    public void testNestedObjectType(){
        test_six queryParam = new test_six();
        queryParam.state_1 = new test_five();
        queryParam.state_2 = new test_five();

        queryParam.state_1.actor_id = 3000;
        queryParam.state_2.actor_id = 3001;
        queryParam.state_1.first_name= "TEST";
        queryParam.state_2.first_name= "TEST";
        queryParam.state_1.last_name= "TEST";
        queryParam.state_2.last_name= "TEST";
        int Result= library.insert("testTwelve", queryParam);

        assertEquals(Result, 2);
        int [] queryParamTwo = {3000, 3001};
        Result = library.delete("testThirteen", queryParamTwo);
        assertEquals(Result, 2);
     }
}
