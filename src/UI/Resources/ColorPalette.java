package UI.Resources;

import java.awt.*;

public enum ColorPalette {
    COLOR_BTN(new Color(56, 162, 234, 255)),
    COLOR_BTN_HOVER(new Color(27, 84, 120)),
    COLOR_BTN_DEACTIVATED(new Color(181, 177, 177, 255)),

    COLOR_BTN_BACK(new Color(150, 150, 150, 220)),
    COLOR_BTN_BACK_HOVER(new Color(110, 110, 110, 255)),

    COLOR_BTN_DEL(new Color(220, 80, 80)),
    COLOR_BTN_DEL_HOVER(new Color(180, 40, 40)),

    COLOR_TEXT_DARK (new Color(37, 32, 2, 255)),
    COLOR_TEXT_LIGHT(new Color(255, 255, 255, 255)),

    COLOR_BG_LIGHT(new Color(255, 255, 255, 200));

    private final Color color;

    ColorPalette(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }
}
