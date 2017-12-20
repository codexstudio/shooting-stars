package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.*;
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

        ObjectDescriptor(Vector2 worldLocation, Class<? extends DrawableObject> clazz) {
            this.worldLocation = worldLocation;
            this.clazz = clazz;
        }

        @Override
        protected void update(float deltaTime) {}
    }

    private final int FAR_MAX_DISTANCE = 7500;
    private final int MEDIUM_MAX_DISTANCE = 5000;
    private final int CLOSE_MAX_DISTANCE = 2500;
    private final int SPAWN_THRESHOLD = 100;
    private final float CHANCE_ASTEROID = 0.2f; //20% chance to spawn
    private final float CHANCE_ENEMYSHIP = CHANCE_ASTEROID + 0.4f; //40% chance to spawn
    private final float CHANCE_FRIENDLYSHIP = CHANCE_ENEMYSHIP + 0.4f; //40% chance to spawn

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

    PlayerView playerView;

    GameObjectsContainer(Graphics g, GameEventListener listener, PlayerView playerView) {
        this.playerView = playerView;
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

    private DrawableObject newObject(Class<? extends DrawableObject> clazz, Vector2 location) {
        if (clazz == FriendlyShip.class) {
            FriendlyShip tempFriendlyShip = friendlyPool.newObject();
//            tempFriendlyShip.transform.setLocation(location);
            tempFriendlyShip.setWorldLocation(location);
            return tempFriendlyShip;
        } else if (clazz == EnemyShip.class) {
            EnemyShip tempEnemyShip = enemyPool.newObject();
//            tempEnemyShip.transform.setLocation(location);
            tempEnemyShip.setWorldLocation(location);
            return tempEnemyShip;

        } else if (clazz == Asteroid.class) {
            Asteroid tempAsteroid = asteroidPool.newObject();
//            tempAsteroid.transform.setLocation(location);
            tempAsteroid.setWorldLocation(location);
            return tempAsteroid;
        } else {
            return null;
        }
    }

    private DrawableObject newObject(GameObject obj) {
        if (obj instanceof ObjectDescriptor) {
            ObjectDescriptor objDesc = (ObjectDescriptor) obj;
            return newObject(objDesc.clazz, obj.getWorldLocation());
        }
        else {
            return (DrawableObject) obj;
        }
    }

    private ObjectDescriptor createRandomGameObject (){
        float randDistance = randomFloatWithinRange(MEDIUM_MAX_DISTANCE, FAR_MAX_DISTANCE);
        Vector2 worldLocation = MathUtil.randomVectorWithMagnitude(randDistance);

        float randClass = (float) Math.random();

        if (randClass < CHANCE_ASTEROID) {
            ObjectDescriptor tempAsteroid = new ObjectDescriptor(worldLocation, Asteroid.class);
            return tempAsteroid;
        }
        else if (randClass < CHANCE_ENEMYSHIP) {
            ObjectDescriptor tempEnemyShip = new ObjectDescriptor(worldLocation, EnemyShip.class);
            return tempEnemyShip;
        }
        else if (randClass < CHANCE_FRIENDLYSHIP) {
            ObjectDescriptor tempFriendlyShip = new ObjectDescriptor(worldLocation, FriendlyShip.class);
            return tempFriendlyShip;
        }
        else {
            return null;
        }
    }

    float randomFloatWithinRange(float min, float max)
    {
        float range = (max - min) + 1.0f;
        return (float)(Math.random() * range) + min;
    }

    void setUpGameStart(Graphics g) {
        FriendlyShip startingShip = (FriendlyShip) newObject(FriendlyShip.class, new Vector2(g.getWidth() / 2, g.getHeight() / 2));
        startingShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(startingShip);

        //gameObjectsFar.add(newObject(Asteroid.class, new Vector2(100.0f, 100.0f)));

        for (int i = 0; i < SPAWN_THRESHOLD; i++) {
            gameObjectsFar.add(createRandomGameObject());
        }

    }

    void update(VirtualJoystick joystick, float deltaTime) {
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
                        medIterator.remove();
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
            obj.transform.setLocation(obj.getWorldLocation());
            //Log.i("haha", "x : " + playerView.getLocation().getX() + ", y : " + playerView.getLocation().getY());
        }

        for (DrawableObject obj : gameObjectsToDraw) {
            obj.transform.setLocation(playerView.getScreenLocation(obj.getWorldLocation()));
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
