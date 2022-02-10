# CS305 Assignment 5
Submitter name: Aneeket Mangal\
Roll No.: 2019CSB1071\
Course:  CS303
===========================================
## 1. What does this program do
This program is a Java based library that reads SQL queries from an XML file and executes them on an RDBMS.
This library provides an API of following signature:
   ```
   Library(Connection connectionObject, String XMLFilePath)
   ```
The API implements following endpoints:
```bash
>  <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType);
```

```bash
>  <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType);
```

```bash
>  <T> int insert(String queryId, T queryParam);
```
```bash
>  <T> int update(String queryId, T queryParam);
```
```bash
>  <T> int delete(String queryId, T queryParam);
```
The parameters of the query contains placeholders of the form ${placeholder_name} and they are populated
at the runtime with the provided object in the method call.
* In case of update, insert, and delete queries the API returns the number of rows affected.
* In case of selectOne, query API returns the POJO, whose class is provided in the call.
* In case of selectMany, query API returns an ArrayList of POJO.
* The parameters for the SQL queries will be dynamically populated at runtime from the supplied objects.
Populating plain-old-java-objects (POJOs) from the results of SELECT queries.

## 2. A description of how this program works
Terminology associated with the project
* ```queryParam``` : Object sent in the API call, which is used to populate placeholders.
* ```id```/```queryId```: id of a query as in the XML file.
* ```paramType```: name of the object as mentioned in the paramType attribute of the sql query in XML file.
* ```componentType```: Following are considered as the component types.
  * String
  * Primitives (int, short, byte, long, float, double, boolean, and char) and there respective *Wrapper* classes.
  * Arrays of String/Primitives
  * Collection of String/Primitives



Custom Runtime exceptions are defined for the program
* ```ParamTypeDifferentException```: Thrown when ```paramType``` and FQN of ```queryParam``` are not same.
* ```MultipleResultsFoundException```: Thrown when multiple records are fetched in a ```selectOne()``` API call.
* ```QueryNotFoundException```: Thrown when query with the given id cannot be found in the XML file.
* ```NotAComponentType```: Thrown when the ```queryParam``` is not a ```componentType```.



Following Classes are implemented in the program
* ```QueryObject```
  * Contains description of a query, namely:
    * **id**: id of the query in the XML file.
    * **query**: unprocessed query string.
    * **paramType** : contains the expected paramType of queryParam in API calls.
  


* ```XMLParser```
  * This class provides the functionality to fetch SQL queries from the XML file.
  * It has a single constructor which takes in the path of XML file under consideration as a ```Java``` String.
  * It makes use of the ```DOMParser``` API of ```Java```.
  * It defines only one function ```getQueryObject```
  * Methodically, first we fetch a ```Nodelist``` Object of all "sql" tags in the file. Then it looks for the **first** tag that has a ```id``` attribute equal to the required queryId.
  * The function returns an Object of type ```QueryObject```.




* ```StringUtility```
  * After fetching the query using XMLParser, it is populated with fields values of queryParam object.
  * This class provides utility of parsing the fetched queries and populating them.
  * The ```populateQuery()``` method is called by the ```Library``` class during execution.
  * The following procedure is followed for populateQuery
    * First it checks if the ```queryParam``` is *null*.
      * If it's null and the ```paramType``` fetched from the XML is also "null" (String), then program assumes that query has no placeholders and returns the query for further execution as it is.
      * If it's null and the ```paramType``` is not equal to String "null", then it throws a ```ParamTypeDifferentException```.
    * If it's not null we check if it's a ```componentType```.
      * If its a ```componentType```, then it is parsed using inbuilt parsing implementations for them.
      * Else, an exception is thrown which is catched and the program then checks for user defined custom Java Objects
      * It then looks for matching fields of the object with a placeholder in the query and if it is a match, it populates the placeholder.
      * The parsing methods for componentTypes are already defined.
        * In case user wishes to use Objects whose fields are not ```componentType```, then user needs to implement ```toString``` implementations of those fields.
        * In case user wishes to use ```Date```, they need to override toString method of ```java.lang.Date```.
        * User can also provide array/Collection of Object which are not ```componentType```, then again they need to implement toString method for those objects.
  * The finally parsed query are returned for execution by the JDBC.
  * Runtime Exceptions are thrown in case some field of the passed object is null (Due to IllegalAccessException of Reflection API).

  * **Note**: Non-linear collections are not allowed.

* ```Library```: 
  * This class implements the ```SQLRunner``` interface which contains abstract definition of API endpoints as listed above.
  * This class has a single construction which requires a JDBC ```Connection``` object and filePath of the XML which consists of the SQL queries.
  * This class implements the endpoints of the API. 
  * It contains various helper functions to help process the SQL query and run it against a RDBMS. 
  

## 3. How to compile and run this program

## 4. Provide a snapshot of a sample run



