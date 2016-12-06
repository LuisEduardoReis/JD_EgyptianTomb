package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.levels.Level;

public class Snake extends Enemy {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites[4][0]);
	}
	
	
	public Snake(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		hx = 48;
		hy = 48;
		
		vx = 32;
		
		walkSpeed = 64;
		turnOnBump = true;
		turnOnEdge = true;
		
		contactDamage = 25;
	}

}
