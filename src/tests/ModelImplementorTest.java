package tests;

import model.ModelImplementor;
import model.PawnModel;
import nutsAndBolts.PieceSquareColor;

import static nutsAndBolts.PieceSquareColor.WHITE;

public class ModelImplementorTest {

    public ModelImplementor mi;

    public ModelImplementorTest() {}

    /*@Test
    public void testModelImplementor() {
        assertEquals(new PawnModel(new Coord('b', 4), WHITE).toString(), this.mi.findPiece(new Coord('b', 4)).toString());
        assertNull(this.mi.findPiece(new Coord('b', 6)));

        assertEquals(WHITE, this.mi.findPieceColor(new Coord('b', 4)));
        assertNull(this.mi.findPieceColor(new Coord('b', 6)));

        assertTrue(this.mi.isPiecehere(new Coord('b', 4)));
        assertFalse(this.mi.isPiecehere(new Coord('b', 6)));

        assertTrue(this.mi.isMovePieceOk(new Coord('b', 4), new Coord('c', 5), false));
        assertTrue(this.mi.movePiece(new Coord('b', 4), new Coord('c', 5)));
    }*/
}