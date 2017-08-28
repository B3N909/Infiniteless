package me.savant.infinite.shaders;

import org.lwjgl.util.vector.Matrix4f;

import me.savant.camera.Camera;
import me.savant.infinite.Light;
import me.savant.util.Maths;

public class Shader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/me/savant/infinite/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/me/savant/infinite/shaders/fragmentShader.txt";
	
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_sunPosition;
	private int location_sunColor;
	
	public Shader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}


	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "uv");
		super.bindAttribute(2, "normal");
	}


	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_sunPosition = super.getUniformLocation("sunPosition");
		location_sunColor = super.getUniformLocation("sunColor");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadSun(Light sun)
	{
		super.loadVector(location_sunPosition, sun.getPosition());
		super.loadVector(location_sunColor, sun.getColor());
	}
	

}
