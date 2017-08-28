package me.savant.collision;

import java.util.Random;

import javax.net.ssl.SSLContext;

import org.lwjgl.util.vector.Vector3f;

import com.flowpowered.react.body.RigidBody;
import com.flowpowered.react.collision.CollisionDetection;
import com.flowpowered.react.collision.shape.BoxShape;
import com.flowpowered.react.collision.shape.CapsuleShape;
import com.flowpowered.react.engine.DynamicsWorld;
import com.flowpowered.react.engine.Material;
import com.flowpowered.react.math.Matrix3x3;
import com.flowpowered.react.math.Transform;
import com.flowpowered.react.math.Vector3;

import me.savant.camera.Camera;
import me.savant.infinite.voxel.VoxelFace;

public class PhysicsWorld
{
	public static Vector3 GRAVITY = new Vector3(0f, -10f, 0f);
	public static float HEIGHT = 2f;
	
	public RigidBody player;
	Camera c;
	DynamicsWorld w;
	CollisionDetection collision;
	
	public PhysicsWorld(Camera c)
	{
		w = new DynamicsWorld(GRAVITY);
		w.start();
		w.update();
		w.stop();
		
		
		collision = new CollisionDetection(w);
		
		float playerWeight = 50;
		Transform t = new Transform();
		t.setPosition(new Vector3(c.getPosition().x, c.getPosition().y, c.getPosition().z));
		BoxShape capsule = new BoxShape(1f, HEIGHT, 1f);
		Matrix3x3 inertia = new Matrix3x3();
		capsule.computeLocalInertiaTensor(inertia, playerWeight);
		player = new RigidBody(t, playerWeight, inertia, capsule, 1);
		w.addRigidBody(player);
		w.setIsGravityEnabled(true);
		
		Material mat = new Material(0f, 0.25f);
		player.setMaterial(mat);
		
		w.start();
		
		this.c = c;
	}
	
	public void update()
	{
		synchronized(w.getRigidBodies())
		{
			w.update();			
		}
	}
	
	int id = 2;
	public void add(Vector3f position, VoxelFace face)
	{
		BoxShape s = new BoxShape(new Vector3(0.5f, 0.5f, 0.5f), 0.0000001f);
		//VoxelModelBuilder.addPhysics(s, face);
		Transform t = new Transform();
		t.setPosition(new Vector3(position.x, position.y, position.z));
		Matrix3x3 m = new Matrix3x3();
		s.computeLocalInertiaTensor(m, 1f);
		
		RigidBody b = new RigidBody(t, 1f, m, s, id);
		
		b.enableMotion(false);
		b.setMaterial(new Material(0f, 0.25f));
		
		id++;
		synchronized(w.getRigidBodies())
		{
			w.addRigidBody(b);
		}			
	}
}
