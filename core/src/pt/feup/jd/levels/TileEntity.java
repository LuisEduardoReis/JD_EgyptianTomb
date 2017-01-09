package pt.feup.jd.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;

import pt.feup.jd.JDGame;

public class TileEntity {

	Level level;
	String name;
	float x,y;
	int xi, yi;
	TextureRegion sprite;
	float rotation;
	
	public TileEntity(Level level, String name, float x, float y) {
		this.level = level;
		this.name = name;
		this.x = x;
		this.y = y;
		this.xi = (int) (x / JDGame.TILE_SIZE);
		this.yi = (int) (y / JDGame.TILE_SIZE);
		
		this.rotation = 0;
	}
	
	public void update(float delta) {
		
	}
	
	static Affine2 a = new Affine2();
	public void render(SpriteBatch batch) {
		if (sprite != null) {
			a.idt();
			a.translate(x - JDGame.TILE_SIZE/2,y - JDGame.TILE_SIZE/2);
			a.translate(JDGame.TILE_SIZE/2, JDGame.TILE_SIZE/2);
			a.rotate(rotation);
			a.translate(-JDGame.TILE_SIZE/2, -JDGame.TILE_SIZE/2);
			batch.draw(sprite, JDGame.TILE_SIZE, JDGame.TILE_SIZE, a);
		}
	}
}
