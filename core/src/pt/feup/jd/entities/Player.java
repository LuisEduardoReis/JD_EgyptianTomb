package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Player extends Entity {
	
	public static final float MOVE_SPEED = 5*JDGame.TILE_SIZE;
	public static final float JUMP_SPEED = (float) Math.sqrt(2*Entity.GRAVITY*2.5*JDGame.TILE_SIZE); 
	
	static TextureRegion idle_anim[];
	static TextureRegion walk_anim[];
	static boolean initSprites = false;
	static void initSprites() {
		idle_anim = new TextureRegion[1];
		idle_anim[0] = Assets.sprites[1][1];
		
		walk_anim = new TextureRegion[4];
		walk_anim[0] = Assets.sprites[1][0];
		walk_anim[1] = Assets.sprites[1][1];
		walk_anim[2] = Assets.sprites[1][2];
		walk_anim[3] = Assets.sprites[1][1];		
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
		
		sprite = walk_anim;
		anim_delay = 1/8f;
		
		gun_timer = 0;
		gun_delay = 0.25f;
	}
	
	public boolean onGround() {
		if (vy > 0) return false;
		int ts = JDGame.TILE_SIZE;
		int cx = (int) Math.floor(x / ts);
		int cy = (int) Math.floor((y - (hy*0.5f) - 4) / ts);
		return level.getTile(cx,cy).solid;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Movement
		if (onGround()) {
			jumpWindow = jumpWindowDelay;
			jumped = false;
		}
		jumpWindow = Util.stepTo(jumpWindow,0,delta);
		
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP)) && jumpWindow>0 && !jumped) {
			vy = JUMP_SPEED;
			jumped = true;
		}
		
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.LEFT))) {
			vx = -MOVE_SPEED;
			direction = Direction.LEFT;
			sprite = walk_anim;
			anim_speed = 1;
		}
		else if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) {
			vx = MOVE_SPEED;
			direction = Direction.RIGHT;
			sprite = walk_anim;
			anim_speed = 1;
		}
		else {
			sprite = idle_anim;
			vx = 0;
			anim_speed = 0;
		}
		
		// Weapon
		gun_timer = Util.stepTo(gun_timer, 0, delta);
		if (gun_timer == 0 && Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.FIRE))) {
			Bullet b = (Bullet) new Bullet(level).moveTo(x, y);
			float s = 7.5f*JDGame.TILE_SIZE;
			b.vx = (direction == Direction.RIGHT) ? s : -s;
			
			gun_timer = gun_delay;
		}
	}
	

}
