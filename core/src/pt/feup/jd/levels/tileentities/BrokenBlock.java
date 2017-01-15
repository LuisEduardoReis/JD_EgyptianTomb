package pt.feup.jd.levels.tileentities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.Tile;

public class BrokenBlock extends TileEntity {

	static TextureRegion solid_spr, open_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		solid_spr = Assets.sprites64[6][3];
	}
	
	public boolean open;
	
	public BrokenBlock(Level level, String name, float x, float y) {
		super(level, name, x, y);	
		
		if (!initSprites) initSprites();
		
		open = false;
	}
	
	@Override
	public void update(float delta) {
		level.setTile(xi,yi, open ? Tile.AIR : Tile.SOLID);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		sprite = open ? null : solid_spr;
		
		super.render(batch);
	}
	
	
}
