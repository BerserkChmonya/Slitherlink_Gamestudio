package sk.tuke.gamestudio.service.JPAService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

import java.util.List;

@Service
@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void addUser(User user) {
        try {
            entityManager.persist(user);
        }
        catch (Exception e) {
            System.out.println("User already exists");
        }

    }
    @Override
    public User getUser(String username){
        if (username == null) return null;

        List<User> users = entityManager.createNamedQuery("User.getUser", User.class)
                .setParameter("username", username).getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
    @Override
    public void reset() {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}
