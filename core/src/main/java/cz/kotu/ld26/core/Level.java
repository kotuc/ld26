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

}
