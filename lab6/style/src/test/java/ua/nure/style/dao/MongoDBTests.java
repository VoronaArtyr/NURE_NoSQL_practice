package ua.nure.style.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ua.nure.style.entity.Service;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MongoDBTests {

    private MongoDatabase db;
    private Service service;

    @BeforeEach
    void setUp() {
        db = new MongoClient().getDatabase("sound_studio");

        service = new Service(
                "service_name 1",
                "service_description 1",
                "23451.1"
        );
    }

    @Test
    void saveService() {
        final var collection = db.getCollection("service");
        Document document = new Document()
                .append("name", this.service.getName())
                .append("description", this.service.getDescription());
        collection.insertOne(document);
    }

    @Test
    void getService() {
        final var collection = db.getCollection("service");
        final var documents = collection.find(eq("name", service.getName()));
        assertThat(documents).hasAtLeastOneElementOfType(Document.class);
    }

    @Test
    @Disabled
    void saveLotsOfService() {
        final var collection = db.getCollection("service");

        for (int i = 0; i < 10000; i++) {
            Document document = new Document()
                    .append("name", this.service.getName())
                    .append("description", this.service.getDescription());
            collection.insertOne(document);
        }
    }
}