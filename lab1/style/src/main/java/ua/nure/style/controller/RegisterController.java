package ua.nure.style.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nure.style.dao.RoleDAO;
import ua.nure.style.dao.UserDAO;
import ua.nure.style.entity.User;
import ua.nure.style.form.UserRegisterForm;
import ua.nure.style.session.UserAlreadyExistsException;

@Controller
public class RegisterController {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage() {
        ModelAndView mav = new ModelAndView();
        UserRegisterForm userRegisterForm = new UserRegisterForm();
        mav.addObject("UserRegisterForm", userRegisterForm);
        mav.setViewName("register");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String handleRegisterForm(UserRegisterForm userRegisterForm) throws UserAlreadyExistsException {

        if (userDAO.getByEmail(userRegisterForm.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", userRegisterForm.getEmail()));
        }

        User user = new User();
        user.setFname(userRegisterForm.getFName());
        user.setLname(userRegisterForm.getLName());
        user.setEmail(userRegisterForm.getEmail());
        user.setRole(roleDAO.getByName("client").get());
        user.setPassword(passwordEncoder.encode(userRegisterForm.getPasswordHash()));
        userDAO.save(user);

        return "redirect:/login";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView userAlreadyExistsPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("GenericError");
        return mav;
    }

}