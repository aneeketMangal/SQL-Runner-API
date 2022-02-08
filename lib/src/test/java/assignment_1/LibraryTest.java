/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package assignment_1;

import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.exceptions.QueryNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    public Library library;
    public Connection c;
    @BeforeEach
    void init(){
          try {
              Class.forName("com.mysql.cj.jdbc.Driver");
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          }
          try {
              c = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?user=root&password=papamangal&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
              library = new Library(c, "C:\\Users\\lenovo\\Desktop\\Adhoora\\academics\\year3\\software\\cs305_2022\\lib\\src\\test\\resources\\queries.xml");
          } catch (Exception e) {
              e.printStackTrace();
          }
    }

    @Test
    public void selectOneTest (){
        String queryParam = "JOHN";
        String queryId = "testOne";
        test Result= library.selectOne(queryId, queryParam, test.class);
        assertEquals(Result.actor_id, 192);
        String queryParamTwo = "Test";
        test ResultTwo = library.selectOne(queryId, queryParamTwo, test.class);
        assertTrue(ResultTwo == null);
    }

    @Test
    public void selectManyTest (){
        String[] queryParam = {"JOHN", "REESE"};
        String queryId = "testTwo";
        List<test_two> Result= library.selectMany(queryId, queryParam, test_two.class);
        assertEquals(Result.size(), 3);
    }

     @Test
     public void selectOneTestTwo() {
            test_three queryParam = new test_three();
            queryParam.propOne = new ArrayList<String>();
            queryParam.propOne.add("JOHN");
            queryParam.propOne.add("REESE");
            queryParam.propTwo = "WEST";
            test_two Result= library.selectOne("testThree", queryParam, test_two.class);
            assertEquals(Result.actor_id, 197);
     }

     @Test void selectOneTestThree(){
         test_five queryParam = new test_five();
         queryParam.actor_id = 5001;
         queryParam.first_name = "REESE";
         queryParam.last_name = "WEST";

         test_two Result= library.selectOne("testSix", queryParam, test_two.class);
         assertEquals(Result.actor_id, 197);
     }

     @Test
     public void insertTest() {
        test_five queryParam = new test_five();
        queryParam.actor_id = 5003;
        queryParam.first_name = "Aneeket";
        queryParam.last_name = "Mangal";
        String queryId = "testFour";

         int Result= library.insert(queryId, queryParam);
         assertEquals(Result, 1);
         assertThrows(ParamTypeDifferentException.class,  () -> {
             library.insert("testFive", queryParam);
         });

         assertThrows(QueryNotFoundException.class,  () -> {
             library.insert("testRandom", queryParam);
         });
     }
}
