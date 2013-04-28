package cz.kotu.ld26.core;

import playn.core.*;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.random;

/**
 * @author Kotuc
 */
public class Level {

    final int pixelunit = 32;
    final int width = 20;
    final int height = 15;

    public void init() {
        // create and add background image layer
        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);
        final Image pea = assets().getImage("images/pea.png");
        ImmediateLayer imm = graphics().createImmediateLayer(width, height, new ImmediateLayer.Renderer() {
            public void render(Surface surf) {
//                surf.clear();
                for (int i = 0; i < 100; ++i) {
                    int x = (int) (random() * height);
                    int y = (int) (random() * width);
                    surf.drawImage(pea, x, y);
                }

                surf.setFillColor(0xFF00FF00);
                surf.fillRect(0.5f, 1f, 2f, 0.5f);


            }
        });
//        imm.setAlpha(1f);
        imm.setTranslation(50, 50);
        imm.setScale(10);
        graphics().rootLayer().add(imm);

        Font font = graphics().createFont("Courier", Font.Style.BOLD, 16);

        String text = "Text can also be wrapped at a specified width.\n\n" +
                "And wrapped manually at newlines.\nLike this.";
        TextLayout layout = graphics().layoutText(
                text, new TextFormat().withFont(font).withWrapWidth(200));

        CanvasImage image = graphics().createImage(layout.width(), layout.height());
        image.canvas().setFillColor(0xFFFF0066);
        image.canvas().fillText(layout, 0, 0);
        ImageLayer textLayer = graphics().createImageLayer(image);
        graphics().rootLayer().add(textLayer);

    }

    public void update(int delta) {

    }

    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything here!
    }


}
