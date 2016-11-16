package pt.feup.jd.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.JDGame;
import pt.feup.jd.entities.Entity;
import pt.feup.jd.screens.GameScreen;

public class Level {
	GameScreen game;
	
	// Logic
	TiledMap map;
	ArrayList<Entity> entities;
	
	Vector2 cameraPosition;
	
	// Render
	OrthogonalTiledMapRenderer tileRenderer;

	
	
	
	public Level(GameScreen game, String name) {
		this.game = game;
		map = new TmxMapLoader(new InternalFileHandleResolver()).load("levels/"+name+".tmx");
		entities = new ArrayList<Entity>();
		cameraPosition = new Vector2().set(0, 0);
		
		tileRenderer = new OrthogonalTiledMapRenderer(map);
	}

	public void update(float tickdelay) {
		int s = 10;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.UP))) cameraPosition.y += s;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.DOWN))) cameraPosition.y -= s;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.LEFT))) cameraPosition.x -= s;
		if (Gdx.input.isKeyPressed(JDGame.keyBindings.get(JDGame.Keys.RIGHT))) cameraPosition.x += s;
	}
	
	public void renderEntities(SpriteBatch batch) {
		
	}
	
	public void renderTiles(OrthographicCamera camera) {
		tileRenderer.setView(camera);
		tileRenderer.render();
	}

	public Vector2 getCameraPosition() { return cameraPosition; }

}
