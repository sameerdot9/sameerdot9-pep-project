package Service;

import java.sql.SQLException;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    public Account addAccount(Account account) throws SQLException{
        return accountDAO.insertAccount(account);
    }
    
    public Account loginAccount(Account account) throws SQLException{
        return accountDAO.logAccount(account);
    }
}
