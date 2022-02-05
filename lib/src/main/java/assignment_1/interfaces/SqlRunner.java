package assignment_1.interfaces;
import java.util.List;

public interface SqlRunner {
    <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType) throws Exception;
    <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType) throws Exception;
    <T> int delete(String queryId, T queryParam) throws Exception;
    <T> int update(String queryId, T queryParam) throws Exception;
    <T> int insert(String queryId, T queryParam) throws Exception;
}