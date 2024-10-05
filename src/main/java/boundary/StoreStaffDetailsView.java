package boundary;

import control.StaffService;
import entity.Staff;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class StoreStaffDetailsView implements Serializable {

    @Inject
    StaffService staffService;
    private Staff staff;

    /**
     * Initializes the component by setting the staff instance variable to the staff object retrieved from the staff service
     * using the given staff ID.
     *
     * @param staffId the ID of the staff to retrieve from the staff service
     */
    public void init(Integer staffId){
        this.staff = staffService.findStaffById(staffId);
    }

    // Getter and Setter
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
