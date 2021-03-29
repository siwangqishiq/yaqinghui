package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class DateTextView extends TextView {
	public static final int COLOR = Color.rgb(177, 179, 184);
	protected boolean isHaveActivity = false;
	protected boolean isToday = false;
	private Paint paint;
	private Paint textPaint;
	private Path path;

	public DateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(COLOR);
		paint.setTextAlign(Align.CENTER);
		path = new Path();
		
		textPaint = new Paint();
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.CENTER);
	}
	
	public void setToday(){
		isToday = true;
		postInvalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isHaveActivity) {
			path.moveTo(5 * getWidth() / 6, getHeight());
			path.lineTo(getWidth(), getHeight());
			path.lineTo(getWidth(), 5 * getHeight() / 6);
			path.lineTo(5 * getWidth() / 6, getHeight());
			path.close();
			canvas.drawPath(path, paint);
		}
		if (isToday) {
			canvas.drawText("today", getWidth() / 2, getHeight() -2, textPaint);
		}
	}

	public void setHasActivity() {
		isHaveActivity = true;
		invalidate();
	}

	public void setNoneActivity() {
		isHaveActivity = false;
		invalidate();
	}
}
