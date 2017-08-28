package me.savant.infinite;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.savant.camera.Camera;
import me.savant.infinite.chunk.Chunk;
import me.savant.infinite.render.Loader;
import me.savant.infinite.render.Renderer;
import me.savant.infinite.shaders.Shader;
import me.savant.infinite.skybox.SkyboxRenderer;
import me.savant.infinite.texture.Texture;
import me.savant.infinite.voxel.VoxelBlock;
import me.savant.infinite.voxel.World;
import me.savant.util.FPSTimer;


public class GameLoop
{
	public static Loader loader;
	public static Renderer renderer;
	public static Shader shader;
	public static World world;
	public static Camera camera;
	public static boolean isDebug = true;
	//public static PhysicsWorld physics;
	
	private Window window;
	
	
	public GameLoop(Window window)
	{
		this.window = window;
		loop();
	}
	
	private void loop()
	{		
		VoxelBlock.dictionizeBlocks();
		
		loader = new Loader();
		shader = new Shader();
		renderer = new Renderer(shader);
		
		SkyboxRenderer skyboxRenderer = new SkyboxRenderer(loader, Renderer.PROJECTION_MATRIX);
		
		camera = new Camera();
		Light sun = new Light(new Vector3f(150, 2000, 600), new Vector3f(1f, 1f, 1f));
		
		Texture texture = new Texture(loader.loadTexture("res/terrain.png"));
		world = new World(camera, texture);
		world.createChunk(new Vector2f(1, 1));
		//physics = new PhysicsWorld(camera);
		
		while(!Display.isCloseRequested())
		{
			FPSTimer.update();
			
			renderer.prepare();
			shader.start();
			
			shader.loadViewMatrix(camera);
			shader.loadSun(sun);
			
			camera.move();
			//physics.update();
			
			world.Render(camera);
			Chunk.updateQueue();
			
			skyboxRenderer.render(camera);
			
			
			shader.stop();
			window.update();
			
			//System.out.println(FPSTimer.getFPS());
		}
		world.dispose();
		shader.dispose();
		loader.dispose();
		window.close();
	}

}
