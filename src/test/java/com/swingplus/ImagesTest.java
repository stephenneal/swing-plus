package com.swingplus;

import javax.swing.ImageIcon;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Images}
 */
public class ImagesTest {
    @Test
    public void testGetImageIcon() {
        for (Images i : Images.values()) {
            Assert.assertNotNull("image icon is null: " + i.getFilename(), i.getImageIcon());
        }
    }

    @Test
    public void testGetScaledImageIcon() {
        int height = 20;
        for (Images i : Images.values()) {
            ImageIcon ic = i.getScaledImageIcon(height);
            Assert.assertNotNull("scaled image is null: " + i.getFilename(), ic);
            Assert.assertEquals(height, ic.getIconHeight());
        }
    }

    @Test
    public void testGetImage() {
        for (Images i : Images.values()) {
            Assert.assertNotNull("image is null: " + i.getFilename(), i.getImage());
        }
    }

}
