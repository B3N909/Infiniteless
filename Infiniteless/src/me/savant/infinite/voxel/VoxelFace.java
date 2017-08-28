package me.savant.infinite.voxel;

import org.lwjgl.util.vector.Vector2f;

public enum VoxelFace
{
	TOP, BOTTOM, NORTH, SOUTH, EAST, WEST;
	
	public float[] getVertices()
	{
		switch(this)
		{
		case TOP: return new float[]
				{
						-0.5f,0.5f,0.5f,
						-0.5f,0.5f,-0.5f,
						0.5f,0.5f,-0.5f,
						0.5f,0.5f,0.5f
				};
		case BOTTOM: return new float[]
				{
						-0.5f,-0.5f,0.5f,
						-0.5f,-0.5f,-0.5f,
						0.5f,-0.5f,-0.5f,
						0.5f,-0.5f,0.5f
				};
		case SOUTH: return new float[]
				{
						-0.5f,0.5f,0.5f,	
						-0.5f,-0.5f,0.5f,	
						0.5f,-0.5f,0.5f,	
						0.5f,0.5f,0.5f
				};
		case EAST: return new float[]
				{
						0.5f,0.5f,-0.5f,	
						0.5f,-0.5f,-0.5f,	
						0.5f,-0.5f,0.5f,	
						0.5f,0.5f,0.5f
				};
		case NORTH: return new float[]
				{
						-0.5f,0.5f,-0.5f,	
						-0.5f,-0.5f,-0.5f,	
						0.5f,-0.5f,-0.5f,	
						0.5f,0.5f,-0.5f
				};
		case WEST: return new float[]
				{
						-0.5f,0.5f,-0.5f,	
						-0.5f,-0.5f,-0.5f,	
						-0.5f,-0.5f,0.5f,	
						-0.5f,0.5f,0.5f
				};
		default: return null;
		}
	}
	public int[] getIndices()
	{
		switch(this)
		{
		case TOP: return new int[]
				{
						3,1,0,
						2,1,3,
				};
		case BOTTOM: return new int[]
				{
						0,1,3,
						3,1,2,
				};
		case SOUTH: return new int[]
				{
						0,1,3,
						3,1,2,
				};
		case EAST: return new int[]
				{
						3,1,0,
						2,1,3,
				};
		case NORTH: return new int[]
				{
						3,1,0,
						2,1,3,
				};
		case WEST: return new int[]
				{
						0,1,3,
						3,1,2,
				};
		default: return null;
		}
	}
	public float[] getNormals()
	{
		switch(this)
		{
		case TOP: return new float[]
				{
						0,1,0,
						0,1,0,
						0,1,0,
						0,1,0
				};
		case BOTTOM: return new float[]
				{
						0,-1,0,
						0,-1,0,
						0,-1,0,
						0,-1,0
				};
		case NORTH: return new float[]
				{
						0,0,-1,
						0,0,-1,
						0,0,-1,
						0,0,-1
				};
		case EAST: return new float[]
				{
						1,0,0,
						1,0,0,
						1,0,0,
						1,0,0
				};
		case SOUTH: return new float[]
				{
						0,0,1,
						0,0,1,
						0,0,1,
						0,0,1
				};
		case WEST: return new float[]
				{
						-1,0,0,
						-1,0,0,
						-1,0,0,
						-1,0,0
				};
		default: return null;
		}
	}
	public float[] getUVS(Vector2f tex, float textureUnit)
	{
		tex = new Vector2f(tex.x * textureUnit, tex.y * textureUnit);
		return new float[]
				{
						tex.x,tex.y,
						tex.x,tex.y + textureUnit,
						tex.x + textureUnit,tex.y + textureUnit,
						tex.x + textureUnit,tex.y
				};
	}
}
