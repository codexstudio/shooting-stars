package com.codex.shootingstars;

public interface PlayerContainerListener {
    public void onPlayerAdded(FriendlyShip fs);
    public void onPlayerRemoved(FriendlyShip fs);
}
