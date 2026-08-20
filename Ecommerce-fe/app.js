import './style.css';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';
const TOKEN_KEY = 'ECOMMERCE_TOKEN';
const USER_KEY = 'ECOMMERCE_USER';
const CURRENCY = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' });
const PLACEHOLDER_IMAGE = 'https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=900&q=80';

const state = {
    page: 0,
    size: 12,
    totalPages: 0,
    keyword: '',
    categoryCode: '',
    categories: [],
    products: [],
    cart: null,
    activeProduct: null,
};

const dom = {};
let debounceTimer;

document.addEventListener('DOMContentLoaded', () => {
    cacheDom();

    if (document.body.dataset.page === 'home') {
        initHomePage();
    }

    if (document.body.dataset.page === 'auth') {
        initAuthPage();
    }
});

function cacheDom() {
    const ids = [
        'cart-button', 'cart-count', 'cart-total-badge', 'cart-total', 'cart-items',
        'user-chip', 'login-link', 'logout-button', 'keyword-input', 'category-chips',
        'page-status', 'product-grid', 'pagination', 'product-modal', 'product-detail-image',
        'product-detail-meta', 'product-detail-title', 'product-detail-description',
        'product-detail-price', 'product-detail-stock', 'detail-quantity', 'detail-add-cart',
        'login-form', 'register-form', 'auth-message', 'auth-login-tab', 'auth-register-tab',
    ];

    ids.forEach((id) => {
        dom[id] = document.getElementById(id);
    });
}

function initHomePage() {
    syncHeaderState();
    loadCategories();
    loadProducts();
    loadCart();

    if (dom['keyword-input']) {
        dom['keyword-input'].addEventListener('input', () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                state.keyword = dom['keyword-input'].value.trim();
                state.page = 0;
                loadProducts();
            }, 250);
        });
    }

    if (dom['category-chips']) {
        dom['category-chips'].addEventListener('click', (event) => {
            const button = event.target.closest('[data-category]');
            if (!button) {
                return;
            }

            state.categoryCode = button.dataset.category || '';
            state.page = 0;
            renderCategoryChips();
            loadProducts();
        });
    }

    if (dom['pagination']) {
        dom['pagination'].addEventListener('click', (event) => {
            const button = event.target.closest('[data-page]');
            if (!button) {
                return;
            }

            state.page = Number(button.dataset.page || 0);
            loadProducts();
        });
    }

    if (dom['product-grid']) {
        dom['product-grid'].addEventListener('click', handleProductGridAction);
    }

    if (dom['cart-items']) {
        dom['cart-items'].addEventListener('click', handleCartAction);
    }

    if (dom['cart-button']) {
        dom['cart-button'].addEventListener('click', () => {
            document.getElementById('cart-panel')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
        });
    }

    if (dom['logout-button']) {
        dom['logout-button'].addEventListener('click', logout);
    }

    if (dom['product-modal']) {
        dom['product-modal'].addEventListener('click', (event) => {
            const action = event.target.closest('[data-action]')?.dataset.action;
            if (action === 'close-modal' || event.target === dom['product-modal']) {
                closeProductModal();
            }
        });
    }

    if (dom['detail-add-cart']) {
        dom['detail-add-cart'].addEventListener('click', addDetailProductToCart);
    }

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            closeProductModal();
        }
    });
}

function initAuthPage() {
    if (dom['auth-login-tab']) {
        dom['auth-login-tab'].addEventListener('click', () => switchAuthTab('login'));
    }

    if (dom['auth-register-tab']) {
        dom['auth-register-tab'].addEventListener('click', () => switchAuthTab('register'));
    }

    dom['login-form']?.addEventListener('submit', handleLoginSubmit);
    dom['register-form']?.addEventListener('submit', handleRegisterSubmit);
}

function switchAuthTab(tab) {
    const loginForm = dom['login-form'];
    const registerForm = dom['register-form'];
    const loginTab = dom['auth-login-tab'];
    const registerTab = dom['auth-register-tab'];
    const message = dom['auth-message'];

    if (!loginForm || !registerForm || !loginTab || !registerTab) {
        return;
    }

    message.textContent = '';

    const isLogin = tab === 'login';
    loginForm.hidden = !isLogin;
    registerForm.hidden = isLogin;
    loginTab.classList.toggle('active', isLogin);
    registerTab.classList.toggle('active', !isLogin);
}

async function handleLoginSubmit(event) {
    event.preventDefault();
    const message = dom['auth-message'];
    const username = document.getElementById('login-username')?.value.trim() || '';
    const password = document.getElementById('login-password')?.value || '';

    try {
        setAuthMessage('Đang đăng nhập...', 'info');
        const response = await apiFetch('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        });

        if (!response?.token) {
            throw new Error('Không nhận được token đăng nhập');
        }

        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.setItem(USER_KEY, username);
        setAuthMessage('Đăng nhập thành công. Đang chuyển trang...', 'success');
        window.location.href = 'index.html';
    } catch (error) {
        setAuthMessage(error.message || 'Đăng nhập thất bại', 'error');
    }
}

async function handleRegisterSubmit(event) {
    event.preventDefault();

    const payload = {
        username: document.getElementById('reg-username')?.value.trim() || '',
        name: document.getElementById('reg-name')?.value.trim() || '',
        email: document.getElementById('reg-email')?.value.trim() || '',
        phone: document.getElementById('reg-phone')?.value.trim() || '',
        password: document.getElementById('reg-password')?.value || '',
    };

    try {
        setAuthMessage('Đang tạo tài khoản...', 'info');
        await apiFetch('/auth/register', {
            method: 'POST',
            body: JSON.stringify(payload),
        });

        setAuthMessage('Tạo tài khoản thành công. Hãy đăng nhập.', 'success');
        switchAuthTab('login');
    } catch (error) {
        setAuthMessage(error.message || 'Đăng ký thất bại', 'error');
    }
}

async function loadCategories() {
    try {
        const categories = await apiFetch('/category');
        state.categories = Array.isArray(categories) ? categories : [];
        renderCategoryChips();
    } catch (error) {
        state.categories = [];
        renderCategoryChips();
        setHomeMessage(error.message || 'Không tải được danh mục', 'error');
    }
}

async function loadProducts() {
    if (!dom['product-grid']) {
        return;
    }

    setHomeMessage('Đang tải sản phẩm...', 'info');

    const params = new URLSearchParams({
        page: String(state.page),
        size: String(state.size),
    });

    if (state.keyword) {
        params.set('keyword', state.keyword);
    }

    if (state.categoryCode) {
        params.set('categoryCode', state.categoryCode);
    }

    try {
        const pageResponse = await apiFetch(`/products?${params.toString()}`);
        state.products = Array.isArray(pageResponse?.content) ? pageResponse.content : [];
        state.totalPages = pageResponse?.totalPages || 0;
        renderProducts();
        renderPagination();
        setHomeMessage(state.products.length ? 'Sẵn sàng' : 'Không có sản phẩm phù hợp', 'info');
    } catch (error) {
        state.products = [];
        state.totalPages = 0;
        renderProducts();
        renderPagination();
        setHomeMessage(error.message || 'Không tải được danh sách sản phẩm', 'error');
    }
}

async function loadCart() {
    if (!getToken()) {
        renderCart(null);
        return;
    }

    try {
        const cart = await apiFetch('/cart');
        renderCart(cart);
    } catch (error) {
        if (String(error.message || '').toLowerCase().includes('401')) {
            logout(false);
            return;
        }

        renderCart(null);
        setHomeMessage(error.message || 'Không tải được giỏ hàng', 'error');
    }
}

function renderCategoryChips() {
    if (!dom['category-chips']) {
        return;
    }

    const chips = [
        `<button class="chip ${state.categoryCode ? '' : 'active'}" type="button" data-category="">Tất cả</button>`,
        ...state.categories.map((category) => `
            <button class="chip ${state.categoryCode === category.categoryCode ? 'active' : ''}" type="button" data-category="${escapeHtml(category.categoryCode)}">
                ${escapeHtml(category.name)}
            </button>
        `),
    ];

    dom['category-chips'].innerHTML = chips.join('');
}

function renderProducts() {
    if (!dom['product-grid']) {
        return;
    }

    if (!state.products.length) {
        dom['product-grid'].innerHTML = `
            <div class="empty-state">
                <strong>Không tìm thấy sản phẩm nào</strong>
                <p>Thử đổi từ khóa hoặc xóa bộ lọc category.</p>
            </div>
        `;
        return;
    }

    dom['product-grid'].innerHTML = state.products.map((product) => `
        <article class="product-card glass-card" data-product-code="${escapeHtml(product.productCode)}">
            <div class="product-image-wrap">
                <img src="${escapeHtml(product.imageUrl || PLACEHOLDER_IMAGE)}" alt="${escapeHtml(product.name)}">
                <span class="product-badge ${product.outOfStock ? 'danger' : ''}">${product.outOfStock ? 'Hết hàng' : `Còn ${product.quantity ?? 0}`}</span>
            </div>
            <div class="product-copy">
                <p class="product-category">${escapeHtml(product.categoryCode || 'General')}</p>
                <h3>${escapeHtml(product.name)}</h3>
                <div class="product-meta">
                    <strong>${formatMoney(product.price)}</strong>
                    <span>${escapeHtml(product.productCode)}</span>
                </div>
            </div>
            <div class="product-actions">
                <button class="ghost-button" type="button" data-action="view-product" data-product-code="${escapeHtml(product.productCode)}">Chi tiết</button>
                <button class="primary-button" type="button" data-action="add-cart" data-product-code="${escapeHtml(product.productCode)}" ${product.outOfStock ? 'disabled' : ''}>Thêm vào giỏ</button>
            </div>
        </article>
    `).join('');
}

function renderPagination() {
    if (!dom['pagination']) {
        return;
    }

    if (state.totalPages <= 1) {
        dom['pagination'].innerHTML = '';
        return;
    }

    dom['pagination'].innerHTML = Array.from({ length: state.totalPages }, (_, index) => {
        const page = index;
        const active = page === state.page ? 'active' : '';
        return `<button class="page-button ${active}" type="button" data-page="${page}">${page + 1}</button>`;
    }).join('');
}

async function handleProductGridAction(event) {
    const button = event.target.closest('[data-action]');
    if (!button) {
        return;
    }

    const productCode = button.dataset.productCode;
    if (!productCode) {
        return;
    }

    if (button.dataset.action === 'view-product') {
        openProductModal(productCode);
    }

    if (button.dataset.action === 'add-cart') {
        await addToCart(productCode, 1);
    }
}

async function openProductModal(productCode) {
    try {
        const product = await apiFetch(`/products/${encodeURIComponent(productCode)}`);
        state.activeProduct = product;

        if (dom['product-detail-image']) {
            dom['product-detail-image'].src = product.imageUrl || PLACEHOLDER_IMAGE;
            dom['product-detail-image'].alt = product.name || 'Sản phẩm';
        }
        dom['product-detail-meta'].textContent = product.categoryCode || 'Chi tiết sản phẩm';
        dom['product-detail-title'].textContent = product.name || '';
        dom['product-detail-description'].textContent = product.description || 'Chưa có mô tả cho sản phẩm này.';
        dom['product-detail-price'].textContent = formatMoney(product.price);
        dom['product-detail-stock'].textContent = product.outOfStock ? 'Hết hàng' : `Còn ${product.quantity ?? 0}`;
        dom['detail-quantity'].value = 1;
        dom['product-modal'].hidden = false;
    } catch (error) {
        setHomeMessage(error.message || 'Không tải được chi tiết sản phẩm', 'error');
    }
}

function closeProductModal() {
    if (dom['product-modal']) {
        dom['product-modal'].hidden = true;
    }
    state.activeProduct = null;
}

async function addDetailProductToCart() {
    if (!state.activeProduct?.productCode) {
        return;
    }

    const quantity = Number(dom['detail-quantity']?.value || 1);
    await addToCart(state.activeProduct.productCode, quantity);
    closeProductModal();
}

async function addToCart(productCode, quantity) {
    if (!getToken()) {
        setHomeMessage('Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.', 'error');
        window.location.href = 'login.html';
        return;
    }

    try {
        await apiFetch('/cart/items', {
            method: 'POST',
            body: JSON.stringify({ productCode, quantity }),
        });

        setHomeMessage('Đã thêm sản phẩm vào giỏ hàng.', 'success');
        await loadCart();
    } catch (error) {
        setHomeMessage(error.message || 'Không thêm được vào giỏ hàng', 'error');
    }
}

async function handleCartAction(event) {
    const button = event.target.closest('[data-action]');
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    if (!Number.isFinite(id)) {
        return;
    }

    const cartItem = state.cart?.items?.find((item) => item.id === id);
    if (!cartItem) {
        return;
    }

    if (button.dataset.action === 'increment') {
        await updateCartItem(id, (cartItem.quantity || 1) + 1);
    }

    if (button.dataset.action === 'decrement') {
        const nextQuantity = Math.max(1, (cartItem.quantity || 1) - 1);
        await updateCartItem(id, nextQuantity);
    }

    if (button.dataset.action === 'remove') {
        await removeCartItems([id]);
    }
}

async function updateCartItem(cartItemId, quantity) {
    try {
        await apiFetch(`/cart/items/${cartItemId}`, {
            method: 'PATCH',
            body: JSON.stringify({ quantity }),
        });

        await loadCart();
    } catch (error) {
        setHomeMessage(error.message || 'Không cập nhật được giỏ hàng', 'error');
    }
}

async function removeCartItems(cartItemIds) {
    try {
        await apiFetch('/cart/items/delete', {
            method: 'DELETE',
            body: JSON.stringify({ cartItemIds }),
        });

        await loadCart();
    } catch (error) {
        setHomeMessage(error.message || 'Không xoá được sản phẩm khỏi giỏ hàng', 'error');
    }
}

function renderCart(cart) {
    state.cart = cart;

    const items = cart?.items || [];
    const totalAmount = cart?.totalAmount || 0;

    if (dom['cart-count']) {
        dom['cart-count'].textContent = String(items.length);
    }

    if (dom['cart-total']) {
        dom['cart-total'].textContent = formatMoney(totalAmount);
    }

    if (dom['cart-total-badge']) {
        dom['cart-total-badge'].textContent = formatMoney(totalAmount);
    }

    if (!dom['cart-items']) {
        return;
    }

    if (!items.length) {
        dom['cart-items'].innerHTML = `
            <div class="empty-state compact">
                <strong>Giỏ hàng đang trống</strong>
                <p>Hãy thêm vài sản phẩm từ danh sách bên trái.</p>
            </div>
        `;
        return;
    }

    dom['cart-items'].innerHTML = items.map((item) => `
        <article class="cart-item">
            <img src="${escapeHtml(item.imageUrl || PLACEHOLDER_IMAGE)}" alt="${escapeHtml(item.name)}">
            <div class="cart-item-copy">
                <h3>${escapeHtml(item.name)}</h3>
                <p>${formatMoney(item.price)} · ${formatMoney(item.subtotal)}</p>
                <div class="cart-qty">
                    <button class="quantity-button" type="button" data-action="decrement" data-id="${item.id}">−</button>
                    <span>${item.quantity}</span>
                    <button class="quantity-button" type="button" data-action="increment" data-id="${item.id}">+</button>
                </div>
            </div>
            <button class="icon-button danger" type="button" data-action="remove" data-id="${item.id}">×</button>
        </article>
    `).join('');
}

function syncHeaderState() {
    const token = getToken();
    const userName = localStorage.getItem(USER_KEY) || 'Khách';

    if (dom['user-chip']) {
        dom['user-chip'].textContent = token ? `Xin chào, ${userName}` : 'Khách';
    }

    if (dom['login-link']) {
        dom['login-link'].hidden = Boolean(token);
    }

    if (dom['logout-button']) {
        dom['logout-button'].hidden = !token;
    }
}

function logout(redirect = true) {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    if (redirect) {
        window.location.href = 'index.html';
        return;
    }
    syncHeaderState();
    renderCart(null);
}

function setHomeMessage(text, type) {
    if (!dom['page-status']) {
        return;
    }

    dom['page-status'].textContent = text;
    dom['page-status'].dataset.type = type;
}

function setAuthMessage(text, type) {
    if (!dom['auth-message']) {
        return;
    }

    dom['auth-message'].textContent = text;
    dom['auth-message'].dataset.type = type;
}

async function apiFetch(path, options = {}) {
    const headers = new Headers(options.headers || {});
    const bodyIsFormData = options.body instanceof FormData;

    if (!bodyIsFormData && options.body !== undefined && !headers.has('Content-Type')) {
        headers.set('Content-Type', 'application/json');
    }

    const token = getToken();
    if (token) {
        headers.set('Authorization', `Bearer ${token}`);
    }

    const response = await fetch(`${API_BASE_URL}${path}`, {
        ...options,
        headers,
    });

    const contentType = response.headers.get('content-type') || '';
    const raw = await response.text();
    const payload = contentType.includes('application/json') && raw ? JSON.parse(raw) : raw;

    if (!response.ok) {
        const message = typeof payload === 'string'
            ? payload
            : payload?.message || payload?.error || `Request failed with status ${response.status}`;
        throw new Error(message);
    }

    return payload;
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function formatMoney(value) {
    return CURRENCY.format(Number(value || 0));
}

function escapeHtml(value) {
    return String(value ?? '')
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#39;');
}