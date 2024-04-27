import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { ProductImage } from 'src/app/models/product.image';
import { CartService } from 'src/app/services/cart.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss'],
})
export class DetailProductComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;

  constructor(
    private productService: ProductService,
    private cartService: CartService // private router: Router,
  ) // private activetedRoute : ActivatedRoute,
  {}

  ngOnInit() {
    debugger;
    //this.cartService.clearCart();
    const idParam = 1; // Fake temporarily value
    if (idParam !== null) {
      this.productId = +idParam;
    }
    if (!isNaN(this.productId)) {
      this.productService.getDetailProduct(this.productId).subscribe({
        next: (response: any) => {
          // Retrieve list product and change URL
          debugger;
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
            });
          }
          debugger;
          this.product = response;
          // Start with image first;
          this.showImage(0);
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.error('Error fetching detail: ', error);
        },
      });
    } else {
      console.error('Invalid productId:', idParam);
    }
  }

  showImage(index: number): void {
    debugger;
    if (
      this.product &&
      this.product.product_images &&
      this.product.product_images.length > 0
    ) {
      // Make sure the index is within a valid range
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }

      // Assign index and update image
      this.currentImageIndex = index;
    }
  }

  thumbnailClick(index: number) {
    debugger;
    // When I clicked thumbnail
    this.currentImageIndex = index;
  }

  nextImage(): void {
    debugger;
    this.showImage(this.currentImageIndex + 1);
  }

  previousImage(): void {
    debugger;
    this.showImage(this.currentImageIndex - 1);
  }

  addToCart(): void {
    debugger;
    if (this.product) {
      this.cartService.addToCart(this.product.id, this.quantity);
    } else {
      // Handle if product is null
      console.error('Không thể thêm sản phẩm vào giỏ hàng vì sản phẩm null');
    }
  }

  increaseQuantity(): void {
    if (this.quantity < 5) {
      this.quantity++;
    }
  }

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  byNow(){}
}
