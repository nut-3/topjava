const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (result) {
            let jsonObject = JSON.parse(result);
            if (typeof jsonObject === 'object') {
                $(jsonObject).each(function () {
                    if (this.hasOwnProperty("dateTime")) {
                        this.dateTime = this.dateTime.substring(0, 10) + " " + this.dateTime.substring(11, 16);
                    }
                });
            }
            return jsonObject;
        }
    }
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
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
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
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
                $(nRow).attr("id", aData["id"]).attr("data-mealExcess", aData["excess"]);
            }
        })
    );

    //https://stackoverflow.com/a/18317523/2791349
    let userLang = navigator.language || navigator.userLanguage;
    userLang = userLang.split('-')[0];
    $.datetimepicker.setLocale(userLang);

    let startDate = $("#startDate");
    let endDate = $("#endDate");
    startDate.datetimepicker({
        timepicker: false,
        dayOfWeekStart: 1,
        format: "Y-m-d",
        onShow: function (cdt) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        dayOfWeekStart: 1,
        format: "Y-m-d",
        onShow: function (cdt) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });
    let startTime = $("#startTime");
    let endTime = $("#endTime");
    startTime.datetimepicker({
        datepicker: false,
        dayOfWeekStart: 1,
        format: "H:i",
        onShow: function (cdt) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        dayOfWeekStart: 1,
        format: "H:i",
        onShow: function (cdt) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    });
    $("input.datetimepicker").datetimepicker({
        dayOfWeekStart: 1,
        format: "Y-m-d H:i"
    });
});