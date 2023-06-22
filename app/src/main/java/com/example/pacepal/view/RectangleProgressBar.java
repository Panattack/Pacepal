package com.example.pacepal.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;

public class RectangleProgressBar extends View {
    private static final long ANIMATION_DURATION = 5000; // Duration in milliseconds
    private static final int MAX_PROGRESS = 100; // Maximum progress value
    private static final float CORNER_RADIUS = 70f; // Corner radius in pixels

    private int progress;
    private ShapeDrawable progressDrawable;

    public RectangleProgressBar(Context context) {
        super(context);
        init();
    }

    public RectangleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectangleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        progress = 0;
        progressDrawable = new ShapeDrawable();
    }

    public void setProgress(int progress) {
        // Ensure the progress value is within the valid range
        this.progress = Math.max(0, Math.min(progress, MAX_PROGRESS));

        // Trigger a redraw of the progress bar
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the width of the progress bar based on the current progress
        int width = (int) (getWidth() * (progress / 100f));

        // Set the color of the progress bar based on the current progress
        int color = interpolateColor(Color.GREEN, Color.RED, progress / 100f);
        progressDrawable.getPaint().setColor(color);

        // Set the bounds of the progress bar shape with rounded corners
        float[] radii = {CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS,
                CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS};
        RectF bounds = new RectF(0, 0, width, getHeight());
        RoundRectShape roundRectShape = new RoundRectShape(radii, null, null);
        progressDrawable.setShape(roundRectShape);
        progressDrawable.setBounds((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom);

        // Draw the progress bar
        progressDrawable.draw(canvas);
    }

    public void startProgressAnimation() {
        // Animate the progress from 0 to 100 over the specified duration
        ValueAnimator animator = ValueAnimator.ofInt(0, MAX_PROGRESS);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            setProgress(animatedValue);
        });
        animator.start();
    }

    // Helper method to interpolate color based on a fraction value
    private int interpolateColor(int startColor, int endColor, float fraction) {
        int startA = Color.alpha(startColor);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endA = Color.alpha(endColor);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        int interpolatedA = (int) (startA + (fraction * (endA - startA)));
        int interpolatedR = (int) (startR + (fraction * (endR - startR)));
        int interpolatedG = (int) (startG + (fraction * (endG - startG)));
        int interpolatedB = (int) (startB + (fraction * (endB - startB)));

        return Color.argb(interpolatedA, interpolatedR, interpolatedG, interpolatedB);
    }
}