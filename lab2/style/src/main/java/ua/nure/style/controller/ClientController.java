package ua.nure.style.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.Booking;
import ua.nure.style.entity.Service;
import ua.nure.style.entity.Status;
import ua.nure.style.entity.User;
import ua.nure.style.form.DateUtils;
import ua.nure.style.form.NewBookingForm;
import ua.nure.style.session.BadCookieException;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/client")
public class ClientController {

    private final IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    @GetMapping
    public ModelAndView userMainPage(Principal principal) throws BadCookieException {
        ModelAndView mav = new ModelAndView();
        List<Booking> bookings = dao.getAllBookings();
        mav.setViewName("client");
        mav.addObject("bookings", bookings);
        return mav;
    }

    @GetMapping(value = "booking/{bookingId}/cancel")
    public String cancelBooking(@PathVariable String bookingId) {
        Booking booking = dao.getBooking(Long.parseLong(bookingId)).get();
        Status status = dao.getStatusByName("Cancelled").get();
        dao.updateStatus(booking, status);
        return "redirect:/user/client";
    }

    @RequestMapping(value = "booking/new", method = RequestMethod.GET)
    public ModelAndView createBookingPage(Principal principal) throws BadCookieException {
        ModelAndView mav = new ModelAndView();
        mav.addObject("NewBookingForm", new NewBookingForm());
        mav.addObject("servicesList", dao.getAllServices());
        mav.setViewName("newBooking");
        return mav;
    }

    @RequestMapping(value = "booking/new", method = RequestMethod.POST)
    public ModelAndView createBooking(Principal principal, NewBookingForm form) throws ParseException {
        User user = dao.getUserByEmail(principal.getName()).get();

        Booking booking = new Booking();

        booking.setStatus(dao.getStatusByName("Unprocessed").get());
        booking.setClient(user);
        booking.setStartsAt(new Date(DateUtils.htmlDateTimeToMills(form.startDate)));
        booking.setEndsAt(new Date(DateUtils.htmlDateTimeToMills(form.endDate)));

        List<Service> serviceList = new ArrayList<>();
        for (String equipment : form.getService()) {
            Service e = dao.getService(Long.parseLong(equipment)).get();
            serviceList.add(e);
        }
        booking.setServices(serviceList);
        dao.saveBooking(booking);

        return new ModelAndView("redirect:/user/client/");
    }
}

