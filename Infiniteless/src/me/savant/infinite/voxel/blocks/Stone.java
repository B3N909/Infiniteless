package me.savant.infinite.voxel.blocks;

import org.lwjgl.util.vector.Vector2f;

import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.VoxelFace;

public class Stone extends VoxelBlock
{
	public Stone()   
	{
		super((byte) 3, "Stone");
	}

	@Override
	public Vector2f texture(VoxelFace face)
	{
		return new Vector2f(1, 0);
	}
}
