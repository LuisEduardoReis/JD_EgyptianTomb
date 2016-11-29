package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.levels.Level;

public class Bullet extends Entity {

	static TextureRegion[] default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new TextureRegion[1];
		default_anim[0] = Assets.sprites[3][0];
	}
	
	float damage;
	
	public Bullet(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		applyGravity = false;
		
		hx = 4;
		hy = 4;
		
		damage = 10;
	}
	
	@Override
	public void levelCollision(float x, float y) {
		super.levelCollision(x, y);
		
		remove = true;
	}

}
