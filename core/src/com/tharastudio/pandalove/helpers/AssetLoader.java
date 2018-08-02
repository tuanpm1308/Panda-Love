package com.tharastudio.pandalove.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class AssetLoader {
	private HashMap<String, Texture> textures;
	private HashMap<String, Sound> sounds;
	private HashMap<String, Music> musics;

	public AssetLoader() {
		// Load all textures
		textures	= new HashMap<String, Texture>();

		loadTexture("images/screen.png", "screen");
		loadTexture("images/plat.png", "plat");
		loadTexture("images/hole.png", "hole");
		loadTexture("images/key.png", "key");
		loadTexture("images/trap.png", "trap");
		loadTexture("images/player.png", "player");
		loadTexture("images/girl.png", "girl");
		loadTexture("images/start.png", "start");
		loadTexture("images/continue.png", "continue");
		loadTexture("images/bar.png", "bar");
		loadTexture("images/grass.png", "grass");

		// Load all sounds
		sounds		= new HashMap<String, Sound>();
		musics		= new HashMap<String, Music>();

		loadSound("sounds/coin.ogg", "coin");
		loadSound("sounds/fall.ogg", "fall");
		loadSound("sounds/start.ogg", "start");
		loadSound("sounds/wrong.ogg", "wrong");
		loadMusic("sounds/song.ogg", "song");
	}

	public void loadTexture(String path, String key) {
		Texture texture	= new Texture(Gdx.files.internal(path));
		textures.put(key, texture);
	}

	public Texture getTexture(String key) {
		return textures.get(key);
	}


	public void loadSound(String path, String key) {
		Sound sound	= Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(key, sound);
	}

	public Sound getSound(String key) {
		return sounds.get(key);
	}

	public void loadMusic(String path, String key) {
		Music music	= Gdx.audio.newMusic(Gdx.files.internal(path));
		musics.put(key, music);
	}

	public Music getMusic(String key) {
		return musics.get(key);
	}

	public void dispose() {
		for (Texture texture : textures.values()) {
			texture.dispose();
		}

		for (Sound sound: sounds.values()) {
			sound.dispose();
		}

		for (Music music: musics.values()) {
			music.dispose();
		}
	}
}
