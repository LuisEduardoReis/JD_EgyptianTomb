package pt.feup.jd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;

public class GameScreen extends ScreenAdapter {
	JDGame game;
	public GameScreen(JDGame game) {
		this.game = game;
	}
	
	// Logic
	Level level;
	
	// Render
	OrthographicCamera camera;
	Viewport viewport;
	
	BitmapFont font;
	SpriteBatch batch;
	
	@Override
	public void show() {
		
		// Logic
		
		level = new Level(this, "testing");
	
		// Render
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
	}
	
	float accum = 0;
	public static final float tickdelay = 1/60f;
	@Override
	public void render(float delta) {
		accum += delta;

		// Logic
		while(accum > tickdelay) {
			level.update(tickdelay);
			accum -= tickdelay;
		}
		
		// Render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		viewport.apply();
		
		camera.position.set(level.getCameraPosition(),0);
		camera.update();
		
		level.renderTiles(camera);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin(); 
			level.renderEntities(batch);
			//batch.draw(Assets.testImage,0,0);
		batch.end();
		
		camera.position.set(viewport.getScreenWidth()/2,viewport.getScreenHeight()/2,0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			font.draw(batch, "vx:" + level.player.vx + "; vy:" + level.player.vy,20,20);
		batch.end();
	}
	
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
