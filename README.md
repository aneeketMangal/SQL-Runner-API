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
Read the SQL queries from an XML based configuration file.
The parameters for the SQL queries will be dynamically populated at runtime from the supplied objects.
Populating plain-old-java-objects (POJOs) from the results of SELECT queries.

## 2. A description of how this program works
NON LINEAR COLLECTIONS ARE NOT ALLOWED

## 3. How to compile and run this program

## 4. Provide a snapshot of a sample run



