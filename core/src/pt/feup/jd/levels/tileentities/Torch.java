package pt.feup.jd.levels.tileentities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.Sprite;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Torch extends TileEntity {
	
	static Sprite main_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		main_spr = new Sprite();
		main_spr.anim_delay = 1/8f;
		main_spr.addFrame(Assets.sprites64[2][3]);
		main_spr.addFrame(Assets.sprites64[2][4]);
		main_spr.addFrame(Assets.sprites64[2][5]);
	}
	
	int anim_index;
	float anim_timer, anim_delay;
	

	public Torch(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		if (!initSprites) initSprites();
		
		anim_index = 0;
		anim_timer = 0;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		anim_timer += delta;
		while(anim_timer > main_spr.anim_delay) {
			anim_index = (anim_index+1)%(main_spr.frames.size());
			anim_timer-=main_spr.anim_delay;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		sprite = main_spr.frames.get(anim_index);
		super.render(batch);
	}
	@Override
	public void renderLight(SpriteBatch batch) {
		Util.drawLight(batch, x, y, 4, 4, 1, 1, 0.5f, 1);
	}
}
