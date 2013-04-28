package cz.kotu.ld26.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import cz.kotu.ld26.core.LudumDare26Game;

public class LudumDare26GameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new LudumDare26Game());
  }
}
