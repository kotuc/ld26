package cz.kotu.ld26.core;

import static playn.core.PlayN.*;

import playn.core.*;

public class LudumDare26Game extends Game.Default {

    private PeaWorld world;

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int physUnitPerScreenUnit = 32;
    private GroupLayer worldLayer;
    private boolean worldLoaded = true;
    private Font font;
    private Canvas canvas;

    int t = 0;

    public LudumDare26Game() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // create and add background image layer
        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);
        final Image pea = assets().getImage("images/pea.png");
        ImmediateLayer imm = graphics().createImmediateLayer(WIDTH, HEIGHT, new ImmediateLayer.Renderer() {
            public void render(Surface surf) {
//                surf.clear();

//                for (int i = 0; i < 100; ++i) {
//                    int x = (int) (random() * WIDTH);
//                    int y = (int) (random() * HEIGHT);
//                    surf.drawImage(pea, x, y);
//                }

                surf.setFillColor(0xFF00FF00);
                surf.fillRect(0.5f, 1f, 2f, 0.5f);


            }
        });
//        imm.setAlpha(1f);
//        imm.setTranslation(50, 50);
//        imm.setScale(2);
//        graphics().rootLayer().add(imm);

        font = graphics().createFont("Courier", Font.Style.BOLD, 16);
        String text = "Text can also be wrapped at a specified width.\n\n" +
                "And wrapped manually at newlines.\nLike this.";
        TextLayout layout = graphics().layoutText(
                text, new TextFormat().withFont(font).withWrapWidth(200));

        CanvasImage image = graphics().createImage(layout.width(), layout.height());
        canvas = image.canvas();
        canvas.setFillColor(0xFFFF0066);
        canvas.fillText(layout, 0, 0);
        ImageLayer textLayer = graphics().createImageLayer(image);
        graphics().rootLayer().add(textLayer);

        // create our world layer (scaled to "world space")
        worldLayer = graphics().createGroupLayer();
        worldLayer.setScale(physUnitPerScreenUnit);
        graphics().rootLayer().add(worldLayer);

        world = new PeaWorld(worldLayer);

        world.add(new Block(world, world.world, 5, 5, 0));

        // hook up our pointer listener
        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerStart(Pointer.Event event) {
                if (worldLoaded) {
                    Pea pea = new Pea(world, world.world,
                            event.x() / physUnitPerScreenUnit,
                            event.y() / physUnitPerScreenUnit, 0);
                    world.add(pea);
                }
            }
        });


        keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                switch (event.key()) {
                    case LEFT:
                        world.add(new Block(world, world.world, 3, 5, 0));
                        break;
                    case RIGHT:
                        world.add(new Block(world, world.world, 7, 5, 0));
                        break;

                }
            }
        });


    }

    @Override
    public void paint(float alpha) {
        if (worldLoaded) {
            world.paint(alpha);
        }
    }

    @Override
    public void update(int delta) {
        if (worldLoaded) {

            t += delta;

            world.update(delta);

            updateText("t" + t + "delta " + delta);

        }
    }


    void updateText(String text) {
        TextLayout layout = graphics().layoutText(
                text, new TextFormat().withFont(font).withWrapWidth(200));
        canvas.clear();
        canvas.fillText(layout, 0, 0);
    }

}
