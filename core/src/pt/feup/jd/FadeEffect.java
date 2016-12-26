package pt.feup.jd;

public class FadeEffect {
	public float t, duration;
	public boolean up, started; 
	
	public FadeEffect() {
		reset();
		duration = 1;
		up = true;
	}
	
	public void reset() {
		t = 0;
		started = false;
	}
	
	public void start() {
		started = true;		
	}
	
	public void startWithdelay(float delay) {
		t = -delay;
		started = true;
	}
	
	public void update(float delta) {
		if (started) 
			t += delta;
	}
	
	public float getValue() {
		float v = Util.clamp(t/duration, 0,1);
		return up ? v : 1-v;
	}
}
