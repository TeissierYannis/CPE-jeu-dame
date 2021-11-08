package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nutsAndBolts.PieceSquareColor;


/**
 * @author francoise.perrin
 * 
 * Cette classe permet de donner une image aux piéces
 *
 */

public class PieceGui extends ImageView implements CheckersPieceGui{
	

	private Image image;
	private PieceSquareColor pieceColor;

	public PieceGui(Image image, PieceSquareColor color) {
		this.image = image;
		this.pieceColor = color;

		this.setImage(image);
	}

	@Override
	public void promote(Image image) {
		// ToDo Atelier 2, utile pour Atelier 3
		this.image = image;
		this.setImage(image);
	}

	@Override
	public boolean hasSameColorAsGamer(PieceSquareColor gamerColor) {

		// ToDo Atelier 2, utile pour Atelier 4
		return this.pieceColor.equals(gamerColor);
	}
}