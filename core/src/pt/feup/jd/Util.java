package pt.feup.jd;

public class Util {

	public static boolean between(float x, float a, float b) {
		return x >= a && x <= b;
	}

	public static float stepTo(float a, int b, float x) {
		return Math.abs(a-b) < x ? b : a + Math.signum(b-a)*x;
	}
}
