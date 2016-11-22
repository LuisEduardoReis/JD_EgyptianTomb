package pt.feup.jd.levels;

public enum Tile {
	AIR(false),
	SOLID(true);
	
	public boolean solid;
	
	Tile(boolean solid) {
		this.solid = solid;
	};
}
