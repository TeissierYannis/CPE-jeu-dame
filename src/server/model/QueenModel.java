package server.model;


import server.nutsAndBolts.PieceSquareColor;

/**
 * @author francoiseperrin
 * <p>
 * le mode de déplacement et de prise de la reine est différent de celui du pion
 */
public class QueenModel extends AbstractPieceModel  {

    public QueenModel(Coord coord, PieceSquareColor pieceColor) {
        super();
        this.coord = coord;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture) {
        // TODO 3
        boolean ret = false;

        int colDistance = targetCoord.getColonne() - this.getColonne();
        int ligDistance = targetCoord.getLigne() - this.getLigne();

        // Cas d'un d�placement en diagonale
        if (Math.abs(colDistance) == Math.abs(ligDistance)) {
            // sans prise
            ret = true;
        }
        return ret;
    }

}

