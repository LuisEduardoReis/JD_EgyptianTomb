package pt.feup.jd.entities;

import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Enemy extends Entity{
	
	boolean turnOnBump;
	boolean turnOnEdge;
		boolean turning;
	
	float walkSpeed;
	float walkDirection;
	
	float contactDamage;
	float contactDamage_timer, contactDamage_delay;

	
	public Enemy(Level level) {
		super(level);
		
		walkSpeed = 0;
		walkDirection = 1;
		
		turnOnBump = false;
		turnOnEdge = false;
		
		contactDamage = 0;
		contactDamage_timer = 0;
		contactDamage_delay = 1;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);	
		
		vx = walkSpeed * direction;
		
		if (onGround()) turning = false;
		else if (turnOnEdge && !turning){
			turning = true;
			direction = -direction;
		}
		
		contactDamage_timer = Util.stepTo(contactDamage_timer, 0, delta);
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Bullet) {
			health -= ((Bullet) o).damage;
			o.remove = true;
		}
		else
		if (o instanceof Player) {
			if (contactDamage > 0 && contactDamage_timer == 0) {
				o.damage(contactDamage);
				contactDamage_timer = contactDamage_delay;
			}
		}
	}
	
	@Override
	public void levelCollision(float nx, float ny) {
		super.levelCollision(nx, ny);
		
		if (nx != 0 && turnOnBump) { 
			direction = -direction; 
		}
	}
	
}
