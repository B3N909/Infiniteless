package me.savant.util;

import org.lwjgl.util.vector.Vector2f;

public class Vector2v extends Vector2f
{
	private static final long serialVersionUID = -6415493520311946330L;

	public Vector2v(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Vector2v))
			return false;
		Vector2v v = (Vector2v)o;
		return v.x == x && v.y == y;
	}
	
	@Override
	public int hashCode()
	{
		return (int)x + (int)y;
	}
	
}
