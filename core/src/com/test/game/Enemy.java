package com.test.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

    private SpriteBatch batch;

    private Texture enemyImage;
    private Texture attackImage;
    private Texture windupImage;

    private Rectangle enemy;
    private Rectangle attack;
    private Rectangle windup;

    private boolean isAttacking = false;
    private boolean isWindup = false;
    private boolean isAlive = true;
    private boolean isTakingDamage = false;

    private boolean isIframe = false;

    private int windowWidth;
    private int windowHeight;

    private long timerStart;
    private long damageTimerStart;

    public int health;
    public int stamina;
    public int magic;
    public double absorption;
    public int staggerHealth = 0;
    public int staggerMax;

    /**
     * Creates an enemy
     * 
     * @param enemyImageName
     * @param x
     * @param y
     * @param enemyW
     * @param enemyH
     * @param attackImageName
     * @param attackW
     * @param attackH
     * @param windowW
     * @param windowH
     * @param hp
     * @param stam
     * @param mag
     * @param absor
     * @param stag
     */
    public Enemy(String enemyImageName, int x, int y, int enemyW,
                 int enemyH, String attackImageName, int attackW,
                 int attackH, int windowW, int windowH,
                 int hp, int stam, int mag, double absor, int stag) {
        
        health = hp;
        stamina = stam;
        magic = mag;
        absorption = absor;
        staggerMax = stag;

        batch = new SpriteBatch();

        enemy = new Rectangle();

        enemy.x = x;
        enemy.y = y;
        enemy.width = enemyH;
        enemy.height = enemyH;

        enemyImage = new Texture(enemyImageName);

        attack = new Rectangle();

        attack.x = 0;
        attack.y = 0;
        attack.height = attackH;
        attack.width = attackW;

        attackImage = new Texture(attackImageName);

        windup = new Rectangle();

        windup.x = 0;
        windup.y = 0;
        windup.height = 159;
        windup.width = 159;

        windupImage = new Texture("attack.png");

        windowWidth = windowW;
        windowHeight = windowH;
    }


    public void changePos(int changeX, int changeY) {
        enemy.x += changeX;
        enemy.y += changeY;

    }


    public void draw() {
        batch.begin();
        if(isAlive) {
            batch.draw(enemyImage, enemy.x, enemy.y);
        }

        if(isAttacking) {
            batch.draw(attackImage, enemy.x, enemy.y + enemy.height);
        }

        if(isWindup) {
            batch.draw(windupImage, enemy.x + (enemy.width / 2), enemy.y + (enemy.height / 2));
        }
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        enemyImage.dispose();

        attackImage.dispose();
        windupImage.dispose();
    }

    /**
     * Creates a rectangle representing the location and dimensions of the enemy
     *
     * @return A rectangle with the XY coordinates and the width and height of the enemy
     */
    public Rectangle getEnemyHitbox() {
        return enemy;
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
        if(!isTakingDamage) {
            iFrame();
            health -= healthDamage * absorption;
            staggerHealth -= postureDamage;
            // isTakingDamage = true;
            // damageTimerStart = System.currentTimeMillis();
        }
        return new int[] {health, staggerHealth};
    }

    public int getHealth() {
        return health;
    }

    public int timeFromDamage() {
        return (int) (System.currentTimeMillis() - damageTimerStart);
    }

    /**
     * Gets the states of the Enemy
     * @return {isAttacking, isWindup, isTakingDamage, isAlive} 
     */
    public boolean[] getStates() {
        return new boolean[] {isAttacking, isWindup, isAlive, isTakingDamage};
    }

    public void iFrame() {
        damageTimerStart = System.currentTimeMillis();
        isTakingDamage = true;
    }

    public void endIframe() {
        isTakingDamage = false;
    }

    public void revive() {
        isAlive = true;
        health = 1200;
    }
    
}
