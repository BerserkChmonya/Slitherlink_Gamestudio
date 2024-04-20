package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.JDBCService.CommentServiceJDBC;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {
    private CommentServiceJDBC commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceJDBC();
    }

    @Test
    void testAddCommentAndGetComments() {
        Comment comment = new Comment("TestGame", "TestPlayer", "TestComment", getCurrentDate());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("TestGame");

        assertNotNull(comments);
        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("TestPlayer") && c.getComment().equals("TestComment")));
    }

    @Test
    void testReset() {
        Comment comment = new Comment("TestGame", "TestPlayer", "TestComment", getCurrentDate());
        commentService.addComment(comment);

        commentService.reset();

        List<Comment> comments = commentService.getComments("TestGame");

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    private Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
