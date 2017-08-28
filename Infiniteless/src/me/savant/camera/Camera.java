package me.savant.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import me.savant.camera.frustum.FrustumCuller;
import me.savant.infinite.GameLoop;
import me.savant.infinite.render.Ray;
import me.savant.infinite.render.Renderer;
import me.savant.util.Maths;

public class Camera
{	
	private static final float SPEED = 0.05f;
	private static final float SENSITIVITY = 0.025f;
	
	public FrustumCuller culler;
	
	private Vector3f position = new Vector3f(0, 30, 0);
	private float xYaw;
	private float yPitch;
	private boolean grabbed = true;
	private boolean grabbedLock = true;
	private boolean clickLock = true;
	
	
	
	public Camera()
	{
		culler = new FrustumCuller();
	}
	
	public void move()
	{
		culler.update(this, Renderer.PROJECTION_MATRIX);

		if(GameLoop.world.canMove() || GameLoop.isDebug)
		{
			//Vector3 p = GameLoop.physics.player.getTransform().getPosition();
			//position = new Vector3f(p.getX(), p.getY(), p.getZ());
			
			if(grabbed)
			{
				xYaw += Mouse.getDX() * SENSITIVITY;
				yPitch -= Mouse.getDY() * SENSITIVITY;
			}
			Mouse.setGrabbed(grabbed);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				
				if(grabbedLock)
				{
					grabbedLock = false;
					grabbed = !grabbed;
				}
			}
			else
			{
				grabbedLock =  true;
			}
			
			if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1))
			{
				if(clickLock)
				{
					clickLock = false;
					Ray ray = new Ray(position);
					Vector3f pos = ray.getBlock(GameLoop.world, xYaw, yPitch);
					if(Mouse.isButtonDown(0))
					{
						GameLoop.world.setBlock(pos, (byte)0);
						//GameLoop.world.updateChunk(pos);
					}
					else
					{
						pos = new Vector3f(pos.x, pos.y + 1f, pos.z);
						
						GameLoop.world.setBlock(pos, (byte)2);
						//GameLoop.world.updateChunk(pos);
					}
				}
			}
			else
			{
				clickLock = true;
			}
			
			Vector3f localPosition = new Vector3f(0, 0, 0);
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				localPosition.z -= SPEED;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				localPosition.x += SPEED;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				localPosition.x -= SPEED;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				localPosition.z += SPEED;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				localPosition.y -= SPEED * 5;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				localPosition.y += SPEED * 5;
			}
			float dx = (float) (localPosition.z * Math.sin(Math.toRadians(-xYaw)));
			float dz = (float) (localPosition.z * Math.cos(Math.toRadians(-xYaw)));
			float dx2 = (float) (localPosition.x * Math.cos(Math.toRadians(-xYaw)));
			float dz2 = (float) (localPosition.x * Math.sin(Math.toRadians(-xYaw)));
			position = new Vector3f(position.x + dx2 + dx, position.y + localPosition.y, position.z -dz2 + dz);
			//GameLoop.physics.player.applyForceToCenter(new Vector3(dx2 + dx, localPosition.y, -dz2 + dz));
		}
		
	}
	
	public Vector3f getPosition()
	{
		return position;
	}
	
	public float getYaw()
	{
		return xYaw;
	}
	
	public float getPitch()
	{
		return yPitch;
	}
}
