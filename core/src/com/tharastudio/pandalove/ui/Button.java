package com.tharastudio.pandalove.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tharastudio.pandalove.Game;

public class Button {
	private float x, y, width, height;

	private TextureRegion buttonUp, buttonDown;

	private Rectangle bounds;
	private boolean isPressed = false;

	public Button(float x, float y, float width, float height, TextureRegion buttonUp, TextureRegion buttonDown) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.buttonUp = buttonUp;
		this.buttonDown = buttonDown;

		bounds	= new Rectangle(x, y, width, height);
	}

	public void draw(SpriteBatch spriteBatch) {
		if (isPressed) {
			spriteBatch.draw(buttonDown, x, y, width, height);
		} else {
			spriteBatch.draw(buttonUp, x, y, width, height);
		}
	}

	private boolean isClicked(int screenX, int screenY) {
		return bounds.contains(screenX, screenY);
	}

	public boolean isTouchDown(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY)) {
			isPressed	= true;
			return true;
		}

		return false;
	}

	public boolean isTouchUp(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY) && isPressed) {
			isPressed	= false;
			return true;
		}

		isPressed	= false;
		return false;
	}
}
