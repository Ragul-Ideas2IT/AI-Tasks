# Challenge 4: Security Vulnerabilities (Advanced)

## Location
`src/components/ProductDetails/index.js`

---

## 1. Security Issues Identified

- **XSS Vulnerabilities & Unsafe HTML Rendering:**
  - Using `dangerouslySetInnerHTML` with untrusted `product.description` can allow attackers to inject malicious scripts.
- **Unvalidated User Input:**
  - Rendering `product.imageUrl` and `product.title` directly can allow injection of malicious URLs or code.
- **URL/Navigation Security:**
  - Using `window.location.href` with unvalidated `product.id` can allow open redirects or navigation to unintended locations.
- **Image Source Validation:**
  - Directly using `product.imageUrl` can allow loading of malicious or external images.

---

## 2. Before (Original Code)

```jsx
const ProductDetails = ({ product }) => {
  return (
    <div>
      <h2>{product.title}</h2>
      {/* XSS vulnerability */}
      <div dangerouslySetInnerHTML={{__html: product.description}} />
      {/* Unvalidated user input */}
      <img src={product.imageUrl} alt={product.title} />
      <button onClick={() => {
        // No input validation
        window.location.href = `/product/${product.id}`;
      }}>
        View Details
      </button>
    </div>
  );
};
```

---

## 3. After (Secure Version)

```jsx
import React from 'react';
// Optionally, use a library like DOMPurify for HTML sanitization
// import DOMPurify from 'dompurify';

const isValidImageUrl = (url) => {
  // Only allow images from your own domain or trusted sources
  try {
    const parsed = new URL(url, window.location.origin);
    // Example: allow only same-origin images
    return parsed.origin === window.location.origin;
  } catch {
    return false;
  }
};

const isValidId = (id) => /^[a-zA-Z0-9_-]+$/.test(id);

const ProductDetails = ({ product }) => {
  // Sanitize description if you must render HTML
  // const safeDescription = DOMPurify.sanitize(product.description);

  return (
    <div>
      <h2>{String(product.title)}</h2>
      {/* Prefer rendering as plain text */}
      <div>
        {product.description}
      </div>
      {/* If you must render HTML, sanitize it first */}
      {/* <div dangerouslySetInnerHTML={{ __html: safeDescription }} /> */}
      {isValidImageUrl(product.imageUrl) ? (
        <img src={product.imageUrl} alt={String(product.title)} />
      ) : (
        <div>Invalid image</div>
      )}
      <button
        onClick={() => {
          if (isValidId(product.id)) {
            window.location.href = `/product/${product.id}`;
          } else {
            alert('Invalid product ID');
          }
        }}
      >
        View Details
      </button>
    </div>
  );
};

export default ProductDetails;
```

---

## 4. Explanations of the Risks and Fixes

- **XSS via `dangerouslySetInnerHTML`:**
  - Never render untrusted HTML. If you must, use a sanitizer like [DOMPurify](https://github.com/cure53/DOMPurify).
- **Unvalidated Inputs:**
  - Always validate and sanitize any data rendered or used in URLs/attributes.
- **Image Source Validation:**
  - Only allow images from trusted sources to prevent malicious content or tracking.
- **Navigation Security:**
  - Validate IDs and avoid open redirects. Use React Router for navigation if possible.

---

## 5. Summary Table

| Issue                | Risk                                 | Secure Fix                                      |
|----------------------|--------------------------------------|-------------------------------------------------|
| XSS (`dangerouslySetInnerHTML`) | Script injection/XSS                | Sanitize or render as plain text                |
| Unvalidated input    | Malicious code/URLs                  | Validate/sanitize all user data                 |
| Image source         | Malicious/external content           | Allow only trusted domains                      |
| Navigation           | Open redirects, invalid navigation   | Validate IDs, use safe navigation methods       | 