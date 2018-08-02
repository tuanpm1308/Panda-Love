package com.tharastudio.pandalove.worlds;

import static com.tharastudio.pandalove.helpers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.entities.*;

public class GameRenderer {
	private GameWorld gameWorld;

	private float screenWidth, screenHeight;

	private OrthographicCamera cam;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private OrthographicCamera b2dCam;
	private Box2DDebugRenderer b2dRenderer;

	private TextureRegion background;

	public GameRenderer(GameWorld gameWorld) {
		this.gameWorld = gameWorld;

		screenWidth		= Game.V_WIDTH;
		screenHeight	= Game.V_HEIGHT;

		cam		= new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);

		spriteBatch = new SpriteBatch();
		// Attach spriteBatch to camera
		spriteBatch.setProjectionMatrix(cam.combined);

		shapeRenderer	= new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		b2dCam	= new OrthographicCamera();
		b2dCam.setToOrtho(false, screenWidth / PPM, screenHeight / PPM);

		b2dRenderer	= new Box2DDebugRenderer();

		// init assets
		background	= new TextureRegion(Game.assets.getTexture("screen"), 0, 192, 320, 192);
	}

	public void render(float runTime) {
		Gdx.gl.glViewport((int) Game.viewport.x, (int) Game.viewport.y, (int) Game.viewport.width, (int) Game.viewport.height);

		// Fill the entire screen with black, to prevent potential flickering.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw map frame
		spriteBatch.begin();
		spriteBatch.enableBlending();

		spriteBatch.draw(background, (screenWidth - Game.V_WIDTH) / 2, (screenHeight - Game.V_HEIGHT) / 2, Game.V_WIDTH, Game.V_HEIGHT);

		spriteBatch.end();

		// Draw player
		gameWorld.getPlayer().render(spriteBatch);

		// Draw keys
		Array<Key> keys	= gameWorld.getKeys();

		for (int i = 0; i < keys.size; i++) {
			keys.get(i).render(spriteBatch);
		}

		// Draw traps
		Array<Trap> traps	= gameWorld.getTraps();

		for (int i = 0; i < traps.size; i++) {
			traps.get(i).render(spriteBatch);
		}

		// Draw plats
		Array<Plat> plats	= gameWorld.getPlats();

		for (int i = 0; i < plats.size; i++) {
			plats.get(i).render(spriteBatch);
		}

		// Draw bars
		Array<Bar> bars	= gameWorld.getBars();

		for (int i = 0; i < bars.size; i++) {
			bars.get(i).render(spriteBatch);
		}

		// Draw hole
		Hole hole	= gameWorld.getHole();

		if (hole != null) {
			hole.render(spriteBatch);
		}

//		b2dRenderer.render(gameWorld.getWorld(), b2dCam.combined);
	}
}
