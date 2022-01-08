package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupermarketTest {
    private SupermarketCatalog catalog;
    private Teller teller;
    private ShoppingCart theCart;
    private Product toothbrush;
    private Product rice;
    private Product apples;
    private Product cherryTomatoes;

    @BeforeEach
    public void setUp() {
        catalog = new FakeCatalog();
        teller = new Teller(catalog);
        theCart = new ShoppingCart();

        toothbrush = new Product("toothbrush", ProductUnit.Each);
        catalog.addProduct(toothbrush, 0.99);
        rice = new Product("rice", ProductUnit.Each);
        catalog.addProduct(rice, 2.99);
        apples = new Product("apples", ProductUnit.Kilo);
        catalog.addProduct(apples, 1.99);
        cherryTomatoes = new Product("cherry tomato box", ProductUnit.Each);
        catalog.addProduct(cherryTomatoes, 0.69);

    }

    @Test
    public void an_empty_shopping_cart_should_cost_nothing() {
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void one_normal_item() {
        theCart.addProduct(toothbrush);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void two_normal_items() {
        theCart.addProduct(toothbrush);
        theCart.addProduct(rice);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_two_get_one_free() {
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_two_get_one_free_but_insufficient_in_basket() {
        theCart.addProduct(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_five_get_one_free() {
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        theCart.addProduct(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loose_weight_product() {
        theCart.addProductQuantity(apples, .5);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void percent_discount() {
        theCart.addProduct(rice);
        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, rice, 10.0);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void xForY_discount() {
        theCart.addProduct(cherryTomatoes);
        theCart.addProduct(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, cherryTomatoes, .99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void xForY_discount_with_insufficient_in_basket() {
        theCart.addProduct(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, cherryTomatoes, .99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount() {
        theCart.addProductQuantity(apples, 5);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples, 6.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSix() {
        theCart.addProductQuantity(apples, 6);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples, 5.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSixteen() {
        theCart.addProductQuantity(apples, 16);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples, 7.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withFour() {
        theCart.addProductQuantity(apples, 4);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples, 8.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
}
