package net.schrage.server.rpctypes;

import io.grpc.stub.StreamObserver;
import net.schrage.models.Balance;
import net.schrage.models.DepositRequest;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

  public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
    this.balanceStreamObserver = balanceStreamObserver;
  }

  private StreamObserver<Balance> balanceStreamObserver;
  private int accountBalance;

  @Override
  public void onNext(DepositRequest depositRequest) {
    int accountNumber = depositRequest.getAccountNumber();
    int amount = depositRequest.getAmount();
    this.accountBalance = AccountDatabase.addBalance(accountNumber, amount);
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onCompleted() {
    Balance balance = Balance.newBuilder().setAmount(this.accountBalance).build();
    this.balanceStreamObserver.onNext(balance);
    this.balanceStreamObserver.onCompleted();
  }
}
