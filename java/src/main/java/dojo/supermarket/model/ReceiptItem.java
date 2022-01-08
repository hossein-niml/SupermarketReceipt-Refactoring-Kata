package dojo.supermarket.model;

import java.util.Objects;

public class ReceiptItem {
    private final double unitPrice;
    private final ProductQuantity productQuantity;
    private final double price;

    public ReceiptItem(ProductQuantity productQuantity, double unitPrice, double price) {
        this.productQuantity = productQuantity;
        this.unitPrice = unitPrice;
        this.price = price;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public Product getProduct() {
        return productQuantity.getProduct();
    }

    public double getQuantity() {
        return productQuantity.getQuantity();
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptItem that = (ReceiptItem) o;
        return Double.compare(that.price, price) == 0 &&
                Double.compare(that.unitPrice, unitPrice) == 0 &&
                Double.compare(that.getQuantity(), this.getQuantity()) == 0 &&
                Objects.equals(that.getProduct(), this.getProduct());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProduct(), unitPrice, price, getQuantity());
    }


}
