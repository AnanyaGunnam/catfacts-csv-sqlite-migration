import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class AppTest {
    
    
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



}
