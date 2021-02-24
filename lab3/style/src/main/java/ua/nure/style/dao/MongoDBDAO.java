package ua.nure.style.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import ua.nure.style.entity.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@ToString
@EqualsAndHashCode
public class MongoDBDAO implements IMyDao {

    private final MongoCollection<Document> serviceCollection;
    private final MongoCollection<Document> bookingsCollection;
    private final MongoCollection<Document> usersCollection;
    private final MongoCollection<Document> rolesCollection;
    private final MongoCollection<Document> statusesCollection;

    public MongoDBDAO() {
        MongoDatabase db = new MongoClient().getDatabase("style");
        this.serviceCollection = db.getCollection("services");
        this.bookingsCollection = db.getCollection("bookings");
        this.usersCollection = db.getCollection("users");
        this.rolesCollection = db.getCollection("roles");
        this.statusesCollection = db.getCollection("statuses");
    }

    @Override
    public Optional<Service> getService(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        final var document = serviceCollection.find(query).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
            new Service(
                    document.get("_id").toString(),
                    document.get("name").toString(),
                    document.get("description").toString()
            )
        );
    }

    @Override
    public List<Service> getAllServices() {
        final var documents = this.serviceCollection.find();
        List<Service> serviceList = new ArrayList<>();
        for (Document document : documents) {
            serviceList.add(
                new Service(
                        document.get("_id").toString(),
                        document.get("name").toString(),
                        document.get("description").toString()
                )
            );
        }
        return serviceList;
    }

    @Override
    public List<Service> getServicesByBooking(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        final var booking = this.bookingsCollection.find(query).first();

        assert booking != null;

        final var serviceDocuments = booking.getList("service", Document.class);
        List<Service> serviceList = new ArrayList<>();
        for (Document serviceDocument : serviceDocuments) {
            serviceList.add(
                    new Service(
                            serviceDocument.get("_id").toString(),
                            serviceDocument.get("name").toString(),
                            serviceDocument.get("description").toString()
                    )
            );
        }
        return serviceList;
    }

    @Override
    public void saveService(Service service) {
        this.serviceCollection.insertOne(
                new Document()
                    .append("name", service.getName())
                    .append("description", service.getDescription())
        );
    }

    @Override
    public void addService(String serviceId, String bookingId) {
        final var serviceDocument = this.serviceCollection.find(eq("_id", new ObjectId(serviceId))).first();

        this.bookingsCollection.findOneAndUpdate(
                eq("_id", new ObjectId(bookingId)),
                Updates.push("service", serviceDocument)
        );
    }

    @Override
    public Optional<Role> getRole(String id) {
        final Document document = this.rolesCollection.find(eq("_id", new ObjectId(id))).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
                new Role(
                    document.get("_id").toString(),
                    document.get("name").toString()
                )
        );
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        final var roleDocument = this.rolesCollection.find(eq("name", name)).first();
        if (roleDocument == null) {
            return Optional.empty();
        }
        return Optional.of(
                new Role(
                        roleDocument.get("_id").toString(),
                        roleDocument.get("name").toString()
                )
        );
    }

    @Override
    public void deleteRole(Role role) {
        System.out.println("deleteRole");
    }

    @Override
    public void saveRole(Role role) {
        Document document = new Document().append("name", role.getName());
        this.rolesCollection.insertOne(document);
    }

    @Override
    public List<Role> getAllRoles() {
        final var roleDocuments = this.rolesCollection.find();
        List<Role> roleList = new ArrayList<>();
        for (Document roleDocument : roleDocuments) {
            roleList.add(
                    new Role(
                        roleDocument.get("_id").toString(),
                        roleDocument.get("name").toString()
                    )
            );
        }
        return roleList;
    }

    @Override
    public Optional<Booking> getBooking(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        final var document = this.bookingsCollection.find(query).first();
        if (document == null) {
            return Optional.empty();
        }
        final var clientDocument = (Document) document.get("client");
        final String clientId = clientDocument.get("_id").toString();
        return Optional.of(
            new Booking(
                    document.get("_id").toString(),
                    new Status(document.get("status").toString()),
                    this.getUser(clientId).get(),
                    new Date(
                        document.getDate("startsAt").getTime()
                    ),
                    new Date(
                        document.getDate("endsAt").getTime()
                    ),
                    this.getServicesByBooking(document.get("_id").toString())
            )
        );
    }

    @Override
    public List<Booking> getAllBookings() {
        final var allDocuments = this.bookingsCollection.find();
        List<Booking> bookingList = new ArrayList<>();
        for (Document document : allDocuments) {
            bookingList.add(
                    this.getBooking(
                            document.get("_id").toString()
                    ).get()
            );
        }
        return bookingList;
    }

    @Override
    public void saveBooking(Booking booking) {
        List<Document> serviceListDocuments = new ArrayList<>();
        for (Service service : booking.getServices()) {
            var tempDocument = new Document()
                    .append("name", service.getName())
                    .append("description", service.getDescription());
            serviceListDocuments.add(tempDocument);
        }
        Document document = new Document()
                .append("status", booking.getStatus().getName())
                .append("client", new Document()
                        .append("_id", new ObjectId(booking.getClient().getId())))
                .append("startsAt", booking.getStartsAt())
                .append("endsAt", booking.getEndsAt())
                .append("service", serviceListDocuments);

        this.bookingsCollection.insertOne(document);
    }

    @Override
    public void deleteBooking(Booking booking) {
        Bson filter = eq("_id", new ObjectId(booking.getId()));
        this.bookingsCollection.deleteOne(filter);
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        List<Booking> bookingList = new ArrayList<>();
        User client = this.getUserByEmail(email).get();
        String clientId = client.getId();


        final var bookingDocuments = this.bookingsCollection.find(
            eq("client._id", new ObjectId(clientId))
        );

        for (Document document : bookingDocuments) {
            String bookingId = document.get("_id").toString();
            bookingList.add(this.getBooking(bookingId).get());
        }

        return bookingList;
    }

    @Override
    public void updateStatus(Booking booking, Status status) {
        Bson filter = eq("_id", new ObjectId(booking.getId()));
        Bson updateOperation = Updates.set("status", status.getName());
        this.bookingsCollection.updateOne(filter, updateOperation);
    }

    @Override
    public Optional<User> getUser(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        final var document = usersCollection.find(query).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
                new User(
                        document.get("_id").toString(),
                        document.get("fname").toString(),
                        document.get("lname").toString(),
                        document.get("email").toString(),
                        document.get("password").toString(),
                        new Role(document.get("role").toString())
                )
        );
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        final var document = this.usersCollection.find(
                eq("email", email)
        ).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
            new User (
                    document.get("_id").toString(),
                    document.get("fname").toString(),
                    document.get("lname").toString(),
                    document.get("email").toString(),
                    document.get("password").toString(),
                    new Role(document.get("role").toString())
            )
        );
    }

    @Override
    public List<User> getAllUsers() {
        final var documents = this.usersCollection.find();
        List<User> userList = new ArrayList<>();
        for (Document document : documents) {
            userList.add(
                    new User(
                            document.get("_id").toString(),
                            document.get("fname").toString(),
                            document.get("lname").toString(),
                            document.get("email").toString(),
                            document.get("password").toString(),
                            new Role(document.get("role").toString())
                    )
            );
        }
        return userList;
    }

    @Override
    public void saveUser(User user) {
        this.usersCollection.insertOne(
                new Document()
                .append("fname", user.getFname())
                .append("lname", user.getLname())
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("role", user.getRole().getName())
        );
    }

    @Override
    public void deleteUser(User user) {
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = getUserByEmail(name).get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString().toUpperCase())
                .build();
    }

    @Override
    public Optional<Status> getStatus(String id) {
        final var document = this.statusesCollection.find(eq("_id", new ObjectId(id))).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
            new Status(
                    document.get("_id").toString(),
                    document.get("name").toString()
            )
        );
    }

    @Override
    public void saveStatus(Status status) {
        Document document = new Document().append("name", status.getName());
        this.statusesCollection.insertOne(document);
    }

    @Override
    public Optional<Status> getStatusByName(String name) {
        final var statusDocument = this.statusesCollection.find(eq("name", name)).first();
        if (statusDocument == null) {
            return Optional.empty();
        }
        return Optional.of(
            new Status(
                    statusDocument.get("_id") .toString(),
                    statusDocument.get("name").toString()
            )
        );
    }

    @Override
    public List<Status> getAllStatuses() {
        final var documents = this.statusesCollection.find();
        List<Status> statusList = new ArrayList<>();
        for (Document document : documents) {
            statusList.add(
                    new Status(
                            document.get("_id").toString(),
                            document.get("name").toString()
                    )
            );
        }
        return statusList;
    }

    @Override
    public void deleteStatus(Status status) {
    }

    @Override
    public Optional<Service> getServiceByName(String name) {
        final var document = this.serviceCollection.find(eq("name", name)).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(
                new Service(
                        document.get("_id").toString(),
                        document.get("name").toString(),
                        document.get("description").toString()
                )
        );
    }
}
