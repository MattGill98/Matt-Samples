package uk.me.mattgill.samples.richfaces;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.richfaces.resource.SerializableResource;

@Named
@ApplicationScoped
public class MediaData implements Serializable, SerializableResource{
    
    private final int width = 110;
    private final int height = 50;

    private final Color background = new Color(190, 214, 248);
    private final Color drawColor = new Color(0,0,0);
    
    private final Font font = new Font("Serif", Font.TRUETYPE_FONT, 30);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getBackground() {
        return background;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public Font getFont() {
        return font;
    }
}
