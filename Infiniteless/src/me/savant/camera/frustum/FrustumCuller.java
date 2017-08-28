package me.savant.camera.frustum;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.savant.camera.Camera;

public class FrustumCuller
{
	FrustumPlane[] planes;
	public void update(Camera camera, Matrix4f projectionMatrix)
	{
		Matrix4f viewMatrix = new Matrix4f();
		Matrix4f.rotate(0f, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(-camera.getYaw() / 30f, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(0f, new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Matrix4f.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z), viewMatrix, viewMatrix);
		
		planes = new FrustumPlane[6];
		
		Matrix4f m = new Matrix4f();
		Matrix4f.mul(viewMatrix, projectionMatrix, m);
		
		planes[0] = new FrustumPlane();
		planes[0].normal.x = m.m03 + m.m00;
		planes[0].normal.y = m.m13 + m.m10;
		planes[0].normal.z = m.m23 + m.m20;
		planes[0].distance = m.m33 + m.m30;
		
		planes[1] = new FrustumPlane();
		planes[1].normal.x = m.m03 + m.m00;
		planes[1].normal.y = m.m13 + m.m10;
		planes[1].normal.z = m.m23 + m.m20;
		planes[1].distance = m.m33 + m.m30;
		
		planes[2] = new FrustumPlane();
		planes[2].normal.x = m.m03 + m.m00;
		planes[2].normal.y = m.m13 + m.m10;
		planes[2].normal.z = m.m23 + m.m20;
		planes[2].distance = m.m33 + m.m30;
		
		planes[3] = new FrustumPlane();
		planes[3].normal.x = m.m03 + m.m00;
		planes[3].normal.y = m.m13 + m.m10;
		planes[3].normal.z = m.m23 + m.m20;
		planes[3].distance = m.m33 + m.m30;
		
		planes[4] = new FrustumPlane();
		planes[4].normal.x = m.m03 + m.m00;
		planes[4].normal.y = m.m13 + m.m10;
		planes[4].normal.z = m.m23 + m.m20;
		planes[4].distance = m.m33 + m.m30;
		
		planes[5] = new FrustumPlane();
		planes[5].normal.x = m.m03 + m.m00;
		planes[5].normal.y = m.m13 + m.m10;
		planes[5].normal.z = m.m23 + m.m20;
		planes[5].distance = m.m33 + m.m30;
		
		for(FrustumPlane p : planes)
		{
			float length = p.normal.length();
			p.normal = new Vector3f(p.normal.x / length, p.normal.y / length, p.normal.z / length);
			p.distance /= length;
		}
	}
	
	public boolean frustum(Vector3f point)
	{
		for(FrustumPlane p : planes)
		{
			if(p.distance(point) < 0)
				return false;
		}
		return true;
	}
}
