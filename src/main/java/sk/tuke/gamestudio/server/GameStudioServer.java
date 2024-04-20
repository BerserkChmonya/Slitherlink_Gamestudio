package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.JPAService.CommentServiceJPA;
import sk.tuke.gamestudio.service.JPAService.RatingServiceJPA;
import sk.tuke.gamestudio.service.JPAService.ScoreServiceJPA;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }
    @Bean
    public CommentService commentServiceService() {
        return new CommentServiceJPA();
    }
    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }
}
