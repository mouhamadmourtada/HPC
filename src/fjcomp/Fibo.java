/*************************************************************************
�   FJCOMP Previous version 0.0  - Date: Fevrier 2013*
*   FJCOMP Version 1.0  - updated : Avril 2023     *
*   Ce code est genere et mis en forme par le compilateur FJComp         *
* Auteur du Compilateur: Abdourahmane Senghor  -- boya2senghor@yahoo.fr  *
**************************************************************************/

package fjcomp;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class Fibo {
   private final int numThreads;
   private final int maxDepth;
   
   public Fibo(int numThreads, int maxDepth) {
       this.numThreads = numThreads;
       this.maxDepth = maxDepth;
   }
   
   public long fibonacci(int n) {
       ForkJoinPool pool = new ForkJoinPool(numThreads);
       fibonacciImpl afibonacciImpl = new fibonacciImpl(0, n);
       pool.invoke(afibonacciImpl);
       return afibonacciImpl.result;
   }

   private class fibonacciImpl extends RecursiveAction {
      private int currentDepth;
      private int n;
      private long result;

      private fibonacciImpl(int currentDepth, int n) {
         this.currentDepth = currentDepth;
         this.n = n;
      }

      protected void compute() {
         if (currentDepth >= maxDepth) {
            result = fibonacci(n);
         } else {
            if (n == 0) {
               result = 0;
               return;
            }
            if (n == 1) {
               result = 1;
               return;
            }
            fibonacciImpl task1 = new fibonacciImpl(currentDepth + 1, n - 1);
            fibonacciImpl task2 = new fibonacciImpl(currentDepth + 1, n - 2);
            invokeAll(task1, task2);
            result = task1.result + task2.result;
         }
      }

      private long fibonacci(int n) {
         if (n == 0) return 0;
         if (n == 1) return 1;
         return fibonacci(n - 1) + fibonacci(n - 2);
      }
   }

   public static long fiboseq(int n){
      if (n == 0) {
         return 0;
     }
     if (n == 1) {
         return 1;
     }
     long x, y;        
         x = fiboseq(n - 1);           
         y = fiboseq(n - 2); 
     return x + y;
   }

   public static void main(String[] args) {
       final int n = 50;
       
       // Test séquentiel avec Fibonacci original
      System.out.println("Exécution séquentielle...");
      // Fibonacci fibSeq = new Fibonacci();
      long seqStartTime = System.currentTimeMillis();
      // long resultatSeq = fibSeq.fibonacci(n);
      long resultatSeq = fiboseq(n);
      long seqTime = System.currentTimeMillis() - seqStartTime;
      System.out.println("Temps séquentiel: " + (seqTime/1000.0) + " s\n");

       // Tests parallèles avec différentes configurations
      System.out.println("Tests parallèles:");
      System.out.println("Threads\tMaxDepth\tTemps (s)\tAccélération\tCohérence");
      System.out.println("--------------------------------------------------------");

       int[] threadCounts = {2, 4, 6, 8, 12, 20, 30};
       int[] maxDepths = {1, 2, 4, 6, 8, 12, 14, 16, 18};

       for (int threads : threadCounts) {
           for (int depth : maxDepths) {
               Fibo fib = new Fibo(threads, depth);
               
               long parStartTime = System.currentTimeMillis();
               long resultatPar = fib.fibonacci(n);
               long parTime = System.currentTimeMillis() - parStartTime;
               
               // Vérification de la cohérence et calcul de l'accélération
               boolean coherent = (resultatSeq == resultatPar);
               double acceleration = (double)seqTime / parTime;
               
               System.out.printf("%d\t%d\t\t%.3f\t\t%.3f\t\t%s%n",
                   threads,
                   depth,
                   parTime/1000.0,
                   acceleration,
                   coherent ? "OK" : "ERREUR");
           }
       }
   }
}
