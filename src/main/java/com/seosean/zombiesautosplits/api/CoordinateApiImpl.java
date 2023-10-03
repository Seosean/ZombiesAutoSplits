package com.seosean.zombiesautosplits.api;

import com.seosean.zombiesautosplits.ZombiesAutoSplits;

import static com.seosean.zombiesautosplits.ZombiesAutoSplits.XSplitter;
import static com.seosean.zombiesautosplits.ZombiesAutoSplits.YSplitter;

public class CoordinateApiImpl implements CoordinateApi {
    @Override
    public double getX(){
        return ZombiesAutoSplits.getInstance().getXSplitter();
    }

    @Override
    public double getY(){
        return ZombiesAutoSplits.getInstance().getYSplitter();
    }
}
