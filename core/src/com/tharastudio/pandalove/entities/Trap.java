package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.tharastudio.pandalove.helpers.B2DVars;
import com.tharastudio.pandalove.Game;

public class Trap extends B2DSprite {
	private TiledDrawable tiledDrawable;
	private float tiledWidth, tiledHeight;

	public Trap(Body body, float tiledWidth, float tiledHeight) {
		super(body);

		this.tiledWidth		= tiledWidth;
		this.tiledHeight	= tiledHeight;

		Texture texture			= Game.assets.getTexture("trap");
		TextureRegion textureRegion	= new TextureRegion(texture, 8, 8);

		animation.setFrames(textureRegion);

		tiledDrawable	= new TiledDrawable();
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		tiledDrawable.setRegion(animation.getFrame());

		spriteBatch.begin();
		tiledDrawable.draw(spriteBatch, body.getPosition().x * B2DVars.PPM - tiledWidth * B2DVars.PPM / 2, body.getPosition().y * B2DVars.PPM - tiledHeight * B2DVars.PPM / 2, tiledWidth * B2DVars.PPM, tiledHeight * B2DVars.PPM);
		spriteBatch.end();
	}
}
