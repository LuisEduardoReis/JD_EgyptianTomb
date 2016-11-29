package pt.feup.jd.levels;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

public class Trigger {
	
	Level level;
	String name, targetLevel, targetSpawn;
	
	float x,y,w,h;

	public Trigger(Level level, RectangleMapObject o) {
		this.level = level;
		
		name = o.getName();
		targetLevel = o.getProperties().containsKey("targetLevel") ? (String) o.getProperties().get("targetLevel") : null;
		targetSpawn = o.getProperties().containsKey("targetSpawn") ? (String) o.getProperties().get("targetSpawn") : null;
		
		x = o.getRectangle().x;
		y = o.getRectangle().y;
		w = o.getRectangle().width;
		h = o.getRectangle().height;		
	}
	
	public void activate() {
		if (targetLevel == null && targetSpawn != null) level.gotoSpawn(targetSpawn);
	}
}
