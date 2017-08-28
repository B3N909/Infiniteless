package me.savant.infinite.render;

public class Model
{
	private int vaoID;
	private int vertexCount;
	
	public Model(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID()
	{
		return vaoID;
	}

	public int getVertexCount()
	{
		return vertexCount;
	}
	
	
}
