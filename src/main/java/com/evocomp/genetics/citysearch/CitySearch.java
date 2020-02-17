/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evocomp.genetics.citysearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jamhb
 */
public class CitySearch {
    public int sizeX = 0;
    public int sizeY = 0;
    
    public CitySearch(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    
    public Integer[][] createMatrix (){
        Random r = new Random();
        Integer [][] matrix = new Integer[sizeY][sizeX+1];
        for(int i=0;i<sizeY;i++){
            matrix[i] = getShuffledCromo(sizeX);
        }
        return matrix;
    }
    
    public Integer[] getShuffledCromo(int size){
        List<Integer> cromo = new ArrayList<>();
        
        for(int i=1; i<=size; i++){
            cromo.add(i);
        }
        
        Collections.shuffle(cromo);
        Integer [] shuffleCromo = new Integer [size];
        cromo.toArray(shuffleCromo);
        return shuffleCromo;
    }
    
    public Double[] getCromoDistance(Integer [][] paths, Position [] positions){
        Double [] results = new Double[sizeY];
        
        for(int j = 0; j<sizeY;j++){
            double pathDistance = 0;
            for (int i=1; i<sizeX;i++){
                //System.out.println(">>Distance from " + paths[j][i] + " to " + paths[j][i-1]);
                pathDistance = pathDistance + getDistance(positions[paths[j][i-1]],positions[paths[j][i]]);
            }
            results[j] = pathDistance;
        }
        
        return results;
    }
    
    public double getDistance(Position pos1, Position pos2){
        //System.out.println(">Pos1 x: " + pos1.getX() + " y: " + pos1.getY() );
        //System.out.println(">Pos2 x: " + pos2.getX() + " y: " + pos2.getY() );
        double result = Math.sqrt((Math.pow(pos2.getY()-pos1.getY(),2) + Math.pow(pos2.getX()-pos1.getX(),2)));
        //System.out.println("Distance: " + result);
        return result;
    }
    
    public Integer [][] matchMatrix(Double [] distances, Integer [][] shuffledCities){
        Integer [][] matchedMatrix = new Integer[sizeY][sizeX];
        
        for(int j=0; j<sizeY;j++){
            matchedMatrix[j] = getMatchWinner(distances,shuffledCities);
        }
        
        return matchedMatrix;
    }
    
    public Integer [] getMatchWinner(Double [] distances, Integer [][] shuffledCities){
        Integer [] matcher = getShuffledCromo(sizeY-1);
        
        //System.out.println("Random match positions");
        //System.out.println(Arrays.toString(matcher));
        
        int winnerPos = 0;
        double minDistance = distances[matcher[0]];
        
        for(int i =1; i<5; i++){
            if(distances[matcher[i]] < minDistance){
                minDistance = distances[matcher[i]];
                winnerPos = matcher[i];
            }
        }
        
        return shuffledCities[winnerPos];
    }
    
    
    public Integer [] makeReverse(Integer [] array){
        Random r = new Random();
        
        Integer [] reversed = Arrays.copyOf(array, array.length);
        int startPos = r.nextInt(sizeX-2);
        int length = r.nextInt(sizeX-startPos);

        int reverseCount = startPos+length;
        for (int i = startPos; i <= startPos + length; i++) {
            reversed[i] = array[reverseCount];
            reverseCount--;
        }

        //System.out.println(">>StartPos: " + startPos + " Lenght:" + length);
        //System.out.println(">Original: " + Arrays.toString(array));
        //System.out.println(">Reversed: " + Arrays.toString(reversed));
        
        return reversed;
    }
    
    public Integer [] makeShift(Integer [] array){
        Random r = new Random();
        
        Integer [] shifted = Arrays.copyOf(array, array.length);
        int iniPos = r.nextInt((sizeX-2)/2);
        int length = r.nextInt((sizeX-2)/2-iniPos);
        int secPos = iniPos + length +  r.nextInt(((sizeX-2)/2)-length);

        for (int i = 0; i < length; i++) {
            shifted[iniPos + i] = array [secPos + i];
            shifted[secPos + i] = array [iniPos + i];
        }

        //System.out.println(">>firstPos: " + iniPos + " SecondPos: " + secPos + " Lenght:" + length);
        //System.out.println(">Original: " + Arrays.toString(array));
        //System.out.println("> Shifted: " + Arrays.toString(shifted));
        
        return shifted;
    }
    
    public Integer[][] getMutatedChildren(Integer[][] matchedMatrix){
        Random r =new Random();
        Integer[][] mutedChildren = matchedMatrix;
        for(int j =0;j<sizeY;j++){
            if(r.nextBoolean()){
                mutedChildren [j] = makeReverse(matchedMatrix[j]);
            }
            else{
                mutedChildren [j] = makeShift(matchedMatrix[j]);
            }
        }
        
        return mutedChildren;
    }
}
