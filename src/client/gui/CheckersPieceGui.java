package client.gui;

import javafx.scene.image.Image;
import server.nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 * Cette interface permet de vérifier qu'un Node
 * est fonctionnellement une piéce du jeu
 * 
 * Lorsque le pion du server.model est promu en dame
 * le visuel change
 * 
 * La méthode hasSameColorAsGamer sera utilise en mode Client/server
 * pour empécher un joueur de jouer une piéce qui ne lui appartient pas
 */
public interface CheckersPieceGui {
	
	public void promote(Image image);

	public boolean hasSameColorAsGamer(PieceSquareColor gamerColor);
}
