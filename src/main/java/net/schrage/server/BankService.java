package net.schrage.server;

import io.grpc.stub.StreamObserver;
import net.schrage.models.Balance;
import net.schrage.models.BalanceCheckRequest;
import net.schrage.models.BankServiceGrpc;

public class BankService extends BankServiceGrpc.BankServiceImplBase{

  @Override
  public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

    int accountNumber = request.getAccountNumber();
    Balance balance = Balance.newBuilder()
        .setAmount(AccountDatabase.getBalance(accountNumber))
        .build();

    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }
}
