package pt.feup.jd.levels.tileentities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;

public class TileEntity {

	static int idCount;
	
	public Level level;
	public String name;
	public float x,y;
	public int xi, yi;
	public TextureRegion sprite;
	public float rotation;
	public boolean remove;
	
	public TileEntity(Level level, String name, float x, float y) {
		this.level = level;
		this.name = (name == null || name.equals("")) ? "default_"+(idCount++) : name;
		this.xi = (int) (x / JDGame.TILE_SIZE);
		this.yi = (int) (y / JDGame.TILE_SIZE);
		this.x = xi * JDGame.TILE_SIZE;
		this.y = yi * JDGame.TILE_SIZE;
		
		this.rotation = 0;
		this.remove = false;
	}
	
	public void update(float delta) {
		
	}
	
	static Affine2 a = new Affine2();
	public void render(SpriteBatch batch) {
		if (sprite != null) {
			a.idt();
			a.translate(x,y);			
			batch.draw(sprite, JDGame.TILE_SIZE, JDGame.TILE_SIZE, a);
		}
	}
	
	public void renderLight(SpriteBatch batch) {
	}
}
