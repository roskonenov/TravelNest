document.getElementById('cityFilter').addEventListener('change', function() {
    const selectedCity = this.value;
    const items = document.querySelectorAll('.property_item, .attraction-card');

    items.forEach(function(item) {
        if (selectedCity === 'all' || item.getAttribute('data-city') === selectedCity) {
            item.style.display = 'inline-flex';
        } else {
            item.style.display = 'none';
        }
    });
});