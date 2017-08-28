package me.savant.camera.frustum;

import org.lwjgl.util.vector.Vector3f;

public class FrustumPlane
{
	public FrustumPlane()
	{
		normal = new Vector3f();
	}
	
	float distance;
	Vector3f normal;
	
	public float distance(Vector3f point)
	{
		return Vector3f.dot(point, normal) + distance;
	}
}
