package me.savant.util;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.savant.camera.Camera;

public class Maths
{
	public static final float DEGREE2RADIAN = 0.0174533f;
	
	public static Vector3f getDirection(float scale, float yPitch, float xYaw)
	{
		double pitch = ((yPitch + 90) * Math.PI) / 180;
		double yaw = ((xYaw + 270) * Math.PI) / 180;
		
		double x = scale * Math.sin(pitch) * Math.cos(yaw);
		double y = scale * Math.sin(pitch) * Math.sin(yaw);
		double z = scale * Math.cos(pitch);
		
		return new Vector3f((float)x, (float)z, (float)y);
	}
	
	public static Vector3f transformRelative(Vector3f worldPosition, Vector3f relativePosition, float rotation)
	{
		float theta = (float) -Math.toRadians(rotation);
		float px = relativePosition.x;
		float py = relativePosition.z;
		float ox = worldPosition.x;
		float oy = worldPosition.z;
		
		float x = (float) (Math.cos(theta) * (px-ox) - Math.sin(theta) * (py-oy) + ox);
		float z = (float) (Math.sin(theta) * (px-ox) + Math.cos(theta) * (py-oy) + oy);
		
		return new Vector3f(x, relativePosition.y, z);
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera cam)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float)Math.toRadians(cam.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(cam.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.translate(new Vector3f(-cam.getPosition().x, -cam.getPosition().y, - cam.getPosition().z), matrix, matrix);
		return matrix;
	}
	
	public static float[] toFloatArray(List<Float> floatA)
	{
		float[] ar = new float[floatA.size()];
		int i = 0;
		for(float f : floatA)
		{
			ar[i] = f;
			i++;
		}
		return ar;
	}
	
	public static int[] toIntegerArray(List<Integer> intA)
	{
		int[] ar = new int[intA.size()];
		int i = 0;
		for(int f : intA)
		{
			ar[i] = f;
			i++;
		}
		return ar;
	}
	
	public static float distance(Vector3f a, Vector3f b)
	{
	    return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
	}
}
