package tests;

import model.Coord;
import model.PawnModel;
import nutsAndBolts.PieceSquareColor;

public class PawnTest {

    public PawnModel pieceModel1;
    public PawnModel pieceModel2;
    public PawnModel pieceModel3;

    public PawnTest() {}

    /*
    @BeforeClass
    public void setUp() {
        this.pieceModel1 = new PawnModel(new Coord('a', 7), PieceSquareColor.BLACK);
        this.pieceModel2 = new PawnModel(new Coord('b', 4), PieceSquareColor.WHITE);
        this.pieceModel3 = new PawnModel(new Coord('e', 7), PieceSquareColor.BLACK);
    }

    @Test
    public void testPawns() {
        assertEquals(new PawnModel(new Coord('a', 7), PieceSquareColor.BLACK).toString(), this.pieceModel1.toString());
        assertEquals(new PawnModel(new Coord('b', 4), PieceSquareColor.WHITE).toString(), this.pieceModel2.toString());
        assertEquals(new PawnModel(new Coord('e', 7), PieceSquareColor.BLACK).toString(), this.pieceModel3.toString());

        assertTrue(this.pieceModel2.isMoveOk(new Coord('c', 5), false));
        assertTrue(this.pieceModel3.isMoveOk(new Coord('d', 6), false));

        pieceModel2.move(new Coord('c', 5));
        pieceModel3.move(new Coord('d', 6));

        assertTrue(this.pieceModel2.isMoveOk(new Coord('e', 7), true));
        assertFalse(this.pieceModel2.isMoveOk(new Coord('d', 6), true));
        assertFalse(this.pieceModel2.isMoveOk(new Coord('b', 6), true));
        assertFalse(this.pieceModel2.isMoveOk(new Coord('e', 7), false));
        assertFalse(this.pieceModel3.isMoveOk(new Coord('e', 7), false));

        assertTrue(this.pieceModel2.hasThisCoord(new Coord('c', 5)));
    }*/
}