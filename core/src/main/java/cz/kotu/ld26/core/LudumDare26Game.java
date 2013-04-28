package cz.kotu.ld26.core;

import static playn.core.PlayN.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import playn.core.*;

public class LudumDare26Game extends Game.Default {

    public static final float GRAVITY = 80;
    public static final float JUMP_SPEED = -20f;
    public static final float MOVE_SPEED = 10f;
    public static final float MOVE_ACC = 5f;
    private PeaWorld world;

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int physUnitPerScreenUnit = 32;
    private GroupLayer worldLayer;
    private boolean worldLoaded = true;
    private Font font;
    private Canvas canvas;

    private LudumDare26Game.ControlsState controlsState = new ControlsState();

    private int t = 0;
    private Player player;
    private Box spike;

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


        // create our world layer (scaled to "world space")
        worldLayer = graphics().createGroupLayer();
        worldLayer.setScale(physUnitPerScreenUnit);
        graphics().rootLayer().add(worldLayer);

        initTextLayer();

        world = new PeaWorld(worldLayer);

        initLevel1();

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
                    case UP: {
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
                    case K1:
                        initLevel1();
                        break;
                    case K2:
                        initLevel2();
                        break;
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

    private void initTextLayer() {
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
    }

    private void initLevel1() {

        world.clearInit();

        t = 0;

        world.add(new Block(world, world.world, 1, 1, 1, 1, 0));
        world.add(new Block(world, world.world, 0, 0, 1, 1, 0));

        world.add(new Block(world, world.world, 5, 5, 1, 1, 20));

        player = new Player(world, world.world, 3, 10, 0);
        world.add(player);

        world.add(new Block(world, world.world, 3, 12, 1, 1, 0));

        world.add(new Block(world, world.world, 3 + 8, 12, 3, 1, 0));

        world.add(new Block(world, world.world, 3f + 3f, 10, 3, 1, 0));

        world.add(new Block(world, world.world, 3f + 15f, 10, 3, 1, 0));

        spike = new Box(world, world.world, 3f + 15f, 1, 0);

        world.add(spike);
    }

    private void initLevel2() {

        world.clearInit();

        t = 0;

        world.add(new Block(world, world.world, 0, 0, 2, 1, 0));

        world.add(new Block(world, world.world, 5, 5, 1, 1, 20));


        player = new Player(world, world.world, 3, 10, 0);
        world.add(player);

        spike = new Box(world, world.world, 3f + 15f, 1, 0);

        world.add(spike);
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

            playerControl(delta);

            world.update(delta);

            if (t > 10000) {
                spike.getBody().setType(BodyType.DYNAMIC);
            }

            updateText("t" + t + "delta " + delta);

        }
    }

    private void playerControl(int delta) {
        boolean doStop = !(controlsState.leftPressed || controlsState.rightPressed);

        final Body body = player.getBody();
        float linearDamping = 0;
        if (doStop) {
            body.setAngularVelocity(0);
//                linearDamping = 10f;
            Vec2 linearVelocity = body.getLinearVelocity();
            linearVelocity.x *= 0.9;
//                body.applyForce(new Vec2(v, 0), body.getPosition());
            body.setLinearVelocity(linearVelocity);
        } else {
            Vec2 linearVelocity = body.getLinearVelocity();
            final int dir = (controlsState.leftPressed ? -1 : 0) + (controlsState.rightPressed ? 1 : 0);
            float v = linearVelocity.x;
            v += dir * MOVE_ACC;
            v = Math.max(-MOVE_SPEED, Math.min(v, MOVE_SPEED));
            linearVelocity.x = v;
//                body.applyForce(new Vec2(v, 0), body.getPosition());
            body.setLinearVelocity(linearVelocity);
//                linearDamping = 1f;
        }
        body.setLinearDamping(linearDamping);
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
