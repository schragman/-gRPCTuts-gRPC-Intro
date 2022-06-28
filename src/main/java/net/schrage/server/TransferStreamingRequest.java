package net.schrage.server;

import io.grpc.stub.StreamObserver;
import net.schrage.models.Account;
import net.schrage.models.TransferRequest;
import net.schrage.models.TransferResponse;
import net.schrage.models.TransferStatus;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

  private StreamObserver<TransferResponse> transferResponseStreamObserver;

  public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
    this.transferResponseStreamObserver = transferResponseStreamObserver;
  }

  @Override
  public void onNext(TransferRequest transferRequest) {
    int fromAccount = transferRequest.getFromAccout();
    int toAccount = transferRequest.getToAccount();
    int amount = transferRequest.getAmount();
    int balance = AccountDatabase.getBalance(fromAccount);
    TransferStatus status = TransferStatus.FAILED;

    if (balance >= amount && fromAccount != toAccount) {
      AccountDatabase.deductBalance(fromAccount, amount);
      AccountDatabase.addBalance(toAccount, amount);
      status = TransferStatus.SUCCESS;
    }
    Account fromAccountInfo =
        Account.newBuilder().setAccountNumber(fromAccount).setAmount(AccountDatabase.getBalance(fromAccount)).build();
    Account toAccountInfo =
        Account.newBuilder().setAccountNumber(toAccount).setAmount(AccountDatabase.getBalance(toAccount)).build();
    TransferResponse transferResponse = TransferResponse.newBuilder()
        .setStatus(status)
        .addAccounts(fromAccountInfo)
        .addAccounts(toAccountInfo)
        .build();

    this.transferResponseStreamObserver.onNext(transferResponse);
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onCompleted() {
    AccountDatabase.printAccountDetails();
    this.transferResponseStreamObserver.onCompleted();
  }
}
