package com.wupipi.coedit.model;

import java.util.Arrays;

/**
 * User: xudong
 * Date: 9/6/14
 * Time: 7:51 AM
 */
public class Change {
    private String uid;

    private int revision;

    private Pos from;
    private Pos to;
    private String[] text;
    private String[] removed;
    private String origin;

    public Pos getFrom() {
        return from;
    }

    public void setFrom(Pos from) {
        this.from = from;
    }

    public Pos getTo() {
        return to;
    }

    public void setTo(Pos to) {
        this.to = to;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String[] getRemoved() {
        return removed;
    }

    public void setRemoved(String[] removed) {
        this.removed = removed;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return "Change{" +
                "uid='" + uid + '\'' +
                ", revision=" + revision +
                ", from=" + from +
                ", to=" + to +
                ", text=" + Arrays.toString(text) +
                ", removed=" + Arrays.toString(removed) +
                ", origin='" + origin + '\'' +
                '}';
    }
}
