package com.example.wenzty.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenzty on 2017/6/9.
 */

public class WuziqiView extends View{
    private int mPanelWidth;
    private int MAX_LINE = 10;

    private float mLineHeight;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3*1.0f/4;

    private Boolean mIsWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private Boolean mIsGameOver;
    private Boolean mWhiteWinner;

    private int MAX_COUNT_IN_LINE = 5;


    public WuziqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0x99000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece=BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int WidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int WidthMode = MeasureSpec.getMode(widthMeasureSpec);

        int HeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int Width = Math.min(WidthSize,HeightSize);

        if (WidthMode == MeasureSpec.UNSPECIFIED){
            Width = HeightSize;
        }else if (HeightMode == MeasureSpec.UNSPECIFIED){
            Width = WidthSize;
        }

        setMeasuredDimension(Width, Width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth*1.0f/MAX_LINE;

        int PieceWidth = (int) (mLineHeight*ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,PieceWidth,PieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,PieceWidth,PieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if (mIsGameOver) return false;

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point point = getValidPoint(x,y);

            if (mWhiteArray.contains(point)||mBlackArray.contains(point)){
                return false;
            }

            if (mIsWhite){
                mWhiteArray.add(point);
            }else {
                mBlackArray.add(point);
            }
            invalidate();
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBoard(canvas);
        DrawPiece(canvas);
        CheckGameOver();
    }

    private void CheckGameOver() {
        Boolean whiteWin = CheckFiveInLine(mWhiteArray);
        Boolean blackWin = CheckFiveInLine(mBlackArray);
        if (whiteWin || blackWin){
            mIsGameOver = true;
            mWhiteWinner = whiteWin;

            String text = mWhiteWinner?"Winner is White!":"Winner is Black!";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean CheckFiveInLine(List<Point> points) {
        for (Point p : points){
            int x = p.x;
            int y = p.y;
            Boolean win = checkHorizontal(x,y,points);
            if (win)return true;
            win = checkVertical(x,y,points);
            if (win)return true;
            win = checkLeftDiagonal(x,y,points);
            if (win)return true;
            win = checkRightDiagonal(x,y,points);
            if (win)return true;
        }
        return false;
    }

    private Boolean checkHorizontal(int x, int y, List<Point> points) {
    int count = 1;
        //左边
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x -i , y))){
                count++;
            }else {
                break;
           }
        }
            if (count == MAX_COUNT_IN_LINE)return true;
        //右边
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x +i , y))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        return false;
    }

    private Boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //上边
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x , y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        //
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x , y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        return false;
    }

    private Boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //上边
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x-i , y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        //
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x+i , y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        return false;
    }

    private Boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //上边
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x-i , y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        //
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x+i , y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE)return true;
        return false;
    }

    private void DrawPiece(Canvas canvas) {
        for (int i = 0,n = mWhiteArray.size();i<n;i++){
            Point WhitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (WhitePoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (WhitePoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }
        for (int i = 0,n = mBlackArray.size();i<n;i++){
            Point BlackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (BlackPoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (BlackPoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }
    }

    private void DrawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float LineHeight = mLineHeight;

        for (int i=0;i<MAX_LINE;i++){
            int startX = (int) (LineHeight/2);
            int endX = (int) (w - LineHeight/2);
            int y = (int) ((0.5+i)*LineHeight);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }
public void start(){
    mWhiteArray.clear();
    mBlackArray.clear();
    mIsGameOver = false;
    mWhiteWinner = false;
    invalidate();
}
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle){
            Bundle bundle = (Bundle) state;
            mIsGameOver =  bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
