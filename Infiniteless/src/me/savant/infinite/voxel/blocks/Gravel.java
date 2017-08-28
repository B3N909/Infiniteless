package me.savant.infinite.voxel.blocks;

import org.lwjgl.util.vector.Vector2f;

import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.VoxelFace;

public class Gravel extends VoxelBlock
{
	public Gravel()   
	{
		super((byte) 4, "Gravel");
	}

	@Override
	public Vector2f texture(VoxelFace face)
	{
		return new Vector2f(3, 1);
	}
}
