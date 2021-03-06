package model;

public interface Promotable {
	
	/**
	 * @return true si le pion noir est arrivé é la ligne 1
	 * ou si le pion blanc est arrivé é la ligne MAX
	 */
	public boolean isPromotable();
	
	/**
	 * Effectue la promotion du pion
	 */
	public void promote();
}
