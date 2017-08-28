package me.savant.infinite.render;

import me.savant.infinite.shaders.Shader;

public class MasterRenderer
{
	private Shader shader = new Shader();
	//private Renderer renderer = new Renderer(shader);
	
	//private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	
	
	public void dispose()
	{
		shader.dispose();
	}
}
