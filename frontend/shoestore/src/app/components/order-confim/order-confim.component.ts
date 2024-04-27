import { Component, OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-order-confim',
  templateUrl: './order-confim.component.html',
  styleUrls: ['./order-confim.component.scss'],
})
export class OrderConfimComponent implements OnInit {
  cartItems: { product: Product; quantity: number }[] = [];
  totalAmount : number = 0;

  constructor(
    private cartService: CartService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    debugger;
    // Retrieve list products from cart
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());

    // Call service to get information for products based on the list of IDs
    debugger;
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        debugger;
        // Retrieve product info and quantity from cart
        this.cartItems = productIds.map((productId) => {
          debugger;
          // Find the product by ID
          const product = products.find((p) => p.id === productId); // Fixed arrow function syntax

          if (product) {
            // Update product thumbnail URL
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          // Return cart item with product and quantity
          return {
            product: product!,
            quantity: cart.get(productId)!, // Assuming cart is a Map, use get() method to retrieve quantity
          };
        });
        console.log('Cart Items:', this.cartItems); // Output cart items after mapping
      },
      complete: () => {
        debugger;
        this.calculateTotal();
      },
      error: (error) => {
        debugger;
        console.error('Error fetching products by IDs:', error);
      },
    });
  }

  calculateTotal() {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity, 0
    );
  }
}
