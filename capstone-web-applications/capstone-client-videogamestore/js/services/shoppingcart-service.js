let cartService;

class ShoppingCartService {
    cart = { items: [], total: 0 };

    fetchCart() {
        const url = `${config.baseUrl}/cart`;
        return axios.get(url)
            .then(res => {
                const itemsObj = res.data.items || {};
                this.cart.items = Object.values(itemsObj);
                this.cart.total = res.data.total || 0;
                this.updateCartBadge();
            })
            .catch(() => {
                this.cart.items = [];
                this.cart.total = 0;
                this.updateCartBadge();
            });
    }

    addToCart(productId) {
        axios.post(`${config.baseUrl}/cart/products/${productId}`)
            .then(() => this.fetchCart().then(() => this.renderCartPage()))
            .catch(() => templateBuilder.append("error", { error: "Add to cart failed." }, "errors"));
    }

    updateQuantity(productId, quantity) {
        axios.put(`${config.baseUrl}/cart/products/${productId}`, { quantity })
            .then(() => this.fetchCart().then(() => this.renderCartPage()))
            .catch(() => templateBuilder.append("error", { error: "Update quantity failed." }, "errors"));
    }

    removeFromCart(productId) {
        axios.delete(`${config.baseUrl}/cart/products/${productId}`)
            .then(() => this.fetchCart().then(() => this.renderCartPage()))
            .catch(() => templateBuilder.append("error", { error: "Remove item failed." }, "errors"));
    }

    clearCart() {
        axios.delete(`${config.baseUrl}/cart`)
            .then(() => this.fetchCart().then(() => this.renderCartPage()))
            .catch(() => templateBuilder.append("error", { error: "Clear cart failed." }, "errors"));
    }

    updateCartBadge() {
        const badge = document.getElementById("cart-items");
        if (badge) badge.innerText = this.cart.items.length;
    }

    showCartPage() {
        this.fetchCart().then(() => this.renderCartPage());
    }

    renderCartPage() {
        const main = document.getElementById("main");
        main.innerHTML = "";

        // Sidebar placeholder


        // Main cart container
        const contentDiv = document.createElement("div");
        contentDiv.className = "content-form"; // fills the right column naturally
        contentDiv.style.gridColumn = "1 / -1";
        main.appendChild(contentDiv);

        // Cart header
        const cartHeader = document.createElement("div");
        cartHeader.className = "cart-header";
        const h1 = document.createElement("h1");
        h1.innerText = "Shopping Cart";
        const clearBtn = document.createElement("button");
        clearBtn.className = "btn btn-danger";
        clearBtn.innerText = "Clear Cart";
        clearBtn.onclick = () => this.clearCart();
        cartHeader.append(h1, clearBtn);
        contentDiv.appendChild(cartHeader);

        if (this.cart.items.length === 0) {
            contentDiv.innerHTML += "<p>Your cart is empty.</p>";
            return;
        }

        // Cart items
        this.cart.items.forEach(item => contentDiv.appendChild(this.buildItem(item)));

        // Cart footer
        const footer = document.createElement("div");
        footer.className = "cart-footer";
        footer.style.display = "flex";
        footer.style.justifyContent = "space-between";
        footer.style.alignItems = "center";
        footer.style.marginTop = "20px";
        footer.innerHTML = `
            <h3>Total: $${this.cart.total.toFixed(2)}</h3>
             <button id="checkoutBtn" class="btn btn-primary">Checkout</button>

        `;
        contentDiv.appendChild(footer);
    }

    buildItem(item) {
        const outerDiv = document.createElement("div");
        outerDiv.className = "cart-item";

        outerDiv.style.display = "flex";
        outerDiv.style.justifyContent = "space-between";
        outerDiv.style.padding = "12px";
        outerDiv.style.borderBottom = "1px solid #ccc";

        // Image
        const imgDiv = document.createElement("div");
        imgDiv.style.flex = "0 0 100px";
        const img = document.createElement("img");
        img.src = `images/products/${item.product.imageUrl}`;
        img.style.width = "100%";
        img.style.cursor = "pointer";
        img.addEventListener("click", () => showImageDetailForm(item.product.name, img.src));
        imgDiv.appendChild(img);
        outerDiv.appendChild(imgDiv);

        // Product info
        const infoDiv = document.createElement("div");
        infoDiv.style.flex = "1";
        infoDiv.style.marginLeft = "12px";
        const name = document.createElement("h4");
        name.innerText = item.product.name;
        name.style.margin = "0 0 4px 0";
        const desc = document.createElement("p");
        desc.innerText = item.product.description;
        desc.style.margin = "0 0 4px 0";
        desc.style.fontSize = "0.9rem";
        desc.style.color = "#555";
        const price = document.createElement("p");
        price.innerText = `$${item.product.price}`;
        price.style.fontWeight = "bold";
        price.style.margin = "0";
        infoDiv.append(name, desc, price);
        outerDiv.appendChild(infoDiv);

        // Quantity + Remove
        const actionDiv = document.createElement("div");
        actionDiv.style.display = "flex";
        actionDiv.style.alignItems = "center";
        actionDiv.style.gap = "8px";
        const qtyInput = document.createElement("input");
        qtyInput.type = "number";
        qtyInput.min = 1;
        qtyInput.value = item.quantity;
        qtyInput.style.width = "60px";
        qtyInput.onblur = () => {
            if (qtyInput.value != item.quantity) {
                this.updateQuantity(item.product.productId, qtyInput.value);
            }
        };
        const removeBtn = document.createElement("button");
        removeBtn.className = "btn btn-outline-danger";
        removeBtn.innerText = "🗑️";
        removeBtn.onclick = () => this.removeFromCart(item.product.productId);
        actionDiv.append(qtyInput, removeBtn);
        outerDiv.appendChild(actionDiv);

        return outerDiv;
    }
}


