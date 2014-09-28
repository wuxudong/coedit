package com.wupipi.coedit.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: xudong
 * Date: 9/6/14
 * Time: 12:07 PM
 */
public class Paper {

    List<String> lines = new ArrayList<String>();

    AtomicInteger revision = new AtomicInteger(0);

    public String content() {
        return StringUtils.join(lines, System.lineSeparator());
    }

    public synchronized OpResult applyChange(Change change) {
        // clipPos
        if (change.getFrom().getLine() <0) {
            change.getFrom().setLine(0);
            change.getFrom().setCh(0);

        }

        if (change.getTo().getLine() >= lines.size()) {
            change.getTo().setLine(lines.size() - 1);
            change.getTo().setCh(lines.get(change.getTo().getLine()).length());

        }

        if (change.getTo().getCh() > lines.get(change.getTo().getLine()).length()) {
            change.getTo().setCh(lines.get(change.getTo().getLine()).length());

        }



        if (change.getFrom().getLine() >= lines.size()) {
            change.getFrom().setLine(lines.size() - 1);
            change.getFrom().setCh(lines.get(change.getTo().getLine()).length());

        }

        if (change.getFrom().getCh() > lines.get(change.getTo().getLine()).length()) {
            change.getFrom().setCh(lines.get(change.getTo().getLine()).length());

        }


        boolean resync = false;
        // check revision
        if (revision.incrementAndGet() != (change.getRevision())) {
            resync = true;
        }

        applyChop(change);

        boolean headLine = true;
        int l = 0;
        for (String s : change.getText()) {

            if (headLine) {
                int fromL = change.getFrom().getLine();
                int fromCh = change.getFrom().getCh();
                lines.set(fromL, lines.get(fromL).substring(0,
                        fromCh) + s + lines.get(fromL).substring(fromCh));
                l = fromL;
                l++;
                headLine = false;
            } else {
                lines.add(l, s);
            }
        }

        OpResult result = new OpResult();
        result.setChange(change);
        result.setNextRevision(revision.get() + 1);
        result.setResync(resync);
        return result;

    }

    void applyChop(Change change) {
        int l = change.getTo().getLine();

        boolean singleLine = change.getFrom().getLine() == change.getTo().getLine();

        if (singleLine) {
            chopTextInSingleLine(change.getFrom().getLine(), change.getFrom().getCh(), change.getTo().getCh());
        } else {

            String headString = "";
            String tailString = "";

            while (l >= change.getFrom().getLine()) {
                if (lines.size() > l) {
                    if (l == change.getTo().getLine()) {
                        tailString = chopTextFromLineStart(l, change.getTo().getCh());
                        lines.remove(l);
                    } else if (l == change.getFrom().getLine()) {
                        headString = chopTextToLineEnd(l, change.getFrom().getCh());
                        lines.set(l, headString + tailString);
                    } else {
                        lines.remove(l);
                    }
                }

                l--;
            }

        }
    }

    String chopTextToLineEnd(int l, int ch) {
        String s = lines.get(l);
        return chopTextInSingleLine(l, ch, s.length());
    }

    String chopTextFromLineStart(int l, int ch) {
        return chopTextInSingleLine(l, 0, ch);
    }

    String chopTextInSingleLine(int l, int startCh, int endCh) {
        String s = lines.get(l);
        String headFragment = s.substring(0, startCh);
        String tailFragment = s.substring(endCh, s.length());
        String result = headFragment + tailFragment;
        lines.set(l, result);
        return result;
    }


    public Paper() {
        this.lines = new ArrayList<String>();
        lines.add("");
    }


    // for unit test
    void setLines(List<String> lines) {
        this.lines = lines;
    }

    public int getRevision() {
        return revision.get();
    }
}
