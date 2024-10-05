package boundary;

import control.StoreService;
import entity.Store;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class storeDetailsView implements Serializable {

    @Inject
    StoreService storeService;
    private Store store;

    /**
     * Initializes the component by setting the store instance variable to the store object retrieved from the store service
     * using the given store ID.
     *
     * @param storeId the ID of the store to retrieve from the store service
     */
    public void init(Integer storeId){
        this.store = storeService.getStoreById(storeId);
    }

    // Getter and Setter
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
