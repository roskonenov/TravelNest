document.getElementById('cityFilter').addEventListener('change', function() {
    const selectedCity = this.value;
    const houseItems = document.querySelectorAll('.house_item');

    houseItems.forEach(function(item) {
        if (selectedCity === 'all' || item.getAttribute('data-city') === selectedCity) {
            item.style.display = 'inline-flex';
        } else {
            item.style.display = 'none';
        }
    });
});