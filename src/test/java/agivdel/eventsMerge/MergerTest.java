package agivdel.eventsMerge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class MergerTest {
    Merger merger;

    private final LocalTime t1 = LocalTime.of(10, 0);
    private final LocalTime t2 = LocalTime.of(10, 30);
    private final LocalTime t3 = LocalTime.of(11, 0);
    private final LocalTime t4 = LocalTime.of(11, 30);
    private final LocalTime t5 = LocalTime.of(12, 0);
    private final LocalTime t6 = LocalTime.of(12, 30);
    private final LocalTime t7 = LocalTime.of(13, 0);
    private final LocalTime t8 = LocalTime.of(13, 30);
    private final LocalTime t9 = LocalTime.of(14, 0);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void beforeTest() {
        merger = new Merger();
    }

    @Test
    public void isOverlapTest() {
        Event e1 = new Event(t2, t3);

        Event e2 = new Event(t2, t5);
        Assert.assertTrue(merger.isOverlap(e1, e2));

        Event e3 = new Event(t1, t4);
        Assert.assertTrue(merger.isOverlap(e1, e3));
    }

    @Test
    public void isNotOverlapTest() {
        Event e1 = new Event(t2, t3);

        Event e2 = new Event(t3, t6);
        Assert.assertFalse(merger.isOverlap(e1, e2));

        Event e3 = new Event(t1, t2);
        Assert.assertFalse(merger.isOverlap(e1, e3));
    }

    @Test
    public void mergeTest() {
        Event e1 = new Event(t1, t7);
        Event e2 = new Event(t2, t4);

        e1 = merger.mergeOf(e1, e2);
        Assert.assertEquals(t1, e1.getStart().toLocalTime());
        Assert.assertEquals(t7, e1.getEnd().toLocalTime());
    }

    @Test
    public void setToMergeWithNullSetTest() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The eventSet can't be null.");
        merger.setToMerge(null);
    }

    @Test
    public void setToMergeWithEmptySetTest() {
        Set<Event> hashSet = merger.setToMerge(new HashSet<>());
        Assert.assertEquals(new HashSet<>(), hashSet);

        Set<Event> treeSet = merger.setToMerge(new TreeSet<>());
        Assert.assertEquals(new TreeSet<>(), treeSet);

        Set<Event> linkedHashSet = merger.setToMerge(new LinkedHashSet<>());
        Assert.assertEquals(new LinkedHashSet<>(), linkedHashSet);
    }

    @Test
    public void hashSetToMergeWithOnlyNullElementTest() {
        Set<Event> eventHashSet0 = new HashSet<>();
        eventHashSet0.add(null);

        Set<Event> eventSet = merger.setToMerge(eventHashSet0);
        Assert.assertEquals(eventSet, eventHashSet0);
    }

    @Test
    public void linkedHashSetToMergeWithOnlyNullElementTest() {
        Set<Event> eventLinkedHashSet0 = new LinkedHashSet<>();
        eventLinkedHashSet0.add(null);

        Set<Event> eventSet = merger.setToMerge(eventLinkedHashSet0);
        Assert.assertEquals(eventSet, eventLinkedHashSet0);
    }

    @Test
    public void hashSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventHashSet0 = new HashSet<>();
        eventHashSet0.add(new Event(t1, t8));

        Set<Event> eventHashSet1 = merger.setToMerge(eventHashSet0);
        Assert.assertEquals(eventHashSet0, eventHashSet1);
    }

    @Test
    public void treeSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventTreeSet0 = new TreeSet<>();
        eventTreeSet0.add(new Event(t5, t9));

        Set<Event> eventTreeSet1 = merger.setToMerge(eventTreeSet0);
        Assert.assertEquals(eventTreeSet0, eventTreeSet1);
    }

    @Test
    public void linkedHashSetToMergeWithOnlyNotNullElementTest() {
        Set<Event> eventLinkedHashSet0 = new LinkedHashSet<>();
        eventLinkedHashSet0.add(new Event(t8, t9));

        Set<Event> eventLinkedHashSet1 = merger.setToMerge(eventLinkedHashSet0);
        Assert.assertEquals(eventLinkedHashSet0, eventLinkedHashSet1);
    }

    @Test
    public void setToMergeWithCorrectSetTest() {
        Set<Event> eventSet = getEventSet();
        System.out.println(eventSet);

        Set<Event> mergedSet2 = merger.setToMerge(eventSet);
        Assert.assertEquals(3, mergedSet2.size());
    }

    private Set<Event> getEventSet() {
        Set<Event> eventSet = new TreeSet<>();
        eventSet.add(new Event(t1, t2));
        eventSet.add(new Event(t2, t5));
        eventSet.add(new Event(t4, t7));
        eventSet.add(new Event(t7, t9));
        return eventSet;
    }
}
