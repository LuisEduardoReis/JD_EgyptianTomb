package pt.feup.jd.levels;

import java.util.ArrayList;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.entities.Entity;
import pt.feup.jd.entities.Player;
import pt.feup.jd.screens.GameScreen;

public class Level {
	GameScreen game;
	
	// Logic
	public TiledMap map;
	Tile[] tiles;
	int map_width, map_height;
	Player player;
	ArrayList<Entity> entities;
	
	Vector2 cameraPosition;
	
	// Render
	OrthogonalTiledMapRenderer tileRenderer;

		
	public Level(GameScreen game, String name) {
		this.game = game;
		map = new TmxMapLoader(new InternalFileHandleResolver()).load("levels/"+name+".tmx");
		
		map_width = (Integer) map.getProperties().get("width");
		map_height = (Integer) map.getProperties().get("height");
		tiles = new Tile[map_width*map_height];
		for(int i = 0; i < tiles.length; i++) tiles[i] = Tile.AIR;
		TiledMapTileLayer tiled_tiles = (TiledMapTileLayer) map.getLayers().get("solid"); 
		for(int yy = 0; yy<map_height; yy++) {
			for(int xx = 0; xx<map_width; xx++) {
				TiledMapTileLayer.Cell cell = tiled_tiles.getCell(xx, yy);
				if (cell != null)
					tiles[yy*map_width+xx] = Tile.valueOf((String) cell.getTile().getProperties().get("type"));
			}
		}
		
		entities = new ArrayList<Entity>();
		cameraPosition = new Vector2().set(0, 0);
		
		player = new Player(this);
		entities.add(player);
		
		tileRenderer = new OrthogonalTiledMapRenderer(map);
	}

	public void update(float delta) {
		for(Entity e : entities) e.update(delta);
		for(Entity e : entities) e.levelCollision();
		if (player != null) cameraPosition.set((int) player.x, (int) player.y);
	}


	public void renderEntities(SpriteBatch batch) {
		for(Entity e : entities) e.render(batch);
	}
	
	public void renderTiles(OrthographicCamera camera) {
		tileRenderer.setView(camera);
		tileRenderer.render();
	}

	public Vector2 getCameraPosition() { return cameraPosition; }

	public Tile getTile(int x, int y) {
		return (x < 0 || y < 0 || x >= map_width || y >= map_height) ? Tile.SOLID : tiles[y*map_width+x];
	}
}
