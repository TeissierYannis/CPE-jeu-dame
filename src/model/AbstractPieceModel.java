package model;

import nutsAndBolts.PieceSquareColor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPieceModel implements PieceModel {

    protected Coord coord;
    protected PieceSquareColor pieceColor;

    public char getColonne() {
        return this.coord.getColonne();
    }

    public int getLigne() {
        return coord.getLigne();
    }

    public boolean hasThisCoord(Coord coord) {
        return this.coord.equals(coord);
    }

    public void move(Coord coord) {
        this.coord = coord;
    }

    public PieceSquareColor getPieceColor() {
        return pieceColor;
    }

    public abstract boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture);

    public List<Coord> getCoordsOnItinerary(Coord targetCoord) {
        List<Coord> coordsOnItinerary = new LinkedList<Coord>();

        int colDistance = targetCoord.getColonne() - this.getColonne();
        int rowDistance = targetCoord.getLigne() - this.getLigne();
        // lettre droite positif gauche negatif
        // chiffre pos vers 10 autre 0

        char col = this.getColonne();
        int row = this.getLigne();

        for (int i = 0; i < Math.abs(colDistance); i++) {
            col += (colDistance > 0) ? 1 : -1;
            row += (rowDistance > 0) ? 1 : -1;

            Coord middleCoord = new Coord(col, row);
            coordsOnItinerary.add(middleCoord);
        }
        System.out.println("Coords : " + coordsOnItinerary);

        return coordsOnItinerary;
    };

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return " [" + pieceColor.toString().charAt(0) + coord + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPieceModel that)) return false;
        return coord.equals(that.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord);
    }
}
