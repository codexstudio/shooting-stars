package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.*;
import com.filip.androidgames.framework.impl.VirtualJoystick;
import com.filip.androidgames.framework.types.Vector2;

import java.util.*;

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

    private final int FAR_MAX_DISTANCE = 5000;
    private final int MEDIUM_MAX_DISTANCE = 2500;
    private final int CLOSE_MAX_DISTANCE = 1000;
    private final int SPAWN_THRESHOLD = 200;

    //Chances below must add up to 100%
    private final float CHANCE_ASTEROID = 0.3f; //30% chance to spawn
    private final float CHANCE_ENEMYSHIP = CHANCE_ASTEROID + 0.35f; //35% chance to spawn
    private final float CHANCE_FRIENDLYSHIP = CHANCE_ENEMYSHIP + 0.35f; //35% chance to spawn

    private float currTime;

    private List<GameObject> gameObjectsFar;
    private List<GameObject> gameObjectsMedium;
    private List<DrawableObject> gameObjectsClose = new ArrayList<DrawableObject>();
    private List<DrawableObject> gameObjectsToDraw;
    private PlayerContainer playerContainer;

    private Pool<FriendlyShip> friendlyPool;
    private Pool<EnemyShip> enemyPool;
    private Pool<Asteroid> asteroidPool;

    GameObjectsContainer(Graphics g, GameEventListener listener, PlayerView playerView) {
        currTime = 0;
        gameObjectsFar = new ArrayList<>();
        gameObjectsMedium = new ArrayList<>();
        gameObjectsToDraw = new ArrayList<>();
        playerContainer = new PlayerContainer(listener, playerView);

        friendlyPool = new Pool<>(
                () -> new FriendlyShip(
                        g.newPixmap(Settings.PlayerShip, Graphics.PixmapFormat.ARGB8888),
                        FriendlyShip.ControllerState.AI_CONTROLLED, -500.0f, -500.0f, 0.25f, 0.25f),
                100
        );
        enemyPool = new Pool<>(
                () -> new EnemyShip(
                        g.newPixmap("EnemyShip.png", Graphics.PixmapFormat.ARGB8888),
                        -500.0f, -500.0f, 0.4f, 0.4f),
                100
        );
        asteroidPool = new Pool<>(
                () -> new Asteroid(
                        g.newPixmap("Asteroid.png", Graphics.PixmapFormat.ARGB8888),
                        -500.0f, -500.0f, 0.5f, 0.5f),
                100
        );

        setUpGameStart(g, playerView);
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
        }
        return (DrawableObject) obj;
    }

    private ObjectDescriptor createRandomGameObject(PlayerView playerView) {
        Random random = new Random();
        float randDistance = random.nextFloat() * (FAR_MAX_DISTANCE - MEDIUM_MAX_DISTANCE) + MEDIUM_MAX_DISTANCE;
        Vector2 worldLocation = Vector2.sum(MathUtil.randomVectorWithMagnitude(randDistance), playerView.getLocation());

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

    private void checkCollisions() {
        List<DrawableObject> drawList = gameObjectsToDraw;
        List<FriendlyShip> frList = playerContainer.friendlyShipList;

        for (ListIterator<DrawableObject> drawIterator = drawList.listIterator(); drawIterator.hasNext();) {
            DrawableObject obj = drawIterator.next();
            for (ListIterator<FriendlyShip> frIterator = frList.listIterator(); frIterator.hasNext();) {
                FriendlyShip frSp = frIterator.next();
                if (frSp.isCollidingWith(obj)) {
                    if (obj.getClass() == Asteroid.class || obj.getClass() == EnemyShip.class) {
                        frSp.changeControllerState(FriendlyShip.ControllerState.AI_CONTROLLED);
                        gameObjectsClose.remove(frSp);
                        friendlyPool.free(frSp);
                        frIterator.remove();
                        if (frList.isEmpty()) {
                            //gameOver();
                        }
                    }
                    else if (obj.getClass() == FriendlyShip.class && ((FriendlyShip) obj).getState() == FriendlyShip.ControllerState.AI_CONTROLLED) {
                        ((FriendlyShip) obj).changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
                        ((FriendlyShip) obj).offset = Vector2.difference(obj.getWorldLocation(), playerContainer.getLocation());
                        frIterator.add((FriendlyShip) obj);
                        gameObjectsClose.remove(frSp);
                        drawIterator.remove();
                    }
                }
            }
        }

        gameObjectsToDraw = drawList;
        playerContainer.friendlyShipList = frList;

        List<DrawableObject> drawListOne = gameObjectsToDraw;
        List<DrawableObject> drawListTwo = gameObjectsToDraw;

        for (ListIterator<DrawableObject> drawOneIterator = drawListOne.listIterator(); drawOneIterator.hasNext();) {
            DrawableObject objOne = drawOneIterator.next();
            for (ListIterator<DrawableObject> drawTwoIterator = drawListTwo.listIterator(); drawTwoIterator.hasNext();) {
                DrawableObject objTwo = drawTwoIterator.next();
                if (objOne.getClass() == Asteroid.class) {
                    if (objOne.isCollidingWith(objTwo)) {
                        gameObjectsClose.remove(objTwo);
                        drawTwoIterator.remove();
                    }
                }
                else if (objOne.getClass() == EnemyShip.class) {
                    if (objOne.isCollidingWith(objTwo)) {
                        if (objTwo.getClass() == FriendlyShip.class) {
                            gameObjectsClose.remove(objTwo);
                            drawTwoIterator.remove();
                        }
                    }
                }
            }
        }

        gameObjectsToDraw = drawListTwo;

    }

    private void setUpGameStart(Graphics g, PlayerView playerView) {
        FriendlyShip startingShip = (FriendlyShip) newObject(FriendlyShip.class, new Vector2(g.getWidth() / 2, g.getHeight() / 2));
        startingShip.offset = new Vector2();
        startingShip.changeControllerState(FriendlyShip.ControllerState.PLAYER_CONTROLLED);
        playerContainer.addShip(startingShip);

        for (int i = 0; i < SPAWN_THRESHOLD; i++) {
            gameObjectsFar.add(createRandomGameObject(playerView));
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

            for (int i = gameObjectsFar.size() + gameObjectsMedium.size() + gameObjectsClose.size(); i < SPAWN_THRESHOLD; i++) {
                gameObjectsFar.add(createRandomGameObject(playerView));
            }

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

        checkCollisions();

        playerContainer.rotateShips(joystick.getDirection());
        playerContainer.setLocation(playerView.getLocation());
        playerContainer.update(deltaTime);

        for (FriendlyShip obj : playerContainer.friendlyShipList) {
            obj.transform.setLocation(playerView.getScreenLocation(obj.getWorldLocation()));
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
