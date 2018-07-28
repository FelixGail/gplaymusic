package com.github.felixgail.gplaymusic.model.snippets;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class ColorStyles implements Serializable {

  @Expose
  private Color primary;
  @Expose
  private Color scrim;
  @Expose
  private Color accent;

  public Color getPrimary() {
    return primary;
  }

  public Color getScrim() {
    return scrim;
  }

  public Color getAccent() {
    return accent;
  }

  public static class Color {

    @Expose
    private int red;
    @Expose
    private int green;
    @Expose
    private int blue;

    public Color(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }

    public int getRed() {
      return red;
    }

    public int getGreen() {
      return green;
    }

    public int getBlue() {
      return blue;
    }
  }
}
