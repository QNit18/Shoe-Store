import { Injectable } from '@angular/core';
import { ProductService } from './product.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: Map<number, number> = new Map(); // Using Map to save cart, key is id, value is quantity

  constructor(private productService: ProductService) {
    //Retrieve data cart from localStorage when initialize
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = new Map(JSON.parse(storedCart));
    }
  }

  addToCart(productId: number, quantity: number = 1): void {
    debugger;
    if (this.cart.has(productId)) {
      // If product exists in cart
      this.cart.set(productId, this.cart.get(productId)! + quantity);
    } else {
      // If product dont exists in cart
      this.cart.set(productId, quantity);
    }

    // When change cart, save into localStorage
    this.saveCartToLocalStorage();
  }

  getCart(): Map<number, number>{
    return this.cart;
  }

  saveCartToLocalStorage() : void{
    debugger;
    localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
  }

  clearCart(): void{
    this.cart.clear();
    this.saveCartToLocalStorage();
  }
}
