package background;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import sound.ResourceLoader;
import core.Actor;
import core.State;

/**
 * Holds a background and its function to alter the movement
 * @author Vinzenz Boening
 */
public class BackgroundActor extends Actor{

	public static enum Type{
		TREE ("res/bg/treehuge.png"),
		DIRT ("res/bg/dirt.png");
		
		private final String path;
		Type(String path){
			this.path = path;
		}
		public String getPath(){
			return path;
		}
	}
	  
	private static final long serialVersionUID = -7327728536198764908L;
	
	private double speed = 0.0008;
	private BufferedImage bimage;
	
	public BackgroundActor(State parent, double x, double y, Type type) {
		super(parent, x, y);
		try{
			bimage = (BufferedImage) ResourceLoader.load(type.getPath());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the position of this actor
	 */
	public void update() {
		x+=speed;
	}
	
	/**
	 * Set the speed of this actor
	 * 
	 * @param speed the vertical speed
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * Get the speed of this actor
	 */
	public double getSpeed(){
		return speed;
	}

	/**
	 * Paints the actor on the screen
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2D = (Graphics2D) g;		
		g2D.drawImage(bimage, parent.getCoordX(x), parent.getCoordY(y), parent.getWidth(w), parent.getHeight(h), null);
	}
	
}
