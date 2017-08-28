package me.savant.infinite.voxel;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import me.savant.util.Maths;

public class VoxelModelBuilder
{
	List<Float> vertices = new ArrayList<Float>();
	List<Integer> indices = new ArrayList<Integer>();
	List<Float> normals = new ArrayList<Float>();
	List<Float> uvs = new ArrayList<Float>();
	int vertexPointer = 1;
	
	public void start()
	{
		vertices.clear();
		indices.clear();
		normals.clear();
		uvs.clear();
		vertexPointer = 1;
	}
	
	public void build(Vector3f position, float density, byte data, VoxelFace face)
	{
		position = new Vector3f(position.x / density, position.y / density, position.z / density);
		density = 2f;
		for(float f : face.getUVS(VoxelBlock.getBlock(data).getTexture(face), VoxelBlock.textureUnit)) //UVs
		{
			uvs.add(f);
		}
		for(float n : face.getNormals()) //Normals
			normals.add(n);
		for(int i : face.getIndices()) //Indices
			indices.add(i + (vertices.size() / 3));
		for(float v : face.getVertices()) //Vertices
		{
			if(vertexPointer == 1)
			{
				vertices.add((v / density) + position.x);
			}
			else if(vertexPointer == 2)
			{
				vertices.add((v / density) + position.y);
			}
			else if(vertexPointer == 3)
			{
				vertices.add((v / density) + position.z);
				vertexPointer = 0;
			}
			vertexPointer++;
		}
	}
	
	public float[] getVertices()
	{
		synchronized(vertices)
		{
			return Maths.toFloatArray(vertices);
		}
	}
	
	public int[] getIndices()
	{
		synchronized(indices)
		{
			return Maths.toIntegerArray(indices);
		}
	}
	
	public float[] getNormals()
	{
		synchronized(normals)
		{
			return Maths.toFloatArray(normals);
		}
	}
	
	public float[] getUVS()
	{
		synchronized(uvs)
		{
			return Maths.toFloatArray(uvs);
		}
	}
}