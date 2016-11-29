package pt.feup.jd;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public class Util {

	public static boolean between(float x, float a, float b) {
		return x >= a && x <= b;
	}

	public static float stepTo(float a, int b, float x) {
		return Math.abs(a-b) < x ? b : a + Math.signum(b-a)*x;
	}

	public static Vector2 getMapObjectPosition(MapObject o) {
		Vector2 r = new Vector2();
		MapProperties p = o.getProperties();
		r.set((Float) p.get("x"), (Float) p.get("y"));
		r.add(((Float) p.get("width"))/2, ((Float) p.get("height")/2));
		
		return r;
	}
}
