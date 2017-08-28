package me.savant.util;

public class FPSTimer
{
	private static int frameCounter = 0;
	private static long startTime = 0;
	private static float fps = 0f;
	
	public static void update()
	{
		if(frameCounter == fps)
		{
			frameCounter = 0;
			startTime = System.currentTimeMillis();
		}
		frameCounter++;
		fps = frameCounter / ((System.currentTimeMillis() - startTime) / 1000f);
	}
	
	public static float getFPS()
	{
		return fps;
	}
}
