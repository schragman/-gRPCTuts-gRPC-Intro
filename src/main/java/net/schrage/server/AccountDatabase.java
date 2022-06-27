package net.schrage.server;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {

  /**
   * This is a Test DB
   */
  private static final Map<Integer, Integer> MAP = IntStream
      .rangeClosed(1, 10)
      .boxed()
      .collect(Collectors.toMap(Function.identity(), v -> v * 10));

  public static int getBalance(int accoundId) {
    return MAP.get(accoundId);
  }

  public static Integer addBalance(int accountId, int amount) {
    return MAP.computeIfPresent(accountId, (k, v) -> v + amount);
  }

  public static Integer deductBalance(int accountId, int amount) {
    return MAP.computeIfPresent(accountId, (k, v) -> v - amount);
  }

}
