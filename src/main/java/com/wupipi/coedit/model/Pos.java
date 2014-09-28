package com.wupipi.coedit.model;

/**
 * User: xudong
 * Date: 9/6/14
 * Time: 7:50 AM
 */
public class Pos {
    private int line;
    private int ch;
    private int xRel;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getxRel() {
        return xRel;
    }

    public void setxRel(int xRel) {
        this.xRel = xRel;
    }

    public Pos(int line, int ch) {
        this.line = line;
        this.ch = ch;
    }

    public Pos() {
    }

    @Override
    public String toString() {
        return "Pos{" +
                "line=" + line +
                ", ch=" + ch +
                ", xRel=" + xRel +
                '}';
    }
}
