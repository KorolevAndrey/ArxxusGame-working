package com.wasim.arxxusgame.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.Overlap2DStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.ImageItem;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;
import com.wasim.arxxusgame.ArxxusGame;
import com.wasim.arxxusgame.controller.PlayerCollisionHandler;
import com.wasim.arxxusgame.controller.PlayerController;
import com.wasim.arxxusgame.global.AmbienceColorInfo;
import com.wasim.arxxusgame.global.Data;
import com.wasim.arxxusgame.global.GameSoundEffects;
import com.wasim.arxxusgame.global.Life;
import com.wasim.arxxusgame.global.PlayerDataRenderer;

public class LevelTwo extends Overlap2DStage implements Screen {

	ArxxusGame game;
	ImageItem imageSaw1, imageSaw2, imageBlade, imageHome;
	ImageItem imagePause;

	// player
	CompositeItem itemPlayer, itemSpike1, itemSpike2;
	PlayerController playerController;
	PlayerDataRenderer playerDataRenderer;

	// coins
	List<SpriteAnimation> coins = new ArrayList<SpriteAnimation>();

	// background ambience color when player hit to spikes
	AmbienceColorInfo ambienceColorInfo = new AmbienceColorInfo();

	public LevelTwo(ArxxusGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		clear();
		initSceneLoader();
		sceneLoader.loadScene("level_two");
		addActor(sceneLoader.getRoot());

		itemPlayer = sceneLoader.sceneActor.getCompositeById("player");
		// playerAnimation = itemPlayer.getSpriteAnimationById("animation");
		itemSpike1 = sceneLoader.sceneActor.getCompositeById("item_spike1");
		itemSpike2 = sceneLoader.sceneActor.getCompositeById("item_spike2");

		imageSaw1 = sceneLoader.sceneActor.getImageById("image_saw1");
		imageSaw2 = sceneLoader.sceneActor.getImageById("image_saw2");
		imageBlade = sceneLoader.sceneActor.getImageById("blade");
		imageHome = sceneLoader.sceneActor.getImageById("home");

		imagePause = sceneLoader.sceneActor.getImageById("btnPause");
		imagePause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.pauseAndPlayGame();
			}
		});

		imageSaw1.setOrigin(0);
		imageSaw2.setOrigin(0);
		imageBlade.setOrigin(0);

		game.homeXPosition = imageHome.getX();

		playerController = new PlayerController(this);
		itemPlayer.addScript(playerController);

		playerDataRenderer = new PlayerDataRenderer(game);

		// coins
		for (int i = 0; i < 32; i++) {
			coins.add(sceneLoader.sceneActor.getSpriteAnimationById("coin"
					+ (i + 1)));
		}

		game.backTexture = new Texture(Gdx.files.internal("back/back11.png"));

		Data.LEVEL = 2;
		Data.SCORE = 0;
		Data.COINS_COLLECTED = 0;
		// Data.saveData();
		Life.health = 100;
	}

	@Override
	public void act(float delta) {
		float playerX, playerY, playerHeight;
		float objectX, objectY, objectWidth, objectHeight;

		playerX = itemPlayer.getX();
		playerY = itemPlayer.getY();
		playerHeight = itemPlayer.getHeight() - 30;

		// pause button
		imagePause.setX(playerX-150);
		imagePause.setY(playerY + 320);

		// rotating
		//System.out.println("Balde:" + imageBlade.getX());
		imageSaw1.setRotation(imageSaw1.getRotation() + 200 * delta);
		imageSaw2.setRotation(imageSaw2.getRotation() + 200 * delta);
		imageBlade.setRotation(imageBlade.getRotation() + 100 * delta);

		// obstacles saw1
		objectX = imageSaw1.getX() - 40;
		objectY = imageSaw1.getY();
		objectWidth = imageSaw1.getWidth();
		objectHeight = imageSaw1.getHeight();

		// player overllaping code
		if (PlayerCollisionHandler.isOverlaps(playerX, playerY, objectX,
				objectY, objectWidth, objectHeight)) {
			sceneLoader.setAmbienceInfo(ambienceColorInfo.setRedAmbience());
			Life.dead = true;
			System.out.println("Dead at Saw1");
			GameSoundEffects.deadSound.play();
		} else {
			// obstacles big blade
			objectX = imageBlade.getX() - 60;
			objectY = imageBlade.getY() + 30;
			objectWidth = imageBlade.getWidth();
			objectHeight = imageBlade.getHeight();

			if (PlayerCollisionHandler
					.isOverlaps(playerX, playerY + playerHeight, objectX,
							objectY, objectWidth, objectHeight)) {
				sceneLoader.setAmbienceInfo(ambienceColorInfo.setRedAmbience());
				Life.dead = true;
				System.out.println("Dead at big blade");
				GameSoundEffects.deadSound.play();
			} else {
				// obstacles saw2
				objectX = imageSaw2.getX() - 40;
				objectY = imageSaw2.getY();
				objectWidth = imageSaw2.getWidth();
				objectHeight = imageSaw2.getHeight();

				if (PlayerCollisionHandler.isOverlaps(playerX, playerY,
						objectX, objectY, objectWidth, objectHeight)) {
					sceneLoader.setAmbienceInfo(ambienceColorInfo
							.setRedAmbience());
					Life.dead = true;
					System.out.println("Dead at Saw2");
					GameSoundEffects.deadSound.play();
				} else {
					// obstacles spike1
					objectX = itemSpike1.getX() - 60;
					objectY = itemSpike1.getY();
					objectWidth = itemSpike1.getWidth();
					objectHeight = itemSpike1.getHeight();

					if (PlayerCollisionHandler.isOverlaps(playerX, playerY,
							objectX, objectY, objectWidth, objectHeight)) {
						Life.health -= 0.2;
						System.out.println("Life-- at spike1");
						// ambient color
						sceneLoader.setAmbienceInfo(ambienceColorInfo
								.setRedAmbience());
						if (!GameSoundEffects.screamSound.isPlaying()) {
							GameSoundEffects.screamSound.play();
						}
					} else {
						// obstacles spike2
						objectX = itemSpike2.getX() - 60;
						objectY = itemSpike2.getY();
						objectWidth = itemSpike2.getWidth();
						objectHeight = itemSpike2.getHeight() + 30;

						if (PlayerCollisionHandler.isOverlaps(playerX, playerY,
								objectX, objectY, objectWidth, objectHeight)) {
							Life.health -= 0.2;
							System.out.println("Life-- at spike2");
							// ambient color
							sceneLoader.setAmbienceInfo(ambienceColorInfo
									.setRedAmbience());
							GameSoundEffects.screamSound.play();
						} else {
							// ambient color to white if not at both spikes
							sceneLoader.setAmbienceInfo(ambienceColorInfo
									.setWhiteAmbience());
							// coins
							Iterator<SpriteAnimation> iterator = coins
									.iterator();
							while (iterator.hasNext()) {
								SpriteAnimation coin = iterator.next();

								objectX = coin.getX() - 35;
								objectY = coin.getY() - 20;
								objectWidth = coin.getWidth();
								objectHeight = coin.getHeight();

								if (PlayerCollisionHandler.isOverlaps(playerX,
										playerY, objectX, objectY, objectWidth,
										objectHeight)) {
									Data.COINS_COLLECTED++;
									GameSoundEffects.coinSound.play();
									Data.SCORE += Data.COINS_COLLECTED * 5;
									System.out.println("coin x:" + coin.getX()
											+ " score:" + Data.SCORE);
									coin.remove();
									iterator.remove();
								}
							}
						}
					}
				}
			}
		}

		//System.out.println("Score:" + Data.SCORE);

		super.act(delta);
	}

	@Override
	public void render(float delta) {
		if (!Life.dead) {
			float xPos = itemPlayer.getX();

			if (xPos > game.homeXPosition && Data.COINS_COLLECTED > 19) {
				GameSoundEffects.homeSound.play();
				Data.LEVEL = 3;
				game.levelLoader.loadLevel(game, Data.LEVEL);
			}

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			game.drawBackTexture(0, 0, 960, 540);
			act(delta);
			draw();
			playerDataRenderer.renderPlayerData();

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
		coins.clear();
		System.out.println("At LevelTwo.dispose()");
		super.dispose();
	}

}
