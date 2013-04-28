package cz.kotu.ld26.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import cz.kotu.ld26.core.LudumDare26Game;

public class LudumDare26GameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("ld26/");
    PlayN.run(new LudumDare26Game());
  }
}
