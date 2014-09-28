package com.wupipi.coedit.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PaperTest {

    @org.junit.Test
    public void testChopTextInSingleLineAtMiddle() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world"));


        Paper paper = new Paper();
        paper.setLines(lines);

        paper.chopTextInSingleLine(0, 2, 5);

        assertEquals("lione: hello world", paper.content());

    }

    @org.junit.Test
    public void testChopTextInSingleLineAtBegining() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world"));


        Paper paper = new Paper();
        paper.setLines(lines);

        paper.chopTextInSingleLine(0, 0, 5);

        assertEquals("one: hello world", paper.content());

    }

    @org.junit.Test
    public void testChopTextInSingleLineAtEnd() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world"));


        Paper paper = new Paper();
        paper.setLines(lines);

        paper.chopTextInSingleLine(0, 7, 21);

        assertEquals("line on", paper.content());

    }

    @org.junit.Test
    public void testChopTextInSingleLineWholeLine() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world"));


        Paper paper = new Paper();
        paper.setLines(lines);

        paper.chopTextInSingleLine(0, 0, 21);

        assertEquals("", paper.content());

    }

    @org.junit.Test
    public void testChopTextInMultiline() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world", "line two: nice day"));


        Paper paper = new Paper();
        paper.setLines(lines);

        Change change = new Change();
        change.setFrom(new Pos(0, 16));
        change.setTo(new Pos(1, 5));
        paper.applyChop(change);

        assertEquals("line one: hello two: nice day", paper.content());

    }
    @org.junit.Test
    public void testDeleteMultiline() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world", "line two: nice day", "line three: wow"));


        Paper paper = new Paper();
        paper.setLines(lines);

        Change change = new Change();
        change.setFrom(new Pos(0, 0));
        change.setTo(new Pos(2, 0));
        paper.applyChop(change);

        assertEquals("line three: wow", paper.content());
    }

    @org.junit.Test
    public void testApplyChange() throws Exception {
        List<String> lines = new ArrayList<String>(Arrays.asList("line one: hello world", "line two: nice day", "line three: wow"));


        Paper paper = new Paper();
        paper.setLines(lines);

        Change change = new Change();
        change.setFrom(new Pos(0, 8));
        change.setTo(new Pos(1, 8));
        change.setText(new String[]{"y"});
        paper.applyChange(change);

        assertArrayEquals(new String[]{"line oney: nice day", "line three: wow"}, paper.lines.toArray(new String[0]));
    }
}