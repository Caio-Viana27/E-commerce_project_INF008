import java.util.LinkedList;
import java.util.Scanner;

public class Customer extends Account {
    private Address deliveryAddress;
    private LinkedList<Order> orderHistory;

    public Customer(String name, String email, String password, Address deliveryAddress) {
        super(name, email, password, "customer");
        this.deliveryAddress = deliveryAddress;
        this.orderHistory = new LinkedList<Order>();
    }

    private Product searchProductList(LinkedList<Product> productList, String id) {
        for (var product : productList) {
            if (product.idMatches(id)) {
                return product;
            }
        }
        return null;
    }

    public BoughtProduct addProductToShoppingCart(LinkedList<Product> productList, Scanner scanner) {

        Product product;
        while (true) {
            Menu.separator();
            System.out.println("List of products");
            System.out.println("To select a product type its id");
            for (var tempProd : productList) {
                if (tempProd.getInStorage() > 0)
                    tempProd.display();
            }
            System.out.print("\nEnter id: ");
            String id = scanner.nextLine();

            product = this.searchProductList(productList, id);
            if (product == null)
                Menu.invalidOptionWarning("id");
            else
                break;
        }
        int quantity = Menu.selectQuantity(scanner, product.getInStorage());
        product.setInStorage(product.getInStorage() - quantity);

        return new BoughtProduct(product, quantity);
    }

    public void finishOrder(ShoppingCart shoppingCart) {
        orderHistory.add(new Order(shoppingCart));
    }

    public void display() {
        super.display();
        System.out.println("Delivery address:");
        deliveryAddress.display();
        System.out.println("Order history:");
        this.orderHistoryDisplay();
    }

    private void orderHistoryDisplay() {
        if (orderHistory.size() == 0) {
            System.out.println("No order history yet");
            return;
        }
        for (var order : orderHistory) {
            order.display();
        }
    }

    public boolean hasHistoryOrder() {
        return this.orderHistory.size() > 0;
    }

    public Order searchMoreExpensiveOrder(Order currentMoreExpensive) {
        for (Order order : orderHistory) {
            if (currentMoreExpensive == null || !currentMoreExpensive.isMoreExpensiveOrder(order)) {
                currentMoreExpensive = order;
            }
        }
        return currentMoreExpensive;
    }
}
