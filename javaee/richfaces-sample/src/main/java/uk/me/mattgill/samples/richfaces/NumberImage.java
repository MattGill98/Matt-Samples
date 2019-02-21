package uk.me.mattgill.samples.richfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class NumberImage {

    private final int width;
    private final int height;

    private final int maxNumber;
    private final int minNumber;

    private final Color background;
    private final Color drawColour;

    private final Font font;

    @Inject
    private RandomNumberGenerator generator;

    public NumberImage() {
        this.width = 40;
        this.height = 16;

        this.maxNumber = 9999;
        this.minNumber = 1000;

        this.background = new Color(190, 214, 248);
        this.drawColour = new Color(0, 0, 0);

        this.font = new Font("Serif", Font.TRUETYPE_FONT, 15);
    }

    public void paint(OutputStream out, Object data) throws IOException {
        Integer digits = generator.getNumberBetween(minNumber, maxNumber);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.setBackground(background);
        graphics2D.setColor(drawColour);
        graphics2D.clearRect(0, 0, width, height);
        graphics2D.setFont(font);
        graphics2D.drawString(digits.toString(), 0, 15);

        ImageIO.write(img, "png", out);
    }

}