package sk.tuke.gamestudio.service.JPAService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
@Service
@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        String jpql = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player";
        TypedQuery<Rating> query = entityManager.createQuery(jpql, Rating.class);

        query.setParameter("game", rating.getGame());
        query.setParameter("player", rating.getPlayer());

        try {
            Rating existingRating = query.getSingleResult();

            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(rating.getRatedOn());
        } catch (NoResultException e) {
            entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Double avg = entityManager.createNamedQuery("Rating.getAverageRating", Double.class)
                .setParameter("game", game)
                .getSingleResult();
        return avg != null ? avg.intValue() : -1;
    }


    @Override
    public int getRating(String game, String player) throws RatingException {
        return entityManager.createNamedQuery("Rating.getRating", Integer.class)
                .setParameter("game", game).setParameter("player", player).getSingleResult();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}
