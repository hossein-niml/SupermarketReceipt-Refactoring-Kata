package dojo.supermarket.model;

public class ProductQuantity {
    private final Product product;
    private double quantity;

    public ProductQuantity(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void addQuantity(double quantity) {
        this.quantity += quantity;
    }
}
