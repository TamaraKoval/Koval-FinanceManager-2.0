package Storage;

import java.text.ParseException;

public interface Storable<Object> {

    public void addToStorage(Object obj) throws ParseException;
}
