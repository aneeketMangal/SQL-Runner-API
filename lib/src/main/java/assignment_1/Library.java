/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package assignment_1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import assignment_1.exceptions.DatabaseNotConnectedException;
import assignment_1.exceptions.ParamTypeDifferentException;
import assignment_1.interfaces.SqlRunner;
import assignment_1.model.QueryObject;
import assignment_1.service.XMLParser;

public class Library implements SqlRunner {

    public Connection connection;
    public String xmlFilePath;
    public XMLParser xmlParser;

    // Constructor for library Object
    public Library(Connection connection, String filePath) {
        this.connection = connection;
        this.xmlFilePath = filePath;
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
        Class<?> classType = queryParam.getClass();
        if (classType.isPrimitive()) {
            if (classType.getName().equals("java.lang.String")) {
                populatedQuery = populatedQuery.replace("${value}", "\"" + queryParam.toString() + "\"");
            } else {

                populatedQuery = populatedQuery.replace("{$value}", queryParam.toString());
            }
        } else if (classType.isArray()) {

            System.out.println(" ---- " + classType.getName());
            String temp = String.join(",", (CharSequence[]) queryParam);
            populatedQuery = populatedQuery.replace("${value}", "(" + temp + ")");
        }
        // only linear collections are allowed
        else if (Collection.class.isAssignableFrom(classType)) {
            
            String temp = String.join(",", (CharSequence[]) ((AbstractCollection<String>) queryParam).toArray());
            System.out.println(temp);
            populatedQuery = populatedQuery.replace("${value}", "(" + temp + ")");
        } else {
            Field[] fields = queryParam.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                String fieldClass = field.getType().getName();
                String fieldValue = null;
                try {
                    fieldValue = (String) field.get(queryParam);
                    if (fieldClass.equals("String")) {
                        populatedQuery = populatedQuery.replace("${" + fieldName + "}", "\"" + fieldValue + "\"");
                    } else {
                        populatedQuery = populatedQuery.replace("${" + fieldName + "}", fieldValue);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw (new RuntimeException(e));
                }
            }
        }
        return populatedQuery;
    }

    public static void main(String[] args) {
        QueryObject qObj = new QueryObject(
                "findMovies",
                "java.lang.String",
                "SELECT a, b, c FROM my_table WHERE x=${value};"

        );
        int[] arr = { 2, 3, 4};
        ArrayList<String> list = new ArrayList<String>();
        list.add("anee");
        System.out.println(new Library(null, "").populateQuery(qObj, arr));

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
            int columnCount = resultMeta.getColumnCount();
            String [] columnNames = new String[columnCount];
            Field [] fields = resultType.getDeclaredFields();
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i-1] = resultMeta.getColumnName(i);
                // retrievedField.set(classObject, value);
            }
            if (resultSet.next()) {
                for(int i  =0 ; i<columnCount; i++){
                    String value = resultSet.getString(i+1);
                    
                }
                R result = resultType.getDeclaredConstructor().newInstance();

                return result;
            }
            return null;
        } catch (IllegalArgumentException | SecurityException | InstantiationException | SQLException
                | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType) {
        try {
            ResultSet resultSet = this.runSelectQuery(queryId, queryParam);
            List<R> result = new ArrayList<R>();

            while (resultSet.next()) {
                R newR = resultType.getDeclaredConstructor().newInstance();
                result.add(newR);
            }
            return result;
        } catch (IllegalArgumentException | SecurityException | SQLException | InstantiationException
                | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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
