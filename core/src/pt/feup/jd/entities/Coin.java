package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Coin extends Entity {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites32[5][0]);
		
	}
	
	private float anim_offset;
	
	public Coin(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		applyGravity = true;
		
		hx = 4;
		hy = 4;
		
		anim_offset = Util.randomRange(0, 3);
	}
	
	@Override
	public void update(float delta) {	
		applyGravity = true;
		
		vx *= 0.95f;
		
		offset_y = 4*((float) Math.sin(2*Math.PI*(t + anim_offset))+1);
		
		if (level.player != null) {
			float d = Util.pointDistance(level.player.x, level.player.y, x,y);
			if (d < JDGame.TILE_SIZE) {
				applyGravity = false;
				vx = 3*JDGame.TILE_SIZE * (level.player.x - x) / d;
				vy = 3*JDGame.TILE_SIZE * (level.player.y - y) / d;
			}
			if (d < 8) {
				level.game.addCoins(1);
				remove = true;
			}
		}
		
		super.update(delta);
	}
}
