package entities.Mouvement;

/**
 * Created by minato on 21/12/16.
 */
public interface Mouvement {
    double [] move(long delta, double dx, double dy);
    double [] moveAlea(long delta, double dx, double dy);
}
