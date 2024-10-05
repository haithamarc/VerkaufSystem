document.getElementById("page").addEventListener("click", function(){
    const headerBlockMenu = document.getElementById("header-c-block-menu");
    if(!headerBlockMenu.classList.contains("u-hide")){
                headerBlockMenu.classList.remove("u-fadein");
        headerBlockMenu.classList.add("u-fadeout");
        
        setTimeout(() => {
            headerBlockMenu.classList.add("u-hide");
        }, 200);
    }
});

const currentUserAvatar = document.getElementById("current-user-avatar");
currentUserAvatar.addEventListener("click", function(e){
    e.stopPropagation();
    const headerBlockMenu = document.getElementById("header-c-block-menu");
    if(headerBlockMenu.classList.contains("u-hide")){
        headerBlockMenu.classList.add("u-fadein");
        headerBlockMenu.classList.remove("u-hide");
        headerBlockMenu.classList.remove("u-fadeout");
    }else {
        headerBlockMenu.classList.remove("u-fadein");
        headerBlockMenu.classList.add("u-fadeout");
        
        setTimeout(() => {
            headerBlockMenu.classList.add("u-hide");
        }, 200);
    }
});

/* filterInputField */

/*const filterInputField = document.getElementById("j_idt25:inputField");
filterInputField.addEventListener("keyup", function(e){
   const filterButton = document.getElementById("j_idt25:filterButton");
    if(filterInputField.value === ""){
        filterButton.classList.add("c-button--bg-black-disabled");
        filterButton.classList.remove("u-cursor--pointer");
    }else {
        filterButton.classList.remove("c-button--bg-black-disabled");
        filterButton.classList.add("c-button--bg-black");
        filterButton.classList.add("u-cursor--pointer");
    }
});*/

/*  NOTIFICATION ICON  */

const notificationIcon = document.getElementById("notification-icon");
notificationIcon.addEventListener("click", function(e){
    e.stopPropagation();
    const notificationComponent = document.getElementById("c-notification");
    const pageCoverComponent = document.getElementById("c-page-cover");
    if( notificationComponent.classList.contains("u-hide")){
        notificationComponent.classList.add("u-fadein-right");
        notificationComponent.classList.remove("u-hide");
        notificationComponent.classList.remove("u-fadeout-right");
        pageCoverComponent.classList.remove("u-fadeout--page-cover");
        setTimeout(() => {
            pageCoverComponent.classList.add("u-fadein--page-cover");
            pageCoverComponent.classList.remove("u-hide");
        }, 50);
    }else {
        notificationComponent.classList.remove("u-fadein-right");
        notificationComponent.classList.add("u-fadeout-right");
        pageCoverComponent.classList.remove("u-fadein--page-cover");
        pageCoverComponent.classList.add("u-fadeout--page-cover");
        
        setTimeout(() => {
            notificationComponent.classList.add("u-hide");
            pageCoverComponent.classList.add("u-hide");
        }, 300);
    }
});

/*  NOTIFICATION CLOSE BUTTON  */

/*const notificationCloseButton = document.getElementById("notification-close-button");
notificationCloseButton.addEventListener("click", function(e){
    e.stopPropagation();
    const notificationComponent = document.getElementById("c-notification");
    const pageCoverComponent = document.getElementById("c-page-cover");
    notificationComponent.classList.remove("u-fadein-right");
    notificationComponent.classList.add("u-fadeout-right");
    pageCoverComponent.classList.remove("u-fadein--page-cover");
    pageCoverComponent.classList.add("u-fadeout--page-cover");
    
    setTimeout(() => {
        notificationComponent.classList.add("u-hide");
        pageCoverComponent.classList.add("u-hide");
    }, 500);
});*/

/*  PAGECOVERCOMPONENT  */

/*
const pageCoverComponent = document.getElementById("c-page-cover");
pageCoverComponent.addEventListener("click", function(){
    const notificationComponent = document.getElementById("c-notification");
    const pageCoverComponent = document.getElementById("c-page-cover");
    if(!notificationComponent.classList.contains("u-hide")){
        notificationComponent.classList.remove("u-fadein-right");
        notificationComponent.classList.add("u-fadeout-right");
        pageCoverComponent.classList.remove("u-fadein--page-cover");
        pageCoverComponent.classList.add("u-fadeout--page-cover");
        
        setTimeout(() => {
            notificationComponent.classList.add("u-hide");
            pageCoverComponent.classList.add("u-hide");
        }, 500);
    }

    const popupComponent = document.getElementById("c-popup");

    if(!popupComponent.classList.contains("u-hide")){
        const popupComponent = document.getElementById("c-popup");
        const pageCoverComponent = document.getElementById("c-page-cover");
    
        popupComponent.classList.remove("u-fadein");
        popupComponent.classList.add("u-fadeout");
        pageCoverComponent.classList.remove("u-fadein--page-cover");
        pageCoverComponent.classList.add("u-fadeout--page-cover");
    
        setTimeout(() => {
            popupComponent.classList.add("u-hide");
            pageCoverComponent.classList.add("u-hide");
        }, 100);
    }

    const formComponent = document.getElementById("c-form");
    if(!formComponent.classList.contains("u-hide")){
        const formComponent = document.getElementById("c-form");
        const pageCoverComponent = document.getElementById("c-page-cover");
    
        formComponent.classList.remove("u-fadein");
        formComponent.classList.add("u-fadeout");
        pageCoverComponent.classList.remove("u-fadein--page-cover");
        pageCoverComponent.classList.add("u-fadeout--page-cover");
    
        setTimeout(() => {
            formComponent.classList.add("u-hide");
            pageCoverComponent.classList.add("u-hide");
        }, 100);
    }

    const formComponentSecond = document.getElementById("c-form-second");
    if(!formComponentSecond.classList.contains("u-hide")){
        const formComponentSecond = document.getElementById("c-form-second");
        const pageCoverComponent = document.getElementById("c-page-cover");
    
        formComponentSecond.classList.remove("u-fadein");
        formComponentSecond.classList.add("u-fadeout");
        pageCoverComponent.classList.remove("u-fadein--page-cover");
        pageCoverComponent.classList.add("u-fadeout--page-cover");
    
        setTimeout(() => {
            formComponentSecond.classList.add("u-hide");
            pageCoverComponent.classList.add("u-hide");
        }, 100);
    }

}); */


/* DELETE BUTTON */

/*
const deleteButton = document.getElementById("delete-button");
deleteButton.addEventListener("click", function(e){
    e.stopPropagation();
    const popupComponent = document.getElementById("c-popup");
    const pageCoverComponent = document.getElementById("c-page-cover");
    if(popupComponent.classList.contains("u-hide")){
        popupComponent.classList.add("u-fadein");
        popupComponent.classList.remove("u-hide");
        popupComponent.classList.remove("u-fadeout");
        pageCoverComponent.classList.remove("u-fadeout--page-cover");
        pageCoverComponent.classList.add("u-fadein--page-cover");
        pageCoverComponent.classList.remove("u-hide");
    }
}); */


/* C-POPUP-SKIP */

/*

const popipSkip = document.getElementById("c-popup-skip");
popipSkip.addEventListener("click", function(e){
    e.stopPropagation();
    const popupComponent = document.getElementById("c-popup");
    const pageCoverComponent = document.getElementById("c-page-cover");

    popupComponent.classList.remove("u-fadein");
    popupComponent.classList.add("u-fadeout");
    pageCoverComponent.classList.remove("u-fadein--page-cover");
    pageCoverComponent.classList.add("u-fadeout--page-cover");

    setTimeout(() => {
        popupComponent.classList.add("u-hide");
        pageCoverComponent.classList.add("u-hide");
    }, 100);
});

 */


/*  C-FORM */

/*

const editButton = document.getElementById("edit-button");
editButton.addEventListener("click", function(e){
    e.stopPropagation();
    const formComponent = document.getElementById("c-form");
    const pageCoverComponent = document.getElementById("c-page-cover");
    if(formComponent.classList.contains("u-hide")){
        formComponent.classList.add("u-fadein");
        formComponent.classList.remove("u-hide");
        formComponent.classList.remove("u-fadeout");
        pageCoverComponent.classList.remove("u-fadeout--page-cover");
        pageCoverComponent.classList.add("u-fadein--page-cover");
        pageCoverComponent.classList.remove("u-hide");
    }
});
*/

/* C-FORM CLOSE BUTTON */

/*

const formCloseButton = document.getElementById("form-close-button");
formCloseButton.addEventListener("click", function(e){
    e.stopPropagation();
    const formComponent = document.getElementById("c-form");
    const pageCoverComponent = document.getElementById("c-page-cover");

    formComponent.classList.remove("u-fadein");
    formComponent.classList.add("u-fadeout");
    pageCoverComponent.classList.remove("u-fadein--page-cover");
    pageCoverComponent.classList.add("u-fadeout--page-cover");

    setTimeout(() => {
        formComponent.classList.add("u-hide");
        pageCoverComponent.classList.add("u-hide");
    }, 100);
});
*/

/*  C-FORM-SECOND */

const constnewCustomerButton = document.getElementById("new-customer-button");
constnewCustomerButton.addEventListener("click", function(e){
    console.log("click");
    e.stopPropagation();
    const formComponentSecond = document.getElementById("c-form-second");
    const pageCoverComponent = document.getElementById("c-page-cover");
    if(formComponentSecond.classList.contains("u-hide")){
        formComponentSecond.classList.add("u-fadein");
        formComponentSecond.classList.remove("u-hide");
        formComponentSecond.classList.remove("u-fadeout");
        pageCoverComponent.classList.remove("u-fadeout--page-cover");
        pageCoverComponent.classList.add("u-fadein--page-cover");
        pageCoverComponent.classList.remove("u-hide");
    }
});


/* C-FORM CLOSE BUTTON SECOND

const formCloseButtonSecond = document.getElementById("form-close-button-second");
formCloseButtonSecond.addEventListener("click", function(e){
    e.stopPropagation();
    const formComponentSecond = document.getElementById("c-form-second");
    const pageCoverComponent = document.getElementById("c-page-cover");

    formComponentSecond.classList.remove("u-fadein");
    formComponentSecond.classList.add("u-fadeout");
    pageCoverComponent.classList.remove("u-fadein--page-cover");
    pageCoverComponent.classList.add("u-fadeout--page-cover");

    setTimeout(() => {
        formComponentSecond.classList.add("u-hide");
        pageCoverComponent.classList.add("u-hide");
    }, 100);
});

 */



/* C-PAGINATION NEXT BUTTON  */

const paginationNextButton = document.getElementById("j_idt25:pagination-next-button");
paginationNextButton.addEventListener("click", function(e){
    e.stopPropagation();

    /* paginationProgressionContent */
    
    const paginationProgressionContent = document.getElementById("pagination-progression-content");
    const paginationProgressionContentwidth = paginationProgressionContent.offsetWidth;
    paginationProgressionContent.style.width = paginationProgressionContentwidth + 25 + "px";

    /* paginationProgressIcon */

    const paginationProgressIcon = document.getElementById("pagination-progression-icon");
    var paginationProgressIconleft = paginationProgressIcon.offsetLeft;
    paginationProgressIcon.style.left = paginationProgressIconleft + 25 + "px";
    paginationProgressIconleft = paginationProgressIcon.offsetLeft;

    /* Start-button

    const paginationStartButton = document.getElementById("j_idt25:pagination-start-button");
    paginationStartButton.classList.remove("c-button--bg-red-disabled");
    paginationStartButton.classList.add("c-button--bg-red");*/

    /* prev-button
    const paginationPrevButton = document.getElementById("j_idt25:pagination-prev-button");
    paginationPrevButton.classList.remove("c-button--bg-red-disabled");
    paginationPrevButton.classList.add("c-button--bg-red");*/


    /* Currentpage

    const paginationCurentPage = document.getElementsByClassName("c-pagination__current-page");
    const currentPage = paginationCurentPage[0].getAttribute("value");

    var currentPageHtmlEl = paginationCurentPage[0];

    currentPageHtmlEl.classList.remove("c-pagination__current-page--bgc-red");
    currentPageHtmlEl.classList.remove("c-pagination__current-page--bold");
    currentPageHtmlEl.classList.remove("c-pagination__current-page");
    currentPageHtmlEl.classList.remove("c-pagination__current-page--color-white");

    currentPageHtmlEl.classList.add("c-pagination__link--color-black");
    currentPageHtmlEl.classList.add("o-inline-block");
    currentPageHtmlEl.classList.add("c-pagination__link");*/



    /*  LINK

    const paginationLinks = document.getElementsByClassName("c-pagination__link");

    console.log(currentPage);

    console.log(paginationLinks[currentPage]);

    var newCurrentPage = paginationLinks[currentPage];

    newCurrentPage.classList.remove("c-pagination__link--color-black");
    newCurrentPage.classList.remove("o-inline-block");
    newCurrentPage.classList.remove("c-pagination__link");

    newCurrentPage.classList.add("c-pagination__current-page--bgc-red");
    newCurrentPage.classList.add("c-pagination__current-page--bold");
    newCurrentPage.classList.add("c-pagination__current-page");
    newCurrentPage.classList.add("c-pagination__current-page--color-white");*/

});



/* C-PAGINATION Prev BUTTON */

const paginationPrevButton = document.getElementById("j_idt25:pagination-prev-button");
paginationNextButton.addEventListener("click", function(e){
    e.stopPropagation();

    /* paginationProgressionContent */
    
    const paginationProgressionContent = document.getElementById("pagination-progression-content");
    const paginationProgressionContentwidth = paginationProgressionContent.offsetWidth;
    paginationProgressionContent.style.width = paginationProgressionContentwidth + 25 + "px";

    /* paginationProgressIcon */

    const paginationProgressIcon = document.getElementById("pagination-progression-icon");
    var paginationProgressIconleft = paginationProgressIcon.offsetLeft;
    paginationProgressIcon.style.left = paginationProgressIconleft + 25 + "px";
    paginationProgressIconleft = paginationProgressIcon.offsetLeft;

    /* Start-button

    const paginationStartButton = document.getElementById("pagination-start-button");
    paginationStartButton.classList.remove("c-button--bg-red-disabled");
    paginationStartButton.classList.add("c-button--bg-red");*/

    /* prev-button
    const paginationPrevButton = document.getElementById("pagination-prev-button");
    paginationPrevButton.classList.remove("c-button--bg-red-disabled");
    paginationPrevButton.classList.add("c-button--bg-red"); */


    /* Currentpage

    const paginationCurentPage = document.getElementsByClassName("c-pagination__current-page");
    const currentPage = paginationCurentPage[0].getAttribute("value");

    var currentPageHtmlEl = paginationCurentPage[0];

    currentPageHtmlEl.classList.remove("c-pagination__current-page--bgc-red");
    currentPageHtmlEl.classList.remove("c-pagination__current-page--bold");
    currentPageHtmlEl.classList.remove("c-pagination__current-page");
    currentPageHtmlEl.classList.remove("c-pagination__current-page--color-white");

    currentPageHtmlEl.classList.add("c-pagination__link--color-black");
    currentPageHtmlEl.classList.add("o-inline-block");
    currentPageHtmlEl.classList.add("c-pagination__link");*/
    


    /*  LINK

    const paginationLinks = document.getElementsByClassName("c-pagination__link");

    console.log(currentPage);

    console.log(paginationLinks[currentPage]);

    var newCurrentPage = paginationLinks[currentPage];

    newCurrentPage.classList.remove("c-pagination__link--color-black");
    newCurrentPage.classList.remove("o-inline-block");
    newCurrentPage.classList.remove("c-pagination__link");

    newCurrentPage.classList.add("c-pagination__current-page--bgc-red");
    newCurrentPage.classList.add("c-pagination__current-page--bold");
    newCurrentPage.classList.add("c-pagination__current-page");
    newCurrentPage.classList.add("c-pagination__current-page--color-white");*/

});