package controllers;

import entities.UniformDeaneLocation;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;

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
import session_beans.UniformDeaneLocationJpaController;

@Named("uniformDeaneLocationController")
@SessionScoped
public class UniformDeaneLocationController implements Serializable {

    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "HRServicesDBPU")
    private EntityManagerFactory emf = null;

    private UniformDeaneLocation current;
    private DataModel items = null;
    private UniformDeaneLocationJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public UniformDeaneLocationController() {
    }

    public UniformDeaneLocation getSelected() {
        if (current == null) {
            current = new UniformDeaneLocation();
            current.setUniformDeaneLocationPK(new entities.UniformDeaneLocationPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private UniformDeaneLocationJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new UniformDeaneLocationJpaController(utx, emf);
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getUniformDeaneLocationCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findUniformDeaneLocationEntities(getPageSize(), getPageFirstItem()));
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
        current = (UniformDeaneLocation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new UniformDeaneLocation();
        current.setUniformDeaneLocationPK(new entities.UniformDeaneLocationPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("UniformDeaneLocationCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (UniformDeaneLocation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("UniformDeaneLocationUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (UniformDeaneLocation) getItems().getRowData();
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
            getJpaController().destroy(current.getUniformDeaneLocationPK());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle3").getString("UniformDeaneLocationDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle3").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getUniformDeaneLocationCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findUniformDeaneLocationEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findUniformDeaneLocationEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findUniformDeaneLocationEntities(), true);
    }

    @FacesConverter(forClass = UniformDeaneLocation.class)
    public static class UniformDeaneLocationControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UniformDeaneLocationController controller = (UniformDeaneLocationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "uniformDeaneLocationController");
            return controller.getJpaController().findUniformDeaneLocation(getKey(value));
        }

        entities.UniformDeaneLocationPK getKey(String value) {
            entities.UniformDeaneLocationPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.UniformDeaneLocationPK();
            key.setRegion(Integer.parseInt(values[0]));
            key.setShipTo(Short.parseShort(values[1]));
            return key;
        }

        String getStringKey(entities.UniformDeaneLocationPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getRegion());
            sb.append(SEPARATOR);
            sb.append(value.getShipTo());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UniformDeaneLocation) {
                UniformDeaneLocation o = (UniformDeaneLocation) object;
                return getStringKey(o.getUniformDeaneLocationPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UniformDeaneLocation.class.getName());
            }
        }

    }

}
