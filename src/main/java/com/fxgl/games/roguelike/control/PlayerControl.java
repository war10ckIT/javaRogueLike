package com.fxgl.games.roguelike.control;


import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.TypeComponent;
import com.fxgl.games.roguelike.GameApp;
import com.fxgl.games.roguelike.GameTypes;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class PlayerControl extends Component {

	private PositionComponent position;
	private int maxBombs = 1;
	private int bombsPlaced = 0;

	@Override
	public void onUpdate(double tpf) { }

	public void increaseMaxBombs() {
		maxBombs++;
	}

	public void placeBomb() {
		if (bombsPlaced == maxBombs) {
			return;
		}

		bombsPlaced++;

		int x = position.getGridX(GameApp.TILE_SIZE);
		int y = position.getGridY(GameApp.TILE_SIZE);

		Entity bomb = FXGL.getApp()
				.getGameWorld()
				.spawn("Bomb", new SpawnData(x * 40, y * 40).put("radius", GameApp.TILE_SIZE / 2));

		FXGL.getMasterTimer().runOnceAfter(() -> {
			bomb.getComponent(BombControl.class).explode();
			bombsPlaced--;
		}, Duration.seconds(2));
	}

	public void moveRight() {
		if (canMove(new Point2D(GameApp.TILE_SIZE, 0))) {
			position.translateX(GameApp.TILE_SIZE);
		}
	}

	public void moveLeft() {
		if (canMove(new Point2D(-GameApp.TILE_SIZE, 0))) {
			position.translateX(-GameApp.TILE_SIZE);
		}
	}

	public void moveUp() {
		if (canMove(new Point2D(0, -GameApp.TILE_SIZE))) {
			position.translateY(-GameApp.TILE_SIZE);
		}
	}

	public void moveDown() {
		if (canMove(new Point2D(0, GameApp.TILE_SIZE))) {
			position.translateY(GameApp.TILE_SIZE);
		}
	}

	private boolean canMove(Point2D direction) {
		Point2D newPosition = position.getValue().add(direction);

		return FXGL.getApp()
				.getGameScene()
				.getViewport()
				.getVisibleArea()
				.contains(newPosition)
				
				&&
				newPosition.getX()>= 0 && newPosition.getX()< FXGL.getApp().getGameScene().getWidth()
				&&
				newPosition.getY()>= 0 && newPosition.getY()< FXGL.getApp().getGameScene().getHeight()
				&&

				FXGL.getApp()
				.getGameWorld()
				.getEntitiesAt(newPosition)
				.stream()
				.filter(e -> e.hasComponent(TypeComponent.class))
				.map(e -> e.getComponent(TypeComponent.class))
				.noneMatch(type -> type.isType(GameTypes.BRICK)
						|| type.isType(GameTypes.WALL)
						|| type.isType(GameTypes.BOMB));
	}
}