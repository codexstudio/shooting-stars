package com.codex.shootingstars;

import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Pool;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameObjectsContainer {

    private final int FAR_MAX_DISTANCE = 7500;
    private final int MEDIUM_MAX_DISTANCE = 5000;
    private final int CLOSE_MAX_DISTANCE = 2500;

    private int ticks;

    private Pixmap friendlyShipPixmap;
    private Pixmap enemyShipPixmap;
    private Pixmap asteroidPixmap;

    private List<GameObject> gameObjectsFar;
    private List<GameObject> gameObjectsMedium;
    private List<DrawableObject> gameObjectsClose;

    private Pool<FriendlyShip> friendlyPool;
    private Pool<EnemyShip> enemyPool;
    private Pool<Asteroid> asteroidPool;

    private Pool.PoolObjectFactory<FriendlyShip> friendlyPoolFactory;
    private Pool.PoolObjectFactory<EnemyShip> enemyPoolFactory;
    private Pool.PoolObjectFactory<Asteroid> asteroidPoolFactory;

    GameObjectsContainer(Graphics g) {
        ticks = 1;
        gameObjectsFar = new ArrayList<GameObject>();
        gameObjectsMedium = new ArrayList<GameObject>();
        gameObjectsClose = new ArrayList<DrawableObject>();

        friendlyShipPixmap = g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888);
        enemyShipPixmap = g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888);
        asteroidPixmap = g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888);

        friendlyPoolFactory = new Pool.PoolObjectFactory<FriendlyShip>() {
            @Override
            public FriendlyShip createObject() {
                FriendlyShip temp = new FriendlyShip(friendlyShipPixmap, FriendlyShip.ControllerState.AI_CONTROLLED, -500.0f, -500.0f, 0.25f, 0.25f);
                return temp;
            }
        };

        enemyPoolFactory = new Pool.PoolObjectFactory<EnemyShip>() {
            @Override
            public EnemyShip createObject() {
                EnemyShip temp = new EnemyShip(enemyShipPixmap, -500.0f, -500.0f, 0.5f, 0.5f);
                return temp;
            }
        };

        asteroidPoolFactory = new Pool.PoolObjectFactory<Asteroid>() {
            @Override
            public Asteroid createObject() {
                Asteroid temp = new Asteroid(asteroidPixmap, -500.0f, -500.0f, 0.5f, 0.5f);
                return temp;
            }
        };

        friendlyPool = new Pool<>(friendlyPoolFactory, 100);
        enemyPool = new Pool<>(enemyPoolFactory, 100);
        asteroidPool = new Pool<>(asteroidPoolFactory, 100);
    }

    void insertObjects (){

    }

    void update(PlayerView playerView, Graphics g) {
        ticks++;

        //Check Far Distance Items
        if (ticks > 100) {
            List<GameObject> farList = gameObjectsFar;
            for (Iterator<GameObject> farIterator = farList.iterator(); farIterator.hasNext(); ) {
                GameObject farObj = farIterator.next();

                //Free Game Object
                if (playerView.distanceFromObject(farObj) > FAR_MAX_DISTANCE) {
                    if (farObj instanceof DrawableObject) {
                        if (farObj.classOfObject == FriendlyShip.class) {
                            friendlyPool.free((FriendlyShip) farObj);
                        } else if (farObj.classOfObject == EnemyShip.class) {
                            enemyPool.free((EnemyShip)farObj);
                        } else if (farObj.classOfObject == Asteroid.class) {
                            asteroidPool.free((Asteroid)farObj);
                        }
                    }
                    else {
                        farIterator.remove();
                    }
                }
                //Move to Medium
                else if (playerView.distanceFromObject(farObj) < MEDIUM_MAX_DISTANCE) {
                    gameObjectsMedium.add(farObj);
                    farIterator.remove();
                }
            }
            gameObjectsFar = farList;
            ticks = 1;
        }

        //Check Medium Distance Items
        if (ticks % 50 == 0) {
            List<GameObject> medList = gameObjectsMedium;
            for (Iterator<GameObject> medIterator = medList.iterator(); medIterator.hasNext(); ) {
                GameObject medObj = medIterator.next();

                //Move to Far
                if (playerView.distanceFromObject(medObj) > MEDIUM_MAX_DISTANCE) {
                    gameObjectsFar.add(medObj);
                    medIterator.remove();
                }
                // Create object info and move to close
                else if (playerView.distanceFromObject(medObj) < CLOSE_MAX_DISTANCE) {
                    if (medObj instanceof DrawableObject) {
                        gameObjectsClose.add((DrawableObject) medObj);
                        medIterator.remove();
                    } else {
                        if (medObj.classOfObject == FriendlyShip.class) {
                            gameObjectsClose.add(friendlyPool.newObject());
                        } else if (medObj.classOfObject == EnemyShip.class) {
                            gameObjectsClose.add(enemyPool.newObject());
                        } else if (medObj.classOfObject == Asteroid.class) {
                            gameObjectsClose.add(asteroidPool.newObject());
                        }
                    }
                }
            }
            gameObjectsMedium = medList;
        }

        //Check Close Distance Items
        List<DrawableObject> closeList = gameObjectsClose;
        for (Iterator<DrawableObject> closeIterator = closeList.iterator(); closeIterator.hasNext(); ) {
            DrawableObject closeObj = closeIterator.next();

            //Move to Medium
            if (playerView.distanceFromObject(closeObj) > CLOSE_MAX_DISTANCE) {
                gameObjectsMedium.add(closeObj);
                closeIterator.remove();
            }
            //Check to Draw
            else if (playerView.isWithinView(closeObj)) {
                closeObj.draw(g);
            }
        }
        gameObjectsClose = closeList;
    }
}
