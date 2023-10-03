package com.seosean.zombiesautosplits.splitter.socket;

import com.seosean.zombiesautosplits.splitter.LiveSplitSplitter;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LiveSplitSocketSplitter implements LiveSplitSplitter {

    private final Executor executor;

    private final String host;

    private final int port;

    public LiveSplitSocketSplitter(Executor executor, String host, int port) {
        this.executor = Objects.requireNonNull(executor, "executor");
        this.host = Objects.requireNonNull(host, "host");
        this.port = port;
    }

    @Override
    public CompletableFuture<Void> startOrSplit() {
        return sendCommand("startorsplit");
    }

    @SuppressWarnings("SameParameterValue")
    private CompletableFuture<Void> sendCommand(String command) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        executor.execute(() -> {
            try (Socket socket = new Socket(host, port);
                 OutputStream outputStream = socket.getOutputStream();
                 Writer writer = new OutputStreamWriter(outputStream)) {
                writer.write(command + "\r\n");
                writer.flush();

                future.complete(null);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}
