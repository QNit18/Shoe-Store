export class OrderDTO {
  user_id: number;
  fullname: string;
  email: string;
  phone_number: string;
  shipping_address: string;
  note: string;
  total_money: number;
  payment_method: string;
  shipping_method: string;
  coupon_code: string;
  cart_items: { product_id: number; quantity: number }[];

  constructor(data: any) {
    this.user_id = data.user_id;
    this.fullname = data.fullname;
    this.email = data.email;
    this.phone_number = data.phone_number;
    this.shipping_address = data.address;
    this.note = data.note;
    this.total_money = data.total_money;
    this.payment_method = data.payment_method;
    this.shipping_method = data.shipping_method;
    this.coupon_code = data.coupon_code;
    this.cart_items = data.cart_items;
  }
}