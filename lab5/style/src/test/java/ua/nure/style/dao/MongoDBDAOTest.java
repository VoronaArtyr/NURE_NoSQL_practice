package ua.nure.style.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nure.style.entity.*;

import java.sql.Date;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MongoDBDAOTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    private final MongoDBDAO dao = new MongoDBDAO();

    private Service service;
    private User user;
    private Booking booking;
    private Role role;
    private Status status;

    private final MongoCollection<Document> serviceCollection;
    private final MongoCollection<Document> usersCollection;
    private final MongoCollection<Document> bookingsCollection;
    private final MongoCollection<Document> rolesCollection;
    private final MongoCollection<Document> statusesCollection;

    public MongoDBDAOTest() {
        var db = new MongoClient().getDatabase("style");
        this.serviceCollection = db.getCollection("service");
        this.usersCollection = db.getCollection("users");
        this.bookingsCollection = db.getCollection("bookings");
        this.rolesCollection = db.getCollection("roles");
        this.statusesCollection = db.getCollection("statuses");
    }




    @BeforeEach
    void setUp() {

        service = new Service(
                "test name one",
                "test description one",
                "1"
        );

        this.dao.saveService(service);
        user = new User(
                "test fname",
                "test lname",
                "test email",
                passwordEncoder.encode("password"),
                new Role("test role")
        );
        this.dao.saveUser(user);

        booking = new Booking(
            new Status("unprocessed"),
            dao.getUserByEmail(user.getEmail()).get(),
            new Date(System.currentTimeMillis()),
            new Date(System.currentTimeMillis()),

            Arrays.asList(
                    new Service("service one", "description eq one", "2"),
                    new Service("service one", "description eq ", "2"),
                    new Service("service two", "description eq ", "2"),
                    new Service("service three", "description one", "2")
            )
        );
        this.dao.saveBooking(booking);

        role = new Role(
                "test role 1"
        );
        this.dao.saveRole(role);

        status = new Status(
                "test status name"
        );
        this.dao.saveStatus(status);
    }

    @AfterEach
    void tearDown() {
        this.serviceCollection.drop();
        this.bookingsCollection.drop();
        this.statusesCollection.drop();
        this.rolesCollection.drop();
        this.usersCollection.drop();
    }

    @Test
    void getServiceById() {
        this.serviceCollection.insertOne(
                new Document()
                    .append("name", "getServiceByIdTestName")
                    .append("description", service.getDescription())
        );
        final var document = this.serviceCollection.find(
                eq("name", "getServiceByIdTestName")
        ).first();
        assert document != null;
        String id = document.get("_id").toString();
        assertTrue(dao.getService(id).isPresent());
    }

    @Test
    void getAllService() {
        final var allService = dao.getAllServices();
        assertThat(allService).hasAtLeastOneElementOfType(Service.class);
    }

    @Test
    void saveService() {
        dao.saveService(service);
    }

    @Test
    void getUserById() {
        this.usersCollection.insertOne(
                new Document()
                        .append("fname", "getUserByIdTestName")
                        .append("lname", user.getLname())
                        .append("email", user.getEmail())
                        .append("password", passwordEncoder.encode("password"))
                        .append("role", user.getRole().getName())
        );
        final var document = this.usersCollection.find(
                eq("fname", "getUserByIdTestName")
        ).first();
        assert document != null;
        String id = document.get("_id").toString();
        assertTrue(dao.getUser(id).isPresent());
    }

    @Test
    void saveUser() {
        dao.saveUser(user);
    }

    @Test
    void getAllUsers() {
        assertThat(dao.getAllUsers()).hasAtLeastOneElementOfType(User.class);
    }

    @Test
    void getUserByEmailTest() {
        dao.saveUser(user);
        assertThat(dao.getUserByEmail(user.getEmail())).isPresent();
    }

    @Test
    void saveBooking() {
        dao.saveBooking(booking);
    }

    @Test
    void getBooking() {
        final var bookingDocument = this.bookingsCollection.find(eq("status", "unprocessed")).first();
        assert bookingDocument != null;
        assertThat( dao.getBooking(bookingDocument.get("_id").toString()) ).isPresent();
    }

    @Test
    void addServiceToBooking() {
        Service serviceToSave = new Service("New service One", "Description on eq1", "234.1");
        this.dao.saveService(serviceToSave);
        Document newServiceDocument = this.serviceCollection.find(eq("name", "New service One")).first();
        this.dao.saveBooking(booking);
        Document bookingDocument = this.bookingsCollection.find().first();
        assert newServiceDocument != null;
        assert bookingDocument != null;
        this.dao.addService(
                newServiceDocument.get("_id").toString(),
                bookingDocument.get("_id").toString()
        );
        final var savedBooking = this.dao.getBooking(bookingDocument.get("_id").toString()).get();
//        System.out.println("Booking Before:");
//        System.out.println(booking);
//        System.out.println("Booking After");
//        System.out.println(savedBooking);
        assertThat(savedBooking.getServices()).contains(serviceToSave);
    }

    @Test
    void saveRole() {
        this.dao.saveRole(role);
    }

    @Test
    void saveStatus() {
        this.dao.saveStatus(status);
    }

    @Test
    void getAllStatuses() {
        this.dao.saveStatus(status);
        final var allStatuses = this.dao.getAllStatuses();
        System.out.println(allStatuses);
        assertThat(allStatuses).isNotEmpty();
    }

    @Test
    void getStatus() {
        this.dao.saveStatus(status);
        final var statusDocument = this.statusesCollection.find().first();
        assert statusDocument != null;
        final var gotStatus = this.dao.getStatus(statusDocument.get("_id").toString());
        System.out.println(gotStatus);
        assertThat(gotStatus).isPresent();
    }

    @Test
    void updateStatus() {

        Status fromStatus = new Status("from status");
        Status toStatus = new Status("to status");

        this.dao.saveStatus(fromStatus);
        this.dao.saveStatus(toStatus);

        this.dao.saveBooking(booking);
        final var bookingDocument = this.bookingsCollection.find().first();
        assert bookingDocument != null;
        Booking r1 = this.dao.getBooking(bookingDocument.get("_id").toString()).get();
        this.dao.updateStatus(r1, fromStatus);
        assertThat(this.dao.getBooking(bookingDocument.get("_id").toString()).get().getStatus()).isEqualTo(fromStatus);
        this.dao.updateStatus(r1, toStatus);
        assertThat(this.dao.getBooking(bookingDocument.get("_id").toString()).get().getStatus()).isEqualTo(toStatus);
    }

    @Test
    void deleteBooking() {
        this.dao.saveBooking(booking);
        final var bookingDocument = this.bookingsCollection.find().first();
        assert bookingDocument != null;
        String id = bookingDocument.get("_id").toString();
        booking.setId(id);
        this.dao.deleteBooking(booking);
        assertThat(this.dao.getBooking(id)).isEmpty();
    }

    @Test
    void getBookingByEmail() {
        this.dao.saveUser(user);
        String email = user.getEmail();
        this.dao.saveBooking(booking);
        this.dao.saveBooking(booking);
        final var bookings = this.dao.getBookingsByEmail(email);
        assertThat(bookings).isNotEmpty();
    }

    @Test
    void getAllBookings() {
        this.dao.saveBooking(booking);
        this.dao.saveBooking(booking);
        this.dao.saveBooking(booking);
        final var allBookings = this.dao.getAllBookings();
        assertThat(allBookings).isNotEmpty();
    }



}