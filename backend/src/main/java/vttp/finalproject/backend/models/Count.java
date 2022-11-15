package vttp.finalproject.backend.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Count {
    
    private Integer count;

    public Integer getCount() { return count; }

    public void setCount(Integer count) { this.count = count; }

    public static Count populate(SqlRowSet rs) {

        Count count = new Count();
        count.setCount(rs.getInt("count"));
        return count;
    }

    public static JsonObject toJson(Count count) {
        return Json.createObjectBuilder()
            .add("count", count.getCount())
            .build();
    }
    
}
