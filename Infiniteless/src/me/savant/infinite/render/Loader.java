package me.savant.infinite.render;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import me.savant.infinite.texture.TextureData;

public class Loader
{
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public Model toVAO(float[] positions, float[] uvs, float[] normals, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesVBO(indices);
		StoreData(0, 3, positions);
		StoreData(1, 2, uvs);
		StoreData(2, 3, normals);
		unbindVAO();
		return new Model(vaoID, indices.length);
	}
	
	public Model toVAO(float[] positions, int dimensions)
	{
		int vaoID = createVAO();
		StoreData(0, dimensions, positions);
		unbindVAO();
		return new Model(vaoID, positions.length / dimensions);
	}
	
	private TextureData decodeTextureFile(String file)
	{
		int width = 0;
		int height = 0;
		ByteBuffer b = null;
		try
		{
			FileInputStream in = new FileInputStream(file);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			b = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(b, width * 4, Format.RGBA);
			b.flip();
			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Failed to load texture!");
			System.exit(-1);
		}
		return new TextureData(width, height, b);
	}
	
	public int loadCubeMap(String[] files)
	{
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for(int i = 0; i < files.length; i++)
		{
			TextureData data = decodeTextureFile(files[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return texID;
	}
	
	public int loadTexture(String file)
	{
		org.newdawn.slick.opengl.Texture t = null;
		try
		{
			t = TextureLoader.getTexture("PNG", new FileInputStream(file));
		}
		catch (Exception e)
		{
			System.err.println("Failed to load file!");
			System.exit(-1);
		}
		int textureID = t.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void StoreData(int attributeNumber, int size, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer b = toFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	private FloatBuffer toFloatBuffer(float[] data)
	{
		FloatBuffer b = BufferUtils.createFloatBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	private IntBuffer toIntBuffer(int[] data)
	{
		IntBuffer b = BufferUtils.createIntBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	private void bindIndicesVBO(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer b = toIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);
		
	}
	
	public void dispose()
	{
		for(int vao : vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture : textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
}
