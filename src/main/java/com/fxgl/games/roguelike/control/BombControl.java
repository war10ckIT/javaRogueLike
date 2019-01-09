package com.fxgl.games.roguelike.control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.fxgl.games.roguelike.GameApp;
import com.fxgl.games.roguelike.GameTypes;

public class BombControl extends Component {

    private int radius;

    public BombControl(int radius) {
        this.radius = radius;
    }

    @Override
    public void onUpdate(double tpf) { }

    public void explode() {
        BoundingBoxComponent bbox = getEntity().getBoundingBoxComponent();

        FXGL.getApp()
                .getGameWorld()
                .getEntitiesInRange(bbox.range(radius, radius))
                .stream()
                .filter(e -> e.isType(GameTypes.BRICK))
                .forEach(e -> {
                    FXGL.<GameApp>getAppCast().onWallDestroyed(e);
                    e.removeFromWorld();
                });

        getEntity().removeFromWorld();
    }
}
