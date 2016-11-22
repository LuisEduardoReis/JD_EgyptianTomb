package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;

public class Entity {
	public Level level;
	
	public float x,y;
	public float dx, dy;
	public float fx,fy;
	public int width, height;
	
	
	public Entity(Level level) {
		this.level = level;
		
		this.width = 32;
		this.height = 32;
		
		this.fx = 0.85f;
		this.fy = 1.00f;
	}	
	
	public void update(float delta) {
		dy-=10*delta;
		dx*=fx;
		dy*=fy;
		
		dx = Math.min(JDGame.TILE_SIZE/2, Math.max(dx, -JDGame.TILE_SIZE/2));
		dy = Math.min(JDGame.TILE_SIZE/2, Math.max(dy, -JDGame.TILE_SIZE/2));
		
		x += dx;
		y += dy;
	}
	
	public void render(SpriteBatch batch) {
		
	}
	
	public boolean onGround() {
		int ts = JDGame.TILE_SIZE;
		int cx = (int) (x / ts);
		int cy = (int) (y / ts);
		float yr = (y / ts) - cy;
		float lowerY = ((float)(ts/2 - height/2))/ts;
		return level.getTile(cx,cy-1).solid && yr <= lowerY;
	}

	public void levelCollision() {
		int ts = JDGame.TILE_SIZE;
		int cx = (int) (x / ts);
		int cy = (int) (y / ts);
		float xr = (x / ts) - cx;
		float yr = (y / ts) - cy;
	
		float upperX = ((float)(ts/2 + width/2))/ts;
		float lowerX = ((float)(ts/2 - width/2))/ts;
		float upperY = ((float)(ts/2 + height/2))/ts;
		float lowerY = ((float)(ts/2 - height/2))/ts;
		
		if (level.getTile(cx+1,cy).solid && xr >= upperX) {
			xr = upperX;
			dx = 0;
		}
		if (level.getTile(cx-1,cy).solid && xr <= lowerX) {
			xr = lowerX;
			dx = 0;
		}
		if (level.getTile(cx,cy+1).solid && yr >= upperY) {
			yr = upperY;
			dy = 0;
		}
		if (level.getTile(cx,cy-1).solid && yr <= lowerY) {
			yr = lowerY;
			dy = 0;
		}		
		
		x = (cx+xr) * ts;
		y = (cy+yr) * ts;
	}
}
