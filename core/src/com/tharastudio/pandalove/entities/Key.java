package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tharastudio.pandalove.Game;

public class Key extends B2DSprite {

	public Key(Body body) {
		super(body);

		Texture texture			= Game.assets.getTexture("key");
		TextureRegion[] sprites	= TextureRegion.split(texture, 8, 8)[0];

		setAnimation(sprites, 1 / 5f);
	}
}
