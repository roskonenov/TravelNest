$(function () {
    // Guard against missing plugins
    if (!$.fn.datepicker || !$.fn.timepicker) {
        return;
    }

    // Initialize datepickers
    $("#startDate, #endDate").datepicker({
        dateFormat: "dd-mm-yy",
        minDate: 0, // Disable past dates
        firstDay: 1, // Start week on Monday
        showAnim: "fadeIn",
        onSelect: function (dateText, inst) {
            let id = $(this).attr("id");
            if (id === "startDate") {
                let start = $("#startDate").datepicker("getDate");
                if (start) {
                    $("#endDate").datepicker("option", "minDate", start);
                    let currentEnd = $("#endDate").datepicker("getDate");
                    if (currentEnd && currentEnd < start) {
                        $("#endDate").datepicker("setDate", null);
                    }
                }
            }
            validateDates();
        }
    });

    // Initialize timepicker
    $("#time").timepicker({
        timeFormat: 'HH:mm',
        interval: 15,
        startTime: '00:00',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });

    function validateDates() {
        let startDate = $("#startDate").datepicker("getDate");
        let endDate = $("#endDate").datepicker("getDate");

        if (startDate && endDate) {
            if (endDate < startDate) {
                $("#endDate").datepicker("setDate", null);
            }
        }
    }

    $("#startDate").on("change", validateDates);
    $("#endDate").on("change", validateDates);
});