package entities.Mouvement;

/**
 * Created by minato on 21/12/16.
 */
public class MouvementPokemon implements Mouvement {

    public double[] move(long delta, double dx, double dy) {
        return new double[]{(delta * dx) / 1000, (delta * dy) / 1000};
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        int test = (int)(Math.random() * 10) / 2;
        switch (test) {
            case 0 :
                return new double [] {((delta * dx) / 1000), (delta * dy) / 1000};
            case 1 :
                return new double [] {((delta * dx) / 500), (delta * dy) / 500};
            case 2 :
                return new double [] {((delta * dx) / 250), (delta * dy) / 250};
            case 3 :
                return new double [] {((delta * dx) / 1250), (delta * dy) / 1250};
            case 4 :
                return new double [] {((delta * dx) / 1500), (delta * dy) / 1500};
        }
        return new double [] {((delta * dx) * Math.random() / 1000), (delta * dy) / 1000};
    }
}
