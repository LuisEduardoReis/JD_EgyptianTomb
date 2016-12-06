package pt.feup.jd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture testImage;
	public static Texture spritesheet;
	public static TextureRegion[][] sprites64, sprites32;
	
	public static void createAssets() {
		
		int l = JDGame.TILE_SIZE;
		
		testImage = new Texture("badlogic.jpg");
		spritesheet = new Texture("spritesheet.png");
		spritesheet.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		sprites64 = TextureRegion.split(spritesheet, l, l);
		sprites32 = TextureRegion.split(spritesheet, l/2, l/2);
		
		for(int i = 0; i < sprites64.length; i++)
			for(int j = 0; j < sprites64[i].length; j++) 
				Util.fixBleeding(sprites64[i][j]);
		for(int i = 0; i < sprites32.length; i++)
			for(int j = 0; j < sprites32[i].length; j++) 
				Util.fixBleeding(sprites32[i][j]);
		
	}
}
