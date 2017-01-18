package pt.feup.jd.entities;

import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Enemy extends Entity{
	
	boolean followPlayer;
	
	float walkSpeed;
	float walkDirection;
	
	float contactDamage;
	float contactDamage_timer, contactDamage_delay;
	
	public float range;
	float inRange_timer, inRange_delay;

	float distanceToPlayer;
	
	int coinLoot;
	
	public Enemy(Level level) {
		super(level);
		
		walkSpeed = 0;
		walkDirection = 1;
		

		followPlayer = false;
		
		contactDamage = 0;
		contactDamage_timer = 0;
		contactDamage_delay = 1;
		
		range = 3.5f*JDGame.TILE_SIZE;
		inRange_timer = 0;
		inRange_delay = 2.5f;
		
		coinLoot = 0;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);	
		
		distanceToPlayer = Util.pointDistance(level.player, this);
		
		if (level.player != null &&  distanceToPlayer < range)	inRange_timer = inRange_delay;		
		inRange_timer = Util.stepTo(inRange_timer, 0, delta);
		
		
		contactDamage_timer = Util.stepTo(contactDamage_timer, 0, delta);
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Bullet) {
			damage(((Bullet)o).damage);
			o.remove = true;
		}
		
		if (o instanceof Fireball) {
			damage(((Fireball)o).damage);
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
	public boolean checkSolid(Tile t) {
		return super.checkSolid(t) || t == Tile.LADDER;
	}
	
	
	@Override
	public void die() {
		super.die();

		for(int i = 0; i < coinLoot; i++) {
			Coin coin = (Coin) new Coin(level).moveTo(x,y);
			coin.vy = Util.randomRange(2*JDGame.TILE_SIZE, 4*JDGame.TILE_SIZE);
			coin.vx = Util.randomRange(-JDGame.TILE_SIZE, JDGame.TILE_SIZE);
		}
	}
}
