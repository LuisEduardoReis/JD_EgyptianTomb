package pt.feup.jd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class ShopSubScreen {
	
	GameScreen game;
	
	Stage stage;
	Skin skin;

	public ShopSubScreen(GameScreen game) {
		this.game = game;
		stage = new Stage(game.viewport);
		skin = new Skin();
		
		Gdx.input.setInputProcessor(stage);
		
		Pixmap pixmap = new Pixmap(100, 30, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		
		skin.add("default", new BitmapFont());
		
		TextButtonStyle bstyle = new TextButtonStyle();
		bstyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		bstyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		bstyle.checked = skin.newDrawable("white", Color.WHITE);
		bstyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		bstyle.font = skin.getFont("default");
		skin.add("default", bstyle);
		
		LabelStyle lstyle = new LabelStyle();
		lstyle.font = skin.getFont("default");
		skin.add("default", lstyle);
		
		Table table = new Table();
			Label label = new Label("test:", skin);
			TextButton button = new TextButton("test", bstyle);
		
			table.add(label).padRight(10f);
			table.add(button).padBottom(10f);
			table.row();
		table.setFillParent(true);
		stage.addActor(table);
	}
	
	void render(float delta) {
		stage.act(Math.min(delta, 1/30f));
		stage.draw();
	}
	
	
}
