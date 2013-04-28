package cz.kotu.ld26.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import cz.kotu.ld26.core.LudumDare26Game;

public class LudumDare26GameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new LudumDare26Game());
  }
}
