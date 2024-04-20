package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.slitherlink.consoleui.UI;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.service.RestService.CommentServiceRestClient;
import sk.tuke.gamestudio.service.RestService.RatingServiceRestClient;
import sk.tuke.gamestudio.service.RestService.ScoreServiceRestClient;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class GamestudioApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GamestudioApplication.class).web(WebApplicationType.NONE).run(args);
    }
    @Bean
    public CommandLineRunner runner(UI ui) {
        return args -> ui.play();
    }

    @Bean
    public UI consoleUI() {
        return new UI();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }
    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }
    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
}
