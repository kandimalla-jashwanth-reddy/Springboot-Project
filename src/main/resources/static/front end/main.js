// API Configuration
const API_BASE_URL = "http://localhost:9090/api";

// DOM Elements
const logoutBtn = document.getElementById("logout");
const userGreeting = document.getElementById("user-greeting");
const cartCount = document.querySelector(".cart-count");

// Initialize the app
document.addEventListener("DOMContentLoaded", () => {
    updateUserGreeting();
    loadBooks();
    setupEventListeners();
    
    // Load featured books on homepage
    if (window.location.pathname.includes("index.html") || 
        window.location.pathname === "/") {
        loadFeaturedBooks();
    }
});

// Update user greeting in header
function updateUserGreeting() {
    if (userGreeting) {
        const user = JSON.parse(localStorage.getItem("user")) || {};
        const username = user.username || 'Guest';
        userGreeting.querySelector('span').textContent = `Hello, ${username}`;
    }
}

// Setup event listeners
function setupEventListeners() {
    // Logout
    if (logoutBtn) {
        logoutBtn.addEventListener("click", handleLogout);
    }
    
    // Registration
    if (document.getElementById("registerForm")) {
        document.getElementById("registerForm").addEventListener("submit", handleRegister);
    }
    
    // Login
    if (document.getElementById("loginForm")) {
        document.getElementById("loginForm").addEventListener("submit", handleLogin);
    }
    
    // Book management
    if (document.getElementById("bookForm")) {
        document.getElementById("bookForm").addEventListener("submit", handleAddBook);
    }
}

// Load featured books
async function loadFeaturedBooks() {
    try {
        const response = await fetch(`${API_BASE_URL}/books?limit=4`);
        if (!response.ok) throw new Error("Failed to load featured books");
        
        const books = await response.json();
        renderBooks(books, document.getElementById("featuredBooks"));
    } catch (error) {
        console.error("Error loading featured books:", error);
    }
}

// Load all books
async function loadBooks(adminView = false) {
    const container = document.getElementById("booksContainer");
    if (!container) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/books`);
        if (!response.ok) throw new Error("Failed to load books");
        
        const books = await response.json();
        renderBooks(books, container, adminView);
    } catch (error) {
        container.innerHTML = `<div class="error">${error.message}</div>`;
    }
}

// Render books
function renderBooks(books, container, adminView = false) {
    container.innerHTML = "";
    
    if (books.length === 0) {
        container.innerHTML = "<p>No books available at the moment.</p>";
        return;
    }
    
    books.forEach(book => {
        const bookCard = document.createElement("div");
        bookCard.className = "book-card";
        bookCard.innerHTML = `
            <div class="book-image">
                <img src="${book.imageUrl || 'https://via.placeholder.com/150x200?text=No+Cover'}" alt="${book.title}">
            </div>
            <div class="book-info">
                <h3 class="book-title">${book.title}</h3>
                <p class="book-author">By ${book.author}</p>
                <div class="book-rating">
                    ${renderRating(book.rating || 0)}
                </div>
                <p class="book-price">$${book.price.toFixed(2)}</p>
                ${adminView ? `<button class="delete-book" data-id="${book.id}">Delete Book</button>` : `<button class="add-to-cart" data-id="${book.id}">Add to Cart</button>`}
            </div>
        `;
        container.appendChild(bookCard);
    });

    // Add event listeners
    if (adminView) {
        document.querySelectorAll(".delete-book").forEach(btn => {
            btn.addEventListener("click", handleDeleteBook);
        });
    } else {
        document.querySelectorAll(".add-to-cart").forEach(btn => {
            btn.addEventListener("click", handleAddToCart);
        });
    }
}

// Render star rating
function renderRating(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';
    
    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<i class="fas fa-star"></i>';
        } else if (i === fullStars && hasHalfStar) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }
    
    return stars;
}

// Handle registration
async function handleRegister(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        if (response.ok) {
            showNotification('Account successfully created! Please log in.', 'success');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
        } else {
            const errorText = await response.text();
            showNotification(errorText || 'Registration failed', 'error');
        }
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Handle login
async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (!response.ok) throw new Error('Login failed');
        const data = await response.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data.user));
        window.location.href = 'index.html';
    } catch (error) {
        alert(error.message);
    }
}

// Handle logout
function handleLogout(e) {
    e.preventDefault();
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "index.html";
}

// Handle add to cart
async function handleAddToCart(e) {
    const bookId = e.target.getAttribute("data-id");
    const token = localStorage.getItem("token");
    
    if (!token) {
        showNotification("Please login to add items to cart", "error");
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/cart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({ bookId })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to add to cart");
        }

        updateCartCount();
        showNotification("Item added to cart", "success");
    } catch (error) {
        showNotification(error.message, "error");
    }
}

// Update cart count
async function updateCartCount() {
    if (!cartCount) return;
    
    const token = localStorage.getItem("token");
    if (!token) {
        cartCount.textContent = "0";
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/cart/count`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            cartCount.textContent = data.count || "0";
        }
    } catch (error) {
        console.error("Error updating cart count:", error);
    }
}

// Handle add book (admin)
async function handleAddBook(e) {
    e.preventDefault();
    const submitBtn = e.target.querySelector("button");
    const originalText = submitBtn.innerHTML;
    
    try {
        // Show loading state
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Adding...';
        
        const title = document.getElementById("title").value;
        const author = document.getElementById("author").value;
        const price = parseFloat(document.getElementById("price").value);
        const imageUrl = document.getElementById("imageUrl").value || "https://via.placeholder.com/150x200?text=No+Cover";

        const response = await fetch(`${API_BASE_URL}/books`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({ title, author, price, imageUrl })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to add book");
        }

        // Show success and reload
        submitBtn.innerHTML = '<i class="fas fa-check"></i> Added!';
        setTimeout(() => {
            location.reload();
        }, 1000);
    } catch (error) {
        showNotification(error.message, "error");
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
    }
}

// Handle delete book (admin)
async function handleDeleteBook(e) {
    const bookId = e.target.getAttribute("data-id");
    
    if (!confirm("Are you sure you want to delete this book?")) return;

    try {
        const response = await fetch(`${API_BASE_URL}/books/${bookId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to delete book");
        }

        showNotification("Book deleted successfully", "success");
        setTimeout(() => {
            location.reload();
        }, 1000);
    } catch (error) {
        showNotification(error.message, "error");
    }
}

// Show notification
function showNotification(message, type = "success") {
    const notification = document.createElement("div");
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <i class="fas fa-${type === "success" ? "check-circle" : "exclamation-circle"}"></i>
        <span>${message}</span>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.classList.add("show");
        setTimeout(() => {
            notification.classList.remove("show");
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }, 10);
}

// Add notification styles
const notificationStyles = document.createElement("style");
notificationStyles.textContent = `
.notification {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 20px;
    border-radius: 4px;
    color: white;
    display: flex;
    align-items: center;
    gap: 10px;
    transform: translateX(150%);
    transition: transform 0.3s ease-out;
    z-index: 1000;
}

.notification.show {
    transform: translateX(0);
}

.notification.success {
    background-color: #28a745;
}

.notification.error {
    background-color: #dc3545;
}

.notification i {
    font-size: 1.2rem;
}
`;
document.head.appendChild(notificationStyles);


// Registration Form Validation
if (document.getElementById("registerForm")) {
    const form = document.getElementById("registerForm");
    const inputs = form.querySelectorAll("input");
    
    inputs.forEach(input => {
        input.addEventListener("blur", () => {
            validateInput(input);
        });
        
        input.addEventListener("input", () => {
            if (input.getAttribute("focused") === "true") {
                validateInput(input);
            }
        });
    });
    
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        let isValid = true;
        
        inputs.forEach(input => {
            input.setAttribute("focused", "true");
            if (!validateInput(input)) {
                isValid = false;
            }
        });
        
        if (isValid) {
            // Form is valid, proceed with registration
            const username = document.getElementById("username").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            
            // Your registration API call here
            console.log("Form submitted:", { username, email, password });
        }
    });
}

function validateInput(input) {
    const errorMessage = input.nextElementSibling;
    
    if (input.validity.valid) {
        input.style.borderColor = "#a6a6a6";
        errorMessage.style.display = "none";
        return true;
    } else {
        input.style.borderColor = "#c40000";
        errorMessage.style.display = "block";
        return false;
    }
}

// Update renderBooks() function
bookCard.innerHTML = `
    ...
    <p class="book-price">$${book.price.toFixed(2)}</p>
    <button class="add-to-cart" data-id="${book.id}">  <!-- Changed _id to id -->
    ...
`;

// Update handleAddToCart
const response = await fetch(`${API_BASE_URL}/orders/place-by-ids`, {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify({
        userId: currentUser.id,
        bookIds: [bookId]
    })
});



// Add to main.js
async function loadOrders() {
    const container = document.getElementById("ordersContainer");
    if (!container) return;
    
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        const user = JSON.parse(localStorage.getItem("user"));
        const response = await fetch(`${API_BASE_URL}/orders/user/${user.id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            const orders = await response.json();
            renderOrders(orders, container);
        }
    } catch (error) {
        console.error("Error loading orders:", error);
    }
}

function renderOrders(orders, container) {
    if (orders.length === 0) {
        container.innerHTML = `
            <div class="empty-orders">
                <i class="fas fa-box-open"></i>
                <h3>You have no orders yet</h3>
                <p>Start shopping to see your orders here</p>
                <a href="books.html" class="auth-button">Shop Now</a>
            </div>
        `;
        return;
    }

    container.innerHTML = orders.map(order => `
        <div class="order-item">
            <div class="order-image">
                <img src="${order.items[0].book.imageUrl || 'https://via.placeholder.com/100'}" alt="${order.items[0].book.title}">
            </div>
            <div class="order-details">
                <div class="order-title">${order.items[0].book.title}</div>
                <div class="order-date">Ordered on ${new Date(order.orderDate).toLocaleDateString()}</div>
                <div class="order-price">Total: $${order.totalPrice.toFixed(2)}</div>
                <div class="order-status">Status: ${order.status || 'Processing'}</div>
            </div>
        </div>
    `).join('');
}



// Update the loadBooks function to handle search and categories
async function loadBooks() {
    const container = document.getElementById("booksContainer");
    if (!container) return;
    
    // Get search query from URL or search box
    const urlParams = new URLSearchParams(window.location.search);
    const search = urlParams.get('search') || document.querySelector('.search-input')?.value;
    const category = urlParams.get('category');
    
    try {
        let url = `${API_BASE_URL}/books`;
        const params = new URLSearchParams();
        
        if (search) params.append('search', search);
        if (category) params.append('category', category);
        
        if (params.toString()) url += `?${params.toString()}`;
        
        const response = await fetch(url);
        if (!response.ok) throw new Error("Failed to load books");
        
        const books = await response.json();
        renderBooks(books, container);
    } catch (error) {
        container.innerHTML = `<div class="error">${error.message}</div>`;
    }
}

// Update the search functionality
document.querySelector('.search-button')?.addEventListener('click', (e) => {
    e.preventDefault();
    const searchInput = document.querySelector('.search-input');
    if (searchInput && searchInput.value) {
        window.location.href = `books.html?search=${encodeURIComponent(searchInput.value)}`;
    }
});

// Update the category dropdown
document.querySelector('.search-select')?.addEventListener('change', (e) => {
    if (e.target.value && e.target.value !== "All Categories") {
        window.location.href = `books.html?category=${encodeURIComponent(e.target.value)}`;
    }
});

// Single book detail logic for Book.html
if (window.location.pathname.includes('Book.html')) {
    document.addEventListener('DOMContentLoaded', loadSingleBook);
}

async function loadSingleBook() {
    const params = new URLSearchParams(window.location.search);
    const bookId = params.get('id');
    const container = document.getElementById('booksContainer');
    if (!bookId || !container) {
        container.innerHTML = '<p>Book not found.</p>';
        return;
    }
    try {
        const response = await fetch(`${API_BASE_URL}/books/${bookId}`);
        if (!response.ok) throw new Error('Book not found');
        const book = await response.json();
        container.innerHTML = `
            <div class="book-detail">
                <img src="${book.imageUrl || 'https://via.placeholder.com/150x200?text=No+Cover'}" alt="${book.title}">
                <h2>${book.title}</h2>
                <p><strong>Author:</strong> ${book.author}</p>
                <p><strong>Category:</strong> ${book.category || ''}</p>
                <p><strong>Price:</strong> $${book.price.toFixed(2)}</p>
                <p><strong>Description:</strong> ${book.description || ''}</p>
                ${book.available !== false ? `<button class="add-to-cart" data-id="${book.id}">Add to Cart</button>` : `<span class='out-of-stock'>Out of Stock</span>`}
            </div>
        `;
        if (book.available !== false) {
            document.querySelector('.add-to-cart').addEventListener('click', handleAddToCart);
        }
    } catch (error) {
        container.innerHTML = `<div class="error">${error.message}</div>`;
    }
}