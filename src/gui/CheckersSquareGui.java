package gui;

/**
 * @author francoise.perrin
 * Cette interface permet de vérifier qu'un Node
 * est fonctionnellement une case du jeu
 *  
 */
public interface CheckersSquareGui {
	
	/**
	 *Retourne l'indice du carré sur la grille (Né de 0 é 99)
	 */
	int getSquareCoord();
}
