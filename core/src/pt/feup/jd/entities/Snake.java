package pt.feup.jd.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.levels.Level;

public class Snake extends Enemy {

	static TextureRegion[] default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new TextureRegion[1];
		default_anim[0] = Assets.sprites[4][0];
	}
	
	
	public Snake(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		hx = 48;
		hy = 48;
	}

}
