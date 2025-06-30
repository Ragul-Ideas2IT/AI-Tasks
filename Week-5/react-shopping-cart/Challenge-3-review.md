# Challenge 3: Error Handling Gaps (Intermediate)

## Location
`src/services/api.js`

---

## 1. Problem: Missing Error Handling in API Calls
- No try-catch blocks
- No network or HTTP status code validation
- No user-friendly error messages
- No retry mechanism for failed requests
- No loading state management
- No data validation

---

## 2. Before (Original Code)

```js
export const fetchProducts = async () => {
  const response = await fetch('/api/products');
  const data = await response.json(); // No error handling!
  return data;
};

export const processPayment = async (paymentData) => {
  const response = await fetch('/api/payment', {
    method: 'POST',
    body: JSON.stringify(paymentData)
  });
  return response.json(); // No error handling!
};
```

---

## 3. After (Enhanced Error Handling & Production Patterns)

```js
// Utility: Retry wrapper for fetch
async function fetchWithRetry(url, options = {}, retries = 3, backoff = 300) {
  for (let i = 0; i < retries; i++) {
    try {
      const response = await fetch(url, options);
      if (!response.ok) {
        // HTTP error (non-2xx)
        const errorText = await response.text();
        throw new Error(`HTTP ${response.status}: ${errorText || response.statusText}`);
      }
      return response;
    } catch (error) {
      if (i === retries - 1) throw error;
      // Exponential backoff
      await new Promise(res => setTimeout(res, backoff * Math.pow(2, i)));
    }
  }
}

// Fetch products with error handling, validation, and loading state
export const fetchProducts = async (setLoading) => {
  if (setLoading) setLoading(true);
  try {
    const response = await fetchWithRetry('/api/products');
    const data = await response.json();
    // Optional: Validate data structure here
    if (!Array.isArray(data)) {
      throw new Error('Invalid products data format');
    }
    return data;
  } catch (error) {
    // User-friendly error message
    throw new Error(`Failed to fetch products: ${error.message}`);
  } finally {
    if (setLoading) setLoading(false);
  }
};

// Process payment with error handling, validation, and loading state
export const processPayment = async (paymentData, setLoading) => {
  if (setLoading) setLoading(true);
  try {
    // Validate paymentData before sending
    if (!paymentData || typeof paymentData !== 'object') {
      throw new Error('Invalid payment data');
    }
    const response = await fetchWithRetry('/api/payment', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(paymentData)
    });
    const result = await response.json();
    // Optional: Validate result structure here
    if (!result.success) {
      throw new Error(result.message || 'Payment failed');
    }
    return result;
  } catch (error) {
    throw new Error(`Payment processing error: ${error.message}`);
  } finally {
    if (setLoading) setLoading(false);
  }
};
```

---

## 4. Key Enhancements Explained

- **Comprehensive try-catch blocks:** All async logic is wrapped in try-catch for error interception.
- **Network error handling:** Handles fetch/network errors and HTTP status codes.
- **HTTP status code validation:** Checks `response.ok` and throws for non-2xx responses.
- **User-friendly error messages:** Errors are rethrown with clear, actionable messages.
- **Retry mechanisms:** `fetchWithRetry` retries failed requests with exponential backoff.
- **Loading states management:** Optional `setLoading` callback toggles loading state before/after the request.
- **Data validation:** Checks for expected data structure (e.g., array for products, object for payment).

---

## 5. Production-Ready Patterns

- Use a utility like `fetchWithRetry` for all network requests.
- Always validate both request and response data.
- Provide hooks or callbacks for loading/error state management in UI.
- Never expose raw error messages to users—wrap them in user-friendly language. 