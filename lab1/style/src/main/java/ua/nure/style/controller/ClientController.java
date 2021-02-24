package ua.nure.style.controller;

import ua.nure.style.dao.ServiceDao;
import ua.nure.style.dao.BookingDao;
import ua.nure.style.dao.StatusDAO;
import ua.nure.style.dao.UserDAO;
import ua.nure.style.entity.Service;
import ua.nure.style.entity.Booking;
import ua.nure.style.entity.Status;
import ua.nure.style.entity.User;
import ua.nure.style.form.NewBookingForm;
import ua.nure.style.session.BadCookieException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nure.style.form.DateUtils;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/client")
public class ClientController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private StatusDAO statusDAO;

    @GetMapping
    public ModelAndView userMainPage(Principal principal) throws BadCookieException {
        ModelAndView mav = new ModelAndView();
        List<Booking> bookings = bookingDao.getAll();
        mav.setViewName("client");
        mav.addObject("bookings", bookings);
        return mav;
    }

    @GetMapping(value = "booking/{bookingId}/cancel")
    public String cancelBooking(@PathVariable String bookingId) {
        Booking booking = bookingDao.get(Long.parseLong(bookingId)).get();
        Status status = statusDAO.getByName("Cancelled").get();
        bookingDao.updateStatus(booking, status);
        return "redirect:/user/client";
    }

    @RequestMapping(value = "booking/new", method = RequestMethod.GET)
    public ModelAndView createBookingPage(Principal principal) throws BadCookieException {
        ModelAndView mav = new ModelAndView();
        mav.addObject("NewBookingForm", new NewBookingForm());
        mav.addObject("servicesList", serviceDao.getAll());
        mav.setViewName("newBooking");
        return mav;
    }

    @RequestMapping(value = "booking/new", method = RequestMethod.POST)
    public ModelAndView createBooking(Principal principal, NewBookingForm form) throws ParseException {
        User user = userDAO.getByEmail(principal.getName()).get();

        Booking booking = new Booking();

        booking.setStatus(statusDAO.getByName("Unprocessed").get());
        booking.setClient(user);
        booking.setStartsAt(new Date(DateUtils.htmlDateTimeToMills(form.startDate)));
        booking.setEndsAt(new Date(DateUtils.htmlDateTimeToMills(form.endDate)));

        List<Service> serviceList = new ArrayList<>();
        for (String equipment : form.getService()) {
            Service e = serviceDao.get(Long.parseLong(equipment)).get();
            serviceList.add(e);
        }
        booking.setServices(serviceList);
        bookingDao.save(booking);

        return new ModelAndView("redirect:/user/client/");
    }
}

