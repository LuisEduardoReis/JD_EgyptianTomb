package pt.feup.jd.levels.tileentities.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import pt.feup.jd.JDGame;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.tileentities.TileEntity;
import pt.feup.jd.levels.triggers.Trigger;

public class HealthPack extends TileEntity implements Trigger {

	public int cost, heal;
	
	public HealthPack(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		cost = 1;
		heal = 25;
	}
	
	@Override
	public void collide() {
		if (level.player.health < level.player.max_health) {
			if (level.game.coins >= cost) {
				level.game.tooltip = "Press " + Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE)) + " to buy "+heal+" health for " + cost + " coin"+((cost>1)?"s":"");
				
				if (Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.USE))) {
					level.game.takeCoins(cost);
					level.player.heal(heal);
				}
			} else {
				level.game.tooltip = "Not enough coins (" + cost + " coin"+((cost>1)?"s":"")+")";
			}
		} else {
			level.game.tooltip = "Full health";
		}
	}	
	
	
	
	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getW() { return JDGame.TILE_SIZE; }
	@Override
	public float getH() { return JDGame.TILE_SIZE; }

	@Override
	public boolean active() { return false; }

	@Override
	public void setActive(boolean active) {}
}
