package com.example.usergen.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class OnlineImageResourceTest {

    @NonNull
    private URL getURL() {

        try {
            return new URL("https://i.redd.it/0wlq67l5dnn31.jpg");
        } catch (MalformedURLException ex) {
            fail("Test URL is malformed");
            throw new RuntimeException(ex);
        }

    }

    @NonNull
    private URL getAlternativeURL() {

        try {
            return new URL("https://i.redd.it/jbjk6429n0o61.png");
        } catch (MalformedURLException ex) {
            fail("Test URL is malformed");
            throw new RuntimeException(ex);
        }

    }

    @Test
    public void getContent() {

        OnlineImageResource resource = new OnlineImageResource(getURL());

        try {
            Bitmap map = resource.getBitmap();

            assertNotNull(map);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] bytes = stream.toByteArray();

            assertTrue(bytes.length > 512);

            boolean allZero = true;

            for (int i = 0; i < 15; i++) {
                if (bytes[i] != 0) {
                    allZero = false;
                    break;
                }
            }

            assertFalse(allZero);


        } catch (IOException ex) {
            fail("Failed to get resource bitmap");
        }
    }

    @Test
    public void equals() {

        OnlineImageResource first = new OnlineImageResource(getURL());
        OnlineImageResource second;

        try {
            second = new OnlineImageResource(first.getBitmap());
        }
        catch (IOException ex) {
            fail("Failed to get resource bitmap");
            throw new RuntimeException(ex);
        }

        assertEquals(first, second);
        assertEquals(second, first);

        OnlineImageResource third = new OnlineImageResource(getAlternativeURL());

        assertNotEquals(first, third);
        assertNotEquals(third, first);

        assertNotEquals(second, third);
        assertNotEquals(third, second);


    }

}