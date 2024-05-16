package sk.tuke.gamestudio.service.JPAService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;

import java.util.Collections;
import java.util.List;
@Service
@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        if (comment == null || comment.getComment().isEmpty()) return;

        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = entityManager.createNamedQuery("Comment.getComments", Comment.class)
                .setParameter("game", game).getResultList();
        Collections.reverse(comments);
        return comments;
    }

    @Override
    public void reset() throws CommentException {
        entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
    }
}
