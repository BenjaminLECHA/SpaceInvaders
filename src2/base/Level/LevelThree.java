package base.Level;

import base.Game;
import entities.Pokemon.Mystherbe;
import entities.Entity;
import entities.Pokemon.Pokemon;
import entities.Pokemon.Raflesia;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minato_Namikazee on 27/12/2016.
 */
public class LevelThree implements level {

    public List<Entity> pokemons = new ArrayList<Entity>();

    @Override
    public List<Entity> createPokemon(Game g) {
        Entity pokemon = new Raflesia(g, "sprites/raflesia.png", 350,250);
        pokemons.add(pokemon);
        return pokemons;
    }

    @Override
    public void fond(Game jeu, Graphics g) {
        g.drawImage(new ImageIcon("src2/fond/003.jpg").getImage(),0,0,800,600,jeu);
    }

}
