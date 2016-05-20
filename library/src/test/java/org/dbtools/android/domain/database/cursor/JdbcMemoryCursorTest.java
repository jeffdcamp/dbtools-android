package org.dbtools.android.domain.database.cursor;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JdbcMemoryCursorTest {

    private String[] columnNames = {"_id", "first_name", "last_name"};
    private List<List<Object>> emptyList = new ArrayList<>();
    private List<List<Object>> singleList = Collections.singletonList(Arrays.<Object>asList(1, "James Earl", "Jones"));
    private List<List<Object>> mainList;

    JdbcMemoryCursor emptyCursor = new JdbcMemoryCursor(columnNames, emptyList);
    JdbcMemoryCursor singleCursor = new JdbcMemoryCursor(columnNames, singleList);
    JdbcMemoryCursor mainCursor;

    @Before
    public void setUp() throws Exception {
        mainList = new ArrayList<>();
        mainList.add(Arrays.<Object>asList(1, "Darth", "Vader"));
        mainList.add(Arrays.<Object>asList(2, "Boba", "Fett"));
        mainList.add(Arrays.<Object>asList(3, "Luke", "Skywalker"));
        mainList.add(Arrays.<Object>asList(4, "Leia", "Organa"));
        mainList.add(Arrays.<Object>asList(5, "Han", "Solo"));

        mainCursor = new JdbcMemoryCursor(columnNames, mainList);
    }

    @Test
    public void moveToFirst() throws Exception {
        assertFalse("EmptyCursor", emptyCursor.moveToFirst());
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertTrue("SingleCursor", singleCursor.moveToFirst());
        assertEquals("SingleCursor", 0, singleCursor.getPosition());
        assertEquals("SingleCursor", "James Earl", singleCursor.getString(singleCursor.getColumnIndex("first_name")));
        assertTrue("MainCursor", mainCursor.moveToFirst());
        assertEquals("MainCursor", 0, mainCursor.getPosition());
        assertEquals("MainCursor", "Darth", mainCursor.getString(singleCursor.getColumnIndex("first_name")));
    }

    @Test
    public void moveToLast() throws Exception {
        assertFalse("EmptyCursor", emptyCursor.moveToLast());
        assertEquals("EmptyCursor", -1, emptyCursor.getPosition()); // Undefined behavior matching AbstractCursor
        assertTrue("SingleCursor", singleCursor.moveToLast());
        assertEquals("SingleCursor", 0, singleCursor.getPosition());
        assertEquals("SingleCursor", "James Earl", singleCursor.getString(singleCursor.getColumnIndex("first_name")));
        assertTrue("MainCursor", mainCursor.moveToLast());
        assertEquals("MainCursor", 4, mainCursor.getPosition());
        assertEquals("MainCursor", "Han", mainCursor.getString(singleCursor.getColumnIndex("first_name")));
    }

    @Test
    public void moveToNext() throws Exception {
        emptyCursor.moveToFirst();
        singleCursor.moveToFirst();
        mainCursor.moveToFirst();
        assertFalse("EmptyCursor", emptyCursor.moveToNext());
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.moveToNext());
        assertEquals("SingleCursor", 1, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.moveToNext());
        assertEquals("MainCursor", 1, mainCursor.getPosition());
        assertEquals("MainCursor", "Boba", mainCursor.getString(singleCursor.getColumnIndex("first_name")));
    }

    @Test
    public void moveToPrevious() throws Exception {
        emptyCursor.moveToLast();
        singleCursor.moveToLast();
        mainCursor.moveToLast();
        assertFalse("EmptyCursor", emptyCursor.moveToPrevious());
        assertEquals("EmptyCursor", -1, emptyCursor.getPosition()); // Undefined behavior matching AbstractCursor
        assertFalse("SingleCursor", singleCursor.moveToPrevious());
        assertEquals("SingleCursor", -1, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.moveToPrevious());
        assertEquals("MainCursor", 3, mainCursor.getPosition());
        assertEquals("MainCursor", "Leia", mainCursor.getString(singleCursor.getColumnIndex("first_name")));
    }

    @Test
    public void moveToPosition() throws Exception {
        assertFalse("EmptyCursor", emptyCursor.moveToPosition(-5));
        assertEquals("EmptyCursor", -1, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.moveToPosition(-5));
        assertEquals("SingleCursor", -1, singleCursor.getPosition());
        assertFalse("MainCursor", mainCursor.moveToPosition(-5));
        assertEquals("MainCursor", -1, mainCursor.getPosition());

        assertFalse("EmptyCursor", emptyCursor.moveToPosition(0));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertTrue("SingleCursor", singleCursor.moveToPosition(0));
        assertEquals("SingleCursor", 0, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.moveToPosition(0));
        assertEquals("MainCursor", 0, mainCursor.getPosition());

        assertFalse("EmptyCursor", emptyCursor.moveToPosition(4));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.moveToPosition(4));
        assertEquals("SingleCursor", 1, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.moveToPosition(4));
        assertEquals("MainCursor", 4, mainCursor.getPosition());

        assertFalse("EmptyCursor", emptyCursor.moveToPosition(10));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.moveToPosition(10));
        assertEquals("SingleCursor", 1, singleCursor.getPosition());
        assertFalse("MainCursor", mainCursor.moveToPosition(10));
        assertEquals("MainCursor", 5, mainCursor.getPosition());
    }

    @Test
    public void move() throws Exception {
        emptyCursor.moveToFirst();
        singleCursor.moveToFirst();
        mainCursor.moveToFirst();

        // Move some negative to move before first row
        assertFalse("EmptyCursor", emptyCursor.move(-5));
        assertEquals("EmptyCursor", -1, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.move(-5));
        assertEquals("SingleCursor", -1, singleCursor.getPosition());
        assertFalse("MainCursor", mainCursor.move(-5));
        assertEquals("MainCursor", -1, mainCursor.getPosition());

        // Move 1 to move to first row if available
        assertFalse("EmptyCursor", emptyCursor.move(1));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertTrue("SingleCursor", singleCursor.move(1));
        assertEquals("SingleCursor", 0, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.move(1));
        assertEquals("MainCursor", 0, mainCursor.getPosition());

        // move to end of main cursor
        assertFalse("EmptyCursor", emptyCursor.move(4));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.move(4));
        assertEquals("SingleCursor", 1, singleCursor.getPosition());
        assertTrue("MainCursor", mainCursor.move(4));
        assertEquals("MainCursor", 4, mainCursor.getPosition());

        // Move past main cursor
        assertFalse("EmptyCursor", emptyCursor.move(10));
        assertEquals("EmptyCursor", 0, emptyCursor.getPosition());
        assertFalse("SingleCursor", singleCursor.move(10));
        assertEquals("SingleCursor", 1, singleCursor.getPosition());
        assertFalse("MainCursor", mainCursor.move(10));
        assertEquals("MainCursor", 5, mainCursor.getPosition());
    }

    @Test
    public void isFirst() throws Exception {
        emptyCursor.moveToFirst();
        singleCursor.moveToFirst();
        mainCursor.moveToFirst();
        assertFalse("EmptyCursor", emptyCursor.isFirst());
        assertTrue("SingleCursor", singleCursor.isFirst());
        assertTrue("MainCursor", mainCursor.isFirst());

        emptyCursor.moveToNext();
        singleCursor.moveToNext();
        mainCursor.moveToNext();
        assertFalse("EmptyCursor", emptyCursor.isFirst());
        assertFalse("SingleCursor", singleCursor.isFirst());
        assertFalse("MainCursor", mainCursor.isFirst());

    }

    @Test
    public void isBeforeFirst() throws Exception {
        emptyCursor.moveToFirst();
        singleCursor.moveToFirst();
        mainCursor.moveToFirst();
        assertFalse("EmptyCursor", emptyCursor.isBeforeFirst()); // Undefined behavior matching AbstractCursor
        assertFalse("SingleCursor", singleCursor.isBeforeFirst());
        assertFalse("MainCursor", mainCursor.isBeforeFirst());

        emptyCursor.moveToPrevious();
        singleCursor.moveToPrevious();
        mainCursor.moveToPrevious();
        assertTrue("EmptyCursor", emptyCursor.isBeforeFirst());
        assertTrue("SingleCursor", singleCursor.isBeforeFirst());
        assertTrue("MainCursor", mainCursor.isBeforeFirst());

    }

    @Test
    public void isLast() throws Exception {
        emptyCursor.moveToLast();
        singleCursor.moveToLast();
        mainCursor.moveToLast();
        assertFalse("EmptyCursor", emptyCursor.isLast());
        assertTrue("SingleCursor", singleCursor.isLast());
        assertTrue("MainCursor", mainCursor.isLast());

        emptyCursor.moveToNext();
        singleCursor.moveToNext();
        mainCursor.moveToNext();
        assertFalse("EmptyCursor", emptyCursor.isLast());
        assertFalse("SingleCursor", singleCursor.isLast());
        assertFalse("MainCursor", mainCursor.isLast());

    }

    @Test
    public void isAfterLast() throws Exception {
        emptyCursor.moveToLast();
        singleCursor.moveToLast();
        mainCursor.moveToLast();
        assertFalse("EmptyCursor", emptyCursor.isAfterLast()); // Undefined behavior matching AbstractCursor
        assertFalse("SingleCursor", singleCursor.isAfterLast());
        assertFalse("MainCursor", mainCursor.isAfterLast());

        emptyCursor.moveToNext();
        singleCursor.moveToNext();
        mainCursor.moveToNext();
        assertTrue("EmptyCursor", emptyCursor.isAfterLast());
        assertTrue("SingleCursor", singleCursor.isAfterLast());
        assertTrue("MainCursor", mainCursor.isAfterLast());

    }
}