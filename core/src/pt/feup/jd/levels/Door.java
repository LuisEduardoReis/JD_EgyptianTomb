package pt.feup.jd.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import pt.feup.jd.JDGame;

public class Door extends TriggerAdapter {

	public Door(Level level, RectangleMapObject o) {
		super(level, o);
	}

	@Override
	public void collide() {
		if (targetLevel != null || targetSpawn != null)
			level.game.showDoorTooltip = true;
		
		if (Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.USE))) {
			if (targetLevel == null && targetSpawn != null) level.gotoLevel(level.name, targetSpawn);
			if (targetLevel != null) level.gotoLevel(targetLevel, targetSpawn);
		};
	}
	
	

}
