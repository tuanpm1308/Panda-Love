package com.tharastudio.pandalove.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tharastudio.pandalove.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title	= "Panda Love Game by TharaStudio";
		config.width	= 640;
		config.height	= 384;

		new LwjglApplication(new Game(), config);
	}
}
