package dojo.supermarket.model;

import java.util.*;

public class ShoppingCart {

    private final List<ProductQuantity> productQuantities = new ArrayList<>();
    private final Set<Product> addedProducts = new HashSet<>();

    public List<ProductQuantity> getProductQuantities() {
        return new ArrayList<>(productQuantities);
    }

    public void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    public void addItemQuantity(Product product, double quantity) {
        if (addedProducts.contains(product)) {
            productQuantities.stream().filter(productQuantity -> productQuantity.getProduct().equals(product))
                    .findFirst().get().addQuantity(quantity);
        } else {
            productQuantities.add(new ProductQuantity(product, quantity));
            addedProducts.add(product);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (ProductQuantity productQuantity : productQuantities) {
            Product p = productQuantity.getProduct();
            double quantity = productQuantity.getQuantity();
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                Discount discount = null;
                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    discount = handleForAmountOffer(2, p, quantity, offer, unitPrice);
                } else if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    discount = handleForAmountOffer(5, p, quantity, offer, unitPrice);
                } else if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    discount = handleThreeForTwo(p, quantity, unitPrice);
                } else if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = handlePercentDiscount(p, quantity, offer, unitPrice);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }
        }
    }


    Discount handleForAmountOffer(int x, Product p, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= x) {
            int intDivision = quantityAsInt / x;
            double pricePerUnit = offer.argument * intDivision;
            double theTotal = (quantityAsInt % x) * unitPrice;
            double total = pricePerUnit + theTotal;
            double discount = unitPrice * quantity - total;
            return new Discount(p, x + " for " + offer.argument, -discount);
        }
        return null;
    }

    Discount handlePercentDiscount(Product p, double quantity, Offer offer, double unitPrice) {
        return new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
    }

    Discount handleThreeForTwo(Product p, double quantity, double unitPrice) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt > 2) {
            int intDivision = quantityAsInt / 3;
            double discountAmount = quantity * unitPrice - ((intDivision * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
            return new Discount(p, "3 for 2", -discountAmount);
        }
        return null;
    }

}
