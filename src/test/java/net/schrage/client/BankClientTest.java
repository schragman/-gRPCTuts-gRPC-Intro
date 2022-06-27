package net.schrage.client;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.schrage.models.Balance;
import net.schrage.models.BalanceCheckRequest;
import net.schrage.models.BankServiceGrpc;
import net.schrage.models.WithdrawRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

  private BankServiceGrpc.BankServiceBlockingStub blockingStub;
  private BankServiceGrpc.BankServiceStub nonBlockingStub;

  @BeforeAll
  public void setup() {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
    this.nonBlockingStub = BankServiceGrpc.newStub(managedChannel);

  }

  @Test
  public void balanceTest() {
    BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
        .setAccountNumber(7)
        .build();

    Balance balance = this.blockingStub.getBalance(balanceCheckRequest);

    System.out.println("Received: " + balance.getAmount());
  }

  @Test
  public void withdrawTest() {
    WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build();
    this.blockingStub.withdraw(withdrawRequest)
        .forEachRemaining(money -> System.out.println("Received : " + money.getValue()));
  }

  @Test
  public void withdrawAsyncTest() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build();
    this.nonBlockingStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
    latch.await();
    //Uninterruptibles.sleepUninterruptibly(4, TimeUnit.SECONDS);
  }


}
