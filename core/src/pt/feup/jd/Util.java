package pt.feup.jd;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public class Util {

	public static Random random = new Random();
	
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
}
