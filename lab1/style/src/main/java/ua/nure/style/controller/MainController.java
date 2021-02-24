package ua.nure.style.controller;

import ua.nure.style.entity.Booking;
import ua.nure.style.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nure.style.StyleApplication;
import ua.nure.style.dao.*;

import java.security.Principal;

@Controller
public class MainController {

    private final ServiceDao serviceDao;
    private final BookingDao bookingDao;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final StatusDAO statusDAO;
    private final PasswordEncoder passwordEncoder;

    public MainController(ServiceDao serviceDao, BookingDao bookingDao, UserDAO userDAO, RoleDAO roleDAO, StatusDAO statusDAO, PasswordEncoder passwordEncoder) {
        this.serviceDao = serviceDao;
        this.bookingDao = bookingDao;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.statusDAO = statusDAO;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mainPage(@CookieValue(value = StyleApplication.COOKIE_NAME, defaultValue = "") String cookie) {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public String redirectToUserHomePage(Principal principal) {
        User user = userDAO.getByEmail(principal.getName()).get();
        return String.format("redirect:/user/%s", user.getRole().getName());
    }

    @RequestMapping(value = "/booking/new", method = RequestMethod.GET)
    public ModelAndView getAnonymousBookingPage(@CookieValue(value = StyleApplication.COOKIE_NAME, defaultValue = "") String cookie) {
        String redirectUrl = "redirect:/user/Client/booking/new";
        return new ModelAndView(redirectUrl);
    }

    @RequestMapping(value = "/booking/accepted/{id}", method = RequestMethod.GET)
    public ModelAndView getBookingAcceptedPage(@PathVariable String id) {
        ModelAndView mav = new ModelAndView();
        long idL = Long.parseLong(id);
        Booking booking = this.bookingDao.get(idL).get();
        mav.setViewName("accepted");
        mav.addObject("booking", booking);
        return mav;
    }
}
