package pt.feup.jd.entities;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Mummy extends Enemy {

	static Sprite default_anim;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		default_anim = new Sprite();
		default_anim.anim_delay = 0.25f;
		default_anim.addFrame(Assets.sprites64[4][4]);
		default_anim.addFrame(Assets.sprites64[4][5]);
		default_anim.addFrame(Assets.sprites64[4][6]);
		default_anim.addFrame(Assets.sprites64[4][5]);
	}
	
	float follow_timer, follow_delay;
	
	public Mummy(Level level) {
		super(level);
		
		if (!initSprites) initSprites();
		
		sprite = default_anim;
		
		hx = 32;
		hy = 48;
		
		walkSpeed = 64;
		
		follow_timer = 0;
		follow_delay = 0.5f;
		
		contactDamage = 25;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		int targetDirection = (level.player.x > x)? 1 : -1;
		if (targetDirection != direction && follow_timer == 0) {
			direction = targetDirection;
			follow_timer = follow_delay;
		}
		follow_timer = Util.stepTo(follow_timer,0, delta);
		
		
		if (range < 0 || inRange_timer > 0) {
			vx = direction * walkSpeed;
		} else
			vx = 0;
	}

}
