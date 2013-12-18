package actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sound.AudioClip;
import sound.ResourceLoader;
import core.Actor;
import core.Scene;

@SuppressWarnings("serial")
public class Player extends Actor {
	
	//TODO : CHANGE VALUES
	private double maxHeight = 0;
	//-- TODO
	
	private double initY = 0;
	
	//w = 0.0018
	private double weight = 0.0022;
	private double force = 0;
	
	private double rotate = 0;
	private double rotationspeed = 0.18;
	
	private String pathDieSound = "res/sound.wav";
	private String pathJumpSound = "res/Jump.wav";
	private AudioClip asJump;
	private AudioClip asDie;
	private ArrayList<BufferedImage> runnimation = new ArrayList<>();
	
	public boolean dead = false;
	
	// force to add to the player on button press (jump)
	private double jumpforce = -0.017;
	
	public Player(Scene parent) {
		super(parent);
		x = 0.1;
		
		asJump = (AudioClip) ResourceLoader.load(pathJumpSound);
		asJump.open();
		asDie = (AudioClip) ResourceLoader.load(pathDieSound);
		asDie.open();
		w = 0.035;
		h = 0.035;
		
		runnimation.add((BufferedImage) ResourceLoader.load("res/player/p_01.png"));
		runnimation.add((BufferedImage) ResourceLoader.load("res/player/p_02.png"));
		runnimation.add((BufferedImage) ResourceLoader.load("res/player/p_03.png"));
		runnimation.add((BufferedImage) ResourceLoader.load("res/player/p_04.png"));
		
	}
	
	public void kill() {
		dead = true;
		System.out.println("Player died");
		asDie.start();
	}
	
	public boolean jump() {
		return addForce(jumpforce, 0.21);
	}
	
	public boolean isGrounded() {
		return y >= parent.getGround();
		// TODO: add check if player is on an obstacle
	}
	
	/**
	 * Checks if player is beneath or over an obstacle
	 * @return
	 * the touched obstacle
	 */
	public Actor getTouchedObstacle() {
		//0.00025
		Actor left = new Actor(parent, x, y + force);// + 0.00025);
		for (Actor child:parent.getActors()){
			if(left.intersects(child)){
				child.surf(this);
				return child;
			}
			else {
			}
		}
		return null;
	}

	public boolean addForce(double force, double maxHeight) {
		//SURFJUMPFIX
		this.w += 0.05;
		this.x -= 0.01;
		if(isGrounded() || (getTouchedObstacle()!=null && getTouchedObstacle().isGround)) {
			asJump.start();
			
			//SURFJUMPFIX
			this.w -= 0.05;
			this.x += 0.01;
			
			this.force = force;
			this.maxHeight = y - maxHeight;
			System.currentTimeMillis();
			this.initY = y;
			return true;
		}
		
		//SURFJUMPFIX
		this.w -= 0.05;
		this.x += 0.01;
		
		return false;
	}
	
	@Override
	public void update() {
	
		x += parent.getScrollSpeed();
		/*double ax=trans.getTranslateX();
		double ay=trans.getTranslateY();
		trans.translate(x, y);
		trans.rotate(0.02, x, y);
		trans.translate(ax, ay);*/
		rotate += rotationspeed;
		
	}
	
	@Override
	public void fixedUpdate() {
		//--JUMP--
		
		if(parent.getSpaceState() && y > maxHeight ){
			double meh = (y - initY) <= 0 ? (y - initY) : -0.5; // surfjump fix
			force += weight * (meh/(maxHeight - initY));
			//System.out.println(weight * ((y - initY)/(maxHeight - initY)));
		}
		else{
			force += weight;
		}
		
		//-- SURF --
		try {
			Actor touch = getTouchedObstacle();
			if(touch!=null && force > 0 && touch.isGround
					){
				if(!this.intersects(touch))
					y = getTouchedObstacle().y-h-0.0001; //0.0002
				else {
					touch.collide(this);
				}
				force = 0;
			}
			else if(touch!=null&&touch.intersects(this)) {
				touch.collide(this);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		//-- /SURF --
		if(!dead)y+=force;
		
		//-- GROUND
		if(isGrounded()) {
			y = parent.getGround();
			force = 0;
		}
		//-- /GROUND --
		//--/JUMP--
	}

	@Override
	public void paintComponent(Graphics g ) {

		
		//RENDER
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setColor(Color.LIGHT_GRAY);
		//System.out.println(parent.getCoordX(x)+" "+ parent.getCoordY(y)+" "+ parent.getWidth(w)+" "+ parent.getHeight(h)+" - "+parent.getPosition());
		g2D.drawRect(parent.getCoordX(x)+1, parent.getCoordY(y)+1, parent.getWidth(w)-2, parent.getHeight(h)-2);
		
		g2D.drawImage(runnimation.get((int) (rotate%4)), parent.getCoordX(x-0.009), parent.getCoordY(y-0.005), parent.getWidth(w+0.02), parent.getHeight(h+0.01), null);

	}
	
}
