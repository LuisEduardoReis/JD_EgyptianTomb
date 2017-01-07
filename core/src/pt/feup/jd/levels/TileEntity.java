package pt.feup.jd.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.JDGame;

public class TileEntity {

	Level level;
	String name;
	int xi, yi;
	TextureRegion sprite;
	
	public TileEntity(Level level, String name, float x, float y) {
		this.level = level;
		this.name = name;
		this.xi = (int) (x / JDGame.TILE_SIZE);
		this.yi = (int) (y / JDGame.TILE_SIZE);
	}
	
	public void update(float delta) {
		
	}
	
	public void render(SpriteBatch batch) {
		if (sprite != null) {
			batch.draw(sprite, xi * JDGame.TILE_SIZE, yi * JDGame.TILE_SIZE);
		}
	}
}
