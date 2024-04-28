import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderDTO } from 'src/app/dtos/order/order.dto';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent {
  orderForm: FormGroup; // Object help validation data of form
  cartItems: { product: Product; quantity: number }[] = [];
  totalAmount: number = 0;
  coupon: string = ''; // Voucher
  orderData: OrderDTO = {
    user_id: 2,
    fullname: '',
    email: '',
    phone_number: '',
    shipping_address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private fb: FormBuilder //
  ) {
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', Validators.email],
      phone_number: ['', [Validators.required, Validators.minLength(6)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      note: [''],
      payment_method: 'cod',
      shipping_method: 'express',
    });
  }

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

  placeOrder() {
    debugger;
    if (this.orderForm.valid) {
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value,
      };

      this.orderData.cart_items = this.cartItems.map(cartItem => ({
        product_id : cartItem.product.id,
        quantity : cartItem.quantity
      }));

      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response) => {
          debugger;
          console.log('Create order success');
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger
          console.error('Have error: ', error);
        },
      });
    } else {
      alert('Data invalid');
    }
  }

  calculateTotal() {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }
}
