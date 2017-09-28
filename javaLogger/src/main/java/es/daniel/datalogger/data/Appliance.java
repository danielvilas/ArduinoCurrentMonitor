package es.daniel.datalogger.data;

import java.awt.*;

public enum Appliance {
    TV(Color.RED,0),
    BluRay (Color.BLUE,1),
    AppleTV(Color.YELLOW,2),
    IpTV(Color.GREEN,3),
    ;
    Color color;
    int position;

    Appliance(Color color, int position) {
        this.color = color;
        this.position=position;
    }

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }
}
