package vttp.finalproject.backend.models;

import java.io.IOException;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Quote {
    
    private String quote;
    private String author;
    
    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public static Quote create(String json) throws IOException {
        Quote q = new Quote();
        JsonReader r = Json.createReader(new StringReader(json));
        JsonArray a = r.readArray();
        JsonObject quote = a.getJsonObject(0);
        q.quote = (quote.getString("q"));
        q.author = (quote.getString("a"));
        
        return q;
    }

    public static JsonObject toJson(Quote quote) {
        return Json.createObjectBuilder()
            .add("quote", quote.getQuote())
            .add("author", quote.getAuthor())
            .build();
    }
    
    

}
