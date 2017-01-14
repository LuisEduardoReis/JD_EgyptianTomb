package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class BallOfMagic extends Entity {


	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.anim_delay = 1/20f;
		default_anim.addFrame(Assets.sprites64[6][0]);
		default_anim.addFrame(Assets.sprites64[6][1]);
		default_anim.addFrame(Assets.sprites64[6][2]);
		default_anim.addFrame(Assets.sprites64[6][1]);
	}
	
	float damage;
	
	public BallOfMagic(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		applyGravity = false;
		
		hx = 8;
		hy = 8;
		
		damage = 25;
	}
	
	@Override
	public void levelCollision(float x, float y, Tile t) {
		super.levelCollision(x, y, t);
		
		remove = true;
	}

	@Override
	public void renderLight(SpriteBatch batch) {		
		Util.drawLight(batch, x, y, 2.5f, 2.5f, 1, 0.5f, 1f, 1f);
	}
	
}
