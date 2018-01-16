package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer {

    //Members
    public List<FriendlyShip> friendlyShipList;
    private GameEventListener listener;
    private Vector2 location;
    private int score;

    //Constructor
    public PlayerContainer(GameEventListener listener, PlayerView playerView) {
        this.listener = listener;
        this.location = playerView.getLocation();
        friendlyShipList = new ArrayList<FriendlyShip>();
        score = 0;
    }

    //Setters & Getters
    public List<FriendlyShip> getFriendlyShipList() {
        return friendlyShipList;
    }

    public int getScore() { return score; }
    public void setScore(int value) { score = value; }

    //Methods
    public void addShip(FriendlyShip ship) {
        if (ship.getState() == FriendlyShip.ControllerState.PLAYER_CONTROLLED) {
            friendlyShipList.add(ship);
        }
        else { Log.wtf("WARNING!", "Tried to add a non PLAYER_CONTROLLED" + ship.getClass().getName() + "to the list."); }
    }

    public void removeShip(FriendlyShip ship) {
        ship.changeControllerState(FriendlyShip.ControllerState.AI_CONTROLLED);
        ship.offset.setX(0.0f);
        ship.offset.setY(0.0f);
        friendlyShipList.remove(ship);
        if (friendlyShipList.size() <= 0){
            listener.deathListener();
        }
    }

    public void rotateShips(Vector2 direction) {
        for (FriendlyShip obj : friendlyShipList) {
                obj.transform.setRotation(direction);
        }
    }

    public void adjustPosition(PlayerView playerView) {
        Vector2 avgLocation = new Vector2();
        for (FriendlyShip obj : friendlyShipList) {
            avgLocation.setX(avgLocation.getX() + obj.transform.getLocation().getX());
            avgLocation.setY(avgLocation.getY() + obj.transform.getLocation().getY());
        }
        avgLocation.setX(avgLocation.getX() / friendlyShipList.size());
        avgLocation.setY(avgLocation.getY() / friendlyShipList.size());
        float xOffset = avgLocation.getX() - playerView.width / 2;
        float yOffset = avgLocation.getY() - playerView.height / 2;
        for (FriendlyShip obj : friendlyShipList) {
            obj.offset = (new Vector2(obj.offset.getX() - xOffset, obj.offset.getY() - yOffset));
        }
    }

    void update(float deltaTime) {
        for (FriendlyShip obj : friendlyShipList) {
//            if (!obj.getWorldLocation().equals(Vector2.sum(obj.getWorldLocation(), obj.offset))) {
//                obj.setWorldLocation(Vector2.sum(location, obj.offset.unitVector()));
//            }
//            else {
//                obj.setWorldLocation(location);
//            }
            obj.setWorldLocation(Vector2.sum(location, obj.offset));
        }
    }

    void setLocation(Vector2 location) { this.location = location; }
    Vector2 getLocation() { return location; }
}
