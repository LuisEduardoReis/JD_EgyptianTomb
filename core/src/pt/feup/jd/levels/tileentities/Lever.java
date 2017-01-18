package pt.feup.jd.levels.tileentities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.triggers.Trigger;

public class Lever extends TileEntity implements Trigger {
	
	static TextureRegion on_spr, off_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		on_spr = Assets.sprites64[5][0];
		off_spr = Assets.sprites64[5][1];
	}
	
	boolean on;
	
	public Lever(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		if (!initSprites) initSprites();
		
		on = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		sprite = on ? on_spr : off_spr;
		super.render(batch);
	}

	@Override
	public void collide() {
		level.game.tooltip = "Press "+Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE))+" to toggle lever.";
		
		if (Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.USE))) {
			Util.playSound(Assets.gate_lever);
			on = !on;
		};
	}

	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getW() { return JDGame.TILE_SIZE; }
	@Override
	public float getH() { return JDGame.TILE_SIZE; }

	@Override
	public boolean active() { return false; }

	@Override
	public void setActive(boolean active) {}
}
