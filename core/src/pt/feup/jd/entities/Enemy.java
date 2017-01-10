package pt.feup.jd.entities;

import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Enemy extends Entity{
	
	boolean turnOnBump;
	boolean turnOnEdge;
		boolean turning;
	
	float walkSpeed;
	float walkDirection;
	
	float contactDamage;
	float contactDamage_timer, contactDamage_delay;
	
	public float range;
	float inRange_timer, inRange_delay;

	
	public Enemy(Level level) {
		super(level);
		
		walkSpeed = 0;
		walkDirection = 1;
		
		turnOnBump = false;
		turnOnEdge = false;
		
		contactDamage = 0;
		contactDamage_timer = 0;
		contactDamage_delay = 1;
		
		range = 3.5f*JDGame.TILE_SIZE;
		inRange_timer = 0;
		inRange_delay = 2.5f;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);	
		
		if (level.player != null && Util.pointDistance(level.player, this) < range)
			inRange_timer = inRange_delay;
		
		if (range < 0 || inRange_timer > 0) {
			vx = walkSpeed * direction;
			
			if (onGround()) turning = false;
			else if (turnOnEdge && !turning){
				turning = true;
				direction = -direction;
			}
		} else
			vx = 0;
		
		inRange_timer = Util.stepTo(inRange_timer, 0, delta);
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
	public void levelCollision(float nx, float ny, Tile t) {
		super.levelCollision(nx, ny, t);
		
		if (nx != 0 && turnOnBump) { 
			direction = -direction; 
		}
	}
	
}
