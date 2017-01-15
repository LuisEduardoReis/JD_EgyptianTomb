package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Collision;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Entity {
	
	public Level level;
	
	public float x,y;
	public float px,py;
	public int hx, hy;
	public float vx, vy;
	public int direction;
	
	public boolean colideWithLevel;
	public float bounciness;
	public boolean applyGravity;
	
	public boolean visible = true;
	public Sprite sprite;
	public float anim_timer, anim_speed; 
	public int anim_index;
	
	public float rotation;	
	public float offset_x, offset_y;
	public float scale_x, scale_y;
	
	public float health, max_health;
	public float damage_anim_timer, damage_anim_delay;
	public boolean dead;
	
	public boolean remove = true;
	
	public float t;
	
	public float gravity;
	
	
	public Entity(Level level) {
		this.level = level;
		level.addEntity(this);
		
		hx = 32;
		hy = 32;
				
		colideWithLevel=true;
		bounciness=0; 
		applyGravity=true;
		
		gravity = Float.parseFloat(JDGame.getGlobalProperty("GRAVITY", 15*JDGame.TILE_SIZE+""));
		
		visible = true;
		
		anim_timer = 0;
		anim_index = 0;
		anim_speed = 1;
		
		direction = 1;
		
		health = max_health = 100;
		damage_anim_timer = 0;
		damage_anim_delay = 1;
		remove = false;
		
		rotation = 0;
		offset_x = 0; 
		offset_y = 0;
		scale_x = 1;
		scale_y = 1;
		
		t = 0;
	}
	
	public void preupdate(float delta) {
		px = x;
		py = y;
	}
	public void update(float delta) {
		// Logic
		if (applyGravity) vy-=gravity*delta;
		
		if (health <= 0 && !dead) die(); 
		
		t += delta;
		
		// Animation
		damage_anim_timer = Util.stepTo(damage_anim_timer, 0, delta);
		
		if (sprite != null) {
			if (sprite.anim_delay > 0) {
				anim_timer += delta * anim_speed;
				while (anim_timer > sprite.anim_delay) {
					anim_index++;			
					anim_timer -= sprite.anim_delay;
				}
				if (!sprite.anim_loop && anim_index >= sprite.frames.size()) anim_index = sprite.frames.size()-1;
			} else {
				anim_index = 0;
			}
		}
	}
	
	public void damage(float damage) {
		if (dead) return;
		health = Util.stepTo(health, 0, damage);
		damage_anim_timer = damage_anim_delay;
	}
	public void heal(float heal) {
		if (dead) return;
		health = Util.stepTo(health, max_health, heal);
	}
	
	public void die() {
		dead = true;
		remove = true;		
	}

	public void postupdate(float delta) {
		x += vx*delta;
		y += vy*delta;
		
		if(sprite != null) anim_index %= sprite.frames.size();
	}
	
	public void setSprite(Sprite sprite) {
		if (sprite != this.sprite) {
			anim_index = 0;
			anim_timer = 0;
			this.sprite = sprite;
		}		
	}

	public static Color color = new Color();
	public void render(SpriteBatch batch) {
		if (!visible) return;
		
		if(sprite != null) {			
			color.set(1,1,1,1);
			if (damage_anim_timer > 0) {
				float s = damage_anim_timer / damage_anim_delay;
				color.set(1,1-s,1-s,1);
			}
			sprite.render(batch,anim_index, x + offset_x, y + offset_y, direction*scale_x, scale_y, rotation, color);
		}
	}
	
	public void renderDebug(ShapeRenderer renderer) {
		renderer.setColor(1,1,1,1);
		renderer.rect((int)(x-hx/2),(int)(y-hy/2),hx,hy);
	}
	
	public void renderLight(SpriteBatch batch) {
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
			Tile rt = Tile.AIR;
	
			for(int yi = minYi; yi <= maxYi; yi++) {
			for(int xi = minXi; xi <= maxXi; xi++) {
				Tile t = level.getTile(xi, yi);
				if (!checkSolid(t)) continue;
				
				float nr = Collision.sweepAABB(
						px-hx/2, py-hy/2, hx, hy, 
						xi*ts, yi*ts, ts, ts, 
						dx, dy, v2);
				if (nr < r) {
					r = nr;
					n.set(v2);
					rt = t;
				}
			}}
			
			float ep = 0.001f;
			x = px + r*dx + ep*n.x;
			y = py + r*dy + ep*n.y;
			
			if (r == 1) break;
			else {
				levelCollision(n.x,n.y,rt);
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
	
	public boolean checkSolid(Tile t) {
		return t.solid;
	}

	public void levelCollision(float nx, float ny, Tile t) {		
		if (t == Tile.SPIKE) damage(health);
	}

	public void entityCollision(Entity o) {
		
		if (o instanceof Fireball) {
			damage(((Fireball) o).damage);
			o.remove = true;
		}

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
		return level.getTile(cx,cy).jumpable;
	}
	public boolean onGroundWide() {
		if (vy > 0) return false;
		int ts = JDGame.TILE_SIZE;
		int cy = (int) Math.floor((y - (hy*0.5f) - 4) / ts);
		int mincx = (int) Math.floor((x - hx*0.5f) / ts);
		int maxcx = (int) Math.floor((x + hx*0.5f) / ts);
		for(int cx = mincx; cx <= maxcx; cx++)
			if (level.getTile(cx, cy).jumpable)
				return true;
		return false;
	}

	public void destroy() {}
	
}
