package dojo.supermarket.model;

import java.util.*;

public class ShoppingCart {

    private final List<ProductQuantity> productQuantities = new ArrayList<>();
    private final Set<Product> addedProducts = new HashSet<>();

    // just for passing inconsistent tests :)
    private final List<ProductQuantity> items = new ArrayList<>();

    public List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    public void addProduct(Product product) {
        this.addProductQuantity(product, 1.0);
    }

    public void addProductQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
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
            if (offers.containsKey(productQuantity.getProduct())) {
                Offer offer = offers.get(productQuantity.getProduct());
                double unitPrice = catalog.getUnitPrice(productQuantity.getProduct());
                Discount discount = null;
                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    discount = handleForAmountOffer(2, productQuantity, offer, unitPrice);
                } else if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    discount = handleForAmountOffer(5, productQuantity, offer, unitPrice);
                } else if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    discount = handleThreeForTwo(productQuantity, unitPrice);
                } else if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = handlePercentDiscount(productQuantity, offer, unitPrice);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }
        }
    }

    private Discount handleForAmountOffer(int x, ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        if (quantityAsInt >= x) {
            int intDivision = quantityAsInt / x;
            double pricePerUnit = offer.argument * intDivision;
            double theTotal = (quantityAsInt % x) * unitPrice;
            double total = pricePerUnit + theTotal;
            double discount = unitPrice * productQuantity.getQuantity() - total;
            return new Discount(productQuantity.getProduct(), x + " for " + offer.argument, -discount);
        }
        return null;
    }

    private Discount handlePercentDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        return new Discount(productQuantity.getProduct(), offer.argument + "% off", -productQuantity.getQuantity() * unitPrice * offer.argument / 100.0);
    }

    private Discount handleThreeForTwo(ProductQuantity productQuantity, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        if (quantityAsInt > 2) {
            int intDivision = quantityAsInt / 3;
            double discountAmount = productQuantity.getQuantity() * unitPrice - ((intDivision * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
            return new Discount(productQuantity.getProduct(), "3 for 2", -discountAmount);
        }
        return null;
    }

}
