package net.schrage.client.rpctypes;

import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import net.schrage.models.Money;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

  private CountDownLatch latch;

  public MoneyStreamingResponse(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onNext(Money money) {
    System.out.println("Received async : " + money.getValue());
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
    this.latch.countDown();
  }

  @Override
  public void onCompleted() {
    System.out.println("Server is done");
    this.latch.countDown();
  }
}
