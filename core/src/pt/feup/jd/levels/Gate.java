package pt.feup.jd.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pt.feup.jd.Assets;

public class Gate extends TileEntity {

	static TextureRegion open_spr, closed_spr;
	static boolean initSprites = false;
	static void initSprites() {
		initSprites = true;
		
		open_spr = Assets.sprites64[5][2];
		closed_spr = Assets.sprites64[5][3];
	}
	
	boolean open, inverted;
	String lever;
	
	public Gate(Level level, String name, float x, float y) {
		super(level, name, x, y);	
		
		initSprites();
		
		open = false;
		inverted = false;
	}
	
	@Override
	public void update(float delta) {
		if (lever != null && level.tileEntities.containsKey(lever)) {
			Lever leverObj = (Lever) level.tileEntities.get(lever);
			open = leverObj.on ^ inverted;
		} 
		level.setTile(xi,yi, open ? Tile.AIR : Tile.SOLID);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		sprite = open ? open_spr : closed_spr;
		
		super.render(batch);
	}
	
	
}
