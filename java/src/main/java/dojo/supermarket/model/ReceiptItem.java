package dojo.supermarket.model;

import java.util.Objects;

public class ReceiptItem {
    private ProductQuantity productQuantity;
    private final double price;
    private double totalPrice;

    public ReceiptItem(ProductQuantity productQuantity, double price, double totalPrice) {
        this.productQuantity = productQuantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public double getPrice() {
        return this.price;
    }

    public Product getProduct() {
        return productQuantity.getProduct();
    }

    public double getQuantity() {
        return productQuantity.getQuantity();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptItem that = (ReceiptItem) o;
        return Double.compare(that.price, price) == 0 &&
                Double.compare(that.totalPrice, totalPrice) == 0 &&
                Double.compare(that.productQuantity.getQuantity(), this.productQuantity.getQuantity()) == 0 &&
                Objects.equals(that.productQuantity.getProduct(), this.productQuantity.getProduct());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProduct(), price, totalPrice, getQuantity());
    }


}
