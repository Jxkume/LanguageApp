package com.example.testi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {

    private final int expectedID;

    public DrawableMatcher(int resourceID) {
        super(View.class);
        expectedID = resourceID;
    }

    @Override
    protected boolean matchesSafely(View view) {
        if(!(view instanceof ImageView)) {
            return false;
        }

        ImageView imageView = (ImageView) view;

        if(expectedID < 0) {
            return imageView.getDrawable() == null;
        }

        Resources res = view.getContext().getResources();
        Drawable expectedDrawable = res.getDrawable(expectedID);

        if (expectedDrawable == null) {
            return false;
        }

        Bitmap bitmap1 = getBitmap(imageView.getDrawable());
        Bitmap bitmap2 = getBitmap(expectedDrawable);

        return bitmap2.sameAs(bitmap1);
    }

    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void describeTo(Description description) {

    }
}
