package ua.nure.style.controller;

import ua.nure.style.dao.ConnectionFactory;
import ua.nure.style.dao.DAOFactory;
import ua.nure.style.dao.DAOType;
import ua.nure.style.dao.IMyDao;
import ua.nure.style.entity.Booking;
import ua.nure.style.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/user/admin")
public class AdminController {


    private final IMyDao dao = new DAOFactory(new ConnectionFactory()).getDao(DAOType.MYSQL);

    @GetMapping()
    public ModelAndView adminMainPage(Principal principal) {
        ModelAndView mav = new ModelAndView();
        List<Booking> bookings = dao.getAllBookings();
        mav.setViewName("admin");
        mav.addObject("bookings", bookings);
        return mav;
    }

    @GetMapping("/booking/{id}")
    public ModelAndView bookingDetails(Principal principal, @PathVariable String id) {
        long l = Long.parseLong(id);
        Booking booking = dao.getBooking(l).get();
        List<Status> statuses = dao.getAllStatuses();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("booking");
        mav.addObject("booking", booking);
        mav.addObject("statuses", statuses);
        return mav;
    }


    @GetMapping(value = "/booking/{resId}/status/{statusId}")
    public String updateStatus(@PathVariable String resId, @PathVariable String statusId) {
        Booking booking = dao.getBooking(parseLong(resId)).get();
        Status status = dao.getStatus(parseLong(statusId)).get();
        dao.updateStatus(booking, status);

        return String.format("redirect:/user/admin/booking/%d", booking.getId());

    }

    @GetMapping(value = "/booking/{resId}/delete")
    public String deleteBooking(@PathVariable String resId) {
        Optional<Booking> booking = dao.getBooking(parseLong(resId));
        dao.deleteBooking(booking.get());
        return "redirect:/user/admin";
    }
}
