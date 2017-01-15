package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Snake extends Enemy {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.anim_delay = 0.25f;
		default_anim.addFrame(Assets.sprites64[4][0]);
		default_anim.addFrame(Assets.sprites64[4][1]);
		default_anim.addFrame(Assets.sprites64[4][2]);
		default_anim.addFrame(Assets.sprites64[4][3]);
	}
	
	boolean turnOnBump;
	boolean turnOnEdge;
		boolean turning;

	
	public Snake(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		health = max_health = Float.parseFloat(JDGame.getDifficultyProperty("ENEMY_SNAKE_HEALTH", level.game.difficulty,"100"));
		
		sprite = default_anim;
		
		hx = 32;
		hy = 48;
		
		vx = 32;
		
		walkSpeed = Float.parseFloat(JDGame.getDifficultyProperty("ENEMY_SNAKE_WALKSPEED", level.game.difficulty,"64"));
		
		turnOnBump = true;
		turnOnEdge = true;
		
		contactDamage = Float.parseFloat(JDGame.getDifficultyProperty("ENEMY_SNAKE_CONTACT_DAMAGE", level.game.difficulty,"25"));
		contactDamage_delay = Float.parseFloat(JDGame.getDifficultyProperty("ENEMY_SNAKE_CONTACT_DAMAGE_DELAY", level.game.difficulty,"1.0"));
		
		coinLoot = Integer.parseInt(JDGame.getDifficultyProperty("ENEMY_SNAKE_COIN_LOOT", level.game.difficulty,"3"));
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (range < 0 || inRange_timer > 0) {
			vx = walkSpeed * direction;
			
			if (onGround()) turning = false;
			else if (turnOnEdge && !turning){
				turning = true;
				direction = -direction;
			}
		} else
			vx = 0;
	}
	
	@Override
	public void levelCollision(float nx, float ny, Tile t) {
		super.levelCollision(nx, ny, t);
		
		if (nx != 0 && turnOnBump) { 
			direction = -direction; 
		}
	}
	
}
