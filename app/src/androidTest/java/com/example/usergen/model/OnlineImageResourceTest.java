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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;

@RunWith(AndroidJUnit4.class)
public class OnlineImageResourceTest {

    @Test
    public void testBitmapIntegrity() {

        OnlineImageResource resource = new OnlineImageResource(getURL());

        try {
            Bitmap map = resource.getBitmap();

            assertValidBitmap(map);

        } catch (IOException ex) {
            assumeNoException(ex);
        }
    }

    @Test
    public void testLoading() {

        OnlineImageResource resource = new OnlineImageResource(getURL());

        assertFalse(resource.isLoaded());

        try {
            resource.getBitmap();
        } catch (IOException ex) {
            assumeNoException(ex);
        }

        assertTrue(resource.isLoaded());
    }

    @Test
    public void testEquals() {

        OnlineImageResource first = new OnlineImageResource(getURL());
        OnlineImageResource second;

        try {
            second = new OnlineImageResource(first.getBitmap());
        }
        catch (IOException ex) {
            assumeNoException(ex);
            return;
        }

        assertEquals(first, second);
        assertEquals(second, first);

        OnlineImageResource third = new OnlineImageResource(getAlternativeURL());

        assertNotEquals(first, third);
        assertNotEquals(third, first);

        assertNotEquals(second, third);
        assertNotEquals(third, second);


    }

    @Test
    public void testGetBitmapAsync() {

        OnlineImageResource resource = new OnlineImageResource(getAlternativeURL());

        Future<Bitmap> result = resource.getBitmapAsync();

        assertNotNull(result);

        try {
            Bitmap bitmap = result.get();

            assertNotNull(bitmap);
        } catch (ExecutionException | InterruptedException ex) {

            assumeNoException(ex);

            throw new RuntimeException(ex);
        }

    }

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

    private void assertValidBitmap(Bitmap map) {
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
    }



}