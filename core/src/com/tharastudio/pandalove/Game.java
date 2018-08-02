package com.tharastudio.pandalove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tharastudio.pandalove.helpers.AssetLoader;
import com.tharastudio.pandalove.screens.GameScreen;
import com.tharastudio.pandalove.screens.SplashScreen;

public class Game extends com.badlogic.gdx.Game {
	public static final float V_WIDTH		= 320;
	public static final float V_HEIGHT		= 192;
	public static final float ASPECT_RATIO	= V_WIDTH / V_HEIGHT;
	public static final float STEP = 1 / 60f;

	public static AssetLoader assets;
	public static Rectangle viewport;
	public static float scale;
	public static Vector2 crop;

	public SplashScreen splashScreen;
	public GameScreen gameScreen;

	@Override
	public void create () {
		// Load assets
		assets = new AssetLoader();

		calculateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Get screen instances
		splashScreen	= new SplashScreen(this);
		gameScreen		= new GameScreen(this);

		// View splash screen first
		setScreen(splashScreen);
	}

	@Override
	public void dispose() {
		super.dispose();

		assets.dispose();
	}

	public void calculateViewport(float width, float height) {
		float aspectRatio	= width / height;
		scale				= 1;
		crop				= new Vector2(0, 0);

		if (aspectRatio > ASPECT_RATIO) {
			scale	= height / V_HEIGHT;
			crop.x	= (width - V_WIDTH * scale) / 2;
		} else if (aspectRatio < ASPECT_RATIO) {
			scale	= width / V_WIDTH;
			crop.y	= (height - V_HEIGHT * scale) / 2;
		} else {
			scale	= width / V_WIDTH;
		}

		float w	= V_WIDTH * scale;
		float h	= V_HEIGHT * scale;

		viewport	= new Rectangle(crop.x, crop.y, w, h);
	}
}
