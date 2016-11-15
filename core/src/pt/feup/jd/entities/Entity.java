package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.levels.Level;

public class Entity {
	public Level level;
	public Entity(Level level) {
		this.level = level;
	}

	public float x,y;
	
	public void update(float delta) {
		
	}
	
	public void render(SpriteBatch batch) {
		
	}
}
