package pt.feup.jd.levels.triggers;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

import pt.feup.jd.levels.Level;

public class Sign extends TriggerAdapter {

	public Sign(Level level, RectangleMapObject o) {
		super(level, o);
	}
	
	@Override
	public void collide() {
		super.collide();
		
		level.game.tooltip = this.name;
	}

}
