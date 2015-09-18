package controllers;

import entities.Account;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import session_beans.AccountFacadeApplication;

@FacesConverter("accountConverter")
public class AccountConverter implements Converter{
    @Inject
    private AccountFacadeApplication query;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                List<Account> accounts = query.findAll();
                for (Account account : accounts) {
                    if(account.getAccountDescription().toLowerCase().contains(value.toLowerCase())){
                        return account;
                    }
                }
                return null;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid account."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Account) {
            Account p = (Account) value;
            return p.getAccountDescription();
        }
        return "";
    }
}
