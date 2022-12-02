package pixelj.util;

import org.junit.jupiter.api.Test;

public class ChangeablesTests {

    @Test
    public void intGe() {
        final var intVal = new ChangeableInt(0);
        final var booleanVal = intVal.ge(0);
        assert(booleanVal.getValue());

        intVal.setValue(-1);
        assert(!booleanVal.getValue());

        intVal.setValue(0);
        assert(booleanVal.getValue());

        intVal.setValue(-10);
        assert(!booleanVal.getValue());

        intVal.setValue(10);
        assert(booleanVal.getValue());
    }

    @Test
    public void int2Ge() {
        final var intVal1 = new ChangeableInt(0);
        final var intVal2 = new ChangeableInt(0);
        final var booleanVal = intVal1.ge(intVal2);
        assert(booleanVal.getValue());

        intVal1.setValue(-1);
        assert(!booleanVal.getValue());

        intVal2.setValue(10);
        assert(!booleanVal.getValue());

        intVal1.setValue(-11);
        assert(!booleanVal.getValue());

        intVal1.setValue(10);
        assert(booleanVal.getValue());
    }
}
