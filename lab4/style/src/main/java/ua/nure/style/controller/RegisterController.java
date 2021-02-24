package ua.nure.style.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.User;
import ua.nure.style.form.UserRegisterForm;
import ua.nure.style.session.UserAlreadyExistsException;

@Controller
public class RegisterController {

    private final IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    private final PasswordEncoder passwordEncoder;

    public RegisterController(PasswordEncoder passwordEncoder) {
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

        if (dao.getUserByEmail(userRegisterForm.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", userRegisterForm.getEmail()));
        }

        User user = new User();
        user.setFname(userRegisterForm.getFName());
        user.setLname(userRegisterForm.getLName());
        user.setEmail(userRegisterForm.getEmail());
        user.setRole(dao.getRoleByName("client").get());
        user.setPassword(passwordEncoder.encode(userRegisterForm.getPasswordHash()));
        dao.saveUser(user);

        return "redirect:/login";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView userAlreadyExistsPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("GenericError");
        return mav;
    }

}