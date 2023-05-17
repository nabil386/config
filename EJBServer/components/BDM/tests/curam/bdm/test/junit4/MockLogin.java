package curam.bdm.test.junit4;

import curam.util.internal.BizTransaction;
import curam.util.security.CuramPrincipal;
import curam.util.security.RMIAuthentication;
import curam.util.transaction.TransactionInfo;
import curam.util.transaction.TransactionInfo.TransactionType;
import java.rmi.RemoteException;
import java.sql.Connection;

public final class MockLogin extends RMIAuthentication {

  private static MockLogin auth;

  public static MockLogin getInst() throws RemoteException {

    if (null == auth) {
      auth = new MockLogin();
    }
    return auth;
  }

  private MockLogin() throws RemoteException {

    super();
  }

  public void login(final String username) {

    setPrincipal(new CuramPrincipal(username));
    final Connection c = TransactionInfo.getInfo().getInfoConnection();
    TransactionInfo.setTransactionInfo(TransactionType.kOnline,
      new BizTransaction() {

        @Override
        public String getName() {

          return "Unit Test";
        }

        @Override
        public boolean transactional() {

          return true;
        }
      }, null, "en");
    TransactionInfo.getInfo().setInfoConnection(c);
  }
}
