package com.fxgl.games.roguelike;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.fxgl.games.roguelike.control.PlayerControl;

import javafx.scene.input.KeyCode;

public class GameApp extends GameApplication {

    public static final int TILE_SIZE = 40;

    private Entity player;
    private PlayerControl playerControl;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("RogueLike App");
        settings.setVersion("0.1");
        settings.setWidth(600);
        settings.setHeight(600);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                playerControl.moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                playerControl.moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                playerControl.moveDown();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                playerControl.moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                playerControl.placeBomb();
            }
        }, KeyCode.F);
    }

    @Override
    protected void initGame() {
    	GameFactory factory = new GameFactory();
    	getGameWorld().addEntityFactory(factory);
    	
        TextLevelParser levelParser = new TextLevelParser(factory);

        Level level = levelParser.parse("levels/0.txt");
        getGameWorld().setLevel(level);

        getGameWorld().spawn("BG");

        player = getGameWorld().spawn("Player");
        playerControl = player.getComponent(PlayerControl.class);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.POWERUP) {
            @Override
            protected void onCollisionBegin(Entity pl, Entity powerup) {
                powerup.removeFromWorld();
                playerControl.increaseMaxBombs();
            }
        });
    }

    public void onWallDestroyed(Entity wall) {
        if (FXGLMath.randomBoolean()) {
            int x = wall.getPositionComponent().getGridX(GameApp.TILE_SIZE);
            int y = wall.getPositionComponent().getGridY(GameApp.TILE_SIZE);

            getGameWorld().spawn("Powerup", x*40, y*40);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}