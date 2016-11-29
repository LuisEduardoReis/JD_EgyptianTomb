package pt.feup.jd.entities;

import pt.feup.jd.levels.Level;

public class Enemy extends Entity{

	public Enemy(Level level) {
		super(level);
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Bullet) {
			health -= ((Bullet) o).damage;
			o.remove = true;
		}
	}
	
}
