package me.savant.infinite.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import me.savant.infinite.Entity;
import me.savant.infinite.shaders.Shader;
import me.savant.util.Maths;

public class Renderer
{
	public static Matrix4f PROJECTION_MATRIX;
	private static final float FOV = 90f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	public Renderer(Shader shader)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(PROJECTION_MATRIX);
		shader.stop();
	}
	
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(1f, 0f, 0f, 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Entity e, Shader shader)
	{
		TexturedModel texturedModel = e.getModel();
		Model model = texturedModel.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(e.getPosition(), e.getrX(), e.getrY(), e.getrZ(), e.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTID());
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void createProjectionMatrix()
	{
		float ar = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = (float)((1f / Math.tan(Math.toRadians(FOV / 2f))) * ar);
		float xScale = yScale / ar;
		float frustum = FAR_PLANE - NEAR_PLANE;
		
		PROJECTION_MATRIX = new Matrix4f();
		PROJECTION_MATRIX.m00 = xScale;
		PROJECTION_MATRIX.m11 = yScale;
		PROJECTION_MATRIX.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum);
		PROJECTION_MATRIX.m23 = -1;
		PROJECTION_MATRIX.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum);
		PROJECTION_MATRIX.m33 = 0;
	}
}
