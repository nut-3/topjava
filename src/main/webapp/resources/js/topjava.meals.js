const userAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        let startDate = $("#startDate").val(),
            endDate = $("#endDate").val(),
            startTime = $("#startTime").val(),
            endTime = $("#endTime").val();
        if (startDate == null && endDate == null && startTime == null && endTime == null) {
            updateTable();
        } else {
            $.post(ctx.ajaxUrl + "filter",
                {
                    "startDate": startDate,
                    "endDate": endDate,
                    "startTime": startTime,
                    "endTime": endTime
                },
                function (data) {
                    ctx.datatableApi.clear().rows.add(data).draw();
                });
        }
    }
};

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
                    "asc"
                ]
            ],
            //https://stackoverflow.com/questions/14596270/jquery-datatable-add-id-to-tr-element-after-row-is-dynamically-added#14596338
            "fnCreatedRow": function (nRow, aData, iDataIndex) {
                $(nRow).attr("id", aData["id"]).attr("data-mealexcess", aData["excess"]);
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
    $("#startDate").val("");
    $("#endDate").val("");
    $("#startTime").val("");
    $("#endTime").val("");
    ctx.updateTable();
}