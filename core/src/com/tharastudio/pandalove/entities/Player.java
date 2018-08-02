package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tharastudio.pandalove.helpers.B2DVars;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.worlds.GameWorld;

public class Player extends B2DSprite {
	private GameWorld gameWorld;
	private float velocityX	= 0.9f;
	private TextureRegion[] rsprites;

	public Player(Body body, GameWorld gameWorld) {
		super(body);

		this.gameWorld			= gameWorld;

		Texture texture			= Game.assets.getTexture("player");
		TextureRegion[] sprites	= TextureRegion.split(texture, 13, 18)[0];

		rsprites				= new TextureRegion[sprites.length];
		setAnimation(sprites, 1 / 10f);

		for (int i = 0; i < sprites.length; i++) {
			rsprites[i]		= new TextureRegion(sprites[i]);
			rsprites[i].flip(true, false);
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		body.setLinearVelocity(velocityX, body.getLinearVelocity().y);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		TextureRegion frame;

		if (velocityX < 0) {
			frame	= rsprites[animation.getCurrentFrame()];
		} else {
			frame	= animation.getFrame();
		}

		spriteBatch.begin();
		spriteBatch.draw(frame, body.getPosition().x * B2DVars.PPM - width / 2, body.getPosition().y * B2DVars.PPM - height / 2);
		spriteBatch.end();
	}

	public void changeDirection() {
		velocityX	= -velocityX;
	}

	public void jump() {
		if (canJump()) {
			Game.assets.getSound("fall").play();

			body.setLinearVelocity(body.getLinearVelocity().x, 0);
			body.applyForceToCenter(0, 380 / B2DVars.PPM, true);
		}
	}

	public boolean canJump() {
		return gameWorld.playerCanJump();
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}
}
