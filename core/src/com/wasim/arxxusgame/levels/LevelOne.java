package com.wasim.arxxusgame.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.uwsoft.editor.renderer.Overlap2DStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.ImageItem;
import com.wasim.arxxusgame.ArxxusGame;
import com.wasim.arxxusgame.controller.PlayerController;
import com.wasim.arxxusgame.global.Data;
import com.wasim.arxxusgame.global.GameSoundEffects;
import com.wasim.arxxusgame.global.Life;

public class LevelOne extends Overlap2DStage implements Screen {

	ArxxusGame game;
	PlayerController playerController;

	CompositeItem itemPlayer;
	ImageItem imageHome;
	
	public LevelOne(ArxxusGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		clear();
		initSceneLoader();
		sceneLoader.loadScene("level_one");
		addActor(sceneLoader.getRoot());

		itemPlayer = sceneLoader.sceneActor.getCompositeById("player");
		imageHome = sceneLoader.sceneActor.getImageById("home");

		playerController = new PlayerController(this);
		itemPlayer.addScript(playerController);

		game.homeXPosition = imageHome.getX();

		game.backTexture = new Texture(Gdx.files.internal("back/back3.png"));

		Data.LEVEL = 1;
		Data.SCORE = 0;
		//Data.saveData();
		Life.health = 100;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void render(float delta) {
		if (!Life.dead) {
			float xPos = itemPlayer.getX();

			if (xPos > game.homeXPosition) {
				GameSoundEffects.homeSound.play();
				Data.LEVEL = 2;
				game.levelLoader.loadLevel(game, Data.LEVEL);
			}

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.drawBackTexture(0, 0, 960, 540);
			act(delta);
			draw();

		} else {
			game.setScreen(game.gameOver);
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		// imageHome.dispose();
		// itemPlayer.dispose();
		// playerController.dispose();
		System.out.println("At LevelOne.dispose()");
		super.dispose();
	}

}
