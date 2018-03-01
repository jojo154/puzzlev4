package com.jblearning.puzzlev4;

import java.util.Random;

public class Puzzle {
  public static final int NUMBER_PARTS = 3;
  String [][] parts;
  Random random = new Random( );

  public Puzzle( ) {
    parts = new String[NUMBER_PARTS][NUMBER_PARTS];
    parts[0][0] = "A";
    parts[0][1] = "B";
    parts[0][2] = "C";
    parts[1][0] = "D";
    parts[1][1] = "E";
    parts[1][2] = "F";
    parts[2][0] = "G";
    parts[2][1] = "H";
    parts[2][2]= "I";
  }

  public boolean solved( String [][] solution ) {
    if( solution != null && solution.length == parts.length ) {
      for( int i = 0; i < parts.length; i++ ) {
        if( !solution[i].equals( parts[i] ) )
          return false;
      }
      return true;
    }
    else
      return false;
  }

  public String [][] scramble( ) {
    String [][] scrambled = new String[parts.length][parts.length];
    for( int i = 0; i < scrambled.length; i++ )
      for(int j = 0; j < scrambled.length; j++)
      scrambled[i][j] = parts[i][j];

    while( solved( scrambled ) ) {
      for( int i = 0; i < scrambled.length; i++ ) {
        for(int j = 0; j < scrambled.length; j++) {
           int n = random.nextInt(scrambled.length - i) + i;
           int m = random.nextInt(scrambled.length - i) + i;
           String temp = scrambled[i][j];
           scrambled[i][j] = scrambled[n][m];
           scrambled[n][m] = temp;
        }
      }
    }
    return scrambled;
  }

  public int getNumberOfParts( ) {
    return parts.length;
  }

  public String wordToChange( ) {
    return "MOBILE";
  }

  public String replacementWord( ) {
    return "ANDROID";
  }
}
