package com.swing.plus;

import javax.swing.ImageIcon;

import org.junit.Assert;
import org.junit.Test;

import com.swing.plus.Images;

/**
 * Tests for {@link Images}
 * <p>
 * This is considered and integration test because it does not test {@link Images} in isolation, the images must exist.
 * </p>
 */
public class ImagesIntegrationTest {

    /**
     * Asserts the images defined in {@link Images} can be read and returned as per {@link Images#getImageIcon()}.
     */
    @Test
    public void testGetImageIcon() {
        for (Images i : Images.values()) {
            Assert.assertNotNull("image icon is null: " + i.getFilename(), i.getImageIcon());
        }
    }

    /**
     * Asserts the images defined in {@link Images} can be read and returned as per
     * {@link Images#getScaledImageIcon(int)}.
     */
    @Test
    public void testGetScaledImageIcon() {
        int height = 20;
        for (Images i : Images.values()) {
            ImageIcon ic = i.getScaledImageIcon(height);
            Assert.assertNotNull("scaled image is null: " + i.getFilename(), ic);
            Assert.assertEquals(height, ic.getIconHeight());
        }
    }

    /**
     * Asserts the images defined in {@link Images} can be read and returned as per {@link Images#getImage()}.
     */
    @Test
    public void testGetImage() {
        for (Images i : Images.values()) {
            Assert.assertNotNull("image is null: " + i.getFilename(), i.getImage());
        }
    }

}
