package me.savant.infinite.voxel.blocks;

import org.lwjgl.util.vector.Vector2f;

import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.VoxelFace;

public class Dirt extends VoxelBlock
{
	public Dirt()   
	{
		super((byte) 1, "Dirt");
	}
	@Override
	public Vector2f texture(VoxelFace face) {
		return new Vector2f(2, 0);
	}
}
