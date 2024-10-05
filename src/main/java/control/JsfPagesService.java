package control;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class JsfPagesService implements Serializable {
    private static final long serialVersionUID = 1L;
    private String page = "/secure/master_pages/customer.xhtml";

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void changePage(String newPage) {
        this.page = newPage;
    }
}
