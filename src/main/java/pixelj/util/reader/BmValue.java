package pixelj.util.reader;

import org.eclipse.collections.api.list.primitive.IntList;

public sealed interface BmValue permits BmNumber, BmString, BmVector {

    int getInt();

    IntList getVector();

    String getString();
}
