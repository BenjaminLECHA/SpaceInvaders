package entities.Mouvement;

/**
 * Created by minato on 21/12/16.
 */
public class MouvementShip implements Mouvement {
    @Override
    public double[] move(long delta, double dx, double dy) {
        return new double[]{(delta * dx) / 1000, (delta * dy) / 1000};
    }

    @Override
    public double[] moveAlea(long delta, double dx, double dy) {
        return new double[]{0,0};
    }
}
