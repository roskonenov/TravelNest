document.addEventListener("DOMContentLoaded", function() {
    // Get modal elements
    const modal = document.getElementById("rentModal");
    const btn = document.getElementById("rentButton");
    const span = document.getElementsByClassName("close")[0];
    const submitBtn = document.getElementById("submitRentForm");
    const rentForm = document.getElementById("rentForm");
    const errorMsg = document.getElementById("dateErrorMessage");

    if (!modal || !btn) {
        return;
    }

    function closeModal() {
        modal.style.display = "none";
        document.body.style.overflow = "";
    }

    function openModal() {
        modal.style.display = "block";
        document.body.style.overflow = "hidden";
    }

    // Show modal when the button is clicked
    btn.onclick = openModal;

    // Hide modal when the close button is clicked
    if (span) {
        span.onclick = closeModal;
    }

    // Hide modal when clicking outside of it
    window.addEventListener("click", function(event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Close on Escape key
    document.addEventListener("keydown", function(e) {
        if (e.key === "Escape" && modal.style.display === "block") {
            closeModal();
        }
    });

    let endDatePicker = null;

    // Initialize flatpickr with mobile support and date chaining
    const startDatePicker = flatpickr("#startDate", {
        dateFormat: "Y-m-d",
        minDate: "today",
        disableMobile: true,
        onChange: function(selectedDates) {
            if (endDatePicker && selectedDates.length > 0) {
                const nextDay = new Date(selectedDates[0]);
                nextDay.setDate(nextDay.getDate() + 1);
                endDatePicker.set("minDate", nextDay);
                if (endDatePicker.selectedDates.length > 0 && endDatePicker.selectedDates[0] <= selectedDates[0]) {
                    endDatePicker.clear();
                }
            }
            validateDates();
        }
    });

    endDatePicker = flatpickr("#endDate", {
        dateFormat: "Y-m-d",
        minDate: "today",
        disableMobile: true,
        onChange: function() {
            validateDates();
        }
    });

    // Add input event listeners for validation
    const startInput = document.getElementById("startDate");
    const endInput = document.getElementById("endDate");
    if (startInput) startInput.addEventListener("input", validateDates);
    if (endInput) endInput.addEventListener("input", validateDates);

    // Form validation
    if (rentForm) {
        rentForm.addEventListener("submit", function(event) {
            if (!validateDates()) {
                event.preventDefault();
            }
        });
    }

    function validateDates() {
        const startDate = startInput ? startInput.value.trim() : "";
        const endDate = endInput ? endInput.value.trim() : "";

        if (!startDate || !endDate) {
            if (submitBtn) submitBtn.disabled = true;
            if (errorMsg) errorMsg.style.display = "none";
            return false;
        }

        const start = new Date(startDate);
        const end = new Date(endDate);

        if (isNaN(start.getTime()) || isNaN(end.getTime())) {
            if (submitBtn) submitBtn.disabled = true;
            return false;
        }

        if (end <= start) {
            const isBg = document.documentElement.lang === "bg" || 
                (document.getElementById("startDateLabel") && /[\u0400-\u04FF]/.test(document.getElementById("startDateLabel").textContent));
            if (errorMsg) {
                errorMsg.textContent = isBg ? "Крайната дата трябва да е след началната дата." : "End date must be after start date.";
                errorMsg.style.display = "block";
            }
            if (submitBtn) submitBtn.disabled = true;
            return false;
        }

        if (errorMsg) {
            errorMsg.style.display = "none";
        }
        if (submitBtn) {
            submitBtn.disabled = false;
        }
        return true;
    }
});