package ua.nure.style.entity;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class Booking {

    private long id;
    private Status status;
    private User client;
    private Date startsAt;
    private Date endsAt;
    private Date createdAt;
    private List<Service> services;
}
