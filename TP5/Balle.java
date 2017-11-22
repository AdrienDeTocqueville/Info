package tp5;

import java.awt.Color;

public class Balle
{
	float vx, vy;
	float px, py;
	
	Color color;
	
	public void setPos(float _px, float _py)
	{
		px = _px;
		py = _py;
	}
	
	public void setVel(float _vx, float _vy)
	{
		vx = _vx;
		vy = _vy;
	}
	
	public void update(float dt)
	{
		px += vx*dt;
		py -= vy*dt;
	}
	
	public void sym(float x, float y)
	{
		double length = Math.sqrt(x*x + y*y);
		x /= length;
		y /= length;
		
		float dot = x*vx + y*vy;
		
		float x1 = dot*x;
		float y1 = dot*y;
		
		float ux = x1-vx;
		float uy = y1-vy;
		
		vx += 2*ux;
		vy += 2*uy;
	}
}
