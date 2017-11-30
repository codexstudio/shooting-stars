package com.codex.shootingstars;

public interface GameEventListener {
    public void onPlayerAdded(FriendlyShip fs);
    public void onPlayerRemoved(FriendlyShip fs);
}
