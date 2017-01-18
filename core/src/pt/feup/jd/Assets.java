package pt.feup.jd;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture fillTexture;
	
	public static Texture spritesheet;
	public static TextureRegion[][] sprites64, sprites32;
	public static Texture light;
	
	public static String vertexShader;
	public static String fragmentShader;
	public static String defaultFragmentShader;
	
	public static Music music;
	public static Sound gate_lever;
	public static Sound gun_fire;
	public static Sound hurt;
	public static Sound jump;
	public static Sound open_door;
	public static Sound pickup_coin;
	public static Sound push;
	public static Sound wall_break;
	
	public static BitmapFont font;
	
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
		
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music1.wav"));
		music.setLooping(true);
		gate_lever = Gdx.audio.newSound(Gdx.files.internal("sounds/gate_lever.wav"));
		gun_fire = Gdx.audio.newSound(Gdx.files.internal("sounds/gun_fire.wav"));
		hurt = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.wav"));
		jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
		open_door = Gdx.audio.newSound(Gdx.files.internal("sounds/open_door.wav"));
		pickup_coin = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup_coin.wav"));
		push = Gdx.audio.newSound(Gdx.files.internal("sounds/push.wav"));
		wall_break = Gdx.audio.newSound(Gdx.files.internal("sounds/wall_break.wav"));
		
		font = new BitmapFont();		
	}


}
