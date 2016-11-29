package pt.feup.jd.levels;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.Util;
import pt.feup.jd.entities.Entity;
import pt.feup.jd.entities.Player;
import pt.feup.jd.entities.Snake;
import pt.feup.jd.screens.GameScreen;

public class Level {
	GameScreen game;
	
	// Logic
	public TiledMap map;
	Tile[] tiles;
	public int map_width, map_height;
	public Entity player;
	ArrayList<Entity> entities;
	ArrayList<Entity> newEntities;
	HashMap<String, Vector2> spawns;
	HashMap<String, Trigger> triggers;
	
	
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
		
		spawns = new HashMap<String, Vector2>();
		triggers = new HashMap<String, Trigger>();		
		
		entities = new ArrayList<Entity>();
		newEntities = new ArrayList<Entity>();
		cameraPosition = new Vector2().set(0, 0);
		
		createMapObjects();
		
		player = new Player(this);
		player.moveTo(spawns.get("default"));
		
		tileRenderer = new OrthogonalTiledMapRenderer(map);
	}

	private void createMapObjects() {
		MapObjects objects = map.getLayers().get("objects").getObjects();
		Vector2 pd = null;
		if (objects != null) {
			for(MapObject o : objects){
				String type = (String) o.getProperties().get("type");
				Vector2 p = Util.getMapObjectPosition(o);
				
				// Spawns
				if (type.equals("spawn")) {
					spawns.put(o.getName(), p);
					if (pd == null) pd = p;
				}
				// Triggers
				else if (type.equals("trigger")) {
					Trigger trigger = new Trigger(this, (RectangleMapObject) o);
					triggers.put(trigger.name, trigger);
				} 
				// Enemies
				else if (type.startsWith("enemy-")) {
					// Snake
					if (type.equals("enemy-snake")) new Snake(this).moveTo(p.x,p.y);
				}
			}
		}
		if (pd == null) spawns.put("default", new Vector2(0,0));
	}

	
	public void update(float delta) {
		for(Entity e : entities) e.preupdate(delta);
		for(Entity e : entities) e.update(delta);
		if (player != null) { for(Trigger t : triggers.values()) {
			if (Collision.aabbToaabb(player.x, player.y, player.hx, player.hy, t.x, t.y, t.w, t.h))
				t.activate();
		}}
		entities.addAll(newEntities);
		newEntities.clear();
		for(int i = 0; i < entities.size(); i++) 
			if (entities.get(i).remove)
				entities.remove(i);
		for(Entity e : entities) e.postupdate(delta);
		for(Entity e : entities) e.checkLevelCollision();
		for(Entity e : entities) { for(Entity o : entities) {
			if (e == o) continue;
			if (Collision.aabbToaabb(e.x, e.y, e.hx, e.hy, o.x, o.y, o.hx, o.hy)) {
				e.entityCollision(o);
			}
		}} 
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

	public void gotoSpawn(String targetSpawn) {
		if (!spawns.containsKey(targetSpawn)) gotoSpawn("default");
		if (player == null) return;
		
		player.moveTo(spawns.get(targetSpawn));
			
		
	}

	public void addEntity(Entity e) { newEntities.add(e); }
}
