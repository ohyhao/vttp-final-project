package vttp.finalproject.backend.services;

import java.util.Optional;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp.finalproject.backend.models.Quote;

@Service
public class QuoteService {
    
    public static final String QUOTE_SEARCH = "https://zenquotes.io/api/today";

    public Optional<Quote> getRandomQuote() {

        RequestEntity<Void> req = RequestEntity.get(QUOTE_SEARCH).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);
            Quote q = Quote.create(resp.getBody());
            return Optional.of(q);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();    
    }
}
