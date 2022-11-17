package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that handles the logic for the Modify Product screen based on the modifyproduct.fxml file.
 */
public class mainScreen implements Initializable {
    /**
     * A table view for displaying all existing parts.
     */
    public TableView allParts;
    /**
     * Column in the allParts tableview for the part ID.
     */
    public TableColumn partNumber;
    /**
     * Column in the allParts tableview for the part name.
     */
    public TableColumn partName;
    /**
     * Column in the allParts tableview for the part stock.
     */
    public TableColumn partStock;
    /**
     * Column in the allParts tableview for the part price.
     */
    public TableColumn partPrice;
    /**
     * Button to call up the Add Part screen.
     */
    public Button partAdd;
    /**
     * Button to call up the Modify Part screen.
     */
    public Button partModify;
    /**
     * Button to delete the selected part.
     */
    public Button partDelete;
    /**
     * A table view for displaying all existing products.
     */
    public TableView allProducts;
    /**
     * Column in the allProducts table view for the product ID.
     */
    public TableColumn productNumber;
    /**
     *  Column in the allProducts table view for the product name.
     */
    public TableColumn productName;
    /**
     * Column in the allProducts table view for the product stock.
     */
    public TableColumn productStock;
    /**
     * Column in the allProducts table view for the product price.
     */
    public TableColumn productPrice;
    /**
     * Button to call up the Add Product screen.
     */
    public Button productAdd;
    /**
     * Button to call up the Modify Product screen.
     */
    public Button productModify;
    /**
     * Button to delete the selected part.
     */
    public Button productDelete;
    /**
     * The Exit button.
     */
    public Button exitButton;
    /**
     * Text field to enter search query for parts.
     */
    public TextField partSearch;
    /**
     * Button to trigger part search.
     */
    public Button partSearchBtn;
    /**
     * Text field to enter search query for products.
     */
    public TextField productSearch;
    /**
     * Button to trigger product search.
     */
    public Button productSearchBtn;

    /**
     * Creates an alert that is used to display any error or confirmation messages
     * generated by methods within the Modify Product controller.
     */
    Alert a = new Alert(Alert.AlertType.NONE);

    private Part selectedPart;
    private Product selectedProduct;
    private int index = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<Part> parts = Inventory.getAllParts();
        allParts.setItems(parts);

        productNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<Product> products = Inventory.getAllProducts();
        allProducts.setItems(products);

        selectedPart = new InHouse(0, "", 0, 0, 0, 0, 0);
        selectedProduct = new Product(0, "", 0, 0, 0, 0);
    }

    /**
     * Gets the Add Part fxml and prepares a stage for it, then displays the new window.
     *
     * @param actionEvent Not used.
     * @throws IOException Not used.
     */
    public void onPartAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addpart.fxml"));
        Stage stage2 = new Stage();
        stage2.setTitle("Add Part");
        stage2.setScene(new Scene(root, 400, 450));
        stage2.show();
    }

    /**
     * Gets the selected part and its index to pass to the Modify Part controller, then
     * gets the Modify Part fxml and prepares a stage for it, then displays the new window.
     * Generates an error if no part is selected to be modified.
     *
     * @param actionEvent Not used.
     * @throws IOException Not used.
     */
    public void onPartModify(ActionEvent actionEvent) throws IOException {
        selectedPart = (Part) allParts.getSelectionModel().getSelectedItem();
        index = Inventory.getAllParts().indexOf(selectedPart);
        modifyPart.getPartToModify(index, selectedPart);
        if (selectedProduct != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/modifypart.fxml"));
                Stage stage2 = new Stage();
                stage2.setTitle("Modify Part");
                stage2.setScene(new Scene(root, 400, 450));
                stage2.show();
            } catch (Exception e) {
                //ignore
            }
        } else {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("No product selected to modify.");
            a.show();
        }
    }

    /**
     * Deletes the selected part from the inventory. Provides a confirmation
     * dialog to allow the user the choice to cancel their action; if user chooses to cancel,
     * no information is removed, and if user chooses OK the selected part is removed and the tableview
     * of associated parts is updated. An error will display if the user attempts to remove a part
     * with no part selected.
     *
     * @param actionEvent Not used.
     */
    public void onPartDelete(ActionEvent actionEvent) {
        Part selectedPart = (Part) allParts.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setHeaderText(null);
            a.setContentText("Are you sure you want to remove this part?");

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setHeaderText(null);
                a.setContentText("The part has been deleted.");
                a.show();
                allParts.setItems(Inventory.getAllParts());
            } else {
                a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setContentText("Part was not deleted.");
                a.setHeaderText(null);
                a.show();
            }
        } else {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("No part selected for deletion.");
            a.show();
        }

    }

    /**
     * Gets the Add Product fxml and prepares a stage for it, then displays the new window.
     *
     * @param actionEvent Not used.
     * @throws IOException Not used.
     */
    public void onProductAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addproduct.fxml"));
        Stage stage2 = new Stage();
        stage2.setTitle("Add Product");
        stage2.setScene(new Scene(root, 750, 600));
        stage2.show();
    }

    /**
     * Gets the selected product and its index to pass to the Modify Product controller, then
     * gets the Modify Product fxml and prepares a stage for it and displays the new window.
     * Generates an error if no product is selected to be modified.
     *
     * @param actionEvent Not used.
     * @throws IOException Not used.
     */
    public void onProductModify(ActionEvent actionEvent) throws IOException {
        selectedProduct = (Product) allProducts.getSelectionModel().getSelectedItem();
        index = Inventory.getAllProducts().indexOf(selectedProduct);
        modifyProduct.getProducttoModify(index, selectedProduct);
        if (selectedProduct != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/modifyproduct.fxml"));
                Stage stage2 = new Stage();
                stage2.setTitle("Modify Product");
                stage2.setScene(new Scene(root, 750, 600));
                stage2.show();
            } catch (Exception e) {
                //ignore;
            }
        } else {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("No product selected to modify.");
            a.show();
        }

    }

    /**
     * Deletes the selected product from the inventory. Provides a confirmation
     * dialog to allow the user the choice to cancel their action; if user chooses to cancel,
     * no information is removed, and if user chooses OK the selected product is removed and the tableview
     * of products is updated. An error will display if the user attempts to remove a product
     * with no product selected, or that still has associated parts.
     *
     * @param actionEvent Not used.
     */
    public void onProductDelete(ActionEvent actionEvent) {
        Product selectedProduct = (Product) allProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setHeaderText(null);
            a.setContentText("Are you sure you want to remove this product?");

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedProduct.getAllAssociatedParts().size() == 0) {
                    Inventory.deleteProduct(selectedProduct);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText(null);
                    a.setContentText("The product has been deleted.");
                    a.show();
                    allProducts.setItems(Inventory.getAllProducts());
                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("You cannot remove a product that has parts associated with it.");
                    a.show();
                }
            } else {
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Product was not deleted.");
                a.setHeaderText(null);
                a.show();
            }
        } else {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("No product selected for deletion.");
            a.show();
        }
    }


    /**
     * Confirmation dialog that allows user to confirm or cancel closing the program.
     *
     * @param actionEvent Not used.
     */
    public void onExitButton(ActionEvent actionEvent) {
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(null);
        a.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * Searches the list of all parts. The search will look for a partID first, and if
     * no part with that ID is found it will execute a partial string search. Any parts
     * found are added to a list and shown in the updated tableview.
     *
     * @param actionEvent Not used.
     */
    public void getPartResults(ActionEvent actionEvent) {
        String s = partSearch.getText();
        ObservableList<Part> parts = searchByPartName(s);

        if (parts.size() == 0) {
            try {
                int partID = Integer.parseInt(s);
                Part part = searchByPartID(partID);
                if (part != null) {
                    parts.add(part);
                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("No results for search.");
                    a.show();
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        allParts.setItems(parts);
        partSearch.setText("");
    }

    /**
     * Searches the list of all products. The search will look for a productID first, and if
     * no product with that ID is found it will execute a partial string search. Any products
     * found are added to a list and shown in the updated tableview.
     *
     * @param actionEvent Not used.
     */
    public void getProductResults(ActionEvent actionEvent) {
        String s = productSearch.getText();
        ObservableList<Product> products = searchByProductName(s);

        if (products.size() == 0) {
            try {
                int productID = Integer.parseInt(s);
                Product product = searchByProductID(productID);
                if (product != null) {
                    products.add(product);
                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("No results for search.");
                    a.show();
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        allProducts.setItems(products);
        productSearch.setText("");
    }

    /**
     * Method to iterate through allParts and search for any items matching the partial string
     * from user input.
     *
     * @param partialName Gets the string to search on from user input in the search text field.
     * @return Returns an ObservableList populated with any items that meet search criteria.
     */
    private ObservableList<Part> searchByPartName(String partialName) {
        ObservableList<Part> selectedParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();
        if (allParts.size() == 0) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("No parts available! Add parts before searching.");
            a.show();
        } else {
            for (Part part : allParts) {
                if (part.getName().contains(partialName)) {
                    selectedParts.add(part);
                    return selectedParts;
                }
            }
        }
        return allParts;
    }

    /**
     * Method to iterate through allParts and search for any items matching the part ID
     * from user input.
     *
     * @param partID Gets the Part ID to search on from user input in the search text field.
     * @return Returns the Part matching the Part ID, if any. Returns null if no parts found.
     */
    private Part searchByPartID(int partID) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     * Method to iterate through allProducts and search for any items matching the partial string
     * from user input.
     *
     * @param partialName Gets the string to search on from user input in the search text field.
     * @return Returns an ObservableList populated with any items that meet search criteria.
     */
    private ObservableList<Product> searchByProductName(String partialName) {
        ObservableList<Product> selectedProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        if (allProducts.size() == 0) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("No products available! Add products before searching.");
            a.show();
        } else {
            for (Product product : allProducts) {
                if (product.getName().contains(partialName)) {
                    selectedProducts.add(product);
                    return selectedProducts;
                }
            }
        }
        return allProducts;
    }

    /**
     * Method to iterate through allProducts and search for any items matching the product ID
     * from user input.
     *
     * @param productID Gets the Productt ID to search on from user input in the search text field.
     * @return Returns the Product matching the Product ID, if any. Returns null if no products found.
     */
    private Product searchByProductID(int productID) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }


}
