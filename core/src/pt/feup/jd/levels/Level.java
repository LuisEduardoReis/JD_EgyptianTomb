package pt.feup.jd.levels;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.entities.Enemy;
import pt.feup.jd.entities.Entity;
import pt.feup.jd.entities.Player;
import pt.feup.jd.entities.Snake;
import pt.feup.jd.levels.tileentities.FireballTrap;
import pt.feup.jd.levels.tileentities.Gate;
import pt.feup.jd.levels.tileentities.Lever;
import pt.feup.jd.levels.tileentities.TileEntity;
import pt.feup.jd.levels.tileentities.shop.HealthPack;
import pt.feup.jd.levels.triggers.Door;
import pt.feup.jd.levels.triggers.Trigger;
import pt.feup.jd.levels.triggers.TriggerAdapter;
import pt.feup.jd.screens.GameScreen;

public class Level {
	public GameScreen game;
	
	// Logic
	public String name;
	public TiledMap map;
	Tile[] tiles;
	public int map_width, map_height;
	public Player player;
	public ArrayList<Entity> entities;
	ArrayList<Entity> newEntities;
	HashMap<String, Vector2> spawns;
	public HashMap<String, Trigger> triggers;
	public HashMap<String, TileEntity> tileEntities;
	
	Vector2 cameraPosition;
	
	public float trapTimer, trapDuration;
	
	// Level switching
	public boolean persistent;
	
	public boolean levelChange;
	public String targetLevel, targetSpawn;
	
	// Render
	float rumble;
	public OrthogonalTiledMapRenderer tileRenderer;


		
	public Level(GameScreen game, String name) {
		this.game = game;
		this.name = name;
		map = new TmxMapLoader(new InternalFileHandleResolver()).load("levels/"+name+".tmx");
		
		map_width = (Integer) map.getProperties().get("width");
		map_height = (Integer) map.getProperties().get("height");
		tiles = new Tile[map_width*map_height];
		
		for(int i = 0; i < tiles.length; i++) tiles[i] = Tile.AIR;
		
		TiledMapTileLayer tiled_tiles = (TiledMapTileLayer) map.getLayers().get("main"); 
		for(int yy = 0; yy<map_height; yy++) {
			for(int xx = 0; xx<map_width; xx++) {
				TiledMapTileLayer.Cell cell = tiled_tiles.getCell(xx, yy);
				/*System.out.print("\t"+((cell == null) ? " " : 
					"("+((cell.getTile().getId()-1) % 16)+", "+(cell.getTile().getId()-1)/16+")" 
						));*/
				if (cell != null) {
					int cid = cell.getTile().getId()-1;
					tiles[yy*map_width+xx] = (cid >= 0 && cid < Tile.TILESET.length) ? Tile.TILESET[cell.getTile().getId()-1] : Tile.AIR;
				}
			}
			//System.out.println();	System.out.println();
		}
		
		spawns = new HashMap<String, Vector2>();
		triggers = new HashMap<String, Trigger>();
		
		levelChange = false;
		targetLevel = null; targetSpawn = null;	
		
		persistent = !(map.getProperties().containsKey("volatile"));
		
		entities = new ArrayList<Entity>();
		tileEntities = new HashMap<String, TileEntity>();
		newEntities = new ArrayList<Entity>();
		cameraPosition = new Vector2().set(0, 0);
		
		createMapObjects();
		
		trapTimer = -1;
		trapDuration = 0;
		if (map.getProperties().containsKey("trapDelay")) 
			trapDuration = Float.parseFloat((String) map.getProperties().get("trapDelay"));
		
		tileRenderer = new OrthogonalTiledMapRenderer(map, game.batch);
		rumble = 0;
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
					TriggerAdapter trigger = new TriggerAdapter(this, (RectangleMapObject) o);
					triggers.put(trigger.name, trigger);
				}
				// Doors
				else if (type.equals("door")) {
					Door door = new Door(this, (RectangleMapObject) o);
					spawns.put(door.name, p);
					if (pd == null) pd = p;
					triggers.put(door.name, door);
				}
				// Gates
				else if (type.equals("gate")) {
					Gate gate = new Gate(this, o.getName(), p.x,p.y);
					if (o.getProperties().containsKey("lever")) 
						gate.lever = (String) o.getProperties().get("lever");
					gate.inverted = o.getProperties().containsKey("inverted");
					tileEntities.put(gate.name, gate);
				}
				// Lever
				else if (type.equals("lever")) {
					Lever lever = new Lever(this, o.getName(), p.x,p.y);
					tileEntities.put(lever.name, lever);
					triggers.put(lever.name, lever);
				}
				// Fireball Trap
				else if (type.equals("fireballtrap")) {
					FireballTrap trap = new FireballTrap(this, o.getName(), p.x, p.y);
					if (o.getProperties().containsKey("rotation")) trap.rotation =  Float.parseFloat((String) o.getProperties().get("rotation"));
					if (o.getProperties().containsKey("delay")) trap.fireDelay =  Float.parseFloat((String) o.getProperties().get("delay"));
					if (o.getProperties().containsKey("speed")) trap.fireSpeed =  Float.parseFloat((String) o.getProperties().get("speed"));
					if (o.getProperties().containsKey("trigger")) trap.trigger =  ((String) o.getProperties().get("trigger"));
					tileEntities.put(trap.name, trap);
				}
				// Enemies
				else if (type.startsWith("enemy-")) {
					Enemy e = null;
					// Snake
					if (type.equals("enemy-snake")) e = (Snake) new Snake(this).moveTo(p.x,p.y);
					
					
					// Common Properties
					if (e != null) {
						if (o.getProperties().containsKey("range")) e.range = Float.parseFloat((String) o.getProperties().get("range"));
					}
				}
				// Shop
				else if (type.startsWith("shop-")) {
					// Health Pack
					if (type.equals("shop-health")) {
						HealthPack health = new HealthPack(this, o.getName(), p.x, p.y);
						tileEntities.put(health.name, health);
						triggers.put(health.name, health);
					}					
					
				}
			}
		}
		if (!spawns.containsKey("default"))
			spawns.put("default", (pd == null) ? new Vector2(0,0) : pd);
		
	}

	
	public void update(float delta) {		
		// Preupdate
		for(Entity e : entities) e.preupdate(delta);
		rumble = 0;
		
		// Update
		for(TileEntity e : tileEntities.values()) e.update(delta);
		for(Entity e : entities) e.update(delta);
		
		// Triggers
		if (player != null) { for(Trigger t : triggers.values()) {
			if (Collision.aabbToaabb(player.x, player.y, player.hx, player.hy, t.getX(), t.getY(), t.getW(), t.getH())) {
				t.collide();
				t.setActive(true);
			} else
				t.setActive(false);
		}}
		
		// Entity collisions
		for(Entity e : entities) { for(Entity o : entities) {
			if (e == o) continue;
			if (Collision.aabbToaabb(e.x-e.hx/2, e.y-e.hy/2, e.hx, e.hy, o.x-o.hx/2, o.y-o.hy/2, o.hx, o.hy)) {
				e.entityCollision(o);
			}
		}}
		
		// Misc & Timers
		if (trapTimer > 0) {
			trapTimer = Util.stepTo(trapTimer, 0, delta);
			rumble += 2 + 3*(Math.sin(trapTimer)+1);
		}
		if (trapTimer == 0 && player != null) {
			player.damage(player.health);
			game.fadeOut();
			trapTimer = -1;
		}
		
		// New entities
		entities.addAll(newEntities);
		newEntities.clear();
		
		// Remove old entities
		for(int i = 0; i < entities.size(); i++) 
			if (entities.get(i).remove)
				entities.remove(i).destroy();
		
		// Post update
		for(Entity e : entities) e.postupdate(delta);
		
		// Level collisions
		for(Entity e : entities) e.checkLevelCollision(); 
		
		// Camera position
		if (player != null) cameraPosition.set((int) player.x + Util.randomRange(-rumble, rumble), (int) player.y + Util.randomRange(-rumble, rumble));
	}


	public void renderEntities(SpriteBatch batch) {
		for(TileEntity e : tileEntities.values()) e.render(batch);
		for(Entity e : entities) e.render(batch);
	}
		
	public void renderDebug(ShapeRenderer shapeRenderer) {
		for(Entity e : entities) e.renderDebug(shapeRenderer);
		
		shapeRenderer.setColor(Color.RED);
		for(int yy = 0; yy<map_height; yy++) {
			for(int xx = 0; xx<map_width; xx++) {
				if (!tiles[yy*map_width+xx].solid) continue;
				
				shapeRenderer.rect(xx*JDGame.TILE_SIZE, yy*JDGame.TILE_SIZE,JDGame.TILE_SIZE,JDGame.TILE_SIZE);
			}
		}
		
		shapeRenderer.setColor(Color.BLUE);
		for(Trigger t : triggers.values()) {
			shapeRenderer.rect(t.getX(), t.getY(), t.getW(), t.getH());
		}
	}

	public Vector2 getCameraPosition() { return cameraPosition; }

	public Tile getTile(int x, int y) {
		return (x < 0 || y < 0 || x >= map_width || y >= map_height) ? Tile.AIR : tiles[y*map_width+x];
	}
	public void setTile(int x, int y, Tile tile) {
		if (x >= 0 && y >= 0 && x < map_width && y < map_height) tiles[y*map_width+x] = tile;	
	}

	public void addEntity(Entity e) { newEntities.add(e); }
	
	public void gotoSpawn(String targetSpawn) {
		if (player == null) return;
		if (!spawns.containsKey(targetSpawn)) {
			gotoSpawn("default"); 
			return;
		}	
		
		player.moveTo(spawns.get(targetSpawn));
	}

	public void gotoLevel(String tl, String ts) {
		if (tl == null) {
			levelChange = false;
			targetLevel = null;	targetSpawn = null;
		} else {
			levelChange = true;
			targetLevel = tl; targetSpawn = ts;
		}
		
	}

	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void renderLight(SpriteBatch batch) {
		for(Entity e : entities) e.renderLight(batch);		
	}


}
