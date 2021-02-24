package ua.nure.style.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    private String id;
    private Status status;
    private User client;
    private Date startsAt;
    private Date endsAt;
    private Date createdAt;
    private List<Service> services;

    public Booking(Status status, User user, Date startsAt, Date endsAt, List<Service> services) {
        this.status = status;
        this.client = user;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.services = services;
    }

    public Booking(String id, Status status, User user, Date startsAt, Date endsAt, List<Service> services) {
        this.id = id;
        this.status = status;
        this.client = user;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.services = services;
    }
}
