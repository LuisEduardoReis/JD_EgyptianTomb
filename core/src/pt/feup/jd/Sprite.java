package pt.feup.jd;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;

public class Sprite {
	
	public ArrayList<TextureRegion> frames;
		
	public float anim_delay; 
	public boolean anim_loop;	
	
	public Sprite() {
		frames = new ArrayList<TextureRegion>();
		
		anim_loop = true;
		anim_delay = -1;
	}
	
	public void addFrame(TextureRegion frame) { frames.add(frame); }
	
	
	static Affine2 t = new Affine2();
	static Color color = new Color();
	public void render(SpriteBatch batch,int anim_index, float x, float y, float scaleX, float scaleY, Color color) {
		
		int ts = JDGame.TILE_SIZE;
		
		TextureRegion s = frames.get(anim_index);
		
		t.idt();
		t.translate((int) x, (int) y);
		t.scale(scaleX, scaleY);
		t.translate(-ts/2, -ts/2);
	
		
		color.set(color);
		batch.setColor(color);
		batch.draw(s, ts, ts, t);
		batch.setColor(Color.WHITE);
	}
}