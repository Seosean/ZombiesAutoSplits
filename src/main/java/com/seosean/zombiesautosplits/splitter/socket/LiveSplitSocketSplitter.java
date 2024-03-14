package com.seosean.zombiesautosplits.splitter.socket;

import com.seosean.zombiesautosplits.splitter.LiveSplitSplitter;
import org.lwjgl.Sys;
import scala.collection.DebugUtils;

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
        try {
            Socket socket = new Socket(host, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write(command + "\r\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

}
