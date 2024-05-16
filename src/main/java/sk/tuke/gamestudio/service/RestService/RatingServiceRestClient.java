package sk.tuke.gamestudio.service.RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

@Service
public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8000/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String gameName) {
        return restTemplate.getForObject(url + "/" + gameName, Integer.class);
    }

    @Override
    public int getRating(String gameName, String player) {
        return restTemplate.getForObject(url + "/" + gameName + "/" + player, Integer.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
