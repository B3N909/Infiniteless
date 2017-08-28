package me.savant.infinite.voxel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.savant.camera.Camera;
import me.savant.infinite.chunk.Chunk;
import me.savant.infinite.chunk.TerrainNoise;
import me.savant.infinite.texture.Texture;
import me.savant.util.Maths;
import me.savant.util.Vector2v;

public class World
{
	public static int RENDER_DISTANCE = 12;
	public boolean locked = false;
	Map<Vector2v, Chunk> data = Collections.synchronizedMap(new HashMap<Vector2v, Chunk>());
	WorldThread wThread;
	Thread wLoader;
	
	public World(Camera camera, Texture texture)
	{
		wThread = new WorldThread(this, texture, camera);
		wLoader = new Thread(wThread);
	}
	
	public void start()
	{
		wLoader.start();
	}
	
	public void dispose()
	{
		wThread.isRunning = false;
	}
	
	public void Render(Camera camera)
	{
		synchronized(data)
		{
			for(Chunk c : data.values())
			{
				c.Render();			
			}
		}
	}
	
	public boolean isSurrounded(float chunkX, float chunkZ)
	{
		return getChunk(chunkX + 1, chunkZ) != null &&
				getChunk(chunkX - 1, chunkZ) != null &&
				getChunk(chunkX, chunkZ + 1) != null &&
				getChunk(chunkX, chunkZ - 1) != null;
	}
	
	Chunk getChunk(float chunkX, float chunkZ)
	{
		for(Vector2f p : data.keySet())
		{
			if(p.x == chunkX && p.y == chunkZ)
			{
				return data.get(p);
			}
		}
		return null;
	}
	Vector2f getChunk(Vector3f worldPosition)
	{
		int chunkX = (int) Math.floor(worldPosition.x / (Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY));
		int chunkZ = (int) Math.floor(worldPosition.z / (Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY));
		return new Vector2f(chunkX, chunkZ);
	}
	
	//TODO: Optimize get local position
	public byte getWorldBlock(Vector3f worldPosition)
	{
		Vector2f chunkPos = getChunk(worldPosition);
		Vector3f localPosition = new Vector3f(
				worldPosition.x - (chunkPos.x * Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY),
				worldPosition.y,
				worldPosition.z - (chunkPos.y * Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY));
		if(getChunk(chunkPos.x, chunkPos.y) == null)
			return (byte) 0;
		return getLocalBlock(getChunk(chunkPos.x, chunkPos.y), localPosition);
	}
	public byte getLocalBlock(Chunk c, Vector3f localPosition)
	{
		int x = (int) Math.floor(localPosition.x);
		int y = (int) Math.floor(localPosition.y);
		int z = (int) Math.floor(localPosition.z);

		if(localPosition == null || c == null || c.data == null ||
				localPosition.x < 0 || localPosition.x >= Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY || 
				localPosition.y < 0 || localPosition.y >= Chunk.CHUNK_HEIGHT ||
				localPosition.z < 0 || localPosition.z >= Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY)
			return (byte) 0;
		return c.data[x][y][z];
	}
	
	public boolean setBlock(Vector3f worldPosition, byte newData)
	{
		//bThread.updateBlock(worldPosition, newData);
		//return true;
		
		Vector2f chunkPos = getChunk(worldPosition);
		Vector3f localPosition = new Vector3f(
				worldPosition.x - (chunkPos.x * Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY),
				worldPosition.y,
				worldPosition.z - (chunkPos.y * Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY));
		if(getChunk(chunkPos.x, chunkPos.y) == null)
			return false;
		
		Chunk c = getChunk(chunkPos.x, chunkPos.y);
		if(localPosition == null ||
				   localPosition.x < 0 || localPosition.x >= Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY || 
				   localPosition.y < 0 || localPosition.y >= Chunk.CHUNK_HEIGHT ||
				   localPosition.z < 0 || localPosition.z >= Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY ||
				   c == null || c.data == null)
					return false;
		int x = (int) Math.floor(localPosition.x);
		int y = (int) Math.floor(localPosition.y);
		int z = (int) Math.floor(localPosition.z);
		c.data[x][y][z] = newData;
		
		updateChunk(chunkPos.x, chunkPos.y);
		if(x == (Chunk.CHUNK_SIZE - 1))
			updateChunk(chunkPos.x + 1, chunkPos.y);
		if(x == 0)
			updateChunk(chunkPos.x - 1, chunkPos.y);
		if(z == (Chunk.CHUNK_SIZE - 1))
			updateChunk(chunkPos.x, chunkPos.y + 1);
		if(z == 0)
			updateChunk(chunkPos.x, chunkPos.y - 1);
		
		return true;
		
	}
	public boolean updateChunk(Vector3f worldPosition)
	{
		Vector2f chunkPos = getChunk(worldPosition);
		return updateChunk(chunkPos.x, chunkPos.y);
	}
	public boolean updateChunk(float chunkX, float chunkZ)
	{
		if(getChunk(chunkX, chunkZ) == null)
					return false;
		Chunk c = getChunk(chunkX, chunkZ);
		c.update();
		return true;
	}
	
	public boolean canMove()
	{
		return data.size() > RENDER_DISTANCE / 4f;
	}
	
	public void createChunk(Vector2f chunk)
	{
		wThread.buildChunk(chunk);
		wThread.updateChunk(chunk);
	}
}

class WorldThread implements Runnable
{
	boolean isRunning = true;
	World w;
	Texture t;
	Camera c;
	
	public Map<Vector3f, Byte> toChange = new HashMap<Vector3f, Byte>();
	
	public WorldThread(World w, Texture t, Camera c)
	{
		this.w = w;
		this.t = t;
		this.c = c;
	}
	
	public void buildChunk(Vector2f best)
	{
		Chunk c = new Chunk(t);
		c.chunkPosition = new Vector2f(best.x, best.y);
		c.w = w;
		
		for(int lX = 0; lX < Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY; lX++)
		{
			for(int lZ = 0; lZ < Chunk.CHUNK_SIZE * Chunk.CHUNK_DENSITY; lZ++)
			{
				float wX = lX + (best.x * Chunk.CHUNK_SIZE);
				float wZ = lZ + (best.y * Chunk.CHUNK_SIZE);
				for(int lY = 0; lY < TerrainNoise.getHeight(wX, wZ); lY++)
				{
					c.data[lX][lY][lZ] = TerrainNoise.getData(wX, lY, wZ);												
				}
			}
		}
		w.data.put(new Vector2v(best.x, best.y), c);
	}
	
	public void updateChunk(Vector2f best)
	{
		Chunk c = w.getChunk(best.x, best.y);
		if(c != null && !c.hasInitialBuild)
		{
			w.getChunk(best.x, best.y).Build();
		}
	}
	
	@Override
	public void run()
	{
		while(isRunning)
		{
			Vector2f chunkPos = w.getChunk(c.getPosition());
			
			float bestSoFar = 500f;
			Vector2f best = null;
			for(int x = -World.RENDER_DISTANCE - 1 + (int)chunkPos.x; x < World.RENDER_DISTANCE + 1 + (int)chunkPos.x; x++)
			{
				for(int z = -World.RENDER_DISTANCE - 1 + (int)chunkPos.y; z < World.RENDER_DISTANCE + 1 + (int)chunkPos.y; z++)
				{
					float dist = Maths.distance(new Vector3f(x, 0, z), new Vector3f(chunkPos.x, 0, chunkPos.y));
					if(!w.data.containsKey(new Vector2v(x, z)))
						if(dist < bestSoFar && !w.data.containsKey(new Vector2v(x, z)) && dist < World.RENDER_DISTANCE)
						{
							bestSoFar = dist;
							best = new Vector2f(x, z);
						}
				}
			}
			if(best != null)
			{
				buildChunk(best);
			}
			
			
			buildLoop: for(int x = -World.RENDER_DISTANCE - 1; x < World.RENDER_DISTANCE + 1; x++)
			{
				for(int z = -World.RENDER_DISTANCE - 1; z < World.RENDER_DISTANCE + 1; z++)
				{
					float chunkX = chunkPos.x + x;
					float chunkZ = chunkPos.y + z;
					Chunk c = w.getChunk(chunkX, chunkZ);
					if(c != null && !c.hasInitialBuild && w.isSurrounded(chunkX, chunkZ))
					{
						w.getChunk(chunkX, chunkZ).Build();
						break buildLoop;
					}
				}
			}
			
			try
			{
				//TODO: Problem Here
				//Thread.sleep(5L);
			}
			catch (Exception e)
			{
				
			}
		}
	}
}