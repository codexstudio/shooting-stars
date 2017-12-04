package com.codex.shootingstars;

public interface GameEventListener {
    void onPlayerAdded(FriendlyShip fs);
    void onPlayerRemoved(FriendlyShip fs);
}
