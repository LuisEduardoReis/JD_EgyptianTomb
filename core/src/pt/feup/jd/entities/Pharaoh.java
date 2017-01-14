package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Pharaoh extends Enemy {

	static Sprite default_anim;
	static Sprite active_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		default_anim = new Sprite();
		default_anim.addFrame(Assets.sprites64[5][5]);
		
		active_anim = new Sprite();
		active_anim.anim_delay = 0.25f;
		active_anim.addFrame(Assets.sprites64[5][6]);
		active_anim.addFrame(Assets.sprites64[5][7]);
	}
	
	float attack_damage;
	float attack_timer, attack_delay;
	
	
	public Pharaoh(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		hx = 32;
		hy = 48;
			
		attack_damage = 25;
		attack_timer = 0;
		attack_delay = 3;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		attack_timer = Util.stepTo(attack_timer, 0, delta);
		
		if (range < 0 || inRange_timer > 0) {
			sprite = active_anim;
			Player p = level.player;
			if (attack_timer == 0 && p != null) {
				BallOfMagic b = (BallOfMagic) new BallOfMagic(level).moveTo(x, y);
				b.rotation = Util.pointDirection(x,y, p.x, p.y);
				float s = 3f*JDGame.TILE_SIZE;
				b.vx = s*(p.x-x)/distanceToPlayer;
				b.vy = s*(p.y-y)/distanceToPlayer;
				
				attack_timer = attack_delay;
			}
		} else
			sprite = default_anim;
	}
}
