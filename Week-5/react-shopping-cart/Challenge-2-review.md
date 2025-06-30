# Challenge 2: Performance Issues (Intermediate)

## Location
`src/components/ProductList/index.js`

---

## 1. Identifying Performance Bottlenecks
- **Problem:**
  - The filtering logic (`products.filter(...)`) runs on every render, even if `products` and `filters` haven't changed.
  - This causes unnecessary computation and re-renders, especially with large product lists.

---

## 2. Implementing Proper Memoization
- **Solution:**
  - Use `React.useMemo` to memoize the filtered products, so the expensive filter only runs when `products` or `filters` change.

---

## 3. Optimizing Filtering Logic
- **Current logic:**
  - Efficient, but ensure that `filters.sizes` and `filters.maxPrice` are stable references (not recreated on every parent render).
  - If possible, avoid passing new filter objects/arrays from parent unless they actually change.

---

## 4. Adding Performance Monitoring
- **Solution:**
  - Use `console.time` and `console.timeEnd` to measure filtering duration.
  - Optionally, use React DevTools Profiler for deeper analysis.

---

## Before (Original Code)

```jsx
const ProductList = ({ products, filters }) => {
  // Expensive filtering on every render
  const filteredProducts = products.filter(product => {
    return filters.sizes.every(size =>
      product.availableSizes.includes(size)
    ) && product.price <= filters.maxPrice;
  });

  return (
    <div>
      {filteredProducts.map(product => (
        <ProductItem key={product.id} product={product} />
      ))}
    </div>
  );
};
```

---

## After (Optimized Code)

```jsx
import React from 'react';

const ProductList = ({ products, filters }) => {
  // Memoize the filtered products
  const filteredProducts = React.useMemo(() => {
    console.time('Product filtering');
    const result = products.filter(product => {
      return (
        filters.sizes.every(size =>
          product.availableSizes.includes(size)
        ) && product.price <= filters.maxPrice
      );
    });
    console.timeEnd('Product filtering');
    return result;
  }, [products, filters.sizes, filters.maxPrice]);

  return (
    <div>
      {filteredProducts.map(product => (
        <ProductItem key={product.id} product={product} />
      ))}
    </div>
  );
};

export default ProductList;
```

---

## Performance Metrics Example
- With `console.time('Product filtering')` and `console.timeEnd('Product filtering')`, you'll see logs in the browser console like:
  ```
  Product filtering: 1.23ms
  ```

---

## Summary Table

| Issue                        | Before (Original)         | After (Optimized)         |
|------------------------------|---------------------------|---------------------------|
| Filtering on every render    | ❌ Yes                    | ✅ Only when inputs change|
| Memoization                  | ❌ None                   | ✅ useMemo                |
| Filtering logic              | ⚠️ Efficient, but always runs | ✅ Efficient & memoized   |
| Performance monitoring       | ❌ None                   | ✅ console.time           |

---

**Best Practice Tip:**
For even better performance, ensure that `filters.sizes` and `filters.maxPrice` are not recreated on every parent render (use `useMemo` or `useCallback` in the parent if needed). 