package com.test.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class Player {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Texture playerImage;
    private Sprite playerSprite;
    private Texture attackImage;
    private Sprite attackSprite;
    private Texture windupImage;
    private Sprite windupSprite;

    private Rectangle player;
    private Rectangle attack;
    private Rectangle windup;

    public boolean isAttacking = false;
    public boolean isWindup = false;
    public boolean isAlive = true;

    private int windowWidth;
    private int windowHeight;

    private long timerStart;
    private long iFrameTimer;

    public int maxHp;
    public int health;
    public int maxStamina;
    public double stamina;
    public int maxMagic;
    public int magic;
    public double absorption;
    public int staggerHealth = 0;
    public int staggerMax;

    public double movementModifier = 1.0;

    public Player(String playerImageName, int playerX, int playerY,
                  int playerW, int playerH, String attackImageName,
                  int attackW, int attackH, int windowW, int windowH,
                  int hp, int stam, int mag, double absor, int stag) {

        health = hp;
        maxHp = hp;
        stamina = stam;
        maxStamina = stam;
        maxMagic = mag;
        magic = mag;
        absorption = absor;
        staggerMax = stag;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        player = new Rectangle();

        player.x = playerX;
        player.y = playerY;
        player.width = playerW;
        player.height = playerH;

        playerImage = new Texture(playerImageName);
        playerSprite = new Sprite(playerImage);

        attack = new Rectangle();

        attack.x = 0;
        attack.y = 0;
        attack.height = attackH;
        attack.width = attackW;

        attackImage = new Texture(attackImageName);
        attackSprite = new Sprite(attackImage);

        windup = new Rectangle();

        windup.x = 0;
        windup.y = 0;
        windup.height = 159;
        windup.width = 159;

        windupImage = new Texture("attack.png");
        windupSprite = new Sprite(windupImage);

        windowWidth = windowW;
        windowHeight = windowH;

    }

    /**
     * @param changeX Horizontal change with positive being right and negative being left
     * @param changeY Vertical change with positive being up and negative being down
     *
     * Changes the position of the player based on X and Y coordinates given
     */
    public void move(int changeX, int changeY) {
        player.x += changeX * movementModifier;
        player.y += changeY * movementModifier;

    }

    /**
     * Checks to see if any part of the player is going out of bounds and prevents it
     */
    public void checkBounds() {
        if(player.x < 0) {
            player.x = 0;
        }
        if(player.x > windowWidth - player.width) {
            player.x = windowWidth - player.width;
        }
        if(player.y < 0) {
            player.y = 0;
        }
        if(player.y > windowHeight - player.height) {
            player.y = windowHeight - player.height;
        }
    }

    /**
     * Draws the player, attack, and windup images if necessary
     */
    public void draw() {
        attack.x = player.x;
        attack.y = player.y + player.height;
        
        batch.begin();
        if(isAlive) {
           batch.draw(playerSprite, player.x, player.y);
        }

        if(isAttacking && isAlive) {
            batch.draw(attackSprite, attack.x, attack.y);
        }

        if(isWindup && isAlive) {
            batch.draw(windupSprite, player.x + (player.width / 2), player.y + (player.height / 2));
        }
        batch.end();

        statusBar();

    }

    /**
     * Draws the health bar
     */
    public void statusBar() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(12, windowHeight - 19, health - 2, 12);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(12, windowHeight - 39, magic * 2 - 2, 12);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(12, windowHeight - 59, (int) stamina * 4 - 2, 12);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(10, windowHeight - 20, maxHp, 15);
        shapeRenderer.rect(10, windowHeight - 40, maxMagic * 2, 15);
        shapeRenderer.rect(10, windowHeight - 60, maxStamina * 4, 15);
        shapeRenderer.end();
    }

    /**
     * Disposes of all the resources used
     */
    public void dispose() {
        batch.dispose();
        playerImage.dispose();
        attackImage.dispose();
        windupImage.dispose();
        shapeRenderer.dispose();
    }

    /**
     * Starts the timer and changes the characters state to winding up after checking that it can
     */
    public void windup() {
        if(!isAttacking && !isWindup && isAlive) {
            timerStart = System.currentTimeMillis();
            isWindup = true;
            movementModifier = 0.5;
        }
    }

    /**
     * Changes the state of the player to not be winding up and to be attacking
     */
    public void attack() {
        isWindup = false;
        isAttacking = true;
        movementModifier = 0.25;
    }

    /**
     * Runs at the end of the attack to finish resetting the player's state
     */
    public void stopAttack() {
        isAttacking = false;
        movementModifier = 1.0;
    }

    /**
     * Increases the player's movement speed and reduces their stamina
     */
    public void sprint() {
        movementModifier = 1.5;
        if(stamina > 0) {
            stamina -= 60 * Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Regenerates stamina
     */
    public void regenStamina() {
        if(stamina < maxStamina) {
            stamina += 40 * Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Calculates the difference between the time from when the timer started to the current time
     *
     * @return The difference between now and the start of the timer
     */
    public long timeFromStart() {
        return System.currentTimeMillis() - timerStart;
    }

    /**
     * Creates an array of the 3 main states of the player
     *
     * @return If it's winding up, if it's attacking
     */
    public boolean[] getStates() {
        return new boolean[] {isWindup, isAttacking, isAlive};
    }

    /**
     * Creates a rectangle representing the location and dimensions of the enemy
     *
     * @return A rectangle with the XY coordinates and the width and height of the player
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) player.x, (int) player.y,
                (int) player.width, (int) player.height);
    }

    /**
     * Creates a rectangle representing the location of the attack
     *
     * @return A rectangle created from the XY coordinates and the width and height of the attack
     */
    public Rectangle getAttackHitbox() {
        return new Rectangle ((int) attack.x, (int) attack.y,
                (int) attack.width, (int) attack.height);
    }

    public int[] takeDamage(int healthDamage, int postureDamage) {
        health -= healthDamage * absorption;
        staggerHealth -= postureDamage;
        if(health < 0 || !isAlive) {
            health = 0;
        }
        return new int[] {health, staggerHealth};
    }

    public void revive() {
        health = maxHp;
    }

    public void aliveCheck() {
        if(health <= 0) {
            isAlive = false;
        } else {
            isAlive = true;
        }
    }

    public void iFrame() {
        
    }
}
