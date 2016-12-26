package pt.feup.jd;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture fillTexture;
	
	public static Texture spritesheet;
	public static TextureRegion[][] sprites64, sprites32;
	public static Texture light;
	
	public static String vertexShader;
	public static String fragmentShader;
	public static String defaultFragmentShader;
	
	public static void createAssets() {
		
		int l = JDGame.TILE_SIZE;
		
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
		
		light = new Texture("light.png");
		
		vertexShader = new FileHandle("shaders/vertexShader.glsl").readString();
		defaultFragmentShader = new FileHandle("shaders/defaultFragmentShader.glsl").readString();
		fragmentShader =  new FileHandle("shaders/fragmentShader.glsl").readString();
		
	}


}
