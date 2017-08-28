package me.savant.infinite.render;

import org.lwjgl.util.vector.Vector3f;

import me.savant.infinite.voxel.World;
import me.savant.util.Maths;

/*
 * @author Hopson97
 */
public class Ray
{
	private Vector3f position;
	private Vector3f end;
	
	public Ray(Vector3f position)
	{
		position = new Vector3f(position.x + 0.25f, position.y + 0.25f, position.z + 0.5f);
		this.position = position;
		this.end = position;
	}
	
	private void step(float scale, Vector3f direction)
	{
		end = new Vector3f(end.x + direction.x, end.y + direction.y, end.z + direction.z);
	}
	
	public byte getBlockData(World world, float yaw, float pitch)
	{
		end = position;
		float reach = 20;
		float scale = 0.25f;
		float accumulativeScale = 0f;
		
		while(accumulativeScale < reach)
		{
			if(world.getWorldBlock(end) != 0)
				break;
			step(scale, Maths.getDirection(scale, pitch, yaw));
			accumulativeScale += scale;
		}
		return world.getWorldBlock(end);
	}
	
	public Vector3f getBlock(World world, float yaw, float pitch)
	{
		end = position;
		float reach = 6;
		float scale = 1f;
		float accumulativeScale = 0f;
		
		System.out.println(Maths.getDirection(scale, pitch, yaw));
		
		searchLoop: while(accumulativeScale < reach)
		{
			if(world.getWorldBlock(end) != 0)
				break searchLoop;
			step(scale, Maths.getDirection(scale, pitch, yaw));
			accumulativeScale += scale;
		}
		return end;
	}
	
	public Vector3f getEnd()
	{
		return end;
	}
	
	public float getLength()
	{
		return Maths.distance(position, end);
	}
	
}
