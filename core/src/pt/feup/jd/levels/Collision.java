package pt.feup.jd.levels;

import com.badlogic.gdx.math.Vector2;

import pt.feup.jd.Util;

public class Collision {

	public static float sweepAABB(
			float ax, float ay, float ahx, float ahy,
			float bx, float by, float bhx, float bhy,
			float dx, float dy,
			Vector2 n
			) {
		float mx,my,mhx,mhy;
		mx = bx - (ax + ahx);
		my = by - (ay + ahy);
		mhx = ahx + bhx;
		mhy = ahy + bhy;
		
		float h=1,s,nx=0,ny=0;
		//X min
	    s = Collision.lineToPlane(0,0, dx,dy, mx,my, -1,0);
	    if (s >= 0 && dx > 0 && s < h && Util.between(s*dy,my,my+mhy)) 
	        {h = s; nx = -1; ny = 0;} 
		
	    // X max
	    s = Collision.lineToPlane(0,0, dx,dy, mx+mhx,my, 1,0);
	    if (s >= 0 && dx < 0 && s < h && Util.between(s*dy,my,my+mhy))
	        {h = s; nx =  1; ny = 0;}
	    
	    // Y min
	    s = Collision.lineToPlane(0,0, dx,dy, mx,my, 0,-1);
	    if (s >= 0 && dy > 0 && s < h && Util.between(s*dx,mx,mx+mhx))
	        {h = s; nx = 0; ny = -1;} 
		
	    // Y max
	    s = Collision.lineToPlane(0,0, dx,dy, mx,my+mhy, 0,1);
	    if (s >= 0 && dy < 0 && s < h && Util.between(s*dx,mx,mx+mhx))
	        {h = s; nx = 0; ny =  1;} 
		
	    if (n != null) {
	    	n.x = nx;
	    	n.y = ny;
	    }
		return h;
	}
	
	public static float lineToPlane(
			float px, float py,
			float ux, float uy,
			float vx, float vy, 
			float nx, float ny) {
	    float NdotU = nx*ux + ny*uy;
	    if (NdotU == 0) return Float.MAX_VALUE;

	    // return n.(v-p) / n.u
	    return (nx*(vx-px) + ny*(vy-py)) / NdotU;
	}
	
	public static boolean aabbToaabb(
			float x1, float y1, float w1, float h1,  
			float x2, float y2, float w2, float h2) {
		float x = x1 - (x2+w2);
		float y = y1 - (y2+h2);
		float w = w1 + w2;
		float h = h1 + h2;
		
		return x <= 0 && y <= 0 && x+w >= 0 && y+h >= 0;
	}
}

