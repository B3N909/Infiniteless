package me.savant.infinite.chunk;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.savant.infinite.Entity;
import me.savant.infinite.GameLoop;
import me.savant.infinite.render.Model;
import me.savant.infinite.render.TexturedModel;
import me.savant.infinite.texture.Texture;
import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.VoxelFace;
import me.savant.infinite.voxel.VoxelModelBuilder;
import me.savant.infinite.voxel.World;

public class Chunk
{
	public static List<Chunk> updateQueue = new ArrayList<Chunk>();
	
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_DENSITY = 2;
	public static final int CHUNK_HEIGHT = 255;
	
	private VoxelModelBuilder builder = new VoxelModelBuilder();
	
	public byte[][][] data;
	public Vector2f chunkPosition;
	public World w;
	public boolean hasInitialBuild = false;
	
	private Model m;
	private Texture texture;
	private TexturedModel tm;
	private Entity e;
	//private float[] vertices;
	//private float[] normals;
	//private float[] uvs;
	//private int[] indices;
	
	public Chunk(Texture texture)
	{
		this.texture = texture;
		data =  new byte[CHUNK_SIZE * CHUNK_DENSITY][CHUNK_HEIGHT * CHUNK_DENSITY][CHUNK_SIZE * CHUNK_DENSITY];
	}
	
	public void Build()
	{
		if(!hasInitialBuild)
		{
			update();
			hasInitialBuild = true;
		}
	}
	
	public void update()
	{
		builder.start();
		for(int x = 0; x < CHUNK_SIZE * CHUNK_DENSITY; x++)
		{
			for(int z = 0; z < CHUNK_SIZE * CHUNK_DENSITY; z++)
			{
				for(int y = 0; y < CHUNK_HEIGHT * CHUNK_DENSITY; y++)
				{
					if(VoxelBlock.isSolid(data[x][y][z]))
					{
						updateVoxel(x, y, z);
					}
				}
			}
		}
		synchronized(updateQueue)
		{
			updateQueue.add(this);			
		}
	}
	
	public synchronized static void updateQueue()
	{
		synchronized(updateQueue)
		{
			for(Chunk c : updateQueue)
			{
				c.updateGraphics();
			}
			updateQueue.clear();			
		}
	}
	
	public void updateGraphics()
	{
		//TODO: Optimize
		m = GameLoop.loader.toVAO(builder.getVertices(),
				builder.getUVS(),
				builder.getNormals(),
				builder.getIndices());
		tm = new TexturedModel(m, texture);
		e = new Entity(tm, new Vector3f(chunkPosition.x * Chunk.CHUNK_SIZE, 0, chunkPosition.y * Chunk.CHUNK_SIZE), 0, 0, 0, 1f);
	}
	
	private void updateVoxel(int lX, int lY, int lZ) 
	{
		int x = (int)(lX + (chunkPosition.x * CHUNK_SIZE * CHUNK_DENSITY));
		int z = (int)(lZ + (chunkPosition.y * CHUNK_SIZE * CHUNK_DENSITY));
		byte bData = data[lX][lY][lZ];
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x, lY + 1, z))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.TOP);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.TOP);
		}
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x, lY - 1, z))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.BOTTOM);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.BOTTOM);
		}
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x - 1, lY, z))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.WEST);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.WEST);
		}
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x + 1, lY, z))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.EAST);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.EAST);
		}
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x, lY, z + 1))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.SOUTH);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.SOUTH);
		}
		if(!VoxelBlock.isSolid(w.getWorldBlock(new Vector3f(x, lY, z - 1))))
		{
			//GameLoop.physics.add(new Vector3f(x, lY, z), VoxelFace.NORTH);
			builder.build(new Vector3f(lX, lY, lZ), CHUNK_DENSITY, bData, VoxelFace.NORTH);
		}
	}
	
	public void Render()
	{
//		GameLoop.camera.culler.frustum(new Vector3f(chunkPosition.x * CHUNK_SIZE, GameLoop.camera.getPosition().y, chunkPosition.y * CHUNK_SIZE))
		if(hasInitialBuild && e != null)
			GameLoop.renderer.render(e, GameLoop.shader);
	}
	
	
	
}
