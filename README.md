# Guide de Compilation et d'Exécution

Ce projet contient différentes implémentations de multiplication de matrices et de calcul de Fibonacci utilisant différentes approches de parallélisation.

## 1. Version Séquentielle

### Compilation
```bash
javac src/sequentiel/MatrixProduct_sequentiel.java
```

### Exécution
```bash
java -cp src sequentiel.MatrixProduct_sequentiel
```

## 2. Versions Parallèles avec Static Scheduling


### Compilation
```bash
javac src/schedule/staticSchedule.java -d bin
javac -cp bin src/parallel/MatrixProduct_parallel_static.java -d bin
javac -cp bin src/parallel/CoherenceStatic.java -d bin
```

### Exécution
```bash
# Pour tester la version parallèle simple
java -cp bin parallel.MatrixProduct_parallel_static

# Pour tester la cohérence et les performances avec différents nombres de threads
java -cp bin parallel.CoherenceStatic
```

## 3. Versions Parallèles avec Self Scheduling

### Compilation
```bash
javac src/schedule/selfSchedule.java -d bin
javac -cp bin src/parallel/MatrixProduct_parallel_self.java -d bin
javac -cp bin src/parallel/CoherenceSelf.java -d bin
```

### Exécution
```bash
# Pour tester la version parallèle simple
java -cp bin parallel.MatrixProduct_parallel_self

# Pour tester la cohérence et les performances avec différentes tailles de groupes
java -cp bin parallel.CoherenceSelf
```

## 4. Versions avec FJComp

### Compilation de Fibonacci.fj
```bash
# Compiler avec fjcomp
java -cp src/compiler.fjcomp.jar FJCompiler.FJComp src/fjcomp/Fibonacci.fj

# Compiler le fichier Java généré
javac src/fjcomp/Fibonacci.java -d bin
```

### Exécution de Fibonacci
```bash
# Exécuter avec paramètres par défaut
java -cp bin fjcomp.Fibonacci



### Compilation et Exécution de Fibo.java, Fibo c'est pour mesurer les performances et la cohérence
```bash
# Compilation
javac src/fjcomp/Fibo.java -d bin

# Exécution
java -cp bin fjcomp.Fibo
```

## Notes Importantes

1. Assurez-vous d'avoir Java JDK installé sur votre système
2. Pour FJComp, assurez-vous d'avoir le compilateur fjc dans votre PATH
3. Les chemins supposent que vous êtes dans le répertoire racine du projet
4. Les tests de cohérence comparent les résultats avec la version séquentielle
5. Les mesures de performance incluent :
   - Temps d'exécution
   - Accélération (speedup)
   - Vérification de la cohérence des résultats

## Structure des Résultats

- La version séquentielle affiche le temps d'exécution
- Les versions parallèles affichent :
  - Le temps d'exécution
  - L'accélération par rapport à la version séquentielle
  - La vérification de la cohérence des résultats
- Les tests avec différentes configurations affichent un tableau comparatif
```

Ce README fournit toutes les informations nécessaires pour compiler et exécuter les différentes versions du programme, ainsi que la structure des résultats attendus.