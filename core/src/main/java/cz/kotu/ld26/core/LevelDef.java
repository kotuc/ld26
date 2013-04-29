package cz.kotu.ld26.core;

/**
 * @author Kotuc
 */
public abstract class LevelDef {

    static Level getLevel(int level) {
        switch (level) {
            case 0:
                return createZeroLevel();
            case 1:
                return createJumpLevel();
            case 2:
                return createSpikeLevel();
            case 3:
                return createBlickLevel();
            case 4:
                return createFirstLevel();
            case 5:
                return createSecondLevel();
            case 6:
                return createThirdLevel();
            case 7:
                return createLuckLevel();
            case 8:
                return createFinalLevel();
            case 9:
                return createZeroLevel();
            default:
                return createErrorLevel();
        }
    }


    private static Level createZeroLevel() {
        return new Level("Zero") {
            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3f, 10, 0);
                world.add(player);

                world.createBlock(1, 12, 19, 1);
                world.createBlock(10, 2 + 5 + 1, 1, 5).setCollidable(false);

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }
        };
    }


    private static Level createJumpLevel() {
        return new Level("Jump") {
            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3f, 10, 0);
                world.add(player);

                world.createBlock(1, 12, 8, 1);
                world.createBlock(12, 12, 8, 1);

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }
        };
    }

    private static Level createSpikeLevel() {
        return new Level("Spike") {
            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3f, 10, 0);
                world.add(player);

                world.createBlock(1, 12, 11 - 1, 1);
                world.createBlock(12, 12, 8, 1);

                Spike spike = new Spike(world, world.world, 11.5f, 11.5f, (float) Math.toRadians(180));
                world.add(spike);
            }
        };
    }


    private static Level createBlickLevel() {
        return new Level("Blick") {

            private Block step;

            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3f, 10, 0);
                world.add(player);

                world.createBlock(1, 12, 19, 1);
                world.createBlock(10, 2 + 5 + 1, 1, 5).setCollidable(false);

                step = world.createBlock(15, 10, 3, 1);
                step.setCollidable(false);
                world.createBlock(19, 8, 1, 5);

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }

            @Override
            public void update(float delta) {
                super.update(delta);

                step.setCollidable(5 < time);
            }

        };
    }


    private static Level createFirstLevel() {
        return new Level("One") {
            public void init(PeaWorld world) {

                world.add(new Block(world, world.world, 1, 1, 2, 1, 0));

                world.add(new Block(world, world.world, 5, 5, 1, 1, 20));


                player = new Player(world, world.world, 0.5f, 10, 0);
                world.add(player);

//        world.add(new Block(world, world.world, 5+5, 13, 15, 1, 0));


                world.createBlock(10, 2, 1, 5);
                world.createBlock(10, 2 + 5 + 1, 1, 5).setCollidable(false);


                world.createBlock(2, 13, 9 - 2, 1);

                for (int i = 0; i < 5; i++) {
                    world.createBlock(12 + i, 13, 1, 1).setFallTime(5 + i * 0.2f);
                }

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }
        };
    }

    private static Level createSecondLevel() {
        return new Level("Two") {
            public void init(PeaWorld world) {

                world.add(new Block(world, world.world, 1, 1, 1, 1, 0));
                world.add(new Block(world, world.world, 0, 0, 1, 1, 0));

                world.add(new Block(world, world.world, 5, 5, 1, 1, 20));

                player = new Player(world, world.world, 3, 10, 0);
                world.add(player);

                world.add(new Block(world, world.world, 3, 12, 1, 1, 0));

                world.add(new Block(world, world.world, 3 + 8, 12, 3, 1, 0));

                world.add(new Block(world, world.world, 3f + 3f, 10, 3, 1, 0));

                world.add(new Block(world, world.world, 3f + 15f, 10, 3, 1, 0));


                for (int i = 0; i < 10; i++) {
                    final Spike spike = new Spike(world, world.world, 3f + i, 1, 0);
                    spike.setFallTime(2 + 0.2f * i);
                    world.add(spike);
                }


            }
        };
    }

    private static Level createLuckLevel() {
        return new Level("Luck") {

            private Block removeThis;

            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3, 10, 0);
                world.add(player);

                world.createBlock(0, 12, 20, 1);

                removeThis = world.createBlock(10, 2, 1, 10);

                for (int i = 0; i < 10; i++) {
                    final Spike spike = new Spike(world, world.world, 3f + i, 1, 0);
                    spike.setFallTime(2 + 0.2f * i);
                    world.add(spike);
                }

                // TODO block that became walkable to save happy face

            }

            @Override
            public void update(float delta) {
                super.update(delta);

                removeThis.setCollidable(time < 3);
            }

        };
    }


    private static Level createThirdLevel() {
        return new Level("Three") {

            private Block block;

            public void init(PeaWorld world) {

                world.add(new Block(world, world.world, 1, 1, 2, 1, 0));

                world.add(new Block(world, world.world, 5, 5, 1, 1, 20));


                player = new Player(world, world.world, 0.5f, 10, 0);
                world.add(player);

//        world.add(new Block(world, world.world, 5+5, 13, 15, 1, 0));


                world.createBlock(10, 2, 1, 5);
                block = world.createBlock(10, 2 + 5 + 1, 1, 5);
                block.setCollidable(false);


                world.createBlock(2, 13, 9 - 2, 1);

                for (int i = 0; i < 5; i++) {
                    world.createBlock(12 + i, 13, 1, 1).setFallTime(5 + i * 0.2f);
                }

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }

            @Override
            public void update(float delta) {
                super.update(delta);

                block.setCollidable(5 < time);
            }
        };

    }


    private static Level createFinalLevel() {
        return new Level("The end") {

            private Block block;

            public void init(PeaWorld world) {

                world.add(new Block(world, world.world, 1, 1, 2, 1, 0));

                world.add(new Block(world, world.world, 5, 5, 1, 1, 20));


                player = new Player(world, world.world, 0.5f, 10, 0);
                world.add(player);

//        world.add(new Block(world, world.world, 5+5, 13, 15, 1, 0));

                world.createBlock(0, 12, 20, 1);

                // his love
                world.add(new Player(world, world.world, 17, 10, 0));

            }

        };

    }


    private static Level createErrorLevel() {
        return new Level("Error ") {
            public void init(PeaWorld world) {

                player = new Player(world, world.world, 3, 10, 0);
                world.add(player);

                world.createBlock(10, 2, 1, 5);

                world.createBlock(11, 2, 3, 1);
                world.createBlock(11, 4, 2, 1);
                world.createBlock(11, 6, 3, 1);

                world.createBlock(10, 2 + 5 + 1, 1, 5).setCollidable(false);
                world.createBlock(2, 13, 18 - 2, 1);

                Spike spike = new Spike(world, world.world, 3f + 15f, 1, 0);
                spike.setFallTime(5);
                world.add(spike);
            }
        };
    }

}
