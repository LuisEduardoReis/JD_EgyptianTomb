package pt.feup.jd.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pt.feup.jd.Assets;
import pt.feup.jd.FadeEffect;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.entities.Player;
import pt.feup.jd.levels.Level;

public class GameScreen extends ScreenAdapter {
	public final JDGame game;
	public GameScreen(JDGame game) {
		this.game = game;
	}
	
	// Logic
	Level level;
	HashMap<String, Level> levels;
	boolean paused;
	
	public int coins;
	
	float levelChangeTimer, levelChangeDelay;
	float gameOverTimer, gameOverDelay;
	
	// Render
	OrthographicCamera camera;
	public Viewport viewport;
	
	public SpriteBatch batch;
	
	ShapeRenderer shapeRenderer;
	
	public String tooltip;
	
	Texture fillTexture;
	
	ShaderProgram defaultShader;
	ShaderProgram shader;
	
	FrameBuffer fbo;
	
	FadeEffect fadeIn, fadeOut;
	
	HUD hud;
	
	private String checkpointLevel;
	private float checkpointHealth;
	private int checkpointCoins;
	private int checkpointAmmo;
	private int checkpointHammer;
	private float checkpointX;
	private float checkpointY;
	
	@Override
	public void show() {
		
		// Logic
		levels = new HashMap<String, Level>();
					
		levelChangeTimer = -1;
		levelChangeDelay = 1f;
		gameOverTimer = -1;
		gameOverDelay = 3f;
		
		fadeIn = new FadeEffect();
		fadeIn.duration = 1.5f;
		fadeIn.up = false;
		
		fadeOut = new FadeEffect();
		fadeOut.duration = 2f;
		
		paused = false;
		
		coins = 0;
		
		// Render
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		
		batch = new SpriteBatch();
				
		shapeRenderer = new ShapeRenderer();
		
		ShaderProgram.pedantic = false;
		defaultShader = new ShaderProgram(Assets.vertexShader, Assets.defaultFragmentShader);
		shader = new ShaderProgram(Assets.vertexShader, Assets.fragmentShader);
		
		hud = new HUD(this);
					
		// Start
		restart("main");
		fadeIn();
	}
	
	private void restart(String string) {
		levels.clear();
		level = null;
		gotoLevel(string, null); 
	}

	public void fadeIn() {		
		fadeIn.reset();
		fadeIn.start();
		
		fadeOut.reset();		
	}
	
	public void fadeOut() {		
		fadeIn.reset();
			
		fadeOut.reset();
		fadeOut.start();
	}
	
	private void gotoLevel(String name, String spawn) {
		Player player = null;
		boolean comingFromCheckpoint = false;
		
		if (name.equals("controls")) {
			game.setScreen(new SettingsScreen(game));
			return;
		}
		if (name.equals("exit")) {
			Gdx.app.exit();
			return;
		}
		
		if (level != null) {
			level.gotoLevel(null, null);
			
			if (level.name.equals(name)) {
				level.gotoSpawn(spawn);
				return;
			}
			
			comingFromCheckpoint = level.checkpoint;
			player = level.player;
			level.entities.remove(player);
			
			if (!level.persistent) {
				levels.remove(level.name);
				level.destroy();	
			}			
		}
			
		if (levels.containsKey(name))
			level = levels.get(name);
		else {
			level = new Level(this, name);
			levels.put(name, level);
		}
		
		if (player == null) player = new Player(level);
		else level.addEntity(player);
		level.player = player;
		player.level = level;
			
		level.gotoLevel(null, null);
		level.gotoSpawn(spawn);
		
		if (level.checkpoint && comingFromCheckpoint) saveCheckpoint();
	}
	
	private void saveCheckpoint() {
		checkpointLevel = level.name;
		checkpointX = level.player.x;
		checkpointY = level.player.y;
		checkpointHealth = level.player.health;
		checkpointCoins = coins;
		checkpointAmmo = level.player.ammo;
		checkpointHammer = level.player.hammerHits;		
	}

	private void loadCheckpoint() {
		level = null;
		levels.remove(checkpointLevel);
		gotoLevel(checkpointLevel, null);
		level.player.moveTo(checkpointX, checkpointY);
		level.player.dead = false;
		level.player.health = checkpointHealth;
		level.player.ammo = checkpointAmmo;
		level.player.hammerHits = checkpointHammer;
		coins = checkpointCoins;
	}
	
	float accum = 0;
	public static final float tickdelay = 1/60f;
	@Override
	public void render(float delta) {
		accum += delta;

		logic(delta);
				
		// Render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		viewport.apply();
		
		camera.position.set(level.getCameraPosition(),0);
		camera.update();
		
		shader.begin();
		shader.setUniformi("u_lightmap", 1);
		shader.setUniformf("ambientColor", 1, 1, 1, 0.1f);
		shader.end();
		
		batch.setProjectionMatrix(camera.combined);
		
		// Lighting
		fbo.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.setShader(defaultShader);
		batch.begin(); 
			level.renderLight(batch);
		batch.end();		
		fbo.end();
		
		// Lighted entities
			fbo.getColorBufferTexture().bind(1);
			Assets.light.bind(0);
			batch.setShader(shader);
			
			// Tiles		
			level.tileRenderer.setView(camera);
			level.tileRenderer.render();
			
			// Entities
			batch.begin(); 
				level.renderEntities(batch);
			batch.end();
		
		
		//Debug
		if (JDGame.DEBUG) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);
				level.renderDebug(shapeRenderer);
			shapeRenderer.end();
		}
		
		// HUD
		hud.render();
	
	}

	private void logic(float delta) {
		// Logic
		if (levelChangeTimer < 0 && gameOverTimer < 0 && !paused) {
		
			if(accum > tickdelay) {
				tooltip = null;
				
				level.update(tickdelay);
				accum -= tickdelay;
			}
			
			if (level.levelChange) {
				levelChangeTimer = fadeOut.duration;
				fadeOut.start();
			}
			
		} 
		if (levelChangeTimer >= 0) {
			levelChangeTimer = Util.stepTo(levelChangeTimer, 0, delta);
			if (levelChangeTimer == 0) {
				levelChangeTimer = -1;
				gotoLevel(level.targetLevel, level.targetSpawn);
				fadeIn();
			}
		}	
		if (gameOverTimer >= 0) {
			gameOverTimer = Util.stepTo(gameOverTimer, 0, delta);
			if (gameOverTimer == 0) {
				gameOverTimer = -1;
				loadCheckpoint();
				fadeIn();
			}
		}
		
		fadeIn.update(delta);
		fadeOut.update(delta);
				
		
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) gotoLevel("testing", null);
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) gotoLevel("testing2", null);
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) gotoLevel("testing3", null);
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) gotoLevel("testing4", null);
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) gotoLevel("testing_escape", null);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) JDGame.DEBUG ^= true;
	}
	
	public void addCoins(int v) {coins += v;}
	public void takeCoins(int v) { coins -= v;}
	
	@Override
	public void resize(int width, int height) {
		if (fillTexture != null) fillTexture.dispose();
		
		Pixmap p = new Pixmap(width, height, Format.RGB888);
		p.setColor(1, 1, 1, 1); p.fill();
		fillTexture = new Texture(p);
		p.dispose();

		if (fbo != null) fbo.dispose();
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
	
		shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();
		
		viewport.update(width, height);
	}

	public void gameOver() {
		if (gameOverTimer > 0) return;
		fadeOut();
		gameOverTimer = gameOverDelay;
	}

	
}
