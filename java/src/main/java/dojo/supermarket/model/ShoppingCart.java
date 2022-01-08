package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p : productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
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
            int intDivision = quantityAsInt / 2;
            double discountAmount = quantity * unitPrice - ((intDivision * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
            return new Discount(p, "3 for 2", -discountAmount);
        }
        return null;
    }

}
