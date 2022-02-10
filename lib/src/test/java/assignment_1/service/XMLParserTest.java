package assignment_1.service;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import assignment_1.exceptions.QueryNotFoundException;
import assignment_1.model.QueryObject;
import static org.junit.jupiter.api.Assertions.*;


public class XMLParserTest {
    @ParameterizedTest
    @ValueSource(
            strings = {
                    "findMovies",
                    "Temp",
                    "findMoviesByDirector",
                    "findMoviesByDirectorAndYear",
            }
    )
    public void getQueryObjectTest(String queryId){
        XMLParser xmlParser = new XMLParser("C:\\Users\\lenovo\\Desktop\\Adhoora\\academics\\year3\\software\\cs305_2022\\lib\\src\\test\\resources\\queries.xml");
        try {
            QueryObject qObj = xmlParser.getQueryObject(queryId);
            assertEquals(qObj.query, "SELECT * FROM actor WHERE first_name=${value};");
        }
        catch (RuntimeException e){
            assertTrue(e instanceof QueryNotFoundException);
        }
        // checking using wrong file path

        xmlParser = new XMLParser("C:\\Users\\lenovo\\Desktop\\Adhoora\\academics\\year3\\software\\cs305_2022\\lib\\src\\test\\resources\\queriesFalse.xml");
        XMLParser finalXmlParser = xmlParser;
        assertThrows(RuntimeException.class, ()->{
            finalXmlParser.getQueryObject(queryId);
        });
    }

}
