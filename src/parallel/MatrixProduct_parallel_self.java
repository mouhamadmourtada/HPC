/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallel;

import schedule.selfSchedule;
import schedule.selfSchedule.LoopRange;

/**
 *
 * @author dell
 */
public class MatrixProduct_parallel_self {
    private static final int M = 4096;
    private static final int N = 2048;
    private static final int P = 2048;
    private double[][] A, B, C;

    public MatrixProduct_parallel_self(double[][] A, double[][] B, double[][] C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    // Initialisation séquentielle des matrices
    public void initialize() {
        // Initialisation de la matrice A
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = 1.0;
            }
        }

        // Initialisation de la matrice B
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < P; j++) {
                B[i][j] = 1.0;
            }
        }

        // Initialisation de la matrice C
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < P; j++) {
                C[i][j] = 0.0;
            }
        }
    }

    // Classe interne pour les threads de multiplication
    private class MultiplicationTask implements Runnable {
        private final selfSchedule schedule;

        public MultiplicationTask(selfSchedule schedule) {
            this.schedule = schedule;
        }

        @Override
        public void run() {
            LoopRange range;
            while ((range = schedule.loopGetRange()) != null) {
                for (int i = range.start; i <= range.end; i++) {
                    for (int j = 0; j < P; j++) {
                        double sum = 0.0;
                        for (int k = 0; k < N; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] = sum;
                    }
                }
            }
        }
    }

    // // Multiplication parallélisée avec self-scheduling
    // public void multiplierMatrice(int numThreads) {
    //     int groupSize = Math.max(1, M / (numThreads * 4));
    //     selfSchedule schedule = new selfSchedule(0, M - 1, groupSize);

    //     Thread[] threads = new Thread[numThreads];
    //     for (int t = 0; t < numThreads; t++) {
    //         threads[t] = new Thread(new MultiplicationTask(schedule));
    //     }

    //     for (Thread t : threads) {
    //         t.start();
    //     }

    //     for (Thread t : threads) {
    //         try {
    //             t.join();
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    // Nouvelle surcharge pour spécifier la taille du groupe
    public void multiplierMatrice(int numThreads, int groupSize) {
        selfSchedule schedule = new selfSchedule(0, M - 1, groupSize);

        Thread[] threads = new Thread[numThreads];
        for (int t = 0; t < numThreads; t++) {
            threads[t] = new Thread(new MultiplicationTask(schedule));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // La méthode multiplierMatrice(int numThreads) existante peut maintenant utiliser la nouvelle surcharge
    public void multiplierMatrice(int numThreads) {
        int defaultGroupSize = Math.max(1, M / (numThreads * 4));
        multiplierMatrice(numThreads, defaultGroupSize);
    }

    // Surcharge de la méthode originale pour maintenir la compatibilité
    public void multiplierMatrice() {
        multiplierMatrice(Runtime.getRuntime().availableProcessors());
    }

    //Procedure pour afficher le resultat du produit de matrice C=A*B
    public void print(){
        for (int i = 0; i < M; i++)    
        {            
            for(int j = 0; j < P; j++){
                System.out.print(C[i][j] + " ");
            }
            System.out.println();        
        }      
                
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        double[][] A = new double[M][N];
        double[][] B = new double[N][P];
        double[][] C = new double[M][P];

        MatrixProduct_parallel_self matrixProduct = new MatrixProduct_parallel_self(A, B, C);

        matrixProduct.initialize();
        matrixProduct.multiplierMatrice();

        long stopTime = System.currentTimeMillis();
        System.out.println("Temps d'execution : " + (stopTime - startTime) / 1000.0 + " s");

        matrixProduct.print();
    }
}