package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Jar extends Entity {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites64[2][6]);		
	}
	
	public int content;
	
	public Jar(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		applyGravity = true;
		
		hx = 60;
		hy = 60;
		
		content = Integer.parseInt(JDGame.getGlobalProperty("JAR_DEFAULT_CONTENT", "3"));
	}
	
	@Override
	public void entityCollision(Entity o) {
		super.entityCollision(o);
		
		if (o instanceof Bullet) {
			o.remove = true;
			this.remove = true;
		}
	}
	
	@Override
	public void destroy() {
		
		for(int i = 0; i < content; i++) {
			Coin coin = (Coin) new Coin(level).moveTo(x,y);
			coin.vy = Util.randomRange(2*JDGame.TILE_SIZE, 4*JDGame.TILE_SIZE);
			coin.vx = Util.randomRange(-JDGame.TILE_SIZE, JDGame.TILE_SIZE);
		}
	}

}
