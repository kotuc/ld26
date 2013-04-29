package cz.kotu.ld26.core;

/**
 * @author Kotuc
 */
public abstract class Level {

    final int pixelunit = 32;
    final int width = 20;
    final int height = 15;

    Player player;
    float time = 0;

    final String name;

    protected Level(String name) {
        this.name = name;
    }

    public abstract void init(PeaWorld world);

    public void update(float delta) {
        time += delta;
    }



}
