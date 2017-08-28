package me.savant.infinite;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Window
{
	public static void main(String[] args)
	{
		Window w = new Window(1080, 720, 120);
		new GameLoop(w);
	}
	
	
	protected long windowID;
	private int FPS_LOCK;
	public Window(int WIDTH, int HEIGHT, int FPS_LOCK)
	{
		this.FPS_LOCK = FPS_LOCK;
		ContextAttribs attribs = new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
		}
		catch (LWJGLException e)
		{
			System.err.println("Failed to create display!");
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public void update()
	{
		Display.sync(FPS_LOCK);
		Display.update();
	}
	
	public void close()
	{
		Display.destroy();
	}
}
