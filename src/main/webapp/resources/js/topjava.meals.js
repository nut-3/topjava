const mealAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
        ajaxUrl: mealAjaxUrl,
        updateTable: function () {
            let queryString = $("#filter").serialize();
            if (queryString === "startDate=&endDate=&startTime=&endTime=") {
                updateTable();
            } else {
                $.ajax({
                    type: "GET",
                    url: ctx.ajaxUrl + "filter",
                    data: queryString
                }).done(
                    function (data) {
                        redrawTable(data);
                    });
            }
        }
    }
;

$(function () {
    //https://stackoverflow.com/a/18317523/2791349
    let userLang = navigator.language || navigator.userLanguage;
    userLang = userLang.split('-')[0];
    $.datetimepicker.setLocale(userLang);

    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            //https://stackoverflow.com/questions/14596270/jquery-datatable-add-id-to-tr-element-after-row-is-dynamically-added#14596338
            "fnCreatedRow": function (nRow, aData, iDataIndex) {
                $(nRow).attr("id", aData["id"]).attr("data-meal-excess", aData["excess"]);
            }
        })
    );
    $("input.datepicker").datetimepicker({
        timepicker: false,
        dayOfWeekStart: 1,
        format: "Y-m-d"
    });
    $("input.timepicker").datetimepicker({
        datepicker: false,
        dayOfWeekStart: 1,
        format: "H:i"
    });
    $("input.datetimepicker").datetimepicker({
        dayOfWeekStart: 1,
        format: "Y-m-d H:i"
    });
});

function clearFilter() {
    $("#filter")[0].reset();
    ctx.updateTable();
}