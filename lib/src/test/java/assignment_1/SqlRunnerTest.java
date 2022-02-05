package assignment_1;

import dto.Film;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment_1.interfaces.SqlRunner;

import static org.junit.jupiter.api.Assertions.*;

class SqlRunnerTest {

   private SqlRunner sr;
   @BeforeEach
   void setUp() {
       /**
        * TODO: Create an instance of the SqlRunner implementation class,
        * and initialize it for connecting to the Sakila DB on a
        * MySQL DB instance.
        */
   }

   @AfterEach
   void tearDown() {
       // TODO: Close the DB connection and cleanup.
   }

   @Test
   void selectOne() {
    //    Film m = (Film) sr.selectOne("get_movie_by_id", 12, Film.class);
       // Assuming that the DB has that movie with film_id=12, this should pass
    //    assertEquals(m.title, "Matrix Reloaded");
   }

   @Test
   void selectMany() {
   }

   @Test
   void update() {
   }

   @Test
   void insert() {
   }

   @Test
   void delete() {
   }
}
