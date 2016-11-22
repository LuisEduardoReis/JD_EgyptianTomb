package pt.feup.jd;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture testImage;
	public static Texture spritesheet;
	public static TextureRegion[][] sprites;
	
	public static void createAssets() {
		
		int l = JDGame.TILE_SIZE;
		
		testImage = new Texture("badlogic.jpg");
		spritesheet = new Texture("spritesheet.png");
		sprites = TextureRegion.split(spritesheet, l, l);		
		
	}
}
