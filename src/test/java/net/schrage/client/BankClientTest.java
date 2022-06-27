package net.schrage.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.schrage.models.Balance;
import net.schrage.models.BalanceCheckRequest;
import net.schrage.models.BankServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

  private BankServiceGrpc.BankServiceBlockingStub blockingStub;

  @BeforeAll
  public void setup() {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

  }

  @Test
  public void balanceTest() {
    BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
        .setAccountNumber(7)
        .build();

    Balance balance = this.blockingStub.getBalance(balanceCheckRequest);

    System.out.println("Received: " + balance.getAmount());
  }


}
