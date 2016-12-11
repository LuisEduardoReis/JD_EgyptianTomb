package pt.feup.jd.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
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
	
	// Render
	OrthographicCamera camera;
	Viewport viewport;
	
	BitmapFont font;
	SpriteBatch batch;
	
	ShapeRenderer shapeRenderer;

	
	@Override
	public void show() {
		
		// Logic
		levels = new HashMap<String, Level>();
		gotoLevel("testing", null);	
	
		// Render
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();
	}
	
	private void gotoLevel(String name, String spawn) {
		Player player = null;
		// TODO if player goes to same level
		
		if (level != null) {
			player = level.player;
			level.entities.remove(player);
			
			if (!level.persistent) {
				levels.remove(level.name);
				level.destroy();	
			}
			
			level.gotoLevel(null, null);
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

		// Logic
		while(accum > tickdelay) {
			level.update(tickdelay);
			accum -= tickdelay;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) JDGame.DEBUG ^= true;
		
		// Level change
		if (level.levelChange) gotoLevel(level.targetLevel, level.targetSpawn);		
		
		// Render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		viewport.apply();
		
		camera.position.set(level.getCameraPosition(),0);
		camera.update();	
		
		// Tiles
		level.renderTiles(camera);
		
		// Entities
		batch.setProjectionMatrix(camera.combined);
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
				font.draw(batch, layout, (sw - layout.width)/2, sh-32);
			} 
			
			// Aux
			font.getData().setScale(1f);
			font.draw(batch, "v(" + level.player.vx + "; " + level.player.vy+")",20,20);
			font.draw(batch, "p(" + level.player.x + "; " + level.player.y+")",20,40);
		batch.end();
	
	}
	
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
