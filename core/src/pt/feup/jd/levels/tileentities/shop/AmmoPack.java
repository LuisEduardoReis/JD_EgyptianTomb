package pt.feup.jd.levels.tileentities.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import pt.feup.jd.JDGame;
import pt.feup.jd.entities.Player;
import pt.feup.jd.levels.Level;
import pt.feup.jd.levels.tileentities.TileEntity;
import pt.feup.jd.levels.triggers.Trigger;

public class AmmoPack extends TileEntity implements Trigger {

	public int cost, ammount;
	
	public AmmoPack(Level level, String name, float x, float y) {
		super(level, name, x, y);
		
		cost = 1;
		ammount = 10;
	}
	
	@Override
	public void collide() {
		if (level.player.ammo < Player.MAX_AMMO) {
			if (level.game.coins >= cost) {
				level.game.tooltip = "Press " + Input.Keys.toString(JDGame.keyBindings.get(JDGame.Keys.USE)) + " to buy "+ammount+" bullets for " + cost + " coin"+((cost>1)?"s":"");
				
				if (Gdx.input.isKeyJustPressed(JDGame.keyBindings.get(JDGame.Keys.USE))) {
					level.game.takeCoins(cost);
					level.player.ammo += ammount;
				}
			} else {
				level.game.tooltip = "Not enough coins (" + cost + " coin"+((cost>1)?"s":"")+")";
			}
		} else {
			level.game.tooltip = "Ammo full";
		}
	}	
	
	
	
	@Override
	public float getX() { return x - JDGame.TILE_SIZE/2; }
	@Override
	public float getY() { return y - JDGame.TILE_SIZE/2; }
	@Override
	public float getW() { return JDGame.TILE_SIZE; }
	@Override
	public float getH() { return JDGame.TILE_SIZE; }

	@Override
	public boolean active() { return false; }

	@Override
	public void setActive(boolean active) {}
}
