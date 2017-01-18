package pt.feup.jd;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.entities.Entity;

public class Util {

	public static Random random = new Random();
	public static float degToRad = (float) (Math.PI / 180);
	public static float radToDeg = (float) (180 / Math.PI);
	
	
	public static boolean between(float x, float a, float b) {
		return x >= a && x <= b;
	}

	public static float stepTo(float a, float b, float x) {
		return Math.abs(a-b) < x ? b : a + Math.signum(b-a)*x;
	}

	public static Vector2 getMapObjectPosition(MapObject o) {
		Vector2 r = new Vector2();
		MapProperties p = o.getProperties();
		r.set((Float) p.get("x"), (Float) p.get("y"));
		r.add(((Float) p.get("width"))/2, ((Float) p.get("height")/2));
		
		return r;
	}
	
	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
	}

	public static float randomRange(float min, float max) {
		return min + random.nextFloat()*(max - min);
	}

	public static float clamp(float x, int a, int b) {
		return Math.max(a,Math.min(x, b));
	}
	
	public static void drawLight(Batch batch, float x, float y, float w, float h, float r, float g, float b, float a) {
		w *= JDGame.TILE_SIZE;
		h *= JDGame.TILE_SIZE;
		batch.setColor(r,g,b,a);
		batch.draw(Assets.light, x - w/2, y - h/2, w,h);
		batch.setColor(1, 1, 1, 1);
	}

	public static float pointDistance(float x, float y, float x2, float y2) {
		x -= x2; y -= y2;
		return (float) Math.sqrt(x*x + y*y);
	}

	public static float pointDistance(Entity a, Entity b) {
		return Util.pointDistance(a.x,a.y,b.x,b.y);
	}

	public static float pointDirection(float x, float y, float x2, float y2) {
		return (float) Math.atan2(y2-y,x2-x)*radToDeg;
	}

	public static void playSound(Sound sound) {
		if (JDGame.sound_vol > 0) sound.play(JDGame.sound_vol);		
	}
}
