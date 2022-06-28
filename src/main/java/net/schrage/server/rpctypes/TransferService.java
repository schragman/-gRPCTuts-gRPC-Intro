package net.schrage.server.rpctypes;

import io.grpc.stub.StreamObserver;
import net.schrage.models.TransferRequest;
import net.schrage.models.TransferResponse;
import net.schrage.models.TransferServiceGrpc;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
  @Override
  public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
    return new TransferStreamingRequest(responseObserver);
  }
}
