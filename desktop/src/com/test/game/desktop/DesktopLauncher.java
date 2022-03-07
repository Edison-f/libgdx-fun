package com.test.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.test.game.MyGdxGame;

public class DesktopLauncher {


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "ELDEN RING";
		config.width = 1280;
		config.height = 720;
		config.addIcon("old ring.jpg", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
