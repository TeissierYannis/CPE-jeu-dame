package model;


import java.util.LinkedList;
import java.util.List;

import controller.OutputModelData;
import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 * <p>
 * Cette classe gére les aspects métiers du jeu de dame
 * indépendamment de toute vue
 * <p>
 * Elle délégue é son objet ModelImplementor
 * le stockage des PieceModel dans une collection
 * <p>
 * Les piéces sont capables de se déplacer d'une case en diagonale
 * si la case de destination est vide
 * <p>
 * Ne sont pas gérés les prises, les rafles, les dames,
 * <p>
 * N'est pas géré le fait que lorsqu'une prise est possible
 * une autre piéce ne doit pas étre jouée
 */
public class Model implements BoardGame<Coord> {

    private PieceSquareColor currentGamerColor;    // couleur du joueur courant
    private static PieceSquareColor currentPlayer;
    private ModelImplementor implementor;        // Cet objet sait communiquer avec les PieceModel

    public Model() {
        super();
        this.implementor = new ModelImplementor();
        this.currentGamerColor = ModelConfig.BEGIN_COLOR;

        currentPlayer = this.currentGamerColor;

        System.out.println(this);
    }

    @Override
    public String toString() {
        return implementor.toString();
    }

    /**
     * Actions potentielles sur le model : move, capture, promotion pion, rafles
     */
    @Override
    public OutputModelData<Coord> moveCapturePromote(Coord toMovePieceCoord, Coord targetSquareCoord) {

        OutputModelData<Coord> outputModelData = null;

        boolean isMoveDone = false;
        Coord toCapturePieceCoord = null;
        Coord toPromotePieceCoord = null;
        PieceSquareColor toPromotePieceColor = null;

        System.out.println("=================================");
        // Si la piéce est déplaéable (couleur du joueur courant et case arrivée disponible)
        if (this.isPieceMovable(toMovePieceCoord, targetSquareCoord)) {
            System.out.println("1. Piece Movable");

            // S'il n'existe pas plusieurs piéces sur le chemin
            if (this.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord)) {
                System.out.println("2. Max one piece on itinerary");


                //Recherche coord de l'éventuelle piéce é prendre
                toCapturePieceCoord = this.getToCapturePieceCoord(toMovePieceCoord, targetSquareCoord);

                // si le déplacement est légal (en diagonale selon algo pion ou dame)
                boolean isPieceToCapture = toCapturePieceCoord != null;
                if (this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture)) {

                    System.out.println("3. Move piece possible");

                    // déplacement effectif de la piéce
                    this.movePiece(toMovePieceCoord, targetSquareCoord);
                    isMoveDone = true;

                    // suppression effective de la piéce prise
                    this.remove(toCapturePieceCoord);

                    // promotion éventuelle de la piéce aprés déplacement
                    PieceModel piece = this.implementor.findPiece(targetSquareCoord);
                    if (piece instanceof PawnModel pm) {
                        if (pm.isPromotable()) {
                            // TODO atelier 3
                            this.implementor.promote(pm);
                            toPromotePieceColor = pm.getPieceColor();
                            toPromotePieceCoord = new Coord(pm.getColonne(), pm.getLigne());
                        }
                    }

                    // S'il n'y a pas eu de prise
                    // ou si une rafle n'est pas possible alors changement de joueur
                    if (true) {    // TODO : Test é changer atelier 4
                        this.switchGamer();
                        System.out.println("4. Changement de joueur");
                        currentPlayer = this.currentGamerColor;
                    }

                }
            }
        }
        System.out.println(this);

        // Constitution objet de données avec toutes les infos nécessaires é la view
        outputModelData = new OutputModelData<Coord>(
                isMoveDone,
                toCapturePieceCoord,
                toPromotePieceCoord,
                toPromotePieceColor);

        return outputModelData;
    }

    /**
     * @param toMovePieceCoord
     * @param targetSquareCoord
     * @return true si la PieceModel é déplacer est de la couleur du joueur courant
     * et que les coordonnées d'arrivées soient dans les limites du tableau
     * et qu'il n'y ait pas de piéce sur la case d'arrivée
     */
    boolean isPieceMovable(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" aprés test unitaires
        boolean bool = false;

        // TODO : é compléter atelier 4 pour gérer les rafles

        bool = this.implementor.isPiecehere(toMovePieceCoord)
                && this.implementor.getPieceColor(toMovePieceCoord) == this.currentGamerColor
                && Coord.coordonnees_valides(targetSquareCoord)
                && !this.implementor.isPiecehere(targetSquareCoord);

        return bool;
    }

    private List<Coord> getPieceOnItinerary(Coord fromCoord, Coord toCoord) {

        List<Coord> onItineraryPieces = new LinkedList<Coord>();

        List<Coord> coords = this.implementor.getCoordsOnItinerary(fromCoord, toCoord);
        PieceModel piece = null;
        for (Coord coord : coords) {
            piece = this.implementor.findPiece(coord);
            if (piece != null) {
                onItineraryPieces.add(coord);
            }
            piece = null;
        }
        return onItineraryPieces;
    }

    /**
     * @param toMovePieceCoord
     * @param targetSquareCoord
     * @return true s'il n'existe qu'1 seule piéce é prendre d'une autre couleur sur la trajectoire
     * ou pas de piéce é prendre
     */
    private boolean isThereMaxOnePieceOnItinerary(Coord toMovePieceCoord, Coord targetSquareCoord) {
        return this.getPieceOnItinerary(toMovePieceCoord, targetSquareCoord).size() <= 1;
    }

    /**
     * @param toMovePieceCoord
     * @param targetSquareCoord
     * @return les coord de la piéce é prendre, null sinon
     */
    private Coord getToCapturePieceCoord(Coord toMovePieceCoord, Coord targetSquareCoord) {
        Coord toCapturePieceCoord = null;
        PieceModel piece = this.implementor.findPiece(toMovePieceCoord);

        if (this.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord)) {
            List<Coord> coords = this.getPieceOnItinerary(toMovePieceCoord, targetSquareCoord);
            if (coords.size() > 0) {
                toCapturePieceCoord = coords.get(0);
                System.out.println("Piece between source and dest : " + toCapturePieceCoord);
            }
        }

        return toCapturePieceCoord;
    }

    /**
     * @param toMovePieceCoord
     * @param targetSquareCoord
     * @param isPieceToCapture
     * @return true si le déplacement est légal
     * (s'effectue en diagonale, avec ou sans prise)
     * La PieceModel qui se trouve aux coordonnées passées en paramétre
     * est capable de répondre écette question (par l'intermédiare du ModelImplementor)
     */
    boolean isMovePiecePossible(Coord toMovePieceCoord, Coord targetSquareCoord, boolean isPieceToCapture) { // TODO : remettre en "private" aprés test unitaires
        return this.implementor.isMovePieceOk(toMovePieceCoord, targetSquareCoord, isPieceToCapture);
    }

    /**
     * @param toMovePieceCoord
     * @param targetSquareCoord Déplacement effectif de la PieceModel
     */
    void movePiece(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" aprés test unitaires
        this.implementor.movePiece(toMovePieceCoord, targetSquareCoord);
    }

    /**
     * @param toCapturePieceCoord Suppression effective de la piéce capturée
     */
    private void remove(Coord toCapturePieceCoord) {
        this.implementor.removePiece(toCapturePieceCoord);
    }

    void switchGamer() { // TODO : remettre en "private" aprés test unitaires
        this.currentGamerColor = (PieceSquareColor.WHITE).equals(this.currentGamerColor) ?
                PieceSquareColor.BLACK : PieceSquareColor.WHITE;

    }

    public static PieceSquareColor getCurrentPlayer() {
        return currentPlayer;
    }
}