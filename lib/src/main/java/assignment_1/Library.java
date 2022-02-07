/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package assignment_1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.LongBinaryOperator;

import javax.print.DocFlavor.STRING;
import javax.print.attribute.HashAttributeSet;

import assignment_1.exceptions.DatabaseNotConnectedException;
import assignment_1.exceptions.MultipleResultsFound;
import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.interfaces.SqlRunner;
import assignment_1.model.QueryObject;
import assignment_1.service.ReplaceUtility;
import assignment_1.service.XMLParser;

public class Library implements SqlRunner {

    public Connection connection;
    public String xmlFilePath;
    public XMLParser xmlParser;
    public ReplaceUtility ru;

    // Constructor for library Object
    public Library(Connection connection, String filePath) {
        this.connection = connection;
        this.xmlFilePath = filePath;
        this.ru = new ReplaceUtility();
    }

    // Method to check if the connection object is present
    public void checkConnection() {
        if (connection == null) {
            throw new DatabaseNotConnectedException("Database not connected");
        }
    }

    public <T> void checkParamTypes(QueryObject qObj, T queryParam) {
        String paramType = queryParam.getClass().getName();
        String paramTypeInXML = qObj.paramType;
        System.out.println(paramType);
        System.out.println(paramType + " " + paramTypeInXML);
        if (!paramType.equals(paramTypeInXML)) {
            throw new ParamTypeDifferentException(paramType, paramType, qObj.id);
        }
    }

    public <T> String populateQuery(QueryObject qObj, T queryParam) {
        // TODO: Implement this method
        // this.checkParamTypes(qObj, queryParam);

        String populatedQuery = qObj.query;
        Object paramObject = (Object) queryParam;

        try {
            populatedQuery = ru.replaceUtility(paramObject, populatedQuery, "value");
            return populatedQuery;
        } catch (Exception e) {
            // fetching the fields of the class
            Field[] fields = queryParam.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object fieldObject = field;
                String fieldName = field.getName();
                try {
                    populatedQuery = ru.replaceUtility(fieldObject, populatedQuery, fieldName);
                    

                } catch (IllegalArgumentException E) {
                    throw (new RuntimeException(E));
                }
            }

            return populatedQuery;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {

        // Integer a = 5;
        // int b = 3;
        // String c = "hello";
        // anee d = new anee();
        // Boolean dd = true;
        // float ee = 23.4f;
        // double ff = 23.4;
        // String[] e = { "anee", "temp", "funny" };
        // Integer[] eef = { 1, 2, 3 };
        // char[] eeef = { 'a', 'b', 'c' };
        // String q = "SELECT * FROM books WHERE id = ${id};";
        // ReplaceUtility rr = new ReplaceUtility();

        // Collection<String> collection = new PriorityQueue<String>();

        // System.out.println(collection.getClass().getName());
        // collection.add("anee");
        // collection.add("gunny");
        // collection.add("temp");

        // Iterator<?> iterator = collection.iterator();

        // // while loop
        // while (iterator.hasNext()) {
        // System.out.println("value= " + iterator.next().getClass().getName());

        // }

        // System.out.println(rr.replaceUtility(((Object)e), q, "id"));

        // System.out.println(rr.replaceUtility(((Object)dd).getClass().getName(), q,
        // "id", "1"));
        // System.out.println(rr.replaceUtility(((Object)ee).getClass().getName(), q,
        // "id", "1"));
        // System.out.println(rr.replaceUtility(((Object)ff).getClass().getName(), q,
        // "id", "1"));

        // try {
        // Class.forName("com.mysql.cj.jdbc.Driver");
        // } catch (ClassNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection c;
        System.out.println("fds");
        try {
        c =
        DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sakila?allowPublicKeyRetrieval=true&user=root&password=papamangal&useSSL=false");
        Library library = new Library(c,
        "src/main/resources/assignment_1/queries.xml");

        

        Statement statement;
        String finalQuery = "SELECT * from actor where first_name = \"MINNIE\"";
        statement = library.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(finalQuery);
        // resultSet.first();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            // field.set(classObject, value);
            
            System.out.println(rsmd.getColumnName(i));
            
        }
        Date temp;
        List<anee> te = new ArrayList<anee>();
        while(resultSet.next()){
            anee tee = new anee();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                Object value = resultSet.getObject(i + 1);
                Field field = tee.getClass().getDeclaredField(rsmd.getColumnName(i + 1));   //get the field             
                Class<?> teep = field.getType();
                try {
                    // Object tempf = teep.cast(value);
                    field.set(tee, value);
                } catch (Exception e) {
                    System.out.println(e);
                    //TODO: handle exception
                }
            }
            te.add(tee);

        }
        for(anee t : te){
            System.out.println("actor_id: " +t.actor_id);
            System.out.println("first_name: " +t.first_name);
            System.out.println("last_name: " +t.last_name);
            System.out.println("last_update: " +t.last_update);
            System.out.println("<--------------------->");
        }
        // te.length();
        

    }
        catch(Exception e) {
            e.printStackTrace();
            // System.out.println(e.);
        }
        // QueryObject queryObject = library.xmlParser.getQueryObject("findMovies");
        // String finalQuery = library.populateQuery(queryObject, new anee());
        // System.out.println(finalQuery);
        // } catch (SQLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // try (
            // } catch (SQLException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // Library temp = new Library(null, "fdsa");
                
                // QueryObject qObj = new QueryObject(
                //     "findMovies",
                //     "java.lang.String",
                //     "SELECT a, b, c FROM my_table WHERE x=${value};"
                    
                // );
                // Integer [] aa = {1,2,3};
                // String [] bb = {"a","b","c"};
                // // char [] bb = {};

                // String query = temp.populateQuery(qObj,aa);
                // System.out.println(query);
                // query = temp.populateQuery(qObj,bb);
                // System.out.println(query);
                    // System.out.println((((Object) eef), q, "id"));
        // int[] arr = { 2, 3, 4};
        // ArrayList<String> list = new ArrayList<String>();
        // list.add("anee");
        // System.out.println(new Library(null, "").populateQuery(qObj, arr));

        // ArrayList<String> list = new ArrayList<String>();
        // list.add("aneeket");
        // list.add("banawat");
        // String[] arr = new String[2];
        // arr[0] = "anee";
        // arr[1] = "temp";

        // System.out.println(Arrays.toString(list.toArray()));
        // System.out.println(arr.getClass().isArray() + " ---" + Arrays.toString(arr));
        // boolean isCollection = Collection.class.isAssignableFrom(list.getClass());

        // System.out.println(isCollection);
        // System.out.println(a.getClass().getSimpleName() + " --" +a.toString());
    }

    @Override
    public <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType) {
        try {
            ResultSet resultSet = this.runSelectQuery(queryId, queryParam);
            ResultSetMetaData resultMeta = resultSet.getMetaData();
            R returnPOJO = resultType.getDeclaredConstructor().newInstance(); 
            if(resultSet.next()){
                for (int i = 0; i < resultMeta.getColumnCount(); i++) {
                    Object value = resultSet.getObject(i + 1);
                    Field field = resultType.getDeclaredField(resultMeta.getColumnName(i + 1));   //get the field             
                    field.set(returnPOJO, value);
                }
                if(resultSet.next()){
                    throw new MultipleResultsFound(queryId);
                }
                else{
                    return returnPOJO;
                }
            }
            else{
                return null;
            }
        }  
        catch (ClassCastException | IllegalArgumentException | SecurityException | InstantiationException
                | SQLException
                | IllegalAccessException |NoSuchFieldException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } 

    }

    @Override
    public <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType) {
        try {
            ResultSet resultSet = this.runSelectQuery(queryId, queryParam);
            ResultSetMetaData resultMeta = resultSet.getMetaData();
            List<R> parsedOutput= new ArrayList<R>();
            while(resultSet.next()){
                R tempPOJO = resultType.getDeclaredConstructor().newInstance(); 
                for (int i = 0; i < resultMeta.getColumnCount(); i++) {
                    Object value = resultSet.getObject(i + 1);
                    Field field = resultType.getDeclaredField(resultMeta.getColumnName(i + 1));   //get the field             
                    field.set(tempPOJO, value);
                }
                parsedOutput.add(tempPOJO);
            }
            return parsedOutput;   
        }  
        catch (ClassCastException | IllegalArgumentException | SecurityException | InstantiationException
                | SQLException
                | IllegalAccessException |NoSuchFieldException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } 
    }

    public <T> int runCountQuery(String queryId, T queryParam) {
        try {
            this.checkConnection();
            QueryObject queryObject = this.xmlParser.getQueryObject(queryId);
            String finalQuery = this.populateQuery(queryObject, queryParam);
            Statement statement;
            statement = this.connection.createStatement();
            int countAffectedRows = statement.executeUpdate(finalQuery);
            return countAffectedRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> ResultSet runSelectQuery(String queryId, T queryParam) {
        try {
            this.checkConnection();
            QueryObject queryObject = this.xmlParser.getQueryObject(queryId);
            String finalQuery = this.populateQuery(queryObject, queryParam);
            Statement statement;
            statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(finalQuery);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> int insert(String queryId, T queryParam) {
        try {
            return this.runCountQuery(queryId, queryParam);
        } catch (IllegalArgumentException | SecurityException e) {
            throw (new RuntimeException(e));
        }
    }

    @Override
    public <T> int delete(String queryId, T queryParam) {
        try {
            return this.runCountQuery(queryId, queryParam);
        } catch (IllegalArgumentException | SecurityException e) {
            throw (new RuntimeException(e));
        }
    }

    @Override
    public <T> int update(String queryId, T queryParam) {
        try {
            return this.runCountQuery(queryId, queryParam);
        } catch (IllegalArgumentException | SecurityException e) {
            throw (new RuntimeException(e));
        }
    }
}
