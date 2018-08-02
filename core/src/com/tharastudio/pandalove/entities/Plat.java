package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tharastudio.pandalove.Game;

public class Plat extends B2DSprite {

	public Plat(Body body) {
		super(body);

		Texture texture			= Game.assets.getTexture("plat");
		TextureRegion textureRegion	= new TextureRegion(texture);

		setAnimation(textureRegion);
	}
}
