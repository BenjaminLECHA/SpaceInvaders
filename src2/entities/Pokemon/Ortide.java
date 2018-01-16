package entities.Pokemon;

import base.Game;


public class Ortide extends Pokemon {

    private int HP = 2;

    public Ortide(Game game, String ref, int x, int y) {
        super(game,ref,x,y);
    }

    public void healthPoint() {
        if(HP != 0)
            HP--;
        else {
            game.removeEntity(this);
            game.notifyAlienKilled();
        }
    }
}