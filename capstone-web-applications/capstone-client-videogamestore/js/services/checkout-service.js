let checkoutService;

class CheckoutService {

    checkout() {

        if (!userService.isLoggedIn()) {
            templateBuilder.append(
                "error",
                { error: "Please log in to checkout." },
                "errors"
            );
            return;
        }

        const url = `${config.baseUrl}/checkout`;

        axios.post(url, {}, {
            headers: {
                Authorization: `Bearer ${userService.getToken()}`
            }
        })
        .then(response => {
            // 1️⃣ Clear in-memory cart immediately

            console.log("Checkout response from backend:", response.data);
            cartService.cart = { items: [], total: 0 };
            cartService.updateCartDisplay();

            // 2️⃣ Prepare data for Mustache
            const data = {
                checkout: {
                    order: response.data.order,
                    orderLineItemList: response.data.orderLineItemList
                }
            };

            // 3️⃣ Replace main content with order confirmation
            templateBuilder.build("checkout", data, "main");

            const mainDiv = document.getElementById("main");
            mainDiv.appendChild(continueBtn);

        })
        .catch(error => {
            let message = "Checkout failed.";

            if (error.response) {
                if (error.response.status === 400) message = "Your cart is empty.";
                else if (error.response.status === 404) message = "Cart not found.";
                else if (error.response.status === 401) message = "Unauthorized. Please log in.";
            }

            templateBuilder.append(
                "error",
                { error: message },
                "errors"
            );
        });
    }
}

// Initialize service
document.addEventListener("DOMContentLoaded", () => {
    checkoutService = new CheckoutService();
});

// Delegated click listener works with dynamically added button
document.addEventListener("click", e => {
    if (e.target.id === "checkoutBtn") {
        checkoutService.checkout();
    }
});
