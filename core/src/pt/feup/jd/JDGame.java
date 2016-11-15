package pt.feup.jd;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;

import pt.feup.jd.screens.GameScreen;

public class JDGame extends Game {
	
	public static final int TILE_SIZE = 64;
	public static enum Keys {
		UP,DOWN,LEFT,RIGHT
	}
	public static HashMap<Keys,Integer> keyBindings;
	public static void initKeyBindings() {
		keyBindings = new HashMap<>();
		keyBindings.put(Keys.UP,Input.Keys.UP);
		keyBindings.put(Keys.DOWN,Input.Keys.DOWN);
		keyBindings.put(Keys.LEFT,Input.Keys.LEFT);
		keyBindings.put(Keys.RIGHT,Input.Keys.RIGHT);
	}
	
	@Override
	public void create() {
		initKeyBindings();
		Assets.createAssets();
		setScreen(new GameScreen(this));
	}
}