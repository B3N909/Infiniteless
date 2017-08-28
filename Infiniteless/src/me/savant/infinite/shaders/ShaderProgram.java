package me.savant.infinite.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram
{
	private int programID;
	private int vertexID;
	private int fragmentID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile)
	{
		vertexID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected int getUniformLocation(String name)
	{
		return GL20.glGetUniformLocation(programID, name);
	}
	
	protected abstract void getAllUniformLocations();
	
	public void start()
	{
		GL20.glUseProgram(programID);
	}
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadBoolean(int location, boolean value)
	{
		float val = 0;
		if(value)
			val = 1;
		GL20.glUniform1f(location, val);
	}
	
	protected void loadMatrix(int location, Matrix4f value)
	{
		value.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	public void dispose()
	{
		stop();
		GL20.glDetachShader(programID, vertexID);
		GL20.glDetachShader(programID, fragmentID);
		GL20.glDeleteShader(vertexID);
		GL20.glDeleteShader(fragmentID);
		GL20.glDeleteProgram(programID);

	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	private static int loadShader(String file, int type)
	{
		StringBuilder source = new StringBuilder();
		try
		{
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line;
			while((line = r.readLine()) != null)
			{
				source.append(line).append("\n");
			}
			r.close();
		}
		catch (IOException e)
		{
			System.err.println("Could not read shader!");
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, source);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}
}
