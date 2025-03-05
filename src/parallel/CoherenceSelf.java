package parallel;

import sequentiel.MatrixProduct_sequentiel;

public class CoherenceSelf {
    private static final int M = 4096;
    private static final int N = 2048;
    private static final int P = 2048;
    private static final int[] THREAD_COUNTS = {2, 4, 6, 8, 10, 12, 20, 30};
    private static final int[] GROUP_SIZES = {8, 64, 128, 256, 512, 1024, 2048};

    public static void main(String[] args) {
        // Matrices pour version séquentielle
        double[][] A_seq = new double[M][N];
        double[][] B_seq = new double[N][P];
        double[][] C_seq = new double[M][P];
        
        // Exécution séquentielle
        System.out.println("Exécution séquentielle...");
        MatrixProduct_sequentiel seqProduct = new MatrixProduct_sequentiel(A_seq, B_seq, C_seq);
        seqProduct.initialize();
        
        long seqStartTime = System.currentTimeMillis();
        seqProduct.multiplierMatrice();
        long seqTime = System.currentTimeMillis() - seqStartTime;
        
        System.out.println("Temps séquentiel: " + (seqTime/1000.0) + " s");

        // Pour chaque taille de groupe
        for (int groupSize : GROUP_SIZES) {
            System.out.println("\n=== Tests avec taille de groupe " + groupSize + " ===");
            System.out.println("Threads\tTemps (s)\tAccélération\tCohérence");
            System.out.println("------------------------------------------------");

            // Tests avec différents nombres de threads
            for (int threadCount : THREAD_COUNTS) {
                // Nouvelles matrices pour version parallèle
                double[][] A_par = new double[M][N];
                double[][] B_par = new double[N][P];
                double[][] C_par = new double[M][P];
                
                MatrixProduct_parallel_self parProduct = new MatrixProduct_parallel_self(A_par, B_par, C_par);
                parProduct.initialize();
                
                long parStartTime = System.currentTimeMillis();
                parProduct.multiplierMatrice(threadCount, groupSize);  // Utilisation de la nouvelle surcharge
                long parTime = System.currentTimeMillis() - parStartTime;
                
                // Vérification de la cohérence
                boolean coherent = verifierCoherence(C_seq, C_par);
                double acceleration = (double)seqTime / parTime;
                
                System.out.printf("%d\t%.3f\t\t%.3f\t\t%s%n", 
                    threadCount, 
                    parTime/1000.0, 
                    acceleration,
                    coherent ? "OK" : "ERREUR");
            }
        }
    }

    private static boolean verifierCoherence(double[][] C_seq, double[][] C_par) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < P; j++) {
                if (Math.abs(C_seq[i][j] - C_par[i][j]) > 1e-10) {
                    return false;
                }
            }
        }
        return true;
    }
}
