package cz.kotu.ld26.core;

import static playn.core.PlayN.*;

import org.jbox2d.common.Vec2;
import playn.core.*;

public class LudumDare26Game extends Game.Default {

    public static final float GRAVITY = 50;
    public static final float JUMP_SPEED = -20f;
    public static final float MOVE_SPEED = 7.5f;
    private PeaWorld world;

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int physUnitPerScreenUnit = 32;
    private GroupLayer worldLayer;
    private boolean worldLoaded = true;
    private Font font;
    private Canvas canvas;

    private LudumDare26Game.ControlsState controlsState = new ControlsState();

    int t = 0;
    private Player player;

    public LudumDare26Game() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // create and add background image layer
        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);
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

        world.add(new Block(world, world.world, 5, 5, 1, 1, 20));

        player = new Player(world, world.world, 3, 10, 0);
        world.add(player);

        world.add(new Block(world, world.world, 3, 12, 1, 1, 0));

        world.add(new Block(world, world.world, 3 + 8, 12, 3, 1, 0));

        // hook up our pointer listener
        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerStart(Pointer.Event event) {
                if (worldLoaded) {
                    Player player = new Player(world, world.world,
                            event.x() / physUnitPerScreenUnit,
                            event.y() / physUnitPerScreenUnit, 0);
                    world.add(player);
                }
            }
        });


        keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                switch (event.key()) {
                    case LEFT:
                        controlsState.leftPressed = true;
                        break;
                    case RIGHT:
                        controlsState.rightPressed = true;
                        break;
                    case UP:
                    {
                        Vec2 linearVelocity = player.getBody().getLinearVelocity();
                        linearVelocity.y = JUMP_SPEED;
                        player.getBody().setLinearVelocity(linearVelocity);

                        break;
                    }

                }
            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                switch (event.key()) {
                    case LEFT:
                        controlsState.leftPressed = false;
                        break;
                    case RIGHT:
                        controlsState.rightPressed = false;
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

            Vec2 linearVelocity = player.getBody().getLinearVelocity();
            linearVelocity.x = (controlsState.leftPressed?-MOVE_SPEED :0)+(controlsState.rightPressed? MOVE_SPEED :0);
            player.getBody().setLinearVelocity(linearVelocity);

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

    class ControlsState {

        boolean leftPressed = false;
        boolean rightPressed = false;


    }


}
