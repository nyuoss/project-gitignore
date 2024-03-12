import static org.junit.Assert.assertEquals;

import com.example.utils.ArithmeticComponent;
import org.junit.Test;

public class BasicTest {
    @Test
    public void testAddition() {
        assertEquals("2 + 2 should equal 4", 4, ArithmeticComponent.add(2, 2));
    }
}
