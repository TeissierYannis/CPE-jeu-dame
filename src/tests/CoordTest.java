package tests;

import junit.framework.Assert;
import junit.framework.TestCase;
import model.Coord;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoordTest extends TestCase {

    public Coord c1;
    public Coord c2;

    public CoordTest() {}

    @BeforeClass
    public void setUp() {
        this.c1 = new Coord('a', 7);
        this.c2 = new Coord('b', 3);
    }

    @Test
    public void testCoordinates() {

        // assertEquals(10, Coord.MAX);
        assertEquals(new Coord('a', 7), this.c1);
        assertEquals(new Coord('b', 3), this.c2);

        assertTrue(Coord.coordonnees_valides(this.c1));
        assertFalse(Coord.coordonnees_valides(new Coord('w', 9)));
        assertFalse(Coord.coordonnees_valides(new Coord('b', 11)));

        assertFalse(this.c1.equals(this.c2));
        assertTrue(this.c1.equals(new Coord('a', 7)));
        assertFalse(this.c1.equals(new String("Erreur")));

        assertEquals(-1, this.c1.compareTo(this.c2));
        assertEquals(0, this.c1.compareTo(new Coord('a', 7)));
    }
}