package builder.world;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for WorldLoadException.
 * Tests exception creation and message generation with various parameters.
 */
public class WorldLoadExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "World loading failed";
        WorldLoadException exception = new WorldLoadException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndRow() {
        String message = "Invalid tile format";
        int row = 5;
        WorldLoadException exception = new WorldLoadException(message, row);

        String result = exception.getMessage();
        assertTrue(result.contains(message));
        assertTrue(result.contains("line 6")); // row 5 means line 6 (0-indexed)
    }

    @Test
    public void testConstructorWithMessageRowAndColumn() {
        String message = "Unexpected character";
        int row = 3;
        int column = 10;
        WorldLoadException exception = new WorldLoadException(message, row, column);

        String result = exception.getMessage();
        assertTrue(result.contains(message));
        assertTrue(result.contains("line 4")); // row 3 means line 4
        assertTrue(result.contains("character 11")); // column 10 means character 11
    }

    @Test
    public void testMessageWithZeroRowAndColumn() {
        String message = "Error at start";
        WorldLoadException exception = new WorldLoadException(message, 0, 0);

        String result = exception.getMessage();
        assertTrue(result.contains(message));
        assertTrue(result.contains("line 1"));
        assertTrue(result.contains("character 1"));
    }

    @Test
    public void testMessageWithLargeRowAndColumn() {
        String message = "Error far away";
        int row = 999;
        int column = 500;
        WorldLoadException exception = new WorldLoadException(message, row, column);

        String result = exception.getMessage();
        assertTrue(result.contains(message));
        assertTrue(result.contains("line 1000"));
        assertTrue(result.contains("character 501"));
    }

    @Test
    public void testMessageWithNegativeRow() {
        String message = "Error";
        int row = -1;
        WorldLoadException exception = new WorldLoadException(message, row);

        // Should just return the base message without line number
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageWithRowOnly() {
        String message = "Row error";
        int row = 10;
        WorldLoadException exception = new WorldLoadException(message, row);

        String result = exception.getMessage();
        assertTrue(result.contains(message));
        assertTrue(result.contains("line 11"));
        assertFalse(result.contains("character"));
    }

    @Test
    public void testMessageWithRowZero() {
        String message = "First line error";
        WorldLoadException exception = new WorldLoadException(message, 0);

        String result = exception.getMessage();
        assertTrue(result.contains("line 1"));
    }

    @Test
    public void testMultipleExceptionsIndependence() {
        WorldLoadException exception1 = new WorldLoadException("Error 1", 5);
        WorldLoadException exception2 = new WorldLoadException("Error 2", 10);

        assertTrue(exception1.getMessage().contains("line 6"));
        assertTrue(exception2.getMessage().contains("line 11"));
    }

    @Test
    public void testExceptionWithEmptyMessage() {
        WorldLoadException exception = new WorldLoadException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    public void testExceptionWithSpecialCharactersInMessage() {
        String message = "Invalid character '@' found";
        WorldLoadException exception = new WorldLoadException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageFormatConsistency() {
        WorldLoadException exception1 = new WorldLoadException("Error", 5, 10);
        String message1 = exception1.getMessage();

        // Call again to ensure consistent formatting
        String message2 = exception1.getMessage();
        assertEquals(message1, message2);
    }

    @Test
    public void testRowIncrementByOne() {
        WorldLoadException exception1 = new WorldLoadException("Error", 0);
        WorldLoadException exception2 = new WorldLoadException("Error", 1);

        String msg1 = exception1.getMessage();
        String msg2 = exception2.getMessage();

        assertTrue(msg1.contains("line 1"));
        assertTrue(msg2.contains("line 2"));
    }

    @Test
    public void testColumnIncrementByOne() {
        WorldLoadException exception1 = new WorldLoadException("Error", 0, 0);
        WorldLoadException exception2 = new WorldLoadException("Error", 0, 1);

        String msg1 = exception1.getMessage();
        String msg2 = exception2.getMessage();

        assertTrue(msg1.contains("character 1"));
        assertTrue(msg2.contains("character 2"));
    }

    @Test
    public void testLongMessage() {
        String longMessage = "This is a very long error message that contains multiple words " +
                "and explains in detail what went wrong during world loading";
        WorldLoadException exception = new WorldLoadException(longMessage);

        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    public void testExceptionThrowable() {
        try {
            throw new WorldLoadException("Test error");
        } catch (WorldLoadException e) {
            assertEquals("Test error", e.getMessage());
        }
    }

    @Test
    public void testRowAndColumnBoundaries() {
        WorldLoadException exception1 = new WorldLoadException("Error", 0, 0);
        WorldLoadException exception2 = new WorldLoadException("Error", Integer.MAX_VALUE, Integer.MAX_VALUE);

        String msg1 = exception1.getMessage();
        String msg2 = exception2.getMessage();

        assertTrue(msg1.contains("line 1"));
        assertTrue(msg2.contains("line"));
        assertTrue(msg2.contains("character"));
    }
}
