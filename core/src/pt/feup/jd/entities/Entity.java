package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Collision;
import pt.feup.jd.levels.Level;

public class Entity {
	public Level level;
	
	public float x,y;
	public float px,py;
	public int hx, hy;
	public float vx, vy;
	public float fx,fy;
	
	public boolean colideWithLevel;
	
	
	public Entity(Level level) {
		this.level = level;
		
		this.hx = 32;
		this.hy = 32;
		
		this.fx = 0.85f;
		this.fy = 0.85f;
		
		this.colideWithLevel=true;
	}
	
	public void update(float delta) {
		px = x;
		py = y;
		
		vy-=10*delta;
		vx*=fx;
		//vy*=fy;
		
		x += vx;
		y += vy;
	}
	
	public void render(SpriteBatch batch) {
		
	}
	
	public void moveTo(float x, float y) {
		this.x = this.px = x;
		this.y = this.py = y;
	}
	
	public boolean onGround() {
		int ts = JDGame.TILE_SIZE;
		int cx = (int) (x / ts);
		int cy = (int) (y / ts);
		float yr = (y / ts) - cy;
		float lowerY = ((float)(ts/2 - hy/2))/ts;
		return level.getTile(cx,cy-1).solid && yr <= lowerY;
	}

	public static Vector2 v1 = new Vector2(), v2 = new Vector2();
	public void levelCollision() {
		if (!colideWithLevel) return;
		
		int ts = JDGame.TILE_SIZE;
		
		while(true) {
		
			float dx = x - px, dy = y - py;
			int minXi = (int) (Math.min(x, px)-hx/2)/ts, maxXi = (int)(Math.max(x, px)+hx/2)/ts;
			int minYi = (int) (Math.min(y, py)-hy/2)/ts, maxYi = (int)(Math.max(y, py)+hy/2)/ts;
			
			float r = 1; Vector2 n = v1.set(0, 0);
			
			for(int yi = minYi; yi <= maxYi; yi++) {
			for(int xi = minXi; xi <= maxXi; xi++) {
				if (!level.getTile(xi, yi).solid) continue;
				
				float nr = Collision.sweepAABB(
						px-hx/2, py-hy/2, hx, hy, 
						xi*ts, yi*ts, ts, ts, 
						dx, dy, v2);
				if (nr < r) {
					r = nr;
					n.set(v2);
				}
			}}
			
			float ep = 0.001f;
			x = px + r*dx + ep*n.x;
			y = py + r*dy + ep*n.y;
			
			if (r == 1) break;
			else {
				if (n.x != 0) dx = 0;
				if (n.y != 0) dy = 0;
			}
			
			float BdotB = n.x*n.x + n.y*n.y;
			if (BdotB != 0) {
				px = x;
				py = y;
		
				float AdotB = (1-r)*(dx*n.x+dy*n.y);
				x += (1-r)*dx - (AdotB/BdotB)*n.x;
				y += (1-r)*dy - (AdotB/BdotB)*n.y;
			}
		}
		
		
		
	}
}
