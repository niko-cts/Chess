package chatzis.nikolas.schachreader.test;

import chatzis.nikolas.schachreader.utils.FieldNameConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FieldNameConverterTest {

    @Test
    void fromFieldNumber() {
        assertEquals("a1", FieldNameConverter.fromFieldNumber((byte) 0));
        assertEquals("c1", FieldNameConverter.fromFieldNumber((byte) 2));
        assertEquals("h1", FieldNameConverter.fromFieldNumber((byte) 7));
        assertEquals("a2", FieldNameConverter.fromFieldNumber((byte) 8));
        assertEquals("d2", FieldNameConverter.fromFieldNumber((byte) 11));
        assertEquals("h2", FieldNameConverter.fromFieldNumber((byte) 15));
        assertEquals("c4", FieldNameConverter.fromFieldNumber((byte) 26));
        assertEquals("g6", FieldNameConverter.fromFieldNumber((byte) 46));
    }

    @Test
    void fromFieldName() {
        assertEquals(0, FieldNameConverter.fromFieldName("a1"));
        assertEquals(2, FieldNameConverter.fromFieldName("c1"));
        assertEquals(8, FieldNameConverter.fromFieldName("a2"));
        assertEquals(11, FieldNameConverter.fromFieldName("d2"));
        assertEquals(26, FieldNameConverter.fromFieldName("c4"));
        assertEquals(46, FieldNameConverter.fromFieldName("g6"));
    }
}