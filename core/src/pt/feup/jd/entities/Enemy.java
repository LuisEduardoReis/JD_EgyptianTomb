package pt.feup.jd.entities;

import pt.feup.jd.levels.Level;

public class Enemy extends Entity{
	
	boolean turnOnBump;
	boolean turnOnEdge;
		boolean turning;
	
	float walkSpeed;
	float walkDirection;
	
	public Enemy(Level level) {
		super(level);
		
		walkSpeed = 0;
		walkDirection = 1;
		
		turnOnBump = false;
		turnOnEdge = false;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);	
		
		vx = walkSpeed * walkDirection;
		
		if (onGround()) turning = false;
		else if (turnOnEdge && !turning){
			turning = true;
			walkDirection = -walkDirection;
		}
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Bullet) {
			health -= ((Bullet) o).damage;
			o.remove = true;
		}
	}
	
	@Override
	public void levelCollision(float nx, float ny) {
		super.levelCollision(nx, ny);
		
		if (nx != 0 && turnOnBump) { 
			walkDirection = -walkDirection; 
		}
	}
	
}
