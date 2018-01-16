package base.Level;


import base.Game;
import entities.Pokemon.Mystherbe;
import entities.Pokemon.Ortide;
import entities.Entity;
import entities.Pokemon.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelTwo implements level {
    Entity pokemon;
    public List<Entity> pokemons = new ArrayList<Entity>();

    @Override
    public List<Entity> createPokemon(Game g) {
        for (int row = 0; row < 3; row++) {
            for (int x = 0; x < 10; x++) {
                if(row % 2 == 0)
                    pokemon = new Mystherbe(g, "sprites/mystherbe.png", 100 + (x * 50), (50) + row * 30);
                else
                    if(x % 2 == 0)
                        pokemon = new Mystherbe(g, "sprites/mystherbe.png", 100 + (x * 50), (50) + row * 30);
                    else
                        pokemon = new Ortide(g, "sprites/ortide.png", 100 + (x * 50), (50) + row * 30);
                pokemons.add(pokemon);
            }
        }
        return pokemons;
    }

    @Override
    public void fond(Game jeu, Graphics g) {
        g.drawImage(new ImageIcon("src2/fond/002.jpg").getImage(),0,0,800,600,jeu);
    }

}
