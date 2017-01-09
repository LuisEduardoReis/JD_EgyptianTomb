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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
	JDGame game;
	public GameScreen(JDGame game) {
		this.game = game;
	}
	
	// Logic
	Level level;
	HashMap<String, Level> levels;
	
	float levelChangeTimer, levelChangeDelay;
	
	// Render
	OrthographicCamera camera;
	public Viewport viewport;
	
	BitmapFont font;
	public SpriteBatch batch;
	
	ShapeRenderer shapeRenderer;
	
	public boolean showDoorTooltip;
	public boolean showLeverTooltip;
	
	Texture fillTexture;
	
	ShaderProgram defaultShader;
	ShaderProgram shader;
	
	FrameBuffer fbo;
	
	FadeEffect fadeIn, fadeOut;

	
	@Override
	public void show() {
		
		// Logic
		levels = new HashMap<String, Level>();
					
		levelChangeTimer = -1;
		levelChangeDelay = 1f;
		
		fadeIn = new FadeEffect();
		fadeIn.duration = 1.5f;
		fadeIn.up = false;
		
		fadeOut = new FadeEffect();
		fadeOut.duration = 2f;
		
		
		// Render
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
				
		shapeRenderer = new ShapeRenderer();
		
		ShaderProgram.pedantic = false;
		defaultShader = new ShaderProgram(Assets.vertexShader, Assets.defaultFragmentShader);
		shader = new ShaderProgram(Assets.vertexShader, Assets.fragmentShader);
		
				
		// Start
		gotoLevel("testing", null);
		fadeIn();
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
		
		if (level != null) {
			level.gotoLevel(null, null);
			
			if (level.name.equals(name)) {
				level.gotoSpawn(spawn);
				return;
			}
			
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
	}
	
	float accum = 0;
	GlyphLayout layout = new GlyphLayout();
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
		int sw = viewport.getScreenWidth(), sh = viewport.getScreenHeight();
		camera.position.set(sw/2,sh/2,0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(defaultShader);
		batch.begin();
			// Health
			float health = level.player.health;
			for(int i = 0; i < health/10; i++) batch.draw(Assets.sprites32[4][1], 16*i, sh-32);
			
			// Timer
			if (level.trapTimer != -1) {
				int t = (int) Math.floor(level.trapTimer);
				String timerText = String.format("%d:%02d", t / 60, t % 60);
				font.getData().setScale(3f);
				layout.setText(font, timerText);
				font.draw(batch, layout, (sw - layout.width)/2, sh-16);
			} 
			
			// Door Tooltip
			if (showDoorTooltip) {
				font.getData().setScale(1.5f);
				layout.setText(font, "Press "+Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE))+" to open door.");
				font.draw(batch, layout, (sw - layout.width)/2, sh-64);
			}
			// Lever Tooltip
			if (showLeverTooltip) {
				font.getData().setScale(1.5f);
				layout.setText(font, "Press "+Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE))+" to pull lever.");
				font.draw(batch, layout, (sw - layout.width)/2, sh-64);
			}
			// Aux
			if (JDGame.DEBUG) {
				font.getData().setScale(1f);
				font.draw(batch, "v(" + level.player.vx + "; " + level.player.vy+")",20,20);
				font.draw(batch, "p(" + level.player.x + "; " + level.player.y+")",20,40);
				font.draw(batch, levelChangeTimer+"",20,60);
			}
			// Fade in/out
			float f = 0;
			f += fadeIn.getValue();
			f += fadeOut.getValue();			
			batch.setColor(0, 0, 0, f);
			batch.draw(fillTexture,0,0);
			batch.setColor(1,1,1,1);
			
		batch.end();
	
	}

	private void logic(float delta) {
		// Logic
		if (levelChangeTimer < 0) {
		
			while(accum > tickdelay) {
				showDoorTooltip = false;
				showLeverTooltip = false;
				
				level.update(tickdelay);
				accum -= tickdelay;
			}
			
			if (level.levelChange) {
				levelChangeTimer = fadeOut.duration;
				fadeOut.start();
			}
			
		} else {
			levelChangeTimer = Util.stepTo(levelChangeTimer, 0, delta);
			if (levelChangeTimer == 0) {
				levelChangeTimer = -1;
				gotoLevel(level.targetLevel, level.targetSpawn);
				fadeIn();
			}
		}	
		
		fadeIn.update(delta);
		fadeOut.update(delta);
		
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) JDGame.DEBUG ^= true;
	}
	
	
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
}
