package assignment_1.model;

public class QueryObject {
    public String id;
    public String paramType;
    public String query;
    public String mapRowTo;

    public QueryObject(String id, String paramType, String query, String mapRowTo) {
        this.id = id;
        this.paramType = paramType;
        this.query = query;
        this.mapRowTo = mapRowTo;
    }
}
