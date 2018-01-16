package base;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import base.Level.LevelThree;
import entities.*;
import entities.Action.Sort.*;
import entities.Pokemon.Ortide;
import entities.Pokemon.Raflesia;
import entities.Ship;
import entities.Action.Ball.Hyperball;
import entities.Action.Ball.Pokeball;
import entities.Action.Ball.Superball;
import entities.Pokemon.Mystherbe;
import entities.Pokemon.Pokemon;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main dresseur.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game extends Canvas {
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	
	/** The list of all the entities that exist in our game */
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	
	/** The entity representing the player */
	private Ship dresseur;

    /** The speed at which the player's dresseur should move (pixels/sec) */
	private double moveSpeed = 300;
	/** The time at which last fired a shot */
	private long lastFire = 0;

	private long lastFireAlien = 0;

	private long lastFireBall = 0;

	/** The interval between our players shot (ms) */
	private long firingInterval = 500;
	/** The number of pokemons left on the screen */
	private int pokemonCount;

    /** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;

	private int Level = 1;

	/**
	 * Construct our game and set it running.
	 */
	public Game() throws IOException {
		// create a frame to contain our game
		JFrame container = new JFrame("Space Invaders 101");

        //container.add(new JLabel(new ImageIcon("src2/fond/001.jpg")));

		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible
        container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
		// to see at startup
		Level = 1;
		dresseur = new Ship(this,"sprites/dresseur.png",370,550);
		initEntities();
	}

	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();

		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	/**
	 * Initialise the starting state of the entities (dresseur and pokemons). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player dresseur and place it roughly in the center of the screen
		dresseur.setX(370);
		dresseur.setY(550);
        entities.add(dresseur);

		//create pokemons
		pokemonCount = 0;
		List<Entity> aliens = ActionPokemon.createPokemon(this, Level);
		entities.addAll(aliens);
		pokemonCount += aliens.size();
	}
	
	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		Pokeball.setNbEssai();
		waitingForKeyPress = true;
        dresseur.setPowerNull();
	}
	
	/**
	 * Notification that the player has won since all the pokemons
	 * are dead.
	 */
	public void notifyWin() {
        if(Level == 1) {
            Level++;
            message = "Level - 2";
			waitingForKeyPress = true;
            startGame();
        }
        else if(Level == 2) {
            Level++;
            message = "Level - 3";
			Pokeball.setNbEssai();
            waitingForKeyPress = true;
            startGame();
        }
        else {
            Level = 4;
            message = "Well done! You Win!";
            waitingForKeyPress = true;
        }
	}
	
	/**
	 * Notification that an pokemon has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		pokemonCount--;
		
		if (pokemonCount == 0) {
			notifyWin();
		}
		
		// if there are still some pokemons left then they all need to get faster, so
		// speed up all the existing pokemons
		for(Entity entity : entities) {
		    if (entity instanceof Pokemon) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire(Ship ship) {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the pokeball entity, and record the time.
		lastFire = System.currentTimeMillis();
		Pokeball pokeball = new Pokeball(this,"sprites/pokéball.png",ship.getX()+10,ship.getY()-30);
        if(ship.getPower() == 2)
            pokeball = new Superball(this,"sprites/superball.png",ship.getX()+10,ship.getY()-30);
        else if(ship.getPower() >= 3)
            pokeball = new Hyperball(this,"sprites/hyperball.png",ship.getX()+10,ship.getY()-30);
		entities.add(pokeball);
	}

	public void AttaquePokemon(Entity pokemon) {
		if (System.currentTimeMillis() - lastFireAlien < firingInterval * 2) {
			return;
		}
        if (pokemon instanceof Mystherbe) {
            lastFireAlien = System.currentTimeMillis();
            AttaquePokemon shot = new Tranchherbe(this, "sprites/tranchherbe.png", pokemon.getX() - 10, pokemon.getY() + 30);
            entities.add(shot);
        }
        else if (pokemon instanceof Ortide){
            lastFireAlien = System.currentTimeMillis();
            AttaquePokemon shot = new BombBeurk(this, "sprites/bomb-beurk.png", pokemon.getX() - 10, pokemon.getY() + 30);
            entities.add(shot);
        }
        else if (pokemon instanceof Raflesia){
            int loto = (int) (Math.random() * 10000);
            int pos = pokemon.getX() + 50;
            if (loto % 7 == 0) {
                for(int i = 0; i < 10; i++) {
					AttaquePokemon shot = new Tranchherbe(this, "sprites/tranchherbe.png", pos - 20 - (i * 30) , pokemon.getY() + (i * 15));
					shot.setVerticalMovement(150);
					entities.add(shot);
                    shot = new Tranchherbe(this, "sprites/tranchherbe.png", pos + 20 + (i * 30) , pokemon.getY() + (i * 15));
                    shot.setVerticalMovement(150);
                    entities.add(shot);
                    shot = new LanceSoleil(this,"sprites/soleil1.png",pos,pokemon.getY() + (i * 10),false);
                    entities.add(shot);
					shot = new LanceSoleil(this,"sprites/soleil1.png",pos + 10,pokemon.getY() + (i * 10),false);
					entities.add(shot);
                    shot = new LanceSoleil(this,"sprites/soleil2.png",pos,pokemon.getY() + (i * 10),true);
                    shot.setVerticalMovement(130);
                    entities.add(shot);
                    shot = new LanceSoleil(this,"sprites/soleil2.png",pos + 10,pokemon.getY() + (i * 10),true);
                    shot.setVerticalMovement(130);
                    entities.add(shot);
				}
                lastFireAlien = System.currentTimeMillis();
            }
            else if (loto % 2 == 0) {
                lastFireAlien = System.currentTimeMillis();
                AttaquePokemon shot = new BombBeurk(this, "sprites/bomb-beurk.png", pos, pokemon.getY());
                shot.setVerticalMovement(150);
                entities.add(shot);
                shot = new BombBeurk(this, "sprites/bomb-beurk.png", pos - 70, pokemon.getY() + 40);
                shot.setVerticalMovement(150);
                entities.add(shot);
                shot = new BombBeurk(this, "sprites/bomb-beurk.png", pos + 70, pokemon.getY() + 40);
                shot.setVerticalMovement(150);
                entities.add(shot);
            }
            else {
                lastFireAlien = System.currentTimeMillis();
                AttaquePokemon shot = new Tranchherbe(this, "sprites/tranchherbe.png", pos, pokemon.getY());
                shot.setVerticalMovement(150);
                entities.add(shot);
                for (int i = 1; i < 3; i++) {
                    shot = new Tranchherbe(this, "sprites/tranchherbe.png", pos + (15 * i), pokemon.getY() + (15 * i));
                    shot.setVerticalMovement(150);
                    entities.add(shot);
                    shot = new Tranchherbe(this, "sprites/tranchherbe.png", pos - (15 * i), pokemon.getY() + (15 * i));
                    shot.setVerticalMovement(150);
                    entities.add(shot);
                }
            }
        }
    }

    private void Bonus(Entity pokemon, Ship ship) {
        if (System.currentTimeMillis() - lastFireBall < firingInterval * 4) {
            return;
        }

	    Bonus ball;
        lastFireBall = System.currentTimeMillis();
        if(ship.getPower() == 1)
            ball = new Bonus(this,"sprites/superball.png",pokemon.getX() - 10,pokemon.getY() + 30);
        else
            ball = new Bonus(this,"sprites/hyperball.png",pokemon.getX() - 10,pokemon.getY() + 30);
        entities.add(ball);
    }

	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		// keep looping round til the game ends
		while (gameRunning) {

			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			//g.setColor(Color.black);
			g.fillRect(0,0,800,600);

			//Choix de l'image de fond
			//ActionPokemon.fond(this,g,Level);

			// cycle round asking each entity to move itself
            /**
            if (!waitingForKeyPress) {
                for (Entity entity : entities) {
                    ActionPokemon.jeu(this,entity,Level,delta);
                }
            }*/

			if (!waitingForKeyPress) {
                for (Entity entity : entities) {
                    if (entity instanceof Pokemon) {
                        if ((int) (Math.random() * 40000) ==  777 && dresseur.getPower() < 3) {
                            Bonus(entity, dresseur);
                            break;
                        }
                        if(Level == 3) {
                            entity.move(delta);
                            int loto = (int) (Math.random() * 250);
                            if (loto == 7) {
                                AttaquePokemon(entity);
                                break;
                            }
							if (((int) (Math.random() * 1000)  == 250) && dresseur.getPower() < 3) {
								Bonus(entity, dresseur);
								break;
							}
                        }
                        else {
                            if (pokemonCount <= 10) {
                                entity.moveAlea(delta);
                                if (pokemonCount <= 5 && (int) (Math.random() * 50) == (int) (Math.random() * 50))
                                    entity.doLogic();
                                if ((int) (Math.random() * 100) == 0) {
                                    AttaquePokemon(entity);
                                    break;
                                }
                            } else {
                                entity.move(delta);
                                if ((int) (Math.random() * 1000) == 0) {
                                    AttaquePokemon(entity);
                                    break;
                                }
                            }
                        }
                    } else {
                        entity.move(delta);
                    }
                }
			}
			
			// cycle round drawing all the entities we have in the game
            for(Entity entity : entities)
				entity.draw(g);
			
			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify 
			// both entities that the collision has occured
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = entities.get(p);
					Entity him = entities.get(s);
					
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
			
			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			if (logicRequiredThisLoop) {
			    for(Entity entity : entities) {
					entity.doLogic();
				}
				
				logicRequiredThisLoop = false;
			}
			
			// if we're waiting for an "any key" press then draw the 
			// current message 
			if (waitingForKeyPress) {
				if(Level == 4) {
				    for(Entity entity : entities) {
				        removeEntity(entity);
                    }
                    dresseur.setPowerNull();
                    g.setColor(Color.black);
                    g.drawString(message,(750-g.getFontMetrics().stringWidth(message)),50);
                    g.drawString("For start a new game",(750-g.getFontMetrics().stringWidth("For start a new game")),100);
                    g.drawString("Press any key",(750-g.getFontMetrics().stringWidth("Press any key")),150);
				}
                else {
                    g.setColor(Color.white);
                    g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
                    g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
                }

			}
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();
			
			// resolve the movement of the dresseur. First assume the dresseur
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			dresseur.setHorizontalMovement(0);
			
			if ((leftPressed) && (!rightPressed)) {
				dresseur.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				dresseur.setHorizontalMovement(moveSpeed);
			}
			
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire((Ship) dresseur);
			}
			
			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}

    public int getPokemonCount() {
        return pokemonCount;
    }

    /**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 * 
	 * @author Kevin Glass
	 */
	private class KeyInputHandler extends KeyAdapter {
		/** The number of key presses we've had while waiting for an "any key" press */
		private int pressCount = 1;
		
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed 
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "press"
			if (waitingForKeyPress) {
				return;
			}
			
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		} 
		
		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released 
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notification from AWT that a key has been typed. Note that
		 * typing a key means to both press and then release it.
		 *
		 * @param e The details of the key that was typed. 
		 */
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may
			// have had a keyType() event from the user releasing
			// the shoot or move keys, hence the use of the "pressCount"
			// counter.
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start 
					// our new game
					waitingForKeyPress = false;
					if(Level == 4)
					    Level = 1;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}
			
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 * 
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) throws IOException {
		Game g =new Game();

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
        g.gameLoop();
	}
}
