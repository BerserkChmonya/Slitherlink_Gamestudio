package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.JDBCService.ScoreServiceJDBC;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ScoreServiceTest {
    private ScoreServiceJDBC scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new ScoreServiceJDBC();
    }

    @Test
    void testAddScoreAndGetTopScores() {
        Score score = new Score("TestGame", "TestPlayer", 100, getCurrentDate());
        scoreService.addScore(score);

        List<Score> topScores = scoreService.getTopScores("TestGame");

        assertNotNull(topScores);
        assertTrue(topScores.stream().anyMatch(s -> s.getPlayer().equals("TestPlayer") && s.getPoints() == 100));
    }

    @Test
    void testReset() {
        Score score = new Score("TestGame", "TestPlayer", 100, getCurrentDate());
        scoreService.addScore(score);

        scoreService.reset();

        List<Score> topScores = scoreService.getTopScores("TestGame");

        assertNotNull(topScores);
        assertTrue(topScores.isEmpty());
    }


    private Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
