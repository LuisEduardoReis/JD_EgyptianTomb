package pt.feup.jd.levels;

public enum Tile {
	AIR(false, false),
	SOLID(true, true),
	LADDER(false, true), 
	SPIKE(true, true)
	;
	
	public boolean solid, jumpable;
	
	Tile(boolean solid, boolean jumpable) {
		this.solid = solid;
		this.jumpable = jumpable;
	};
	
	public static Tile TILESET[] = {
/*			0		1		2		3		4		5		6		7		8		9		10		11		12		13		14		15	*/
/*00*/		SOLID,	LADDER,	SPIKE,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*01*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*02*/		SOLID,	SOLID,	SOLID,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*03*/		SOLID,	SOLID,	SOLID,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*04*/		SOLID,	SOLID,	SOLID,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*05*/		SOLID,	SOLID,	SOLID,	SOLID,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*06*/		AIR,	SPIKE,	AIR,	LADDER,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*07*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*08*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*09*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*10*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*11*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*12*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*13*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*14*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,
/*15*/		AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR,	AIR
	};
}
