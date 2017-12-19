package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Pool;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Transform2D;
import com.filip.androidgames.framework.types.Vector2;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameObjectsContainer {
    private class ObjectDescriptor extends GameObject {
        Class<? extends DrawableObject> clazz;

        ObjectDescriptor(Transform2D transform, Class<? extends DrawableObject> clazz) {
            this.transform = transform;
            this.clazz = clazz;
        }

        @Override
        protected void update(float deltaTime) {}
    }

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
    private List<DrawableObject> gameObjectsToDraw;
    private PlayerContainer playerContainer;

    private Pool<FriendlyShip> friendlyPool;
    private Pool<EnemyShip> enemyPool;
    private Pool<Asteroid> asteroidPool;

    private Pool.PoolObjectFactory<FriendlyShip> friendlyPoolFactory;
    private Pool.PoolObjectFactory<EnemyShip> enemyPoolFactory;
    private Pool.PoolObjectFactory<Asteroid> asteroidPoolFactory;

    GameObjectsContainer(Graphics g, GameEventListener listener) {
        ticks = 1;
        gameObjectsFar = new ArrayList<>();
        gameObjectsMedium = new ArrayList<>();
        gameObjectsClose = new ArrayList<DrawableObject>();
        gameObjectsToDraw = new ArrayList<DrawableObject>();
        playerContainer = new PlayerContainer(listener);

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

        setUpGameStart(g);
    }

    void insertObject(Class<? extends DrawableObject> clazz) {
        gameObjectsFar.add(new ObjectDescriptor(new Transform2D(), clazz));
    }

    void insertObjects (){

    }

    private boolean free(GameObject obj) {
        if (obj instanceof FriendlyShip) {
            friendlyPool.free((FriendlyShip) obj);
        } else if (obj instanceof EnemyShip) {
            enemyPool.free((EnemyShip) obj);
        } else if (obj instanceof Asteroid) {
            asteroidPool.free((Asteroid) obj);
        } else {
            return false;
        }
        return true;
    }

    private DrawableObject newObject(Class<? extends DrawableObject> clazz, Transform2D transform2D) {
        ObjectDescriptor objDesc = new ObjectDescriptor(transform2D, clazz);
        if (objDesc.clazz == FriendlyShip.class) {
            FriendlyShip tempFriendlyShip = friendlyPool.newObject();
            tempFriendlyShip.setTransform(objDesc.transform);
            return tempFriendlyShip;
        } else if (objDesc.clazz == EnemyShip.class) {
            EnemyShip tempEnemyShip = enemyPool.newObject();
            tempEnemyShip.setTransform(objDesc.transform);
            return tempEnemyShip;
        } else if (objDesc.clazz == Asteroid.class) {
            Asteroid tempAsteroid = asteroidPool.newObject();
            tempAsteroid.setTransform(objDesc.transform);
            return tempAsteroid;
        } else {
            return null;
        }
    }

    private DrawableObject newObject(GameObject obj) {
        if (obj instanceof ObjectDescriptor) {
            ObjectDescriptor objDesc = (ObjectDescriptor) obj;
            return newObject(objDesc.clazz, new Transform2D());
        }
        else {
            return (DrawableObject) obj;
        }
    }

    void setUpGameStart(Graphics g) {
        FriendlyShip startingShip = (FriendlyShip) newObject(FriendlyShip.class, new Transform2D(new Vector2(g.getWidth() / 2, g.getHeight() / 2), new Vector2(), new Vector2(0.25f, 0.25f)));
        startingShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(startingShip);
    }

    void update(PlayerView playerView, VirtualJoystick joystick, float deltaTime) {
        ticks++;

        //Check Far distance Items
        if (ticks > 100) {
            List<GameObject> farList = gameObjectsFar;
            for (Iterator<GameObject> farIterator = farList.iterator(); farIterator.hasNext(); ) {
                GameObject farObj = farIterator.next();

                //Free Game Object
                if (playerView.distanceFromObject(farObj) > FAR_MAX_DISTANCE) {
                    if (farObj instanceof DrawableObject) {
                            free(farObj);
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

        //Check Medium distance Items
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
                        gameObjectsClose.add(newObject(medObj));
                    }
                }
            }
            gameObjectsMedium = medList;
        }

        //Check Close distance Items
        List<DrawableObject> closeList = gameObjectsClose;
        List<DrawableObject> drawList = new ArrayList<>();
        for (Iterator<DrawableObject> closeIterator = closeList.iterator(); closeIterator.hasNext(); ) {
            DrawableObject closeObj = closeIterator.next();

            //Move to Medium
            if (playerView.distanceFromObject(closeObj) > CLOSE_MAX_DISTANCE) {
                gameObjectsMedium.add(closeObj);
                closeIterator.remove();
            }
            //Check to Draw
            else if (playerView.isWithinView(closeObj)) {
                drawList.add(closeObj);
            }
        }
        gameObjectsClose = closeList;
        gameObjectsToDraw = drawList;

        playerContainer.rotateShips(joystick.getDirection());

        for (FriendlyShip obj : playerContainer.friendlyShipList) {
            obj.update(deltaTime);
        }

        for (DrawableObject obj : gameObjectsToDraw) {
            obj.update(deltaTime);
        }
    }

    void draw(Graphics g) {
        for (DrawableObject obj : gameObjectsToDraw) {
            obj.draw(g);
        }
        for (FriendlyShip obj : playerContainer.friendlyShipList) {
            obj.draw(g);
        }
    }
}
