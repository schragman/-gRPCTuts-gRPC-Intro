package net.schrage.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.schrage.models.BankServiceGrpc;
import net.schrage.models.TransferRequest;
import net.schrage.models.TransferServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

  private TransferServiceGrpc.TransferServiceStub nonBlockingStub;

  @BeforeAll
  public void setup() {
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    this.nonBlockingStub = TransferServiceGrpc.newStub(managedChannel);
  }

  @Test
  public void transfer() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    TransferStreamingResponse response = new TransferStreamingResponse(latch);
    StreamObserver<TransferRequest> requestStreamObserver = this.nonBlockingStub.transfer(response);

    for (int i = 0; i < 100; i++) {
      TransferRequest request = TransferRequest.newBuilder()
          .setFromAccout(ThreadLocalRandom.current().nextInt(1, 11))
          .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
          .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
          .build();
      requestStreamObserver.onNext(request);
    }
    requestStreamObserver.onCompleted();
    latch.await();
  }

}
