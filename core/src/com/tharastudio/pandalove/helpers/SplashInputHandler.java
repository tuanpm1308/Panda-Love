package com.tharastudio.pandalove.helpers;

import com.badlogic.gdx.InputProcessor;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.ui.Button;

public class SplashInputHandler implements InputProcessor {
	private Game game;
	private Button playButton;

	public SplashInputHandler(Game game, Button playButton) {
		this.game		= game;
		this.playButton	= playButton;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX	= scaleX(screenX);
		screenY	= scaleY(screenY);

		playButton.isTouchDown(screenX, screenY);

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX	= scaleX(screenX);
		screenY	= scaleY(screenY);

		if (playButton.isTouchUp(screenX, screenY)) {
			game.setScreen(game.gameScreen);

			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / Game.scale) - (int) (Game.crop.x / Game.scale);
	}

	private int scaleY(int screenY) {
		return (int) Game.V_HEIGHT - (int) (screenY / Game.scale) + (int) (Game.crop.y / Game.scale);
	}
}
