package base.Level;

import base.Game;
import entities.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by benjamin.lecha on 14/11/16.
 */
public interface level {
    List<Entity> createPokemon(Game g);
    void fond(Game jeu, Graphics g);
}
