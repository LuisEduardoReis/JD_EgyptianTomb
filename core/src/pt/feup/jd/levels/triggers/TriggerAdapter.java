package pt.feup.jd.levels.triggers;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

import pt.feup.jd.levels.Level;

public class TriggerAdapter implements Trigger {
	
	protected Level level;
	public String name, targetLevel, targetSpawn;
	boolean activateTrap;
	boolean active;
	
	protected float x,y,w,h;

	public TriggerAdapter(Level level, RectangleMapObject o) {
		this.level = level;
		
		name = o.getName();
		targetLevel = o.getProperties().containsKey("targetLevel") ? (String) o.getProperties().get("targetLevel") : null;
		targetSpawn = o.getProperties().containsKey("targetSpawn") ? (String) o.getProperties().get("targetSpawn") : null;
		
		activateTrap = o.getProperties().containsKey("activateTrap");
		
		x = o.getRectangle().x;
		y = o.getRectangle().y;
		w = o.getRectangle().width;
		h = o.getRectangle().height;
		
		active = false;
	}
	
	@Override
	public void collide() {
		if (activateTrap && level.trapDuration > 0 && level.trapTimer == -1) level.trapTimer = level.trapDuration;
	}

	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getW() { return w; }
	@Override
	public float getH() { return h; }

	@Override
	public boolean active() { return active; }

	@Override
	public void setActive(boolean active) { this.active = active; }
}
