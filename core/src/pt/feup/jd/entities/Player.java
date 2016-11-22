package pt.feup.jd.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;

public class Player extends Entity {
	
	
	static TextureRegion walk_anim[];
	static boolean initSprites = false;
	static void initSprites() {
		walk_anim = new TextureRegion[4];
		walk_anim[0] = Assets.sprites[1][0];
		walk_anim[1] = Assets.sprites[1][1];
		walk_anim[2] = Assets.sprites[1][2];
		walk_anim[3] = Assets.sprites[1][1];		
	}
	
	public Player(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		this.width = 16;
		this.height = 16;		
	}
	
	@Override
	public void update(float delta) {
		float s = 10;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP)) && onGround()) dy = s;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.LEFT))) dx = -s;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) dx = s;
		
		if (onGround()) fx = 0.50f; else fx = 0.85f;
		
		super.update(delta);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		batch.draw(Assets.sprites[1][0],(int) x - JDGame.TILE_SIZE/2, (int) y - JDGame.TILE_SIZE/2);
	}

}
