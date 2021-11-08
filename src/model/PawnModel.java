package model;


import java.util.LinkedList;
import java.util.List;

import nutsAndBolts.PieceSquareColor;

import static nutsAndBolts.PieceSquareColor.BLACK;
import static nutsAndBolts.PieceSquareColor.WHITE;

public class PawnModel extends AbstractPieceModel implements Promotable {

    private int direction;

    public PawnModel(Coord coord, PieceSquareColor pieceColor) {
        super();
        this.coord = coord;
        this.pieceColor = pieceColor;
        this.direction = PieceSquareColor.BLACK.equals(this.getPieceColor()) ? -1 : 1;
    }

    @Override
    public boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture) {
        boolean ret = false;

        int colDistance = targetCoord.getColonne() - this.getColonne();
        int ligDistance = targetCoord.getLigne() - this.getLigne();
        int deltaLig = (int) Math.signum(ligDistance);

        // Cas d'un d�placement en diagonale
        if (Math.abs(colDistance) == Math.abs(ligDistance)) {

            // sans prise
            if (!isPieceToCapture) {
                if (deltaLig == this.direction && Math.abs(colDistance) == 1) {
                    ret = true;
                }
            }
            // avec prise
            else {
                if (Math.abs(colDistance) == 2) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public boolean isPromotable() {
        return this.pieceColor == BLACK && this.coord.getLigne() == 1 || this.pieceColor == WHITE && this.coord.getLigne() == 10;
    }
    // TODO FAIRE CA POUR TP 3
    @Override
    public void promote() {
        System.out.println("USELESS");
    }
}