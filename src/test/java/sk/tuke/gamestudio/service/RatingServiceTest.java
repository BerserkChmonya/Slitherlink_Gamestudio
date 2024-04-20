package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.JDBCService.RatingServiceJDBC;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RatingServiceTest {
    private RatingServiceJDBC ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceJDBC();
    }

    @Test
    void testSetRatingAndGetRating() {
        Rating rating = new Rating("TestGame", "TestPlayer", 5, getCurrentDate());
        ratingService.setRating(rating);

        int retrievedRating = ratingService.getRating("TestGame", "TestPlayer");

        assertEquals(5, retrievedRating);
    }
    @Test
    void testGetAverageRating(){
        Rating rating1 = new Rating("TestGame", "TestPlayer", 9, getCurrentDate());
        Rating rating2 = new Rating("TestGame", "TestPlayer1", 7, getCurrentDate());
        ratingService.setRating(rating1);
        ratingService.setRating(rating2);

        int averageRating = ratingService.getAverageRating("TestGame");

        assertEquals(8, averageRating);
    }
    @Test
    void testUpdateRating(){
        Rating rating1 = new Rating("TestGame", "TestPlayer", 9, getCurrentDate());
        Rating rating2 = new Rating("TestGame", "TestPlayer", 7, getCurrentDate());
        ratingService.setRating(rating1);
        ratingService.setRating(rating2);

        int retrievedRating = ratingService.getRating("TestGame", "TestPlayer");

        assertEquals(7, retrievedRating);
    }

    @Test
    void testReset() {
        Rating rating = new Rating("TestGame", "TestPlayer", 5, getCurrentDate());
        ratingService.setRating(rating);

        ratingService.reset();

        assertThrows(RatingException.class, () -> ratingService.getRating("TestGame", "TestPlayer"));
    }

    private Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
