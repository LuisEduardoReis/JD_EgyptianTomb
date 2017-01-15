package pt.feup.jd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;

import pt.feup.jd.screens.GameScreen;

public class JDGame extends Game {
	
	public static final int TILE_SIZE = 64;
	public static enum Keys {
		UP,DOWN,LEFT,RIGHT, FIRE, HAMMER, USE
	}
	public static HashMap<Keys,Integer> keyBindings;
	public static void initKeyBindings() {
		keyBindings = new HashMap<Keys, Integer>();
		keyBindings.put(Keys.UP,Input.Keys.UP);
		keyBindings.put(Keys.DOWN,Input.Keys.DOWN);
		keyBindings.put(Keys.LEFT,Input.Keys.LEFT);
		keyBindings.put(Keys.RIGHT,Input.Keys.RIGHT);
		keyBindings.put(Keys.FIRE,Input.Keys.CONTROL_LEFT);
		keyBindings.put(Keys.HAMMER,Input.Keys.SHIFT_LEFT);
		keyBindings.put(Keys.USE,Input.Keys.DOWN);		
	}
	
	public static boolean DEBUG = false;
	
	public static enum Difficulty {
		EASY, NORMAL, HARD
	}
	
	public static Properties global_props;
	public static HashMap<Difficulty, Properties> difficulty_props;
	
	@Override
	public void create() {
		initKeyBindings();
		Assets.createAssets();		
		loadProperties();		
		setScreen(new GameScreen(this, Difficulty.NORMAL));
	}

	private void loadProperties() {
		global_props = new Properties();
		try {
			FileHandle file = Gdx.files.internal("properties/main.properties");
			if (file.exists()) global_props.load(file.read());
			else throw new FileNotFoundException("Missing /properties/main.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		difficulty_props = new HashMap<JDGame.Difficulty, Properties>();
		for(Difficulty dif : Difficulty.values()) {
			Properties prop = new Properties();
			try {
				FileHandle file = Gdx.files.internal("properties/"+dif.toString().toLowerCase()+".properties");
				if (file.exists()) prop.load(file.read());
				else throw new FileNotFoundException("Missing /properties/"+dif.toString().toLowerCase()+".properties");
			} catch (IOException e) {
				e.printStackTrace();
			}
			difficulty_props.put(dif, prop);			
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		
		/*try {
			FileHandle file = Gdx.files.local("/properties/main.properties");
			global_props.store(file.write(false), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Difficulty dif : difficulty_props.keySet()) {
			Properties prop = difficulty_props.get(dif);
			try {
				FileHandle file = Gdx.files.local("/properties/"+dif.toString().toLowerCase()+".properties");
				prop.store(file.write(false), null);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}*/
	}
	
	
	public static String getGlobalProperty(String name, String _default) {
		if (global_props.containsKey(name)) return global_props.getProperty(name);
		
		System.out.println("Missing global property " + name + ". Using " + _default + ".");
		global_props.put(name, _default);
		return _default;
	}
	
	public static String getDifficultyProperty(String name, Difficulty difficulty, String _default) {
		Properties p = difficulty_props.get(difficulty);
		if (p.containsKey(name)) return p.getProperty(name);
		
		System.out.println("Missing property " + name + " for difficulty " + difficulty.toString()+". Using " + _default + ".");
		p.put(name, _default);
		return _default;
	}
}
