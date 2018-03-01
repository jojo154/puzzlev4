package com.jblearning.puzzlev4;

import java.util.Random;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

public class PuzzleView extends RelativeLayout {
  private TextView [][] tvs;
  private LayoutParams [][] params;
  private int [] colors;

  private int labelHeight;
  private int labelWidth;
  private int startY; // start y coordinate of TextView being moved
  private int startX; // start y coordinate of TextView being moved

  private int startTouchY; // start y coordinate of current touch
  private int startTouchX;
  private int emptyPositionX;
  private int emptyPositionY;
  private int [][] positions;

  public PuzzleView( Activity activity, int width, int height, int numberOfPieces ) {
    super( activity );
    buildGuiByCode( activity, width, height, numberOfPieces );
  }

  public void buildGuiByCode( Activity activity, int width, int height, int numberOfPieces ) {
    positions = new int[numberOfPieces][numberOfPieces];
    tvs = new TextView[numberOfPieces][numberOfPieces];
    colors = new int[tvs.length];
    params = new LayoutParams[tvs.length][tvs.length];
    Random random = new Random( );
    labelHeight = height / 3;
    labelWidth = width /3;

    for( int i = 0; i < tvs.length; i++ ) {
      for( int j = 0; j < tvs.length; j++ ) {
        tvs[i][j] = new TextView(activity);
        colors[i] = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        tvs[i][j].setBackgroundColor(colors[i]);
        params[i][j] = new LayoutParams(labelWidth, labelHeight);
        params[i][j].leftMargin = labelWidth * i;
        params[i][j].topMargin = labelHeight * j;
        addView(tvs[i][j], params[i][j]);
      }
    }
  }

  public void fillGui( String [][] scrambledText ) {
    int minFontSize = DynamicSizing.MAX_FONT_SIZE;
    for( int i = 0; i < tvs.length; i++ ) {
      for( int j = 0; j < tvs.length; j++ ) {
        tvs[i][j].setText(scrambledText[i][j]);
        positions[i][j] = i;

        tvs[i][j].setWidth(params[i][j].width);
        tvs[i][j].setHeight(params[i][j].height);
        tvs[i][j].setPadding(20, 5, 20, 5);

        // find font size dynamically
        int fontSize = DynamicSizing.setFontSizeToFitInView(tvs[i][j]);
        if (minFontSize > fontSize)
          minFontSize = fontSize;
      }
    }
    Log.w("MainActivity", "font size = " + minFontSize);
    // set font size for TextViews
    for( int i = 0; i < tvs.length; i++ )
      for( int j = 0; j < tvs.length; j++ ) {
        tvs[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, minFontSize);
      }
  }

  // Returns the index of tv within the array tvs
  public int indexOfTextViewX( View tv ) {
    if( ! ( tv instanceof TextView ) )
      return -1;
    for( int i = 0; i < tvs.length; i++ ) {
      for( int j = 0; j < tvs.length; j++ ) {
        if (tv == tvs[i][j])
          return i;
      }
    }
    return -1;
  }
  public int indexOfTextViewJ( View tv ) {
    if( ! ( tv instanceof TextView ) )
      return -1;
    for( int i = 0; i < tvs.length; i++ ) {
      for( int j = 0; j < tvs.length; j++ ) {
        if (tv == tvs[i][j])
          return j;
      }
    }
    return -1;
  }

  public void updateStartPositions( int indexX, int indexJ, int y, int x ) {
    startY = params[indexX][indexJ].topMargin;
    startX = params[indexX][indexJ].leftMargin;
    startTouchY = y;
    startTouchX = x;
    emptyPositionX = tvPositionX( indexX, indexJ );
    emptyPositionY = tvPositionY( indexX, indexJ );
  }

  // moves the TextView at index index
  public void moveTextView( int indexX, int indexJ, int y , int x ) {
    params[indexX][indexJ].topMargin = startY + y - startTouchY;
    params[indexX][indexJ].leftMargin = startX + x - startTouchX;
    tvs[indexX][indexJ].setLayoutParams( params[indexX][indexJ]);
  }

  public void enableListener( OnTouchListener listener ) {
    for( int i = 0; i < tvs.length; i++ )
      for( int j = 0; j < tvs.length; j++ )
        tvs[i][j].setOnTouchListener( listener );
  }

  public void disableListener( ) {
    for( int i = 0; i < tvs.length; i++ )
      for( int j = 0; j < tvs.length; j++ )
      tvs[i][j].setOnTouchListener( null );
  }

  // Returns position index within screen of TextField at index tvIndex
  // Accuracy is half a TextView's height
  public int tvPositionY( int tvIndexX, int tvIndexJ ) {
    return ( params[tvIndexX][tvIndexJ].topMargin + labelHeight/2 ) / labelHeight;
  }
  public int tvPositionX( int tvIndexX, int tvIndexJ ) {
    return ( params[tvIndexX][tvIndexJ].leftMargin + labelWidth/2 ) / labelWidth;
  }

  // Swaps tvs[tvIndex] and tvs[positions[toPosition]]
  public void placeTextViewAtPosition( int tvIndexX, int tvIndexY, int newX, int newY) {
    // Move current TextView to position position
    params[tvIndexX][tvIndexY].topMargin = newY * labelHeight;
    params[tvIndexX][tvIndexY].leftMargin = newX * labelWidth;
    tvs[tvIndexX][tvIndexY].setLayoutParams( params[tvIndexX][tvIndexY]);

    // Move TextView just replaced to empty spot
    int index = positions[newX][newY];
    params[newX][newY].topMargin = emptyPositionY * labelHeight;
    params[newX][newY].leftMargin = emptyPositionX * labelWidth;
    tvs[newX][newY].setLayoutParams( params[newX][newY]);

    // Reset positions values
    positions[emptyPositionX][emptyPositionY] = index;
    positions[newX][newY] = tvIndexX;
    positions[newX][newY] = tvIndexY;
  }

  // Returns the current user solution as an array of Strings
//  public String [] currentSolution( ) {
//    String [] current = new String[tvs.length];
//    for( int i = 0; i < current.length; i++ )
//      current[i] = tvs[positions[i][]][].getText( ).toString( );
//
//    return current;
//  }

//  // returns index of TextView whose location includes y
//  public int indexOfTextView( int y ) {
//    int position = y / labelHeight;
//    return positions[position];
//  }

//  // returns text inside TextView whose index is tvIndex
//  public String getTextViewText( int tvIndexX, int tvIndexY ) {
//    return tvs[tvIndexX][tvIndexY].getText( ).toString( );
//  }
//
//  // replace text inside TextView whose index is tvIndex with s
//  public void setTextViewText( int tvIndex, String s ) {
//    tvs[tvIndex].setText( s );
//  }
}