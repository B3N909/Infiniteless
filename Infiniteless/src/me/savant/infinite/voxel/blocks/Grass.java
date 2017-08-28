package me.savant.infinite.voxel.blocks;

import org.lwjgl.util.vector.Vector2f;

import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.VoxelFace;

public class Grass extends VoxelBlock
{
	public Grass()   
	{
		super((byte) 2, "Grass");
	}

	@Override
	public Vector2f texture(VoxelFace face)
	{
		switch(face)
		{
		case TOP:
			return new Vector2f(0, 0);
		case BOTTOM:
			return new Vector2f(2, 0);
		default:
			return new Vector2f(3, 0);
		}
	}
}
