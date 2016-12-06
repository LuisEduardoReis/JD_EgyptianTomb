package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Collision;
import pt.feup.jd.levels.Level;

public class Entity {
	
	public static enum Direction {
		UP(0,1,90f), DOWN(0,-1,270f), LEFT(-1,0,180f), RIGHT(1,0,0f); 
		public int x, y; 
		public float dir;
		Direction(int xx, int yy, float d) {x = xx; y = yy; dir = d;}
	};
	
	public static final float GRAVITY = 15*JDGame.TILE_SIZE;

	public Level level;
	
	public float x,y;
	public float px,py;
	public int hx, hy;
	public float vx, vy;
	public Direction direction;
	
	public boolean colideWithLevel;
	public float bounciness;
	public boolean applyGravity;
	
	public boolean visible = true;
	public TextureRegion[] sprite;
	public float anim_timer, anim_delay, anim_speed; 
	public int anim_index; 
	public boolean anim_loop;
	
	public float health;
	public boolean dead;
	
	public boolean remove = true;
	
	public Entity(Level level) {
		this.level = level;
		level.addEntity(this);
		
		hx = 32;
		hy = 32;
				
		colideWithLevel=true;
		bounciness=0; 
		applyGravity=true;
		
		visible = true;
		
		anim_loop = true;
		anim_timer = 0;
		anim_delay = -1;
		anim_index = 0;
		anim_speed = 1;
		
		direction = Direction.RIGHT;
		
		health = 100;
		remove = false;
	}
	
	public void preupdate(float delta) {
		px = x;
		py = y;
	}
	public void update(float delta) {
		// Logic
		if (applyGravity) vy-=GRAVITY*delta;
		
		if (health <= 0 && !dead) die(); 
		
		// Animation
		if (sprite != null) {
			if (anim_delay > 0) {
				anim_timer += delta * anim_speed;
				while (anim_timer > anim_delay) {
					anim_index++;			
					anim_timer -= anim_delay;
				}
				if (!anim_loop && anim_index >= sprite.length) anim_index = sprite.length-1;
			} else {
				anim_index = 0;
			}
		}
	}
	private void die() {
		dead = true;
		remove = true;		
	}

	public void postupdate(float delta) {
		x += vx*delta;
		y += vy*delta;
		
		anim_index %= sprite.length;
	}
	
	static Affine2 t = new Affine2();
	static Color color = new Color();
	public void render(SpriteBatch batch) {
		if (!visible) return;
		
		int ts = JDGame.TILE_SIZE;
		
		TextureRegion s = null;
		float scaleX = (direction == Direction.LEFT) ?  -1 : 1;
	
		s = sprite[anim_index];
		
		t.idt();
		t.translate((int) x, (int) y);
		t.scale(scaleX, 1);
		t.translate(-ts/2, -ts/2);
	
		
		color.set(Color.WHITE);
		batch.setColor(color);
		batch.draw(s, ts, ts, t);
		batch.setColor(Color.WHITE);
	}
	
	public void renderDebug(ShapeRenderer renderer) {
		renderer.setColor(1,1,1,1);
		renderer.rect((int)(x-hx/2),(int)(y-hy/2),hx,hy);
	}
	
	public static Vector2 v1 = new Vector2(), v2 = new Vector2();
	public void checkLevelCollision() {
		if (!colideWithLevel) return;
		
		int ts = JDGame.TILE_SIZE;
		
		while(true) {
		
			float dx = x - px, dy = y - py;
			int minXi = (int) Math.floor((Math.min(x, px)-hx/2)/ts), maxXi = (int)Math.floor((Math.max(x, px)+hx/2)/ts);
			int minYi = (int) Math.floor((Math.min(y, py)-hy/2)/ts), maxYi = (int)Math.floor((Math.max(y, py)+hy/2)/ts);
			
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
				levelCollision(n.x,n.y);
				if (n.x != 0) vx = -bounciness*vx;
				if (n.y != 0) vy = -bounciness*vy;
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
	
	public void levelCollision(float nx, float ny) {		
	}

	public void entityCollision(Entity other) {
		
	}
	
	public Entity moveTo(float x, float y) {
		this.x = this.px = x;
		this.y = this.py = y;
		return this;
	}
	public Entity moveTo(Vector2 v) { return moveTo(v.x,v.y); }

	public boolean onGround() {
		if (vy > 0) return false;
		int ts = JDGame.TILE_SIZE;
		int cx = (int) Math.floor(x / ts);
		int cy = (int) Math.floor((y - (hy*0.5f) - 4) / ts);
		return level.getTile(cx,cy).solid;
	}

}
