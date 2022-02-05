package assignment_1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import assignment_1.exceptions.DatabaseNotConnectedException;
import assignment_1.exceptions.QueryNotFoundException;
import assignment_1.model.QueryObject;
import assignment_1.service.XMLParser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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
        XMLParser xmlParser = new XMLParser("C:\\Users\\lenovo\\Desktop\\Adhoora\\academics\\year3\\software\\cs305_2022\\lib\\src\\test\\resources\\test.xml");
        try {
            QueryObject qObj = xmlParser.getQueryObject(queryId);
            assertTrue(qObj.query.equals("Test String"));
        }
        catch (RuntimeException e){
            assertTrue(e instanceof QueryNotFoundException);
        }
    }
}
