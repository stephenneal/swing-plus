package com.swing.plus;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enumeration to encapsulate application images and icons. Each entry provides methods to get an {@link ImageIcon}, {@link Image} or a scaled {@link ImageIcon}.
 * <p>
 * At the bottom of this file there is a {@code main} method (commented out) that can be used to view all the images.
 * </p>
 */
public enum Images {
    EXAMPLE_IMAGE_1(getDefaultImageLocation("example1.jpg")),
    EXAMPLE_IMAGE_2(getDefaultImageLocation("example2.jpg"), "Image 2 description");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String filename;
    private String description;
    private ImageIcon icon;

    private Images(String filename) {
        this(filename, null);
    }

    private Images(String filename, String description) {
        this.filename = filename;
        this.description = description;
    }

    protected String getFilename() {
        return filename;
    }

    /**
     * Get the image.
     * 
     * @return the image
     */
    public Image getImage() {
        ImageIcon icon = getImageIcon();
        return (icon == null) ? null : icon.getImage();
    }

    /**
     * Get the image icon.
     * 
     * @return the image icon
     */
    public ImageIcon getImageIcon() {
        if (icon == null) {
            if (filename != null && filename.trim().length() > 0) {
                URL url = Images.class.getResource(filename);
                if (url == null && !filename.startsWith("/")) {
                    url = Images.class.getResource("/" + filename);
                }
                if (url != null) {
                    if (description == null) {
                        icon = new ImageIcon(url);
                    } else {
                        icon = new ImageIcon(url, description);
                    }
                } else {
                    logger.error("file not found: " + filename);
                }
            }
        }
        return icon;
    }

    /**
     * Get the image icon and scale to match the height specified.
     * 
     * @param height
     *            height
     * @return the image icon scaled to match the height specified.
     */
    public ImageIcon getScaledImageIcon(int height) {
        ImageIcon icon = getImageIcon();
        if (icon != null && !(icon.getIconHeight() == height)) {
            return new ImageIcon(icon.getImage().getScaledInstance(height, height, Image.SCALE_REPLICATE));
        }
        return icon;
    }

    /**
     * Append the file name to the default image location, i.e. "resources/images/<<filename>>"
     */
    private static String getDefaultImageLocation(String filename) {
        return "images/" + filename;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // The following will display each path and the matching image.
    //
    // public static void main(String[] args) {
    // final JFrame frame = new JFrame();
    // frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    // JPanel panel = new JPanel();
    // panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    // for (Images i : Images.values()) {
    // panel.add(new JLabel(i.getFilename()));
    // panel.add(new JLabel(i.getImageIcon()));
    // panel.add(new JSeparator());
    // }
    // JScrollPane sp = new JScrollPane(panel);
    // sp.setPreferredSize(new Dimension(500, 500));
    // frame.getContentPane().add(sp);
    // SwingUtilities.invokeLater(new Runnable() {
    // @Override
    // public void run() {
    // frame.pack();
    // frame.setVisible(true);
    // }
    // });
    // }
    //
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
}
