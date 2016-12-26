package pt.feup.jd.levels;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

public class Trigger {
	
	Level level;
	String name, targetLevel, targetSpawn;
	boolean activateTrap;
	
	float x,y,w,h;

	public Trigger(Level level, RectangleMapObject o) {
		this.level = level;
		
		name = o.getName();
		targetLevel = o.getProperties().containsKey("targetLevel") ? (String) o.getProperties().get("targetLevel") : null;
		targetSpawn = o.getProperties().containsKey("targetSpawn") ? (String) o.getProperties().get("targetSpawn") : null;
		
		activateTrap = o.getProperties().containsKey("activateTrap");
		
		x = o.getRectangle().x;
		y = o.getRectangle().y;
		w = o.getRectangle().width;
		h = o.getRectangle().height;
	}
	
	public void activate() {
		if (activateTrap && level.trapDuration > 0 && level.trapTimer == -1) level.trapTimer = level.trapDuration;
	}
}
