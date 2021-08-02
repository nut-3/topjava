const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    setEnabled: function (id) {
        $.ajax({
            type: "PATCH",
            url: ctx.ajaxUrl + id + "?enabled=" + $("#" + id).find("input").first().is(":checked")
        }).done(function () {
            updateTable();
            successNoty("Saved");
        });
    },
    updateTable: function () {
        updateTable();
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                if (!aData["enabled"]) {
                    $(nRow).addClass("inactive");
                }
            }
        })
    );
});