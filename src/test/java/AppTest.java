import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AppTest {


    @TempDir
    Path tempDir;

    @Test
    void getAllFactsReturnsEveryRowInIdOrder() throws Exception {
    
        Path db = tempDir.resolve("test.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (1, 'Cats purr')");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (2, 'Cats, oddly, cannot taste sweetness')");
        }

        List<App.CatFact> facts = new App.FactsHandler().getAllFacts(url);


        assertEquals(2, facts.size());
        assertEquals(1, facts.get(0).id());
        assertEquals("Cats, oddly, cannot taste sweetness", facts.get(1).fact());
    }
    

    @Test
    void findFactByIdReturnsFactWhenItExists() throws Exception {
        Path db = tempDir.resolve("test.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (1, 'Cats purr')");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (2, 'Cats, oddly, cannot taste sweetness')");
        }

        Optional<App.CatFact> found = new App.FactsHandler().findFactById(url, 2);
        assertTrue(found.isPresent());
        assertEquals(2, found.get().id());
        assertEquals("Cats, oddly, cannot taste sweetness", found.get().fact());
    }


    @Test 
    void findFactByIdReturnsEmptyWhenFactDoesNotExist() throws Exception {
        Path db = tempDir.resolve("test.db");
        String url = "jdbc:sqlite:" + db;

        try (Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE facts (id INTEGER PRIMARY KEY AUTOINCREMENT, fact TEXT NOT NULL)");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (1, 'Cats purr')");
            s.executeUpdate("INSERT INTO facts (id, fact) VALUES (2, 'Cats, oddly, cannot taste sweetness')");
        }

        Optional<App.CatFact> found = new App.FactsHandler().findFactById(url, 99);
        assertFalse(found.isPresent());
    }


    @Test
    void CatFactKeepsItsData() {
        // initialize what you need
        App.CatFact fact = new App.CatFact(7, "fact7");

        
        // Assert for what needs to be tested
        assertEquals(7, fact.id());
        assertEquals("fact7", fact.fact());
    }


    @Test
    void CatFactsWithSameValuesAreEqual() {
        App.CatFact fact1 = new App.CatFact(7, "fact7");
        App.CatFact fact2 = new App.CatFact(7, "fact7");


        assertEquals(fact1, fact2);
    }


    @Test
    void CatFactsWithDiffTextAreNotEqual() {
        App.CatFact fact1 = new App.CatFact(7, "fact7");
        App.CatFact fact2 = new App.CatFact(7, "fact 8");


        assertNotEquals(fact1, fact2);
    }


    @Test
    void ListFindsFactByValues() {
        List<App.CatFact> facts = new ArrayList<App.CatFact>();
        facts.add(new App.CatFact(1, "some text"));


        assertTrue(facts.contains(new App.CatFact(1, "some text")));
    }


    @Test
    void NewFactReturnsItsText() {
        App.NewFact fact = new App.NewFact("new fact");


        assertEquals("new fact", fact.fact());
    }


    @Test
    void ErrorBodyReturnsItsMessage() {
        App.ErrorBody error = new App.ErrorBody("error");


        assertEquals("error", error.error());
    }


    @Test
    void CatFactCanHoldEmptyString() {
        App.CatFact fact = new App.CatFact(1, "");


        assertEquals("", fact.fact());
    }


    @Test
    void serializingCatFact() {
        App.CatFact fact = new App.CatFact(1, "some text");
        String json = App.gson.toJson(fact);


        assertTrue(json.contains("1"));
        assertTrue(json.contains("some text"));
    }


    @Test
    void deserializingJsonIntoNewFact() {
        String json = "{\"fact\":\"new fact\"}";
        App.NewFact fact = App.gson.fromJson(json, App.NewFact.class);


        assertEquals("new fact", fact.fact());

    }


    @Test
    void FactCanHaveApostrophe() {
        App.CatFact fact = new App.CatFact(1, "Cats don't dance");
        String json = App.gson.toJson(fact);


        assertTrue(json.contains("'"));
    }


    @Test 
    void FactCanHaveCommas() {
        App.CatFact fact = new App.CatFact(9, "Cats, unlike dogs, cannot taste sweetness");
        String json = App.gson.toJson(fact);
        App.CatFact newFact = App.gson.fromJson(json, App.CatFact.class);


        assertEquals(fact, newFact);
    }


    @Test 
    void loadFromCsvReadsEveryRowInTheFile() throws IOException {
        App.loadFromCsv();


        assertEquals(10, App.facts.size());
    }


    @Test 
    void factWithCommasIsStoredWhole() throws IOException {
        App.loadFromCsv();

        assertEquals("Cats, like people, tend to favor one paw over the other", 
            App.facts.get(8).fact());
    }


    @Test 
    void IdsComeFromTheFile() throws IOException {
        App.loadFromCsv();

        assertEquals(1, App.facts.get(0).id());
    }


    @Test 
    void nextIDIsHighestIdPlusOne() throws IOException {
        App.loadFromCsv();

        assertEquals(11, App.nextId);
    }


    // Test failed; final size is 20. I would change loadFromCsv to clear the list before 
    // loading new facts.
    @Test 
    void callingLoadFromCsvTwiceDoesNotDoubleFacts() throws IOException {
        App.loadFromCsv();
        App.loadFromCsv();

        assertEquals(10, App.facts.size());
    }


}
