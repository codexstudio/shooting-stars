package com.codex.shootingstars;

import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer extends GameObject{

    //Members
    private List<FriendlyShip> friendlyShipList = new ArrayList<FriendlyShip>();

    //Setters & Getters
    public int getShipListSize(){
        return friendlyShipList.size();
    }

    //Methods
    public void addShip(FriendlyShip ship) {
        friendlyShipList.add(ship);
    }

    public void removeShip(FriendlyShip ship) {
        friendlyShipList.remove(ship);
    }

    public void rotateShips(Vector2 direction) {
        for (FriendlyShip obj : friendlyShipList) {
            obj.transform.setRotation(direction);
        }
    }

}
