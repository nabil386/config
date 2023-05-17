package curam.ca.gc.bdm.test.util.dataaccess;

import curam.util.dataaccess.DatabaseCallDescriptor;
import curam.util.type.NotFoundIndicator;
import mockit.Mock;
import mockit.MockUp;

public class DatabaseCallAccessor {

  private static Object result;

  public static void mockDatabaseCall() {

    new MockUp<DatabaseCallDescriptor>() {

      @Mock
      public Object execute(final NotFoundIndicator nf, final Object key) {

        result = key;
        return key;
      }
    };
  }

  public static Object getResult() {

    return result;
  }
}
