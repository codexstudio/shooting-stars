package com.codex.shootingstars;

import android.util.Log;
import com.filip.androidgames.framework.types.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer extends GameObject{

    //Members
    private List<FriendlyShip> friendlyShipList;
    private PlayerContainerListener listener;

    //Constructor
    public PlayerContainer(PlayerContainerListener listener) {
        this.listener = listener;
        friendlyShipList = new ArrayList<FriendlyShip>();
    }

    //Setters & Getters
    public int getShipListSize(){
        return friendlyShipList.size();
    }

    //Methods
    public void addShip(FriendlyShip ship) {
        if (ship.getState() == FriendlyShip.ControllerStates.PLAYER_CONTROLLED) {
            friendlyShipList.add(ship);
            listener.onPlayerAdded(ship);
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
