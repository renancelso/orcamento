package br.com.orcamento.util;

import java.util.ArrayList;
import java.util.List;

public class GeradorPartidas {
    public static void main(String[] args) {  
    	
    	int nrEquipes = 16;
    	
        List<String> clubes = new ArrayList<String>();
        
        for (int i = 1; i <= nrEquipes; i++) {
        	clubes.add("Time "+i);
		}        
       
        if (clubes.size() % 2 == 1) {
            clubes.add(0, "");
        }

        int t = clubes.size();
        int m = clubes.size() / 2;
        
        System.out.println("-------------------------------------- 1º turno: --------------------------------------\n");        
        for (int i = 0; i < t - 1; i++) {
            System.out.println((i + 1) + "a rodada: ");
            for (int j = 0; j < m; j++) {
                //Clube está de fora nessa rodada?              
                if (clubes.get(j).isEmpty())
                    continue;

                //Teste para ajustar o mando de campo
                if (j % 2 == 1 || i % 2 == 1 && j == 0) {
                    System.out.println(clubes.get(t - j - 1) + " x " + clubes.get(j) + "   ");
                } else {
                    System.out.println(clubes.get(j) + " x " + clubes.get(t - j - 1) + "   ");
                }
            }
            System.out.println();
            //Gira os clubes no sentido horário, mantendo o primeiro no lugar
            clubes.add(1, clubes.remove(clubes.size()-1));
        }
                
        // 2º turno
        
        clubes = new ArrayList<String>();        
        for (int i = nrEquipes; i >= 1; i--) {
        	clubes.add("Time "+i);
		}              
        if (clubes.size() % 2 == 1) {
            clubes.add(0, "");
        }
        t = clubes.size();
        m = clubes.size() / 2;
        
        System.out.println("-------------------------------------- 2º turno: --------------------------------------\n");
        for (int i = 0; i < t - 1; i++) {
            System.out.println((i + 1) + "a rodada: ");
            for (int j = 0; j < m; j++) {
                //Clube está de fora nessa rodada?              
                if (clubes.get(j).isEmpty())
                    continue;

                //Teste para ajustar o mando de campo
                if (j % 2 == 1 || i % 2 == 1 && j == 0) {
                    System.out.println(clubes.get(t - j - 1) + " x " + clubes.get(j) + "   ");
                } else {
                    System.out.println(clubes.get(j) + " x " + clubes.get(t - j - 1) + "   ");
                }
            }
            System.out.println();
            //Gira os clubes no sentido horário, mantendo o primeiro no lugar
            clubes.add(1, clubes.remove(clubes.size()-1));
        }
        
        
    }
}