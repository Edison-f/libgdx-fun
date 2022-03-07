package com.test.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {

	public Player samurai;
	public Enemy cruKnight;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	public int windowWidth = 1280;
	public int windowHeight = 720;

	BitmapFont font;

	/**
	 * Creates the objects on program start
	 */
	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, windowWidth, windowHeight);
		batch = new SpriteBatch();

		samurai = new Player("samurai.png",
				(1280 / 2 - 152 / 2), 40, 152,
				191, "sword.png", 300,
				82, windowWidth, windowHeight, 600, 100,
				100, 0.2, 20);

		cruKnight = new Enemy("crucible knight.png",
				(windowWidth / 2 - 143 / 2), windowHeight - 300,
				143, 265, "esword.png",
				300, 82, windowWidth, windowHeight, 1200, 250,
				200, 0.3, 80);

		font = new BitmapFont();

		Gdx.gl.glLineWidth(4);
	}

	/**
	 * Renders the program
	 * Tries to run every screen refresh if possible
	 */
	@Override
	public void render () {
		ScreenUtils.clear(0.4f, 0.6f, 0.8f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		samurai.draw();
		cruKnight.draw();
		batch.begin();
		periodic();
		font.draw(batch, String.valueOf(Gdx.graphics.getDeltaTime()), 40, 40);
//		font.draw(batch, String.valueOf(samurai.getWindupTimer()), 40, 80);
//		font.draw(batch, String.valueOf(samurai.getAttackTimer()), 40, 120);
		font.draw(batch, String.valueOf(System.currentTimeMillis()), 40, 160);
		batch.end();
		processInput();
	}

	/**
	 * Runs at the end of the program to get rid of resources
	 */
	@Override
	public void dispose () {
		samurai.dispose();
		cruKnight.dispose();
	}

	/**
	 * Runs every render loop
	 * Handles time for various functions that require time to work correctly
	 */
	public void periodic() {
		if(samurai.getStates()[0] && samurai.timeFromStart() > 400) {
			samurai.attack();
		}
		if(samurai.getStates()[1] && samurai.timeFromStart() > 500) {
			samurai.stopAttack();
		}
		samurai.aliveCheck();
	}


	public void followPlayer() {
		// delta distance (player-current)
		// center: x + half of width, y + half of height

		//follow
		// int dist = 
		// cruKnight.changePos(dist*walkspeedmultiplier, 0);
		
		double enemyX = cruKnight.getEnemyHitbox().getX() + (cruKnight.getEnemyHitbox().getWidth()/2);
		double enemyY = cruKnight.getEnemyHitbox().getY() + (cruKnight.getEnemyHitbox().getHeight()/2);
	}

	/**
	 * Processes keyboard inputs to change the state of the player character
	 */
	public void processInput() {
		if(samurai.isAlive) {
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				samurai.changePos((int) (-250 * Gdx.graphics.getDeltaTime()), 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				samurai.changePos((int) (250 * Gdx.graphics.getDeltaTime()), 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
				samurai.changePos(0, (int) (250 * Gdx.graphics.getDeltaTime()));
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				samurai.changePos(0, (int) (-250 * Gdx.graphics.getDeltaTime()));
			}
			if(Gdx.input.isKeyPressed(Input.Keys.R)) {
				samurai.windup();
				samurai.takeDamage(100, 0);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.P)) {
			samurai.revive();;
		}
		samurai.checkBounds();
	}

	/**
	 * Deals with detecting if an attack is hitting the player or the enemy
	 */
	public void hitDetection(Player player, Enemy enemy) {
		if (player.getAttackHitbox().overlaps(enemy.getEnemyHitbox())){
			enemy.takeDamage(1,1);
		} else if (enemy.getAttackHitbox().overlaps(player.getHitbox())){
			player.takeDamage(1, 1);
		}
	}

	/**
	 * Deals with detecting if the enemy is colliding with the player and vice versa and prevents it
	 */
	public void collisionDetect(Rectangle playerHitbox, Rectangle enemyHitbox) {
		if (playerHitbox.overlaps(enemyHitbox)){

		}
	}

}
