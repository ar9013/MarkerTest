package com.kangyue.markertest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Plane;

import java.nio.ByteBuffer;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class GdxMarkerTest extends ApplicationAdapter {

	PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Environment environment;

	float camx = 10.0f, camy = 10.0f, camz = 10.0f;

	int count = 0;

	int  radius = 2;
	int  angle = 90;

	@Override
	public void create() {

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(camx, camy, camz);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		instance = new ModelInstance(model);


		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (angle < 95) {

			camz = camz + cos(angle)*radius;
			camx = camx + sin(angle)*radius;

			angle = angle+1;


			Gdx.app.log("camy", "" + camy);

			cam.position.set(camx, camy, camz);

			cam.update();

			modelBatch.begin(cam);
			modelBatch.render(instance, environment);
			modelBatch.end();


			count = count + 1;
			saveScreenshot(count);
		}

		cam.update();
		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();


	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}


	private void saveScreenshot(int number) {

		FileHandle file = new FileHandle("ScreenShots/shot" + number + ".png");
		Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);
		PixmapIO.writePNG(file, pixmap);
	}

	private Pixmap getScreenshot(int x, int y, int w, int h, boolean flipY) {
		Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);

		final Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
		ByteBuffer pixels = pixmap.getPixels();
		Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);

		final int numBytes = w * h * 4;
		byte[] lines = new byte[numBytes];
		if (flipY) {
			final int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		} else {
			pixels.clear();
			pixels.get(lines);
		}

		return pixmap;
	}


}





