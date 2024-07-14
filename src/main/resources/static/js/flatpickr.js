document.addEventListener("DOMContentLoaded", function() {
    // Get modal elements
    const modal = document.getElementById("rentModal");
    const btn = document.getElementById("rentButton");
    const span = document.getElementsByClassName("close")[0];

    // Show modal when the button is clicked
    btn.onclick = function() {
        modal.style.display = "block";
    }

    // Hide modal when the close button is clicked
    span.onclick = function() {
        modal.style.display = "none";
    }

    // Hide modal when clicking outside of it
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    // Initialize flatpickr with validation function
    flatpickr("#startDate", {
        dateFormat: "Y-m-d",
        minDate: "today",
        onChange: validateDates
    });

    flatpickr("#endDate", {
        dateFormat: "Y-m-d",
        minDate: "today",
        onChange: validateDates
    });

    // Form validation
    document.getElementById("rentForm").addEventListener("submit", function(event) {
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;

        if (!validateDates([startDate], [endDate])) {
            event.preventDefault();
            alert("Please ensure that the dates are valid and the end date is after the start date.");
        }
    });

    function validateDates(selectedDates, dateStr, instance) {
        let start;
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;
        const currentDate = new Date();

        if (startDate) {
            start = new Date(startDate);
            if (start < currentDate) {
                alert("Start date cannot be before the current date.");
                return false;
            }
        }

        if (startDate && endDate) {
            start = new Date(startDate);
            const end = new Date(endDate);

            if (end < start) {
                alert("End date must be after the start date.");
                return false;
            }
        }
        return true;
    }
});