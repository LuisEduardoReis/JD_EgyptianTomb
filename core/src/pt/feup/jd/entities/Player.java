package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Player extends Entity {
	
	public static final float MOVE_SPEED = 5*JDGame.TILE_SIZE;
	public static final float JUMP_SPEED = (float) Math.sqrt(2*Entity.GRAVITY*2.5*JDGame.TILE_SIZE);
	public static final float LADDER_SPEED = 4*JDGame.TILE_SIZE;
	public static final int MAX_AMMO = 100;
	
	static Sprite idle_anim;
	static Sprite walk_anim;
	static Sprite jump_anim;
	static Sprite ladder_anim;
	static Sprite dead_anim;
	static Sprite shotgun_spr;	
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
		
		ladder_anim = new Sprite();
		ladder_anim.addFrame(Assets.sprites64[1][4]);
		
		dead_anim = new Sprite();
		dead_anim.addFrame(Assets.sprites64[1][5]);
		
		shotgun_spr = new Sprite(); 
		shotgun_spr.addFrame(Assets.sprites64[2][2]);
	}
	
	float jumpWindow, jumpWindowDelay;
	boolean jumped;
	
	public int ammo;
	float gun_delay, gun_timer, gun_sway;
	boolean can_shoot;
	
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
		gun_sway = 0;
		can_shoot = true;
		
		ammo = 10;
	}
	
	@Override
	public void update(float delta) {
		
		boolean onGround = onGroundWide();
		boolean feetOnLadder = (level.getTile((int) (x / JDGame.TILE_SIZE), (int) ((y  - hy*0.5f) / JDGame.TILE_SIZE)) == Tile.LADDER);
		boolean aboveLadder  = (level.getTile((int) (x / JDGame.TILE_SIZE), (int) ((y  - hy*0.5f - 4) / JDGame.TILE_SIZE)) == Tile.LADDER);
		boolean headOnLadder = (level.getTile((int) (x / JDGame.TILE_SIZE), (int) ((y  + hy*0.5f) / JDGame.TILE_SIZE)) == Tile.LADDER);
		boolean onLadder = feetOnLadder || headOnLadder || aboveLadder;
		
		if (!onGround && onLadder) applyGravity = false;
		else applyGravity = true;
		
		can_shoot = true;
		
		super.update(delta);
		
		// Movement
		if (!dead) {
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
			
			if (onGround) {
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
			
			if (onLadder) {
				if (!aboveLadder || headOnLadder || feetOnLadder) {
					direction = (y % JDGame.TILE_SIZE) < JDGame.TILE_SIZE/2 ? 1 : -1;
					setSprite(ladder_anim);
					can_shoot = false;
				}
				if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP))) {
					vy = LADDER_SPEED;
				} else
				if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.DOWN))) {
					vy = -LADDER_SPEED;
				} else {
					vy = 0;
				}
				
			} 
		} else {
			setSprite(dead_anim);
			vx = 0;
		}
			
				
		// Weapon
		gun_timer = Util.stepTo(gun_timer, 0, delta);
		if (can_shoot && gun_timer == 0 && ammo > 0 && Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.FIRE))) {
			Bullet b = (Bullet) new Bullet(level).moveTo(x + direction*28, y-10);
			float s = 7.5f*JDGame.TILE_SIZE;
			b.vx = direction * s;
			
			gun_timer = gun_delay;
			ammo--;
		}
		if (vx != 0 && onGround) gun_sway += delta;
		
	}
	
	@Override
	public void die() {
		dead = true;
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		if (ammo > 0 && can_shoot && !dead) {
			shotgun_spr.render(batch, 0, 
					x+(direction * 8), (float) (y + 3*Math.sin(2*Math.PI*4*gun_sway)), 
					direction, 1, 
					0, Color.WHITE);
		}

		super.render(batch);
	}
	
	
	@Override
	public void renderLight(SpriteBatch batch) {		
		Util.drawLight(batch, x, y, 7f, 7f, 1, 1, 0.75f, 1);
	}
	

}
