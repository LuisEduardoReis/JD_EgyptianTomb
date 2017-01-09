package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Player extends Entity {
	
	public static final float MOVE_SPEED = 5*JDGame.TILE_SIZE;
	public static final float JUMP_SPEED = (float) Math.sqrt(2*Entity.GRAVITY*2.5*JDGame.TILE_SIZE); 
	
	static Sprite idle_anim;
	static Sprite walk_anim;
	static Sprite jump_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		idle_anim = new Sprite();
		idle_anim.addFrame(Assets.sprites64[1][0]);
		
		walk_anim = new Sprite();
		walk_anim.anim_delay = 1/8f;
		walk_anim.addFrame(Assets.sprites64[1][1]);
		walk_anim.addFrame(Assets.sprites64[1][0]);
		walk_anim.addFrame(Assets.sprites64[1][2]);
		walk_anim.addFrame(Assets.sprites64[1][0]);
		
		jump_anim = new Sprite();
		jump_anim.addFrame(Assets.sprites64[1][3]);
	}
	
	float jumpWindow, jumpWindowDelay;
	boolean jumped;
	
	float gun_delay, gun_timer;
	
	public Player(Level level) {
		super(level);
		
		x = 0;
		y = 0;
		
		if (!initSprites) initSprites();
		
		hx = 24;
		hy = 52;		
		
		jumpWindowDelay = 0.25f;
		
		setSprite(idle_anim);
		
		gun_timer = 0;
		gun_delay = 0.25f;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Movement
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.LEFT))) {
			vx = -MOVE_SPEED;
			direction = -1;
			setSprite(walk_anim);
		}
		else if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) {
			vx = MOVE_SPEED;
			direction = 1;
			setSprite(walk_anim);
		}
		else {
			setSprite(idle_anim);
			vx = 0;
		}
		
		
		if (onGroundWide()) {
			jumpWindow = jumpWindowDelay;
			jumped = false;
		} else {
			setSprite(jump_anim);
		}
		
		jumpWindow = Util.stepTo(jumpWindow,0,delta);
		
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP)) && jumpWindow>0 && !jumped) {
			vy = JUMP_SPEED;
			jumped = true;
		}
		
		
		// Weapon
		gun_timer = Util.stepTo(gun_timer, 0, delta);
		if (gun_timer == 0 && Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.FIRE))) {
			Bullet b = (Bullet) new Bullet(level).moveTo(x, y);
			float s = 7.5f*JDGame.TILE_SIZE;
			b.vx = direction * s;
			
			gun_timer = gun_delay;
		}
		
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Fireball) {
			damage(((Fireball) o).damage);
			o.remove = true;
		}
		
	}
	
	@Override
	public void renderLight(SpriteBatch batch) {		
		Util.drawLight(batch, x, y, 7f, 7f, 1, 1, 0.75f, 1);
	}
	

}
