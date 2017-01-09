package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Bullet extends Entity {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites32[4][0]);
		
	}
	
	float damage;
	
	public Bullet(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		applyGravity = false;
		
		hx = 4;
		hy = 4;
		
		damage = 50;
	}
	
	@Override
	public void levelCollision(float x, float y) {
		super.levelCollision(x, y);
		
		remove = true;
	}

	@Override
	public void renderLight(SpriteBatch batch) {		
		Util.drawLight(batch, x, y, 2f, 2f, 1, 1, 0.75f, 0.25f);
	}
}
