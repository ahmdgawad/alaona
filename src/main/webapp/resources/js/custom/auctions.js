/***** GLOBAL VARIABLES *****/
var total_pages = 10;
var category = "";
var search_pages = 10;
var type;
$(document).ready(function(){

    //console.log("base url: " + baseURL);
    //console.log("window.location.href: " + window.location.href)

    type = "active";
    var username = getUser();
    if(username == null || username == ""){
        console.log("guest entered");
    } else {
        getAuctionsRecommendations(username);
    }

    total_pages = getNumOfAuctions(type);

    getCategories(type);


});

function getAuctionsRecommendations(username){

    var url = window.location.protocol+ "//" + window.location.hostname + ":" +window.location.port + "/user/";
    var destination = url + username + "/recommendations";
    //alert("destination: " + destination);
    if(username != ""){
        //console.log("getting unread messages");
        $.ajax({
            type : "GET",
            dataType:'json',
            data: {username:username},
            url  : destination,
            success:function(data){

                if(data == "problem"){
                    $("#rec-list").css("display","none");
                    $("#no-recommendations").css("display","block");
                } else {
                    for(var i=0; i<data.length; i++){
                        $("#rec-list").append('<li><a href="' + window.location.href + '/item/'+data[i].itemID + '">'+ data[i].name +'</a></li>');
                    }

                }

            }


        });
    }

}




function initListeners(){

    /*** Initializing Listeners after loading the contents of the page ***/
    //alert("Listeners Init");

    $(".categories-search-list").select2({ width: '100%' });

    $("#categories-module").on('click', 'li', function(e) {
        e.preventDefault();

        category = $(this).find('a').attr("href");
        if(category == "all-categories"){
            window.location = window.location.href;
        } else {
            $('#auctions-paginator').css("display","none");
            $('#advSearch-paginator').css("display","none");
            $('#auctions-ByCategory-paginator').css("display","block");
            $('#category-indicator').css("display","block");
            $('#category-indicator h4').empty();
            $('#category-indicator h4').append("Results for Category: " + decodeURIComponent(category));
            // console.log(decodeURIComponent(category))
            var cat_number = $(this).find('span').text()
            // console.log("num of cat: " + cat_number)
            var end=10;
            var quotient = Math.floor(cat_number/10);
            var remainder = cat_number/10;
            var limit=quotient;
            if(remainder > 0){
                limit=quotient+1;;
            }
            if(limit == 0){
                limit=1;
            }
            //var type="active";
            //console.log("limit: " + limit);
            $('#auctions-byCategory-paginator').bootpag({
                total: limit,
                maxVisible: 5
            }).on("page", function(event, num){

                //console.log("category paging event!!!!!")
                var start = (num-1)*end;

                // ... after content load -> change total to 10
                $(this).bootpag({total: limit, maxVisible: 5});
                $('html, body').animate({scrollTop : 0},800);
                getTemplateModule(start,end,decodeURIComponent(category),type);

            });
            getTemplateModule(0,10,decodeURIComponent(category),type);
        }

    });


    $("#accordion").on('show.bs.collapse', function (e) {

        $("#category-indicator").css("display","none");
        $('#auctions-byCategory-paginator').css("display","none");
        $('#auctions-paginator').css("display","none");

    });

    $("#search-button").click(function(event){


        $('#advSearch-paginator').css("display","block");

        var search_data = {};
        var keywords = $("#keywords").val();
        var description = $("#search-desc").val();

        var country = $("#country-search").val();
        var minBid = $("#min-bid-price").val();
        var maxBid = $("#max-bid-price").val();

        search_data["keywords"] = $("#keywords").val();
        search_data["description"] = $("#search-desc").val();
        if($(".categories-search-list").val() == null){
            search_data["categories"] = [];
        } else {
            var categories = [];
            search_data["categories"] = $(".categories-search-list").val();
        }
        //console.log("categories adv SEARCH:::::");
        //console.log(search_data["categories"]);

        search_data["country"] = $("#country-search").val();
        search_data["minBid"] = $("#min-bid-price").val();
        search_data["maxBid"] = $("#max-bid-price").val();
        $("#available-auctions").empty();
        advSearchListeners(search_data);



    });

    $("#reset-button").click(function(e){
        e.preventDefault();
        $("#keywords").val("");
        $("#search-desc").val("");
        var $select_cat = $(".categories-search-list").select2();
        $select_cat.val(null).trigger("change");
        $("#country-search").val("");
        $("#min-bid-price").val("");
        $("#max-bid-price").val("");
    });

    $('input:radio').change(function(){
        $(this).prop('checked', true);
        var id = $(this).attr('id');
        if($("#category-indicator").is(":visible")){
            $("#category-indicator").css("display","none");
        }
        if(id=="all-auctions"){
            //console.log("fetch all auctions");
            type="all";
            total_pages = getNumOfAuctions(type);

            getCategories(type);
        } else {
            type = "active";
            //console.log("fetch active auctions");
            //console.log("type: " + type)
            total_pages = getNumOfAuctions(type);

            getCategories(type);
        }
    });
    //alert("Listeners Init end");

}

function advSearchListeners(search_data){

    getTemplateBySearch(0,10,search_data);
}

function getTemplateBySearch(start,end,search_data){
    $.get( window.location.href + "/template-module", function( template_module ) {

        advanced_search(start,end,search_data,template_module);
    });
}

function advanced_search(start,end,input,template_module) {
    var search_data = JSON.stringify(input);
    //console.log(search_data);
    $.ajax({
        type : "POST",
        dataType:'json',
        url  :window.location.href + "/advanced-search",
        data :{start:start,end:end,search_data:search_data},
        success : function(data) {
            //console.log("ADVANCED SEARCH SUCCESS");
            //$("#search_data").empty();

            if(data.length == 0){
                $('#advSearch-paginator').css("display","none");
                alert("No data found");

            }else {
                console.log(data)
                /* some login for the pagination */
                var resp_size = data.length;
                //console.log("advanced search size: " + resp_size);
                var end=10;
                var quotient = Math.floor(resp_size/10);
                var remainder = resp_size/10;
                var limit=quotient;
                if(remainder > 0){
                    limit=quotient+1;;
                }
                if(limit == 0){
                    limit=1;
                }

                var amount = data[0].size;
                // console.log("amount: " + amount)
                search_pages=limit;
                //console.log("limit: " + limit);
                $('#advSearch-paginator').bootpag({
                    total: amount,
                    maxVisible: 5
                }).on("page", function(event, num){

                    var start = (num-1)*end;

                    // ... after content load -> change total to 10
                    $(this).bootpag({total: limit,maxVisible: 5});
                    $('html, body').animate({scrollTop : 0},800);
                    getTemplateBySearch(start,end,search_data);

                });



                for(var i=0;i<data.length;i++){
                    var panel = $("<div id=\"searchresults\">" + template_module +"</div>");
                    panel.find('.item-listing-seller label').text(data[i].seller);

                    panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+data[i].itemID);
                    panel.find('.item-listing-title a').text(data[i].name);

                    panel.find("#elapseTime h4").text(data[i].expires+"remaining");
                    //panel.find("#category-listing h4").text(data[i].expires+"remaining");
                    panel.find("#firstBid").text("$"+parseFloat(data[i].firstBid).toFixed(2));
                    panel.find("#numberOfbids").text(data[i].numberOfBids + "     " + "Bids");
                    panel.find('.item-details a').attr('href',window.location.href + '/item/'+data[i].itemID);
                    html = panel.html();

                    $("#available-auctions").append(html);

                }

            }
        }
    });
}

function getNumOfAuctions(type){
    /*** Ajax call for retrieving the number of auction
     * in order to create the pagination ***/

    //console.log("getting number of auctions ");
    $.ajax({
        type : "GET",
        dataType:'json',
        data: {type:type},
        //url  : baseURL + "/auctions/numberOfAuctions",
        url  : window.location.href + "/numberOfAuctions",
        success : function(data){
            total_pages = data.auctionsNum;
            initPaging(total_pages,type);
        }

    });

}



function initPaging(total_pages,type){

    /**
     *  Making the pagination using bootpag library
     */
        //console.log("initPaging")
        //console.log("total_pages: " + total_pages/10)

    var end=10;

    $('#auctions-paginator').bootpag({
        total: Math.round(total_pages/10),
        maxVisible: 5
    }).on("page", function(event, num){

        var start = (num-1)*end;
        // ... after content load -> change total to 10
        $(this).bootpag({total: Math.round(total_pages/10), maxVisible: 5});

        $('html, body').animate({scrollTop : 0},800);

        getTemplateModule(start,end,category,type);


    });
    getTemplateModule(0,end,category,type);
    initListeners();



}




function getTemplateModule(start,end,category,type){

    /**
     * Ajax call for retrieving the main module for the page.
     * This is done because the auctions are presented in bootstrap panels.
     * So we need to call the same div content several times
     */

    //console.log("getting template module ");

    $.get( window.location.href + "/template-module", function( template_module ) {
        if(category == ""){
            // getting auctions for all the categories
            getAuctions(start,end,template_module,type);
        } else {
            // getting auctions by category
            getAuctionsByCategory(start,end,template_module,category,type)
        }

    });

}

function getAuctionsByCategory(start,end,template_module,category,type){
    //console.log("Auctions by Category with category: " + category);
    $.ajax({
        type : "GET",
        dataType:'json',
        url  : window.location.href + "/view-auctions-byCategory",
        data :{start:start,size:end,category:category,type:type},
        success : function(auctions) {
            $("#available-auctions").empty();
            console.log("moving on formatting the page")
            if(auctions.length == 0){
                $('#no_auctions').css("display","block");
            }else {
                $('#no_auctions').css("display","none");
                // here for every auction we want to create different panels
                // hence every auction is stored in a panel
                for(var i=0;i<auctions.length;i++){
                    var panel = $("<div id=\"item-lists-module\">" + template_module + "</div>");
                    panel.find('.item-listing-seller label').text(auctions[i].seller);
                    panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id);
                    panel.find('.item-listing-title a').text(auctions[i].name);

                    panel.find("#elapseTime h4").text(auctions[i].expires+"remaining");
                    panel.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
                    panel.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
                    panel.find('.item-details a').attr('href',window.location.href + '/item/'+auctions[i].id);
                    html = panel.html();
                    $("#available-auctions").append(html);

                }
                checkForUser();
                getUnreadMessages();
            }
        }
    });
}


function getAuctions(start,end,template_module,type){
    //console.log("getting auctions");
    $.ajax({
        type : "GET",
        dataType:'json',
        //url  : baseURL + "/auctions/view-auctions/",
        url  :window.location.href + "/view-auctions",
        data :{start:start,size:end,type:type},
        success : function(auctions) {
            $("#available-auctions").empty();
            //console.log("moving on formatting the page")
            if(auctions.length == 0){
                $('#no_auctions').css("display","block");
            }else {
                $('#no_auctions').css("display","none");

                for(var i=0;i<auctions.length;i++){
                    var panel = $("<div id=\"item-lists-module\">" + template_module + "</div>");
                    panel.find('.item-listing-seller label').text(auctions[i].seller);
                    panel.find('.item-listing-title a').attr('href',window.location.href + '/item/'+auctions[i].id);
                    panel.find('.item-listing-title a').text(auctions[i].name);

                    panel.find("#elapseTime h4").text(auctions[i].expires+"remaining");
                    panel.find("#firstBid").text("$"+parseFloat(auctions[i].firstBid).toFixed(2));
                    panel.find("#numberOfbids").text(auctions[i].numberOfBids + "     " + "Bids");
                    panel.find('.item-details a').attr('href',window.location.href + '/item/'+auctions[i].id);
                    html = panel.html();
                    $("#available-auctions").append(html);

                }
                checkForUser();
                getUnreadMessages();
            }
        }
    });
    //console.log("Leaving Get Auctions...");
}


function getCategories(type){

    /* Make ajax call to receive the categories from the db */
    //console.log("getting the categories for type: " + type);
    $.ajax({
        type : "GET",
        dataType:'json',
        data : {type:type},
        url  : window.location.href + "/categories",
        success : function(data) {

            if(data.length == 0){
                var html =  "<li><a>No available categories</a></li>";
                $("#categories-modules").append(html);
            } else {
                var categories = [];
                $("#categories-module").empty();
                var allCategoriesHTML = "<li><a class=\"all-categories\" href=\"all-categories\" " +
                    "style=\"vertical-align: bottom;\">All Categories<span class='label label-warning pull-right'>-</span></a></li>";

                $("#categories-module").append(allCategoriesHTML);
                for(var i = 0; i < data.length; i++) {
                    var html =  "<li style=\"word-break:break-all;\">" +
                        "<a href="+ encodeURIComponent(data[i].category) + " style=\"white-space:normal;\">" +
                        data[i].category +
                        "<span class='label label-warning pull-right'>"+
                        data[i].numOfItems+
                        " </span>" +
                        "</a>" +
                        "</li>";
                    $("#categories-module").append(html);

                    var category = {};
                    category["id"] = data[i].category;
                    category["text"] = data[i].category;
                    categories.push(category)

                }
                $(".categories-search-list").select2({
                    width: '100%',
                    data: categories

                });
            }

        }
    });

}

