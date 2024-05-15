package com.example.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class sudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellsHighlightColor;

    private final int letterColor;
    private final int letterColorSolve;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();

    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private int cellSize;

    private final solver solver = new solver();

    public sudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.sudokuBoard,0,0);
        try {
            boardColor = typedArray.getInteger(R.styleable.sudokuBoard_boardColor,0);
            cellFillColor = typedArray.getInteger(R.styleable.sudokuBoard_cellFillColor,0);
            cellsHighlightColor = typedArray.getInteger(R.styleable.sudokuBoard_cellsHighlightColor,0);
            letterColor = typedArray.getInteger(R.styleable.sudokuBoard_letterColor,0);
            letterColorSolve = typedArray.getInteger(R.styleable.sudokuBoard_letterColorSolve,0);
        }finally {
            typedArray.recycle();
        }
    }
    @Override
    protected void onMeasure(int width,int height){
        super.onMeasure(width,height);
        int dimension = Math.min(this.getMeasuredWidth(),this.getMeasuredHeight());
        cellSize = dimension / 9;
        setMeasuredDimension(dimension,dimension);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setAntiAlias(true);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        colorCell(canvas,solver.getSelectedRow(),solver.getSelectedColumn());

        canvas.drawRect(0,0,getWidth(),getHeight(),boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent  event){
        boolean isValid;
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN){
            solver.setSelectedRow((int)Math.ceil(y/cellSize));
            solver.setSelectedColumn((int)Math.ceil(x/cellSize));
            isValid = true;
        }else{
            isValid = false;
        }
        return isValid;
    }

     void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellSize);
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if (solver.getBoard()[r][c] != 0){
                    String text = Integer.toString(solver.getBoard()[r][c]);
                    float width, height;

                    letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
                    width = letterPaint.measureText(text);
                    height = letterPaintBounds.height();

                    canvas.drawText(text,(c * cellSize) + ((cellSize - width)/2),
                            (r * cellSize + cellSize) -((cellSize - height)/2),letterPaint);
                }
            }
        }

        letterPaint.setColor(letterColorSolve);
        for(ArrayList<Object> letter : solver.getEmptyBoxIndex()){
            int r = (int)letter.get(0);
            int c = (int)letter.get(1);

            String text = Integer.toString(solver.getBoard()[r][c]);
            float width, height;

            letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
            width = letterPaint.measureText(text);
            height = letterPaintBounds.height();

            canvas.drawText(text,(c * cellSize) + ((cellSize - width)/2),
                    (r * cellSize + cellSize) -((cellSize - height)/2),letterPaint);
        }

    }

    private void colorCell(Canvas canvas, int r, int c){
        if(solver.getSelectedColumn() != -1 && solver.getSelectedRow() != -1){
            canvas.drawRect(c * cellSize,0,(c-1) * cellSize,
                    cellSize * 9,cellsHighlightColorPaint);

            canvas.drawRect(0,r * cellSize,cellSize*9,
                    (r-1) * cellSize,cellsHighlightColorPaint);

            canvas.drawRect(c * cellSize,r * cellSize,(c-1) * cellSize,
                    (r-1) * cellSize,cellsHighlightColorPaint);
        }
        invalidate();
    }
    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(9);
        boardColorPaint.setColor(boardColor);
    }

    private void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(3);
        boardColorPaint.setColor(boardColor);
    }
    private void drawBoard(Canvas canvas){
        for (int i=0;i<10;i++){
            if (i % 3 == 0){
                drawThickLine();
            }
            else {
                drawThinLine();
            }
            canvas.drawLine(cellSize * i,0,cellSize * i,getWidth(),boardColorPaint);
        }
        for (int j = 0;j<10;j++){
            if (j % 3 == 0){
                drawThickLine();
            }
            else {
                drawThinLine();
            }
            canvas.drawLine(0,cellSize * j,getWidth(),cellSize * j,boardColorPaint);
        }
    }
    public solver getSolver(){
        return this.solver;
    }
}
