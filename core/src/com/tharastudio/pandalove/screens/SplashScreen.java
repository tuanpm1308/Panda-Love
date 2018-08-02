package com.tharastudio.pandalove.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.helpers.SplashInputHandler;
import com.tharastudio.pandalove.tweenaccessor.SpriteAccessor;
import com.tharastudio.pandalove.ui.Button;

public class SplashScreen implements Screen {
	private Game game;
	private Button playButton;

	private OrthographicCamera cam;
	private SpriteBatch spriteBatch;
	private TextureRegion background;

	private float screenWidth, screenHeight;

	private Sprite girl;

	private TweenManager manager;


	public SplashScreen(Game game) {
		this.game	= game;
	}

	@Override
	public void show() {
		screenWidth		= Game.V_WIDTH;
		screenHeight	= Game.V_HEIGHT;

		cam		= new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);

		spriteBatch = new SpriteBatch();
		// Attach spriteBatch to camera
		spriteBatch.setProjectionMatrix(cam.combined);

		background	= new TextureRegion(Game.assets.getTexture("screen"), 320, 192);
		girl		= new Sprite(Game.assets.getTexture("girl"));
		girl.setPosition(164f, 38f);

		setupTween();

		TextureRegion play	= new TextureRegion(Game.assets.getTexture("start"));

		playButton	= new Button(50, 76, play.getRegionWidth(), play.getRegionHeight(), play, play);

		Gdx.input.setInputProcessor(new SplashInputHandler(game, playButton));
	}

	private void setupTween() {
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		manager	= new TweenManager();
		float y	= girl.getY();

		Tween.to(girl, SpriteAccessor.POSITION_Y, 1f).target(y - 2)
			.ease(TweenEquations.easeNone).repeatYoyo(Tween.INFINITY, .01f)
			.start(manager)
		;
	}

	@Override
	public void render(float delta) {
		manager.update(delta);

		Gdx.gl.glViewport((int) Game.viewport.x, (int) Game.viewport.y, (int) Game.viewport.width, (int) Game.viewport.height);

		// Fill the entire screen with black, to prevent potential flickering.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw map frame
		spriteBatch.begin();
		spriteBatch.enableBlending();

		spriteBatch.draw(background, 0, 0, Game.V_WIDTH, Game.V_HEIGHT);

		girl.draw(spriteBatch);
		playButton.draw(spriteBatch);

		spriteBatch.end();
	}

	@Override
	public void hide() {

	}

	@Override
	public void resize(int width, int height) {
		game.calculateViewport(width, height);
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
