package pt.feup.jd.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.entities.Fireball;

public class FireballTrap extends TileEntity {

	static TextureRegion main_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		main_spr = Assets.sprites64[5][4];
	}
	
	String trigger;
	float fireTimer, fireDelay, fireSpeed;

	public FireballTrap(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		if (!initSprites) initSprites();
		
		sprite = main_spr;
		
		trigger = null;
		fireTimer = 0;
		fireDelay = 4f;
		fireSpeed = 2*JDGame.TILE_SIZE;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		
		fireTimer = Util.stepTo(fireTimer, 0, delta);
		if (fireTimer == 0 && (trigger == null || level.triggers.get(trigger).active())) {
			Fireball b = (Fireball) new Fireball(level).moveTo(x, y);
			b.rotation = rotation;
			b.vx = (float) (fireSpeed * Math.cos(rotation * Util.degToRad));
			b.vy = (float) (fireSpeed * Math.sin(rotation * Util.degToRad));
			
			fireTimer = fireDelay;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
		super.render(batch);
	}

}
