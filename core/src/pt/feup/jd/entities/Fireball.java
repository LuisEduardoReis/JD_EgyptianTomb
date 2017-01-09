package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class Fireball extends Entity {
	
	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		for(int i = 0; i < 8; i++) default_anim.addFrame(Assets.sprites64[3][i]);
		for(int i = 0; i < 8; i++) default_anim.addFrame(Assets.sprites64[3][7-i]);
		default_anim.anim_delay = 1f / 20;
	}

	float damage;
	
	public Fireball(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		hx = 8;
		hy = 8;
		
		applyGravity = false;
		
		damage = 25f;
	}

		
	@Override
	public void levelCollision(float nx, float ny, Tile t) {
		super.levelCollision(nx, ny, t);
		
		remove = true;
	}
	
	@Override
	public void renderLight(SpriteBatch batch) {
		float s = 2.5f + 1f*((float) Math.sin(anim_index / 16f * Math.PI));
		Util.drawLight(batch, x, y, s, s, 1, 0.75f, 0.25f, 0.75f);
	}
	
}
