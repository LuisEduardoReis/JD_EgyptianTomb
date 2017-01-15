package pt.feup.jd.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;

public class HUD {
	
	GameScreen game;
	GlyphLayout layout = new GlyphLayout();
		
	public HUD(GameScreen game) {
		this.game = game;
	}	
	
	public void render() {
		int sw = game.viewport.getScreenWidth(), sh = game.viewport.getScreenHeight();
		OrthographicCamera camera = game.camera;
		SpriteBatch batch = game.batch;
		Level level = game.level;
		BitmapFont font = game.font;
		
		
		camera.position.set(sw/2,sh/2,0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(game.defaultShader);
		batch.begin();
			// Health
			float health = level.player.health;
			for(int i = 0; i < health/10; i++) batch.draw(Assets.sprites32[4][1], 16*i, sh-32);
			
			// Coins
			if (game.coins > 0) {
				batch.draw(Assets.sprites32[5][0], -16, sh-64-8, 64,64);
				font.getData().setScale(1.5f);
				layout.setText(font, ""+game.coins); 
				font.draw(batch, layout, 32, sh-64+4 + 1.5f*layout.height);
			}
			// Ammo
			if (level.player.ammo > 0) {
				batch.draw(Assets.sprites64[2][2], -8, sh-96+8, 64,64);
				font.getData().setScale(1.5f);
				layout.setText(font, ""+level.player.ammo); 
				font.draw(batch, layout, 52, sh-96+12 + 1.5f*layout.height);
			}
			// Ammo
			if (level.player.hammerHits > 0) {
				batch.draw(Assets.sprites64[2][1], -8, sh-128+8, 64,64);
				font.getData().setScale(1.5f);
				layout.setText(font, ""+level.player.hammerHits); 
				font.draw(batch, layout, 52, sh-128+12 + 1.5f*layout.height);
			}
			// Timer
			if (level.trapTimer != -1) {
				int t = (int) Math.floor(level.trapTimer);
				String timerText = String.format("%d:%02d", t / 60, t % 60);
				font.getData().setScale(3f);
				layout.setText(font, timerText);
				font.draw(batch, layout, (sw - layout.width)/2, sh-16);
			} 
			
			// Tooltip
			if (game.tooltip != null) {
				font.getData().setScale(1.5f);
				layout.setText(font, game.tooltip);
				font.draw(batch, layout, (sw - layout.width)/2, sh-64);
			}			
			// Aux
			if (JDGame.DEBUG) {
				font.getData().setScale(1f);
				font.draw(batch, "v(" + level.player.vx + "; " + level.player.vy+")",20,20);
				font.draw(batch, "p(" + level.player.x + "; " + level.player.y+")",20,40);
				font.draw(batch, game.levelChangeTimer+"",20,60);
			}
			// Fade in/out
			float f = 0;
			f += game.fadeIn.getValue();
			f += game.fadeOut.getValue();			
			batch.setColor(0, 0, 0, f);
			batch.draw(game.fillTexture,0,0);
			batch.setColor(1,1,1,1);
			
		batch.end();
	}

}
