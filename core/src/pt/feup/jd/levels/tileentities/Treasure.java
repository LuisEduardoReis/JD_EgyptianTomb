package pt.feup.jd.levels.tileentities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.triggers.Trigger;

public class Treasure extends TileEntity implements Trigger {
	
	static TextureRegion main_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		main_spr = Assets.sprites64[2][7];
	}

	public Treasure(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		if (!initSprites) initSprites();
		
		sprite = main_spr;	
	}
	
	@Override
	public void collide() {
		if (remove) return;
		
		level.game.coins = 1000000;
		remove = true;
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
