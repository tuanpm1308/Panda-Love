package com.tharastudio.pandalove.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.tharastudio.pandalove.helpers.InputHandler;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.worlds.GameRenderer;
import com.tharastudio.pandalove.worlds.GameWorld;

public class GameScreen implements Screen {

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
	private Game game;

	public GameScreen(Game game) {
		this.game	= game;
	}

	@Override
	public void render(float delta) {
		runTime	+= delta;

		world.update(delta);
		renderer.render(runTime);
	}

	@Override
	public void resize(int width, int height) {
		game.calculateViewport(width, height);
	}

	@Override
	public void show() {
		world		= new GameWorld();
		renderer	= new GameRenderer(world);

		Gdx.input.setInputProcessor(new InputHandler(world));
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
