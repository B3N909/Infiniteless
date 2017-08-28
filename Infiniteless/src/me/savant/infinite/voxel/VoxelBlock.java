package me.savant.infinite.voxel;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import me.savant.infinite.voxel.blocks.Dirt;
import me.savant.infinite.voxel.blocks.Grass;
import me.savant.infinite.voxel.blocks.Gravel;
import me.savant.infinite.voxel.blocks.Stone;

public abstract class VoxelBlock
{
	protected static List<VoxelBlock> blocksDictionary = new ArrayList<VoxelBlock>();
	public static float textureUnit = 0.0625f;
	
	public static void dictionizeBlocks()
	{
		new Dirt();
		new Grass();
		new Stone();
		new Gravel();
	}
	
	public abstract Vector2f texture(VoxelFace face);
	
	private byte id;
	private String name;
	private boolean isSolid  = true;

	public VoxelBlock(byte id, String name)
	{
		this.id = id;
		this.name = name;
		blocksDictionary.add(this);
	}
	
	public VoxelBlock(byte id, String name, boolean isSolid)
	{
		this.isSolid = isSolid;
		this.id = id;
		this.name = name;
		blocksDictionary.add(this);
	}
	
	public byte getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isSolid()
	{
		return isSolid;
	}
	
	public Vector2f getTexture(VoxelFace face)
	{
		return texture(face);
	}
	
	public static boolean isSolid(byte id)
	{
		VoxelBlock vb = getBlock(id);
		return vb != null && vb.isSolid();
	}
	
	public static VoxelBlock getBlock(byte id)
	{
		for(VoxelBlock b : blocksDictionary)
		{
			if(b.getID() == id)
			{
				return b;
			}
		}
		return null;
	}
}
