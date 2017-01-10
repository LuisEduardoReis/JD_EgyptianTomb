package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Snake extends Enemy {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites64[4][0]);
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
	
	@Override
	public void die() {
		super.die();

		for(int i = 0; i < 3; i++) {
			Coin coin = (Coin) new Coin(level).moveTo(x,y);
			coin.vy = Util.randomRange(2*JDGame.TILE_SIZE, 4*JDGame.TILE_SIZE);
			coin.vx = Util.randomRange(-JDGame.TILE_SIZE, JDGame.TILE_SIZE);
		}
	}

}
