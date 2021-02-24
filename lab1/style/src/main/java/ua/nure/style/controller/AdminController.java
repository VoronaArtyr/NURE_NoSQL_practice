package ua.nure.style.controller;

import ua.nure.style.dao.BookingDao;
import ua.nure.style.dao.StatusDAO;
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

@Controller
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private StatusDAO statusDAO;

    @GetMapping()
    public ModelAndView adminMainPage(Principal principal) {
        ModelAndView mav = new ModelAndView();
        List<Booking> bookings = bookingDao.getAll();
        mav.setViewName("admin");
        mav.addObject("bookings", bookings);
        return mav;
    }

    @GetMapping("/booking/{id}")
    public ModelAndView bookingDetails(Principal principal, @PathVariable String id) {
        long l = Long.parseLong(id);
        Booking booking = bookingDao.get(l).get();
        List<Status> statuses = statusDAO.getAll();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("booking");
        mav.addObject("booking", booking);
        mav.addObject("statuses", statuses);
        return mav;
    }


    @GetMapping(value = "/booking/{resId}/status/{statusId}")
    public String updateStatus(@PathVariable String resId, @PathVariable String statusId) {
        Booking booking = bookingDao.get(parseLong(resId)).get();
        Status status = statusDAO.get(parseLong(statusId)).get();
        bookingDao.updateStatus(booking, status);

        return String.format("redirect:/user/admin/booking/%d", booking.getId());

    }

    @GetMapping(value = "/booking/{resId}/delete")
    public String deleteBooking(@PathVariable String resId) {
        Optional<Booking> booking = bookingDao.get(parseLong(resId));
        bookingDao.delete(booking.get());
        return "redirect:/user/admin";
    }
}
