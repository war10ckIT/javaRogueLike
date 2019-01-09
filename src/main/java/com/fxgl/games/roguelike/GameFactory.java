package com.fxgl.games.roguelike;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.SpawnSymbol;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.TextEntityFactory;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.fxgl.games.roguelike.control.BombControl;
import com.fxgl.games.roguelike.control.PlayerControl;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameFactory implements TextEntityFactory {

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return Entities.builder()
                .at(0, 0)
                .viewFromNodeWithBBox(new EntityView(new Rectangle(600, 600, Color.LIGHTGREEN)))
                .renderLayer(RenderLayer.BACKGROUND)
				.build();
    }

    @SpawnSymbol('w')
    public Entity newWall(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.WALL)
                .from(data)
                .viewFromNode(new Rectangle(40, 40, Color.GRAY.saturate()))
                .build();
    }

    @SpawnSymbol('b')
    public Entity newBrick(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.BRICK)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick.png", 40, 40))
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.PLAYER)
                .from(data)
                .viewFromNodeWithBBox(new Rectangle(GameApp.TILE_SIZE, GameApp.TILE_SIZE, Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }

    @Spawns("Bomb")
    public Entity newBomb(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.BOMB)
                .from(data)
                .viewFromNodeWithBBox(new Circle(GameApp.TILE_SIZE / 2, Color.BLACK))
                .with(new BombControl(data.get("radius")))
                .build();
    }

    @Spawns("Powerup")
    public Entity newPowerup(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.POWERUP)
                .from(data)
                .viewFromNodeWithBBox(new Rectangle(GameApp.TILE_SIZE, GameApp.TILE_SIZE, Color.YELLOW))
                .with(new CollidableComponent(true))
                .build();
    }

    @Override
    public char emptyChar() {
        return '0';
    }

    @Override
    public int blockWidth() {
        return 40;
    }

    @Override
    public int blockHeight() {
        return 40;
    }
}
