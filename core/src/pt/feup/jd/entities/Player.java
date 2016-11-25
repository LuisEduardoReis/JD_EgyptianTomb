package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Player extends Entity {
	
	public static final float MOVE_SPEED = 6*JDGame.TILE_SIZE;
	public static final float JUMP_SPEED = (float) Math.sqrt(2*Entity.GRAVITY*2.5*JDGame.TILE_SIZE); 
	
	static TextureRegion walk_anim[];
	static boolean initSprites = false;
	static void initSprites() {
		walk_anim = new TextureRegion[4];
		walk_anim[0] = Assets.sprites[1][0];
		walk_anim[1] = Assets.sprites[1][1];
		walk_anim[2] = Assets.sprites[1][2];
		walk_anim[3] = Assets.sprites[1][1];		
	}
	
	float jumpWindow, jumpWindowDelay;
	boolean jumped;
	
	public Player(Level level) {
		super(level);
		
		this.x = 200;
		this.y = 200;
		
		if (!initSprites) initSprites();
		
		this.hx = 48;
		this.hy = 48;		
		
		this.jumpWindowDelay = 0.5f;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (onGround()) {
			jumpWindow = jumpWindowDelay;
			jumped = false;
		}
		jumpWindow = Util.stepTo(jumpWindow,0,delta);
		
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP)) && jumpWindow>0 && !jumped) {
			vy = JUMP_SPEED;
			jumped = true;
		}
		//if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP))) vy = s;
		//if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.DOWN))) vy = -s;
		
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.LEFT))) vx = -MOVE_SPEED;
		else if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) vx = MOVE_SPEED;
		else vx = 0;

	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);	
		batch.draw(Assets.sprites[1][0],(int) x - JDGame.TILE_SIZE/2, (int) y - JDGame.TILE_SIZE/2);
	}

}
