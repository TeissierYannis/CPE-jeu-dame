package model;

import controller.OutputModelData;

import java.io.IOException;

/**
 * @author francoise.perrin
 *
 * Cette interface définit le comportement attendu des jeux de plateau
 * @param <T>
 * 
 * 
 */
public interface BoardGame<T>  {

	/**
	 * @param toMovePieceIndex
	 * @param targetSquareIndex
	 * @return 1 objet complexe
	 * 		- true si le déplacement a été effectué, false sinon
	 * 		- éventuellement les coordonnées de la piéce capturée, null sinon 
	 * 		- éventuellement les coordonnées et la couleur du pion promus en dame, null sinon
	 */
	public OutputModelData<T> moveCapturePromote(T toMovePieceIndex, T targetSquareIndex) throws IOException;
	
}