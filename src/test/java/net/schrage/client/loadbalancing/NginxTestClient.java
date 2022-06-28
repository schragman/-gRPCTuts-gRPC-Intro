package net.schrage.client.loadbalancing;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.schrage.models.Balance;
import net.schrage.models.BalanceCheckRequest;
import net.schrage.models.BankServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {

  private BankServiceGrpc.BankServiceBlockingStub blockingStub;

  @BeforeAll
  public void setup() {

    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8585)
        .usePlaintext()
        .build();

    this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

  }

  @Test
  public void balanceTest() throws InterruptedException {

    for (int i = 0; i < 100; i++) {
      BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
          .setAccountNumber(ThreadLocalRandom.current().nextInt(1,11))
          .build();
      Thread.sleep(500);
      Balance balance = this.blockingStub.getBalance(balanceCheckRequest);

      System.out.println("Received: " + balance.getAmount());
    }
  }
}
