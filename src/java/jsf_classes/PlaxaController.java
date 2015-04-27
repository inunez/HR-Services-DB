package jsf_classes;

import entities.Plaxa;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import session_beans.PlaxaJpaController;

@Named("plaxaController")
@SessionScoped
public class PlaxaController implements Serializable {

    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "HRServicesDBPU")
    private EntityManagerFactory emf = null;

    private Plaxa current;
    private DataModel items = null;
    private PlaxaJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PlaxaController() {
    }

    public Plaxa getSelected() {
        if (current == null) {
            current = new Plaxa();
            current.setPlaxaPK(new entities.PlaxaPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private PlaxaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new PlaxaJpaController(utx, emf);
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getPlaxaCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findPlaxaEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Plaxa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Plaxa();
        current.setPlaxaPK(new entities.PlaxaPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getPlaxaPK().setIdNumber(current.getEmployee().getEmployeePK().getIdNumber());
            current.getPlaxaPK().setStatus(current.getEmployee().getEmployeePK().getStatus());
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("PlaxaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Plaxa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getPlaxaPK().setIdNumber(current.getEmployee().getEmployeePK().getIdNumber());
            current.getPlaxaPK().setStatus(current.getEmployee().getEmployeePK().getStatus());
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("PlaxaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Plaxa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getPlaxaPK());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("PlaxaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getPlaxaCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findPlaxaEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findPlaxaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findPlaxaEntities(), true);
    }

    @FacesConverter(forClass = Plaxa.class)
    public static class PlaxaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlaxaController controller = (PlaxaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "plaxaController");
            return controller.getJpaController().findPlaxa(getKey(value));
        }

        entities.PlaxaPK getKey(String value) {
            entities.PlaxaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.PlaxaPK();
            key.setIdNumber(values[0]);
            key.setStatus(values[1]);
            return key;
        }

        String getStringKey(entities.PlaxaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getStatus());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Plaxa) {
                Plaxa o = (Plaxa) object;
                return getStringKey(o.getPlaxaPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Plaxa.class.getName());
            }
        }

    }

}
