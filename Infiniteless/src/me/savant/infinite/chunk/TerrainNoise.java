package me.savant.infinite.chunk;

import java.util.Random;

import com.flowpowered.noise.module.source.Perlin;

public class TerrainNoise
{
	protected static Perlin p = new Perlin();
	
	
	
	public static double getHeight(float x, float z)
	{
		p.setOctaveCount(6); //Detail
		p.setFrequency(0.3f); //Changes
		p.setPersistence(0.125f); //Smoothing
		
		x /= 5f;
		z /= 5f;
		
		return Math.floor((p.getValue(x + 0.1f, 0, z + 0.1f) * 10) + 20);
	}



	public static byte getData(float wX, float wY, float wZ)
	{
		if(wY == getHeight(wX, wZ) - 1)
		{
			return (byte) 2;
		}
		else if(wY < getHeight(wX, wZ) - 2)
		{
			if(new Random().nextFloat() > 0.75)
			{
				return (byte) 4;
			}
			return (byte) 3;
		}
		else
		{
			return (byte) 1;
		}
	}
}
