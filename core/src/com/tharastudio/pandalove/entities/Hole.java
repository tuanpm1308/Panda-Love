package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tharastudio.pandalove.helpers.B2DVars;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.helpers.Rotation;

public class Hole extends B2DSprite {
	private Rotation rotation;

	public Hole(Body body) {
		super(body);

		Texture texture			= Game.assets.getTexture("hole");
		TextureRegion region	= new TextureRegion(texture);

		setAnimation(region);

		rotation	= new Rotation(18, 1 / 10f);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		rotation.update(delta);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		spriteBatch.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - width / 2, body.getPosition().y * B2DVars.PPM - height / 2, width / 2f, height / 2f, width, height, 1, 1, - rotation.getAngle());
		spriteBatch.end();
	}
}
