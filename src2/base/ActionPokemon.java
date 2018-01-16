package base;

import base.Level.LevelOne;
import base.Level.LevelThree;
import base.Level.LevelTwo;
import entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin.lecha on 14/11/16.
 */
/** The list of all the entities that exist in our game */

public class ActionPokemon {
    private static List<Entity> aliens = new ArrayList<Entity>();

    public static List<Entity> createPokemon(Game g, int level) {
        switch (level) {
            case 1: {
                    aliens = new LevelOne().createPokemon(g);
                }
                break;
            case 2 : {
                    aliens = new LevelTwo().createPokemon(g);
                }
                break;
            case 3 : {
                    aliens = new LevelThree().createPokemon(g);
                }
                break;
        }
        return aliens;
    }

    public static void fond(Game jeu,Graphics g, int level) {
        switch (level) {
            case 1 :
                new LevelOne().fond(jeu,g);
                break;
            case 2 :
                new LevelTwo().fond(jeu,g);
                break;
            case 3 :
                new LevelThree().fond(jeu,g);
                break;
            case 4 :
                g.drawImage(new ImageIcon("src2/fond/004.jpg").getImage(),0,0,800,600,jeu);
                break;
        }
    }
}
