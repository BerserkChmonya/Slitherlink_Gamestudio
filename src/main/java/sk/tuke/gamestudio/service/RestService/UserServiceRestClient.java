package sk.tuke.gamestudio.service.RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

@Service
public class UserServiceRestClient implements UserService {
    @Autowired
    private RestTemplate restTemplate;
    private final String url = "http://localhost:8000/api/user";

    @Override
    public void addUser(User user) {
        try {
            restTemplate.postForEntity(url, user, User.class);
        } catch (Exception e) {

        }
    }

    @Override
    public User getUser(String username) {
        return restTemplate.getForObject(url + "/" + username, User.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
