package pt.feup.jd.levels;

public enum Tile {
	AIR(false, false),
	SOLID(true, true),
	LADDER(false, true), ;
	
	public boolean solid, jumpable;
	
	Tile(boolean solid, boolean jumpable) {
		this.solid = solid;
		this.jumpable = jumpable;
	};
}
