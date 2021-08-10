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
            $(jsonObject).each(function () {
                if (this.hasOwnProperty("dateTime")) {
                    this.dateTime = this.dateTime.substring(0, 10) + " " + this.dateTime.substring(11, 16);
                }
            })
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
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
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