package com.devahmed.techx.onlineshop.Utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

import com.devahmed.techx.onlineshop.Models.Order;
import com.devahmed.techx.onlineshop.Models.Product;
import com.devahmed.techx.onlineshop.Models.User;
import com.devahmed.techx.onlineshop.R;

import java.util.ArrayList;
import java.util.List;

public class BillCanvasView extends View {

    Paint mPaint, otherPaint, outerPaint, mTextPaint;
    RectF mRectF;
    int mPadding;

    float arcLeft, arcTop, arcRight, arcBottom;

    Path mPath;
    Order order;
    List<Product> productList;
    List<String> countList;
    User user;
    String storeName = getResources().getString(R.string.app_name);
    Context context;
    float DISTANCE_BETWEEN_TEXTS = 3f;


    public BillCanvasView(Context context, Order order, List<Product> productList, User user ) {
        super(context);
        this.context = context;
        this.order = order;
        this.productList = productList;
        for(Product product : productList){
            System.out.println("product " + product.getTitle());
        }
        this.user = user;
        this.countList = new ArrayList<>();
        for (Integer keys : order.getOrderedItemsWithCounts().values()){
            countList.add(String.valueOf(keys));
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(5);

        mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(pxFromDp(context, 12));

        otherPaint = new Paint();

        outerPaint = new Paint();
        outerPaint.setStyle(Paint.Style.FILL);
        outerPaint.setColor(Color.YELLOW);

        mPadding = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);


        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        arcLeft = pxFromDp(context, 20);
        arcTop = pxFromDp(context, 20);
        arcRight = pxFromDp(context, 100);
        arcBottom = pxFromDp(context, 100);


        Point p1 = new Point((int) pxFromDp(context, 80) + (screenWidth / 2), (int) pxFromDp(context, 40));
        Point p2 = new Point((int) pxFromDp(context, 40) + (screenWidth / 2), (int) pxFromDp(context, 80));
        Point p3 = new Point((int) pxFromDp(context, 120) + (screenWidth / 2), (int) pxFromDp(context, 80));

        mPath = new Path();
        mPath.moveTo(p1.x, p1.y);
        mPath.lineTo(p2.x, p2.y);
        mPath.lineTo(p3.x, p3.y);
        mPath.close();

        mRectF = new RectF(screenWidth / 4, screenHeight / 3, screenWidth / 6, screenHeight / 2);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the store name
        mTextPaint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        mTextPaint.setTextSize(pxFromDp(context, 30));
        canvas.drawText(storeName , (float) (getWidth() * 0.35), (float) (getHeight() * 0.05), mTextPaint);
        mTextPaint.setTextSize(pxFromDp(context, 12));
        canvas.drawLine(0 , (float) (getHeight() * 0.07), getWidth(), (float) (getHeight() * 0.07), mPaint);

        canvas.drawText(" Item" , (float) (getWidth() * 0.0), (float) (getHeight() * 0.1), mTextPaint);
        canvas.drawText("Price", (float) (getWidth() * 0.4), (float) (getHeight() * 0.1), mTextPaint);
        canvas.drawText("Count", (float) (getWidth() * 0.6), (float) (getHeight() * 0.1), mTextPaint);
        canvas.drawText("Total", (float) (getWidth() * 0.8), (float) (getHeight() * 0.1), mTextPaint);
        for (int i = 0; i < productList.size() ; i++) {
            canvas.drawText( " " +productList.get(i).getTitle(), (float) (getWidth() * 0.0), (float) (getHeight() * 0.05 * (i + DISTANCE_BETWEEN_TEXTS) ), mTextPaint);
            canvas.drawText(" " +Double.toString(productList.get(i).getPrice()), (float) (getWidth() * 0.4), (float) (getHeight() * 0.05 * (i + DISTANCE_BETWEEN_TEXTS)), mTextPaint);
            canvas.drawText(" " +countList.get(i), (float) (getWidth() * 0.6), (float) (getHeight() * 0.05 * (i + DISTANCE_BETWEEN_TEXTS)), mTextPaint);
            double totalPrice = productList.get(i).getPrice() * Integer.parseInt(countList.get(i));
            canvas.drawText(" " + totalPrice, (float) (getWidth() * 0.8), (float) (getHeight() * 0.05 * (i + DISTANCE_BETWEEN_TEXTS)), mTextPaint);
        }
        mTextPaint.setColor(Color.BLUE);
        canvas.drawLine(0 , (float) (getHeight() * 0.05 * (productList.size() + DISTANCE_BETWEEN_TEXTS  + 1)), getWidth(), (float) (getHeight() * 0.05 * (productList.size() + DISTANCE_BETWEEN_TEXTS  + 1)), mPaint);
        canvas.drawText("Total : " + order.getTotalPrice() , (float) (getWidth() * 0.7), (float) (getHeight() * 0.05 * (productList.size() + DISTANCE_BETWEEN_TEXTS  + 2)), mTextPaint);
        canvas.drawText(TimeStampFormatter.timeStampToString((long)order.getTimeStamp()) , (float) (getWidth() * 0.0), (float) (getHeight() * 0.05 * (productList.size() + (DISTANCE_BETWEEN_TEXTS + 2) * 2)), mTextPaint);
    }


    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
