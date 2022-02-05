package assignment_1.model;

public class QueryObject {
    public String id;
    public String paramType;
    public String query;

    public QueryObject(String id, String paramType, String query) {
        this.id = id;
        this.paramType = paramType;
        this.query = query;
    }
}
