package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer {

    //Members
    public List<FriendlyShip> friendlyShipList;
    private GameEventListener listener;

    //Constructor
    public PlayerContainer(GameEventListener listener) {
        this.listener = listener;
        friendlyShipList = new ArrayList<FriendlyShip>();
    }

    //Setters & Getters
    public int getShipListSize(){
        return friendlyShipList.size();
    }

    //Methods
    public void addShip(FriendlyShip ship) {
        if (ship.getState() == FriendlyShip.ControllerState.PLAYER_CONTROLLED) {
            friendlyShipList.add(ship);
        }
        else { Log.wtf("WARNING!", "Tried to add a non PLAYER_CONTROLLED" + ship.getClass().getName() + "to the list."); }
    }

    public void removeShip(FriendlyShip ship) {
        friendlyShipList.remove(ship);
        listener.onPlayerRemoved(ship);
    }

    public void rotateShips(Vector2 direction) {
        for (FriendlyShip obj : friendlyShipList) {
                obj.transform.setRotation(direction);
        }
    }
}
