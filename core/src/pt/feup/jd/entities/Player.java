package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Collision;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;
import pt.feup.jd.levels.tileentities.BrokenBlock;
import pt.feup.jd.levels.tileentities.TileEntity;

public class Player extends Entity {
	
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
		idle_anim.anim_delay = 1/3f;
		idle_anim.addFrame(Assets.sprites64[1][6]);
		idle_anim.addFrame(Assets.sprites64[1][7]);
		
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
	
	public float move_speed;
	public float jump_speed;
	public float ladder_speed;
	public int max_ammo;
	
	public int hammerHits;
	
	public Player(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		x = 0;
		y = 0;
		
		health = Float.parseFloat(JDGame.getDifficultyProperty("PLAYER_STARTING_HEALTH", level.game.difficulty,"100"));
		max_health = Float.parseFloat(JDGame.getDifficultyProperty("PLAYER_MAX_HEALTH", level.game.difficulty,"100"));
		
		move_speed = Float.parseFloat(JDGame.getGlobalProperty("PLAYER_MOVE_SPEED", 5*JDGame.TILE_SIZE+""));
		jump_speed = Float.parseFloat(JDGame.getGlobalProperty("PLAYER_JUMP_SPEED", Math.sqrt(2*15*2.5)*JDGame.TILE_SIZE+""));
		ladder_speed = Float.parseFloat(JDGame.getGlobalProperty("PLAYER_LADDER_SPEED", 4*JDGame.TILE_SIZE+""));
		max_ammo = Integer.parseInt(JDGame.getGlobalProperty("PLAYER_MAX_AMMO", "100"));		
		
		hx = 24;
		hy = 52;		
		
		jumpWindowDelay = Float.parseFloat(JDGame.getGlobalProperty("PLAYER_JUMP_WINDOW_DELAY", "0.25"));
		
		setSprite(idle_anim);
		
		gun_timer = 0;
		gun_delay = 1f/Float.parseFloat(JDGame.getGlobalProperty("PLAYER_BULLETS_PER_SECOND", "4"));
		gun_sway = 0;
		can_shoot = true;
		
		ammo = Integer.parseInt(JDGame.getGlobalProperty("PLAYER_STARTING_AMMO", "10"));		
		hammerHits = Integer.parseInt(JDGame.getGlobalProperty("PLAYER_STARTING_HAMMER", "4"));
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
				vx = -move_speed;
				direction = -1;
				setSprite(walk_anim);
			}
			else if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) {
				vx = move_speed;
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
				vy = jump_speed;
				jumped = true;
			}
			
			if (onLadder) {
				if (!aboveLadder || headOnLadder || feetOnLadder) {
					direction = (y % JDGame.TILE_SIZE) < JDGame.TILE_SIZE/2 ? 1 : -1;
					setSprite(ladder_anim);
					can_shoot = false;
				}
				if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP))) {
					vy = ladder_speed;
				} else
				if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.DOWN))) {
					vy = -ladder_speed;
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
			Bullet b = (Bullet) new Bullet(level).moveTo(x, y); b.x += direction*28; b.y -= 10;
			float s = 7.5f*JDGame.TILE_SIZE;
			b.vx = direction * s;
			b.scale_x = direction;
			
			gun_timer = gun_delay;
			ammo--;
		}
		if (vx != 0 && onGround) gun_sway += delta;
		
		
		// Hammer
		if (hammerHits > 0 && Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.HAMMER))) {
			for(TileEntity t : level.tileEntities.values()) {
				if (!(t instanceof BrokenBlock)) continue;
				BrokenBlock b = (BrokenBlock) t;
				
				if(!b.open && Collision.aabbToaabb(x+(0.5f*hx*direction), y-hy/4, hx, hy/2, b.xi*JDGame.TILE_SIZE, b.yi*JDGame.TILE_SIZE, JDGame.TILE_SIZE, JDGame.TILE_SIZE)) {
					hammerHits--;
					b.open = true;
					break;
				};
			}			
		}
	}
	
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof BallOfMagic) {
			damage(((BallOfMagic) o).damage);
			o.remove = true;
		}

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
	public void renderDebug(ShapeRenderer renderer) {
		super.renderDebug(renderer);
		
		if (hammerHits > 0) {
			renderer.setColor(Color.GREEN);
			renderer.rect(x+(0.5f*hx*direction) - hx/2, y - hy/4, hx,hy/2);
		}
	}
	
	
	@Override
	public void renderLight(SpriteBatch batch) {		
		Util.drawLight(batch, x, y, 7f, 7f, 1, 1, 0.75f, 1);
	}
	

}
