package pt.feup.jd.levels.triggers;

public interface Trigger {

	void collide();
	
	void setActive(boolean active);
	boolean active();

	float getX();
	float getY();
	float getW();
	float getH();
}
