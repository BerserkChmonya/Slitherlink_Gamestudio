package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String login, String password, Model model) {
        User user = userService.getUser(login);
        if (user != null && user.checkPassword(password)) {
            loggedUser = user;
            return "redirect:/slitherlink";
        }

        model.addAttribute("error", "Wrong credentials");
        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String newLogin, String newPassword, Model model) {
        if (newLogin == null || newLogin.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            model.addAttribute("error", "Login and password must be filled");
            return "redirect:/";
        }

        if(newPassword.contains(" ") || newPassword.length() < 4){
            model.addAttribute("error", "Password must be at least 4 characters long and cannot contain spaces");
            return "redirect:/";
        }

        if (userService.getUser(newLogin) != null) {
            model.addAttribute("error", "Username already exists");
            return "redirect:/";
        }

        userService.addUser(new User(newLogin, newPassword));
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

}
