package com.tharastudio.pandalove.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.tharastudio.pandalove.Game;
import com.tharastudio.pandalove.helpers.B2DVars;

public class Bar extends B2DSprite {
	public static final boolean TYPE_TOP	= true;
	public static final boolean TYPE_BOTTOM	= false;

	private TiledDrawable tiledDrawable;
	private float tiledWidth, tiledHeight;
	private boolean type;
	private TextureRegion grass;

	public Bar(Body body, boolean type, float tiledWidth, float tiledHeight) {
		super(body);

		this.tiledWidth		= tiledWidth;
		this.tiledHeight	= tiledHeight;
		this.type			= type;

		Texture texture			= Game.assets.getTexture("bar");
		TextureRegion textureRegion	= new TextureRegion(texture, 0, (int) (texture.getHeight() - tiledHeight * B2DVars.PPM), (int) (tiledWidth * B2DVars.PPM), (int) (tiledHeight * B2DVars.PPM));

		if (type != TYPE_TOP) {
			textureRegion.flip(false, true);

			grass			= new TextureRegion(Game.assets.getTexture("grass"));
		}

		animation.setFrames(textureRegion);

		tiledDrawable	= new TiledDrawable();
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		tiledDrawable.setRegion(animation.getFrame());

		spriteBatch.begin();
		tiledDrawable.draw(spriteBatch, body.getPosition().x * B2DVars.PPM - tiledWidth * B2DVars.PPM / 2, body.getPosition().y * B2DVars.PPM - tiledHeight * B2DVars.PPM / 2, tiledWidth * B2DVars.PPM, tiledHeight * B2DVars.PPM);

		if (type != TYPE_TOP) {
			spriteBatch.draw(grass, body.getPosition().x * B2DVars.PPM - tiledWidth * B2DVars.PPM / 2 - 2, body.getPosition().y * B2DVars.PPM - tiledHeight * B2DVars.PPM / 2 - 3);
		}

		spriteBatch.end();
	}
}
