package Storage;

import java.text.ParseException;

public interface Storable<Object> {

    void addToStorage(Object obj) throws ParseException;
}
