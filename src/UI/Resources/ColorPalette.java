package UI.Resources;

import java.awt.*;

public enum ColorPalette {
    COLOR_BTN(new Color(56, 162, 234, 255)),
    COLOR_BTN_HOVER(new Color(27, 84, 120)),
    COLOR_TEXT_DARK (new Color(37, 32, 2, 255));

    private final Color color;

    ColorPalette(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
}
