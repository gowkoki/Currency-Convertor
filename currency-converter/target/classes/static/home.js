(function () {
    'use strict'
    // Form Validation
    const forms = document.querySelectorAll('.requires-validation')
    Array.from(forms)
      .forEach(function (form) {
        form.addEventListener('submit', function (event) {
          if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
          }

          form.classList.add('was-validated')
        }, false)
      })
})()

// AJAX and Form Handling
$(document).ready(function () {
    const $fromCurrency = $('#fromCurrency');
    const $toCurrency = $('#toCurrency');
    const $convertedAmount = $('#convertedAmount');
    const $loading = $('#loading');
    const $errorMessage = $('#errorMessage');

    // Function to display error messages
    function displayError(message) {
        $errorMessage.text(message).show();
    }

    // Function to hide error messages
    function hideError() {
        $errorMessage.hide();
    }

    // Function to show loading indicator
    function showLoading() {
        $loading.show();
    }

    // Function to hide loading indicator
    function hideLoading() {
        $loading.hide();
    }

    // Fetch and populate currency data
    function fetchCurrencies() {
        showLoading();
        $.ajax({
            url: '/currency/allCurrency',
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                const currencies = response.symbols;
                if (!currencies) {
                    displayError("No currencies found.");
                    return;
                }
                // Clear existing options except the first
                $fromCurrency.find('option:not(:first)').remove();
                $toCurrency.find('option:not(:first)').remove();

                // Populate dropdowns
                $.each(currencies, function (code, name) {
                    $fromCurrency.append(new Option(name, code));
                    $toCurrency.append(new Option(name, code));
                });
            },
            error: function (error) {
                console.error("Error fetching currency data: ", error);
                let message = "Failed to load currencies. Please try again later.";
                if (error.responseJSON && error.responseJSON.error) {
                    message = error.responseJSON.error;
                }
                displayError(message);
            },
            complete: function () {
                hideLoading();
            }
        });
    }

    // Initial fetch of currencies
    fetchCurrencies();

    // Handle form submission and currency conversion
    $('#currencyForm').on('submit', function (e) {
        e.preventDefault(); // Prevent form from submitting normally
        hideError(); // Hide any previous errors

        // Fetch the values from the form
        const fromCurrency = $fromCurrency.val();
        const toCurrency = $toCurrency.val();
        const amount = $('#amount').val();

        if (!fromCurrency || !toCurrency || !amount) {
            displayError("Please fill all fields.");
            return;
        }

        showLoading();

        // Call the conversion API
        $.ajax({
            url: `/currency/convert?from=${encodeURIComponent(fromCurrency)}&to=${encodeURIComponent(toCurrency)}&amount=${encodeURIComponent(amount)}`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                // Assuming response has a 'result' field with the converted amount
                if (response.result) {
                    // Format the converted amount to two decimal places
                    const formattedAmount = parseFloat(response.result).toFixed(2);

                    // Update the content of the <h3> tag with the formatted amount
                    $convertedAmount.text(`Converted Amount: ${formattedAmount}`).show();
                } else {
                    displayError("Conversion failed. Please check the input values.");
                }
            },
            error: function (error) {
                console.error("Error during conversion: ", error);
                let message = "Failed to convert currency. Please try again later.";
                if (error.responseJSON && error.responseJSON.error) {
                    message = error.responseJSON.error;
                }
                displayError(message);
            },
            complete: function () {
                hideLoading();
            }
        });
    });
});

