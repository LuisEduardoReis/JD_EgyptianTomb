package pt.feup.jd.levels.triggers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import pt.feup.jd.Assets;
import pt.feup.jd.JDGame;
import pt.feup.jd.Util;
import pt.feup.jd.levels.Level;

public class Door extends TriggerAdapter {

	public Door(Level level, RectangleMapObject o) {
		super(level, o);
	}

	@Override
	public void collide() {
		if (targetLevel != null || targetSpawn != null)
			level.game.tooltip = "Press "+Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE))+" to open door.";
		
		if (Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.USE))) {
			Util.playSound(Assets.open_door);
			if (targetLevel == null && targetSpawn != null) level.gotoLevel(level.name, targetSpawn);
			if (targetLevel != null) level.gotoLevel(targetLevel, targetSpawn);
		};
	}
	
	

}
