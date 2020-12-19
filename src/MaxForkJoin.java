// Реализация метода ForkJoin, реализующего алгоритм разделяй и властвуй
// на примере нахождения максимального числа в многопоточном режиме...

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class MaxForkJoin {

  static ExecutorService pool = Executors.newWorkStealingPool(10);

  static int parallelMax(List<Integer> list) throws ExecutionException, InterruptedException {

    if(list.size() == 1)
      return list.get(0);

    List<Integer> l1 = list.subList(0, list.size() / 2);
    List<Integer> l2 = list.subList(list.size() / 2, list.size());

    Future<Integer> r1 = pool.submit(() -> parallelMax(l1));
    Future<Integer> r2 = pool.submit(() -> parallelMax(l2));

    return Math.max(r1.get(), r2.get());

  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    Random random = new Random(1);

    List<Integer> ints = random.ints(100).boxed().collect(Collectors.toList());

    // Первый вариант нахождения максимального значения из определенной в заданном классе функции...
    int result = parallelMax(ints);

    // Второй вариант нахождения максимального значения из стандартной функции...
    Optional<Integer> result2 = ints.parallelStream().reduce(Math::max);

    System.out.println("Result = " + result);

    pool.shutdown();

  }
}