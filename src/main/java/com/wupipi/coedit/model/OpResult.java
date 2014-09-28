package com.wupipi.coedit.model;

/**
 * User: xudong
 * Date: 9/12/14
 * Time: 10:23 PM
 */
public class OpResult {
    private Change change;
    private boolean resync;
    private int nextRevision;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public boolean isResync() {
        return resync;
    }

    public void setResync(boolean resync) {
        this.resync = resync;
    }

    public int getNextRevision() {
        return nextRevision;
    }

    public void setNextRevision(int nextRevision) {
        this.nextRevision = nextRevision;
    }

    @Override
    public String toString() {
        return "OpResult{" +
                "change=" + change +
                ", resync=" + resync +
                ", nextRevision=" + nextRevision +
                '}';
    }
}
