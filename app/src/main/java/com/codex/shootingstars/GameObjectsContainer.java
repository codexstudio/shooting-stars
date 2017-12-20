package com.codex.shootingstars;

import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

class GameObjectsContainer {
    private class ObjectDescriptor extends GameObject {
        Class<? extends DrawableObject> clazz;

        ObjectDescriptor(Vector2 worldLocation, Class<? extends DrawableObject> clazz) {
            this.worldLocation = worldLocation;
            this.clazz = clazz;
        }

        @Override
        protected void update(float deltaTime) {
        }
    }

    private final int FAR_MAX_DISTANCE = 7500;
    private final int MEDIUM_MAX_DISTANCE = 5000;
    private final int CLOSE_MAX_DISTANCE = 2500;
    private final int SPAWN_THRESHOLD = 100;
    private final float CHANCE_ASTEROID = 0.2f; //20% chance to spawn
    private final float CHANCE_ENEMYSHIP = CHANCE_ASTEROID + 0.4f; //40% chance to spawn
    private final float CHANCE_FRIENDLYSHIP = CHANCE_ENEMYSHIP + 0.4f; //40% chance to spawn

    private float currTime;

    private List<GameObject> gameObjectsFar;
    private List<GameObject> gameObjectsMedium;
    private List<DrawableObject> gameObjectsClose = new ArrayList<DrawableObject>();
    private List<DrawableObject> gameObjectsToDraw;
    private PlayerContainer playerContainer;

    private Pool<FriendlyShip> friendlyPool;
    private Pool<EnemyShip> enemyPool;
    private Pool<Asteroid> asteroidPool;

    GameObjectsContainer(Graphics g, GameEventListener listener) {
        currTime = 0;
        gameObjectsFar = new ArrayList<>();
        gameObjectsMedium = new ArrayList<>();
        gameObjectsToDraw = new ArrayList<>();
        playerContainer = new PlayerContainer(listener);

        friendlyPool = new Pool<>(
                () -> new FriendlyShip(
                        g.newPixmap("PlayerShip.png", Graphics.PixmapFormat.ARGB8888),
                        FriendlyShip.ControllerState.AI_CONTROLLED, -500.0f, -500.0f, 0.25f, 0.25f),
                100
        );
        enemyPool = new Pool<>(
                () -> new EnemyShip(
                        g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888),
                        -500.0f, -500.0f, 0.5f, 0.5f),
                100
        );
        asteroidPool = new Pool<>(
                () -> new Asteroid(
                        g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888),
                        -500.0f, -500.0f, 0.5f, 0.5f),
                100
        );

        setUpGameStart(g);
    }

    private void free(GameObject obj) {
        if (obj instanceof FriendlyShip) {
            friendlyPool.free((FriendlyShip) obj);
        } else if (obj instanceof EnemyShip) {
            enemyPool.free((EnemyShip) obj);
        } else if (obj instanceof Asteroid) {
            asteroidPool.free((Asteroid) obj);
        }
    }

    private DrawableObject newObject(Class<? extends DrawableObject> clazz, Vector2 location) {
        if (clazz == FriendlyShip.class) {
            FriendlyShip tempFriendlyShip = friendlyPool.newObject();
            tempFriendlyShip.setWorldLocation(location);
            return tempFriendlyShip;
        } else if (clazz == EnemyShip.class) {
            EnemyShip tempEnemyShip = enemyPool.newObject();
            tempEnemyShip.setWorldLocation(location);
            return tempEnemyShip;

        } else if (clazz == Asteroid.class) {
            Asteroid tempAsteroid = asteroidPool.newObject();
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
        } else {
            return (DrawableObject) obj;
        }
    }

    private ObjectDescriptor createRandomGameObject() {
        Random random = new Random();
        float randDistance = random.nextFloat() * (FAR_MAX_DISTANCE - MEDIUM_MAX_DISTANCE) + MEDIUM_MAX_DISTANCE;
        Vector2 worldLocation = MathUtil.randomVectorWithMagnitude(randDistance);

        float randClass = random.nextFloat();

        if (randClass < CHANCE_ASTEROID) {
            return new ObjectDescriptor(worldLocation, Asteroid.class);
        } else if (randClass < CHANCE_ENEMYSHIP) {
            return new ObjectDescriptor(worldLocation, EnemyShip.class);
        } else if (randClass < CHANCE_FRIENDLYSHIP) {
            return new ObjectDescriptor(worldLocation, FriendlyShip.class);
        } else {
            return null;
        }
    }

    private void setUpGameStart(Graphics g) {
        FriendlyShip startingShip = (FriendlyShip) newObject(FriendlyShip.class, new Vector2(g.getWidth() / 2, g.getHeight() / 2));
        startingShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(startingShip);

        gameObjectsFar.add(new ObjectDescriptor(new Vector2(100, 100), FriendlyShip.class));
        gameObjectsFar.add(new ObjectDescriptor(new Vector2(150, 150), FriendlyShip.class));
        gameObjectsFar.add(new ObjectDescriptor(new Vector2(200, 175), FriendlyShip.class));
        gameObjectsFar.add(new ObjectDescriptor(new Vector2(250, 200), FriendlyShip.class));
        for (int i = 0; i < SPAWN_THRESHOLD; i++) {
            gameObjectsFar.add(createRandomGameObject());
        }

    }

    void update(PlayerView playerView, VirtualJoystick joystick, float deltaTime) {
        currTime += deltaTime;

        //Check Far distance Items
        if (currTime > 1.0f) {
            List<GameObject> farList = gameObjectsFar;
            for (Iterator<GameObject> farIterator = farList.iterator(); farIterator.hasNext(); ) {
                GameObject farObj = farIterator.next();

                //Free Game Object
                if (playerView.distanceFromObject(farObj) > FAR_MAX_DISTANCE) {
                    if (farObj instanceof DrawableObject) {
                        free(farObj);
                    } else {
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
            currTime = 0;
        }

        //Check Medium distance Items
        if (currTime > 0.5f) {
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
            obj.transform.setLocation(obj.getWorldLocation());
            obj.update(deltaTime);
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
