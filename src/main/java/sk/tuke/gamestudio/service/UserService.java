package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    void addUser(User user);
    User getUser(String username);
    void reset();
}
