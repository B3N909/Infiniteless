package me.savant.infinite;

import org.lwjgl.util.vector.Vector3f;

import me.savant.infinite.render.TexturedModel;

public class Entity
{
	private TexturedModel model;
	private Vector3f position;
	private float rX, rY, rZ;
	private float scale;
	
	public Entity(TexturedModel model, Vector3f position, float rX, float rY, float rZ, float scale)
	{
		this.model = model;
		this.position = position;
		this.rX = rX;
		this.rY = rY;
		this.rZ = rZ;
		this.scale = scale;
	}
	
	public void increaseRotation(Vector3f rot)
	{
		rX += rot.x;
		rY += rot.y;
		rZ += rot.z;
	}
	
	public void increasePosition(Vector3f pos)
	{
		position.x += pos.x;
		position.y += pos.y;
		position.z += pos.z;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public TexturedModel getModel()
	{
		return model;
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public float getrX()
	{
		return rX;
	}

	public float getrY()
	{
		return rY;
	}

	public float getrZ()
	{
		return rZ;
	}

	public float getScale()
	{
		return scale;
	}
	
	
}
