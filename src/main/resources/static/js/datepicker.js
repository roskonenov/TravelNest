$(function () {
    // Initialize datepickers
    $("#startDate, #endDate").datepicker({
        dateFormat: "dd-mm-yy",
        minDate: 0, // Disable past dates
        onSelect: function () {
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
                alert("End date cannot be before start date.");
                $("#endDate").datepicker("setDate", null);
            }
        }
    }

    $("#startDate").change(validateDates);
    $("#endDate").change(validateDates);
});