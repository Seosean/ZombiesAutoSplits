package com.seosean.zombiesautosplits.splitter;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface LiveSplitSplitter {

    CompletableFuture<Void> startOrSplit();

}
