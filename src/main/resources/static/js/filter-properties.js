document.addEventListener('DOMContentLoaded', function () {
    const cityFilter = document.getElementById('cityFilter');

    if (!cityFilter) {
        return;
    }

    cityFilter.addEventListener('change', function () {
        const selectedCity = this.value;
        const items = document.querySelectorAll('.property_item[data-city], .attraction-card[data-city]');

        items.forEach(function (item) {
            const itemCity = item.getAttribute('data-city');
            const elementToToggle = item.classList.contains('property_item')
                ? item.closest('.property_list')
                : item;

            if (selectedCity === 'all' || itemCity === selectedCity) {
                elementToToggle.style.display = '';
            } else {
                elementToToggle.style.display = 'none';
            }
        });
    });
});