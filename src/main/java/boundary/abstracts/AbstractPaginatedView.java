package boundary.abstracts;

import java.util.ArrayList;
import java.util.List;

// An abstract class for a paginated view
public abstract class AbstractPaginatedView {
    // Current page number, starting at 1
    protected Integer currentPage = 1;
    // Number of items to display per page
    protected Integer itemsPerPage = 10;

    //getters && setters


    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    // Method that returns the total number of pages
    public abstract Integer getTotalPageCount();

    // Return a list of page numbers for pagination display
    public List<Integer> getPageNumbers() {
        Integer totalPageCount = getTotalPageCount();
        int from = currentPage - 5 <= 0 ? 1 : currentPage - 5;
        int to = from + 10 > totalPageCount ? totalPageCount : from + 10;
        List<Integer> result = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            result.add(i);
        }
        return result;
    }

    // Go to the next page
    public void nextPage() {
        Integer totalPageCount = getTotalPageCount();
        System.out.println("TotalPageCount: "+totalPageCount);
        if ((currentPage + 1) <= totalPageCount) {
            currentPage +=1 ;
        }
    }

    // Go to the previous page
    public void prevPage() {
        if ((currentPage - 1) >= 1) {
            currentPage--;
        }
    }

    // Go to the first page
    public void firstPage() {
        currentPage = 1;
    }

    // Go to the last page
    public void lastPage() {
        currentPage = getTotalPageCount();
    }
}
